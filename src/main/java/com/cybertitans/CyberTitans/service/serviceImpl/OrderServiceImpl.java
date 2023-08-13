package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cybertitans.CyberTitans.dto.*;
import com.cybertitans.CyberTitans.enums.OrderStatus;
import com.cybertitans.CyberTitans.exception.ResourceNotFoundException;
import com.cybertitans.CyberTitans.model.OrderItems;
import com.cybertitans.CyberTitans.model.UserOrder;
import com.cybertitans.CyberTitans.model.Product;
import com.cybertitans.CyberTitans.model.User;
import com.cybertitans.CyberTitans.repository.OrderRepository;
import com.cybertitans.CyberTitans.repository.ProductRepository;
import com.cybertitans.CyberTitans.repository.UserRepository;
import com.cybertitans.CyberTitans.service.AuditTrialService;
import com.cybertitans.CyberTitans.service.OrderService;
import com.cybertitans.CyberTitans.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper mapper;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final AuditTrialService auditTrialService;

    public OrderServiceImpl(UserRepository userRepository, OrderRepository orderRepository, ModelMapper mapper, ProductService productService, ProductRepository productRepository, AuditTrialService auditTrialService) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.productService = productService;
        this.productRepository = productRepository;
        this.auditTrialService = auditTrialService;
    }

    @Override
    public AllOrderResponseDTO viewUserOrderHistory(int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = getLoggedInUser();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<UserOrder> orders = orderRepository.findAllByUser(pageable, user);
        AllOrderResponseDTO orderResponse = getOrderResponse(orders);
        return orderResponse;
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatusDTO orderStatusDTO) {
        UserOrder orderToUpdate = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        if (OrderStatus.DELIVERED.equals(orderStatusDTO.getOrderStatus())) orderToUpdate.setOrderStatus(OrderStatus.DELIVERED);
        if (OrderStatus.PENDING.equals(orderStatusDTO.getOrderStatus())) orderToUpdate.setOrderStatus(OrderStatus.PENDING);
        if (OrderStatus.CANCELLED.equals(orderStatusDTO.getOrderStatus())) orderToUpdate.setOrderStatus(OrderStatus.CANCELLED);
        UserOrder save = orderRepository.save(orderToUpdate);

        AuditTrialDTO auditTrial = new AuditTrialDTO();
        auditTrial.setUser(getLoggedInUser());
        auditTrial.setAudit("Order status for order with id " + orderId + " was " + save.getOrderStatus() + " successfully");
        auditTrial.setDate(LocalDate.now());
        auditTrialService.addToAuditTrial(auditTrial);
        return mapper.map(orderToUpdate, OrderResponseDTO.class);

    }

    @Override
    public OrderResponseDTO viewParticularOrder(Long orderId) {
        UserOrder orders = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        return mapper.map(orders, OrderResponseDTO.class);
    }

    @Override
    public OrderResponseDTO cancelOrder(Long orderId) {
        UserOrder orderToUpdate = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        orderToUpdate.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(orderToUpdate);
        return mapper.map(orderToUpdate, OrderResponseDTO.class);
    }

    public String createOrder(CreateOrderRequestDTO createOrderRequestDTO) {
        User user = getLoggedInUser();
        List<Product> products = getProductsByIds(createOrderRequestDTO.getProductIds());

        // Create a new order with products and shipping address
//        OrderResponseDTO orderResponse = // Your logic to create the order
                UserOrder order = new UserOrder();
        order.setDateOrdered(new Date());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setUser(user);
        order.setOrderItems(products.stream().map(product -> {
            OrderItems orderItem = new OrderItems();
            orderItem.setProduct(product);
            orderItem.setQuantity(product.getQuantity());
            orderItem.setSubTotal(product.getProductPrice() * product.getQuantity());
            return orderItem;
        }).collect(Collectors.toList()));
        order.setTotalPrice(products.stream().mapToDouble(Product::getProductPrice).sum());

        orderRepository.save(order);
        return "Order saved successfully";
    }

    public List<Product> getProductsByIds(List<Long> productIds) {
        return productRepository.findByProductIdIn(productIds);
    }

    @Override
    public AllOrderResponseDTO viewOrderHistory(int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = getLoggedInUser();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<UserOrder> orders = orderRepository.findAll(pageable);
        System.out.println(orders);
        AllOrderResponseDTO orderResponse = getOrderResponse(orders);
        return orderResponse;
    }

    private AllOrderResponseDTO getOrderResponse(Page<UserOrder> orders){
        List<UserOrder> orderList = orders.getContent();
        List<OrderResponseDTO> content = orderList.stream().map(this::getOrderResponseDto).collect(Collectors.toList());
        System.out.println(content);
        AllOrderResponseDTO orderResponseDTO = new AllOrderResponseDTO();
        orderResponseDTO.setContent(content);
        orderResponseDTO.setPageNo(orders.getNumber());
        orderResponseDTO.setPageSize(orders.getSize());
        orderResponseDTO.setTotalElements(orders.getNumberOfElements());
        orderResponseDTO.setTotalPages(orders.getTotalPages());
        orderResponseDTO.setLast(orders.isLast());

        return orderResponseDTO;

    }
    private OrderResponseDTO getOrderResponseDto(UserOrder order) {
        return OrderResponseDTO.builder()
                .id(order.getOrderId())
                .subTotal(order.getTotalPrice())
                .totalPrice(order.getTotalPrice())
                .dateOrdered(order.getDateOrdered())
                .shippingAddress(order.getShippingAddress())
                .orderStatus(order.getOrderStatus())
                .productList(order.getOrderItems().stream().map(orderItem -> Product.builder()
                        .productId(orderItem.getProduct().getProductId())
                        .productName(orderItem.getProduct().getProductName())
                        .productPrice(orderItem.getProduct().getProductPrice())
                        .image(orderItem.getProduct().getImage())
                        .createdAt(orderItem.getProduct().getCreatedAt())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    private User getLoggedInUser(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(name, name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
