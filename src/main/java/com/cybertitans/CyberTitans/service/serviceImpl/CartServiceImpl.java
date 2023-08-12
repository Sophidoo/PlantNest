package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cybertitans.CyberTitans.dto.CartDTO;
import com.cybertitans.CyberTitans.dto.CartItemDTO;
import com.cybertitans.CyberTitans.dto.ReviewResponseDTO;
import com.cybertitans.CyberTitans.exception.Exception;
import com.cybertitans.CyberTitans.exception.ResourceNotFoundException;
import com.cybertitans.CyberTitans.model.Cart;
import com.cybertitans.CyberTitans.model.CartItem;
import com.cybertitans.CyberTitans.model.Product;
import com.cybertitans.CyberTitans.model.User;
import com.cybertitans.CyberTitans.repository.CartItemRepository;
import com.cybertitans.CyberTitans.repository.CartRepository;
import com.cybertitans.CyberTitans.repository.ProductRepository;
import com.cybertitans.CyberTitans.repository.UserRepository;
import com.cybertitans.CyberTitans.service.CartService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final ModelMapper mapper;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(ModelMapper mapper, CartRepository cartRepository, UserRepository userRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.mapper = mapper;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public CartItemDTO addToCart(Long productId) {
        User user = getLoggedInUser();

        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product", "id", productId));
        Cart userCart = user.getCart();
        System.out.println(userCart);
        Optional<CartItem> productAlreadyInCart = cartItemRepository.findByCartAndProduct(userCart, product);
        if(productAlreadyInCart.isEmpty()){
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(1);
            cartItem.setUnitPrice(product.getProductPrice());
            cartItem.setSubTotal(product.getProductPrice());
            cartItem.setCart(userCart);
            cartItem.setProduct(product);


            CartItem save = cartItemRepository.save(cartItem);
            userCart.getCartItemList().add(save);
            userCart.setCartQuantity(userCart.getCartQuantity() + 1);

            List<CartItem> cartItemList = userCart.getCartItemList();
            double sum = cartItemList.stream().mapToDouble(CartItem::getSubTotal).sum();
            userCart.setCartTotal(sum);
            cartRepository.save(userCart);
            return mapper.map(save, CartItemDTO.class);

        }

        CartItem cartItem = productAlreadyInCart.get();
        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItem.setSubTotal(cartItem.getSubTotal() + product.getProductPrice());
        CartItem save = cartItemRepository.save(cartItem);

        List<CartItem> cartItemList = userCart.getCartItemList();
        double sum = cartItemList.stream().mapToDouble(CartItem::getSubTotal).sum();
        userCart.setCartTotal(sum);
        cartRepository.save(userCart);
        return mapper.map(save, CartItemDTO.class);
    }

    @Override
    @Transactional
    public String clearCart() {
        User user = getLoggedInUser();
        Cart userCart = user.getCart();
        cartItemRepository.deleteAllByCart_Id(userCart.getId());
        userCart.setCartTotal(0);
        userCart.setCartQuantity(0);
        cartRepository.save(userCart);
        return "Cart cleared Successfully";
    }

    @Override
    public String removeCartItem(Long cartItemId) {
        User user = getLoggedInUser();
        Cart userCart = user.getCart();
        Optional<CartItem> cartItemCheck = cartItemRepository.findByCart_IdAndId(userCart.getId(), cartItemId);
        if (cartItemCheck.isPresent()) {
            CartItem cartItem = cartItemCheck.get();
                removeItem(cartItemId, userCart, cartItem);
            return "Item removed from user cart";
        } else {
            return new Exception(HttpStatus.NOT_FOUND, "Item is not in user cart").getMessage();
        }
    }

    private void removeItem(long cartItemId, Cart cart, CartItem cartItem) {
        cartItemRepository.deleteById(cartItemId);
        cart.setCartTotal(cart.getCartTotal() - cartItem.getSubTotal());
        cart.setCartQuantity(cart.getCartQuantity()-1);
        cartRepository.save(cart);
    }

    @Override
    public String reduceQuantityInCart(Long cartItemid) {
        User user = getLoggedInUser();
        Cart userCart = user.getCart();
        CartItem cartItem = cartItemRepository.findByCart_IdAndId(userCart.getId(), cartItemid).orElseThrow(() -> new Exception(HttpStatus.NOT_FOUND, "Cart item can not be found"));
        if(cartItem.getQuantity() > 1){
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            double productprice = cartItem.getProduct().getProductPrice();
            cartItem.setSubTotal(cartItem.getSubTotal() - productprice);
            cartItemRepository.save(cartItem);
            userCart.setCartTotal(userCart.getCartTotal() - productprice);
            cartRepository.save(userCart);
            return "Quantity reduced by 1";
        }else{
            removeItem(cartItemid, userCart, cartItem);
            return "Item removed from user cart";
        }
    }

    @Override
    public String increaseQuantityInCart(Long cartItemId) {
        User user = getLoggedInUser();
        Cart cart = user.getCart();
        Optional<CartItem> cartItemCheck = cartItemRepository.findByCart_IdAndId(user.getCart().getId(), cartItemId);
        if (cartItemCheck.isPresent()) {
            CartItem cartItem = cartItemCheck.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            double productPrice = cartItem.getProduct().getProductPrice();
            cartItem.setSubTotal(cartItem.getSubTotal() + productPrice);
            cartItemRepository.save(cartItem);
            cart.setCartTotal(cart.getCartTotal() + productPrice);
            cartRepository.save(cart);
            return "Quantity increased by 1";
        } else{
            throw new Exception(HttpStatus.NOT_FOUND,"Item is not in user cart");
        }
    }

    @Override
    public CartDTO viewCart() {
        User user = getLoggedInUser();
        if (user != null) {
            Cart cart = cartRepository.findByUser(user);
            if(cart == null){
                throw new Exception(HttpStatus.UNAUTHORIZED, "User Cart is empty");
            }
            return mapper.map(cart, CartDTO.class);
        }
        throw new Exception(HttpStatus.UNAUTHORIZED, "Authorization failed");
    }

    private User getLoggedInUser(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(name, name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
