package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cybertitans.CyberTitans.dto.*;
import com.cybertitans.CyberTitans.enums.OrderStatus;
import com.cybertitans.CyberTitans.exception.ResourceNotFoundException;
import com.cybertitans.CyberTitans.model.Orders;
import com.cybertitans.CyberTitans.model.User;
import com.cybertitans.CyberTitans.repository.OrderRepository;
import com.cybertitans.CyberTitans.repository.UserRepository;
import com.cybertitans.CyberTitans.service.AuditTrialService;
import com.cybertitans.CyberTitans.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper mapper;
    private final AuditTrialService auditTrialService;

    public OrderServiceImpl(UserRepository userRepository, OrderRepository orderRepository, ModelMapper mapper, AuditTrialService auditTrialService) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.auditTrialService = auditTrialService;
    }

    @Override
    public AllOrderResponseDTO viewUserOrderHistory(int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = getLoggedInUser();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Orders> orders = orderRepository.findAllByUser(pageable, user);
        AllOrderResponseDTO orderResponse = getOrderResponse(orders);
        return orderResponse;
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatusDTO orderStatusDTO) {
        Orders orderToUpdate = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        if (OrderStatus.DELIVERED.equals(orderStatusDTO.getOrderStatus())) orderToUpdate.setOrderStatus(OrderStatus.DELIVERED);
        if (OrderStatus.PENDING.equals(orderStatusDTO.getOrderStatus())) orderToUpdate.setOrderStatus(OrderStatus.PENDING);
        if (OrderStatus.CANCELLED.equals(orderStatusDTO.getOrderStatus())) orderToUpdate.setOrderStatus(OrderStatus.CANCELLED);
        Orders save = orderRepository.save(orderToUpdate);

        AuditTrialDTO auditTrial = new AuditTrialDTO();
        auditTrial.setUser(getLoggedInUser());
        auditTrial.setAudit("Order status for order with id " + orderId + " was " + save.getOrderStatus() + " successfully");
        auditTrial.setDate(LocalDate.now());
        auditTrialService.addToAuditTrial(auditTrial);
        return mapper.map(orderToUpdate, OrderResponseDTO.class);

    }

    @Override
    public OrderResponseDTO viewParticularOrder(Long orderId) {
        Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        return mapper.map(orders, OrderResponseDTO.class);
    }

    @Override
    public OrderResponseDTO cancelOrder(Long orderId) {
        Orders orderToUpdate = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        orderToUpdate.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(orderToUpdate);
        return mapper.map(orderToUpdate, OrderResponseDTO.class);
    }

    @Override
    public AllOrderResponseDTO viewOrderHistory(int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = getLoggedInUser();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Orders> orders = orderRepository.findAllByUser(pageable, user);
        AllOrderResponseDTO orderResponse = getOrderResponse(orders);
        return orderResponse;
    }

    private AllOrderResponseDTO getOrderResponse(Page<Orders> orders){
        List<Orders> orderList = orders.getContent();
        List<OrderResponseDTO> content = orderList.stream().map(order -> mapper.map(order, OrderResponseDTO.class)).collect(Collectors.toList());

        AllOrderResponseDTO orderResponseDTO = new AllOrderResponseDTO();
        orderResponseDTO.setContent(content);
        orderResponseDTO.setPageNo(orders.getNumber());
        orderResponseDTO.setPageSize(orders.getSize());
        orderResponseDTO.setTotalElements(orders.getNumberOfElements());
        orderResponseDTO.setTotalPages(orders.getTotalPages());
        orderResponseDTO.setLast(orders.isLast());

        return orderResponseDTO;

    }

    private User getLoggedInUser(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(name, name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
