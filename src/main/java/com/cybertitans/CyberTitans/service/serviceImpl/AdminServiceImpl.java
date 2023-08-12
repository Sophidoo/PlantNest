package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cybertitans.CyberTitans.dto.*;
import com.cybertitans.CyberTitans.enums.Roles;
import com.cybertitans.CyberTitans.exception.Exception;
import com.cybertitans.CyberTitans.exception.ResourceNotFoundException;
import com.cybertitans.CyberTitans.model.*;
import com.cybertitans.CyberTitans.repository.*;
import com.cybertitans.CyberTitans.service.AdminService;
import com.cybertitans.CyberTitans.service.AuditTrialService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ModelMapper mapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AuditTrialService auditTrialService;
    private final MailingServiceImpl mailingService;
    private final CartRepository cartRepository;

    private final OtpServiceImpl otpService;

    public AdminServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, ProductImageRepository productImageRepository, ModelMapper mapper, OrderRepository orderRepository, UserRepository userRepository, AuditTrialService auditTrialService, MailingServiceImpl mailingService, CartRepository cartRepository, OtpServiceImpl otpService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.mapper = mapper;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.auditTrialService = auditTrialService;
        this.mailingService = mailingService;
        this.cartRepository = cartRepository;
        this.otpService = otpService;
    }

    @Override
    public String createProduct(ProductDTO productDTO) {
        Product product = new Product();
        Optional<Category> productCategory =categoryRepository.findByCategoryName(productDTO.getCategoryName().toLowerCase());
        Category category = new Category();
        if(productCategory.isEmpty()){
            category.setCategoryName(productDTO.getCategoryName());
            categoryRepository.save(category);
            product.setCategory(category);
        }else{
            category = productCategory.get();
            product.setCategory(category);
        }
        product.setProductName(productDTO.getProductName());
        product.setProductPrice(productDTO.getProductPrice());
        product.setDescription(productDTO.getDescription());
        product.setGrowthHabit(productDTO.getGrowthHabit());
        product.setProductPrice(productDTO.getProductPrice());
        product.setProductType(productDTO.getProductType());
        product.setLightLevel(productDTO.getLightLevel());
        product.setWaterRequirement(productDTO.getWaterRequirement());
        product.setQuantity(productDTO.getQuantity());
        Product save = productRepository.save(product);

        AuditTrialDTO auditTrial = new AuditTrialDTO();
        auditTrial.setUser(getLoggedInUser());
        auditTrial.setAudit("Product with id " + save.getProductId() + " was added successfully");
        auditTrial.setDate(LocalDate.now());
        auditTrialService.addToAuditTrial(auditTrial);
        return "Post created successfully with product id " + save.getProductId();
    }

    @Override
    public ProductImage saveProductImage(String imgUrl, Long id) {
        ProductImage productImage = new ProductImage();
        productImage.setImageURL(imgUrl);
        Optional<Product> createdProduct = productRepository.findById(id);

        return getProductImage(id, productImage, createdProduct);
    }

    @Override
    public String updateProduct(ProductDTO productDTO, Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("product", "id", id));
        product.setProductName(productDTO.getProductName());
        product.setProductPrice(productDTO.getProductPrice());
        product.setGrowthHabit(productDTO.getGrowthHabit());
        product.setProductPrice(productDTO.getProductPrice());
        product.setProductType(productDTO.getProductType());
        product.setDescription(productDTO.getDescription());
        product.setLightLevel(productDTO.getLightLevel());
        product.setWaterRequirement(productDTO.getWaterRequirement());
        product.setQuantity(productDTO.getQuantity());
        Product save = productRepository.save(product);

        AuditTrialDTO auditTrial = new AuditTrialDTO();
        auditTrial.setUser(getLoggedInUser());;
        auditTrial.setAudit("Product with id " + save.getProductId() + " was updated successfully");
        auditTrial.setDate(LocalDate.now());
        auditTrialService.addToAuditTrial(auditTrial);

        return "Product updated successfully with id " + save.getProductId();

    }

    @Override
    public ProductImage updateProductImage(String ImgUrl, Long id) {
        Optional<ProductImage> productImage =productImageRepository.findById(id);
        Optional<Product> productToBeUpdated = productRepository.findById(id);
        productImage.get().setImageURL(ImgUrl);
        return getProductImage(id, productImage.get(), productToBeUpdated);
    }

    @Override
    public String deleteProduct(Long Id) {
        Optional<Product> product = productRepository.findById(Id);
        if(!product.isPresent()){
            return new ResourceNotFoundException("Product", "id", Id).getMessage();
        }
        productRepository.deleteById(Id);

        AuditTrialDTO auditTrial = new AuditTrialDTO();
        auditTrial.setUser(getLoggedInUser());
        auditTrial.setAudit("Product with id " + Id + " was deleted");
        auditTrial.setDate(LocalDate.now());
        auditTrialService.addToAuditTrial(auditTrial);
        return "Product deleted successfully";
    }

    @Override
    public AllOrderResponseDTO viewAllOrders(int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = getLoggedInUser();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Orders> orders = orderRepository.findAll(pageable);
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

    @Override
    public OrderResponseDTO viewParticularOrder(long orderId) {
        Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        return mapper.map(orders, OrderResponseDTO.class);
    }

    @Override
    public String addAdmin(AdminDTO adminDTO) {
        if (userRepository.existsByUsername(adminDTO.getUsername()) || userRepository.existsByEmail(adminDTO.getEmail())) {
            User user = userRepository.findByUsernameOrEmail(adminDTO.getUsername(), adminDTO.getEmail()).orElseThrow(() -> new Exception(HttpStatus.NOT_FOUND, "User not found"));
            user.setRole(Roles.ADMIN);
            userRepository.save(user);
            return "User role changed to admin successfully";
        }



        User user = new User();
        if(!adminDTO.getFirstname().equals(null)){
            user.setFirstname(adminDTO.getFirstname());
        }
        if(!adminDTO.getLastname().equals(null)){
            user.setLastname(adminDTO.getLastname());
        }
        user.setUsername(adminDTO.getUsername());
        user.setEmail(adminDTO.getEmail());
        user.setPassword(passwordEncoder().encode(adminDTO.getPassword()));

        user.setRole(Roles.ADMIN);
        user.setVerified(true);
        User save = userRepository.save(user);

        //        Creating user cart as user is created
        Cart cart = new Cart(save);
        cart.setUser(save);
        cartRepository.save(cart);
        return "Successful, Kindly login to continue";
    }




    private static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public String editAdmin(UpdateProfileDTO adminDTO, Long adminId) {
        User loggedUser = userRepository.findById(adminId).orElseThrow(() -> new ResourceNotFoundException("Admin", "id", adminId));
        if(!loggedUser.getRole().equals(Roles.ADMIN)){
            throw new Exception(HttpStatus.FORBIDDEN, "You can not edit this user details");
        }
        loggedUser.setUsername(adminDTO.getUsername());
        loggedUser.setFirstname(adminDTO.getFirstname());
        loggedUser.setLastname(adminDTO.getLastname());
        loggedUser.setEmail(adminDTO.getEmail());
        loggedUser.setPhoneNumber(adminDTO.getPhoneNumber());
        userRepository.save(loggedUser);
        return "Admin details updated successfully";
    }

    @Override
    public UpdateProfileDTO updateSuperAdminDetails(UpdateProfileDTO adminDTO) {
        User loggedUser = getLoggedInUser();
        loggedUser.setUsername(adminDTO.getUsername());
        loggedUser.setFirstname(adminDTO.getFirstname());
        loggedUser.setLastname(adminDTO.getLastname());
        loggedUser.setEmail(adminDTO.getEmail());
        loggedUser.setPhoneNumber(adminDTO.getPhoneNumber());
        userRepository.save(loggedUser);
        return mapper.map(loggedUser, UpdateProfileDTO.class);
    }

    @Override
    public String deleteAdmin(Long adminId) {
        User loggedUser = userRepository.findById(adminId).orElseThrow(() -> new ResourceNotFoundException("Admin", "id", adminId));
        loggedUser.setRole(Roles.USER);

        userRepository.save(loggedUser);
        return "Admin deleted successfully";
    }

    private ProductImage getProductImage(Long id, ProductImage productImage, Optional<Product> productToBeUpdated){
        Product product = new Product();
        if(productToBeUpdated.isPresent()){
            productImage.setProduct(productToBeUpdated.get());
            product.setImage(productImage.getImageURL());
        }else {
            throw new ResourceNotFoundException("Product", "Product Id", id);
        }
        productToBeUpdated.get().setImage(productImage.getImageURL());
        Product save = productRepository.save(productToBeUpdated.get());

        AuditTrialDTO auditTrial = new AuditTrialDTO();
        auditTrial.setUser(getLoggedInUser());
        auditTrial.setAudit("Product Image for product with id " + save.getProductId() + " was added successfully");
        auditTrial.setDate(LocalDate.now());
        auditTrialService.addToAuditTrial(auditTrial);
        return productImageRepository.save(productImage);
    }

    private User getLoggedInUser(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(name, name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
