package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cybertitans.CyberTitans.dto.OrderResponseDTO;
import com.cybertitans.CyberTitans.dto.ShippingAddressDTO;
import com.cybertitans.CyberTitans.enums.OrderStatus;
import com.cybertitans.CyberTitans.model.*;
import com.cybertitans.CyberTitans.repository.OrderItemRepository;
import com.cybertitans.CyberTitans.repository.OrderRepository;
import com.cybertitans.CyberTitans.repository.ShippingAddressRepository;
import com.cybertitans.CyberTitans.repository.UserRepository;
import com.cybertitans.CyberTitans.service.AuthService;
import com.cybertitans.CyberTitans.service.CartService;
import com.cybertitans.CyberTitans.service.CheckoutService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final ShippingAddressRepository shippingAddressRepository;

    private final CartService cartService;

    private final ModelMapper mapper;
    private final AuthService authService;

    public CheckoutServiceImpl(UserRepository userRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository, ShippingAddressRepository shippingAddressRepository, CartService cartService, ModelMapper mapper, AuthService authService) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.shippingAddressRepository = shippingAddressRepository;
        this.cartService = cartService;
        this.mapper = mapper;
        this.authService = authService;
    }

    @Override
    @Transactional
    public OrderResponseDTO checkout(ShippingAddressDTO shippingAddressDTO) {//
        User user = getLoggedInUser();
        Cart userCart = user.getCart();
        if (userCart.getCartItemList().size() < 1) {
            throw new CartIsEmptyException("Your cart is empty");
        }
        UserOrder orders = new UserOrder();
//        System.out.println(user);
        orders.setDateOrdered(new Date());
        orders.setShippingAddress(shippingAddressRepository.save(mapper.map(shippingAddressDTO, ShippingAddress.class)));
        orders.setOrderStatus(OrderStatus.PENDING);
        orders.setUser(user);
        orders.setOrderItems(userCart.getCartItemList().stream().map(item -> mapper.map(item, OrderItems.class)).collect(Collectors.toList()));
        orders.setTotalPrice(userCart.getCartTotal());
//        System.out.println(orders);

        System.out.println(orders.getUser().toString());
        UserOrder save = orderRepository.save(orders);
        cartService.clearCart();
        return mapper.map(orders, OrderResponseDTO.class);
    }

    private User getLoggedInUser(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(name, name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


}
