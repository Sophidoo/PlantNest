package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cybertitans.CyberTitans.dto.OrderResponseDTO;
import com.cybertitans.CyberTitans.dto.ProductDTO;
import com.cybertitans.CyberTitans.dto.ProductResponseDTO;
import com.cybertitans.CyberTitans.enums.ProductType;
import com.cybertitans.CyberTitans.exception.Exception;
import com.cybertitans.CyberTitans.exception.ResourceNotFoundException;
import com.cybertitans.CyberTitans.model.UserOrder;
import com.cybertitans.CyberTitans.model.Product;
import com.cybertitans.CyberTitans.model.User;
import com.cybertitans.CyberTitans.repository.ProductRepository;
import com.cybertitans.CyberTitans.repository.UserRepository;
import com.cybertitans.CyberTitans.service.ProductService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository, ModelMapper mapper) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public ProductDTO getProductById(Long id) {
        User loggedUser = getLoggedInUser();
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        ProductDTO productDTO = mapper.map(product, ProductDTO.class);
        return productDTO;
    }

    @Override
    public ProductResponseDTO getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir, String filterBy, String filterParam, String startRange, String endRange) {
        Sort sort= sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        if("".equals(filterBy) && !"".equals(filterParam)){
                Page<Product> products = productRepository.findAllByFilterParam(pageable, filterParam.toLowerCase());
                return getProductResponse(products);
        }else{
            switch (filterBy){
                case "productName": {
                    Page<Product> filteredProducts = productRepository.findAllByProductNameContains(pageable, filterParam.toLowerCase());
                    return getProductResponse(filteredProducts);
                }
                case "productPrice": {
                    Page<Product> filteredProducts = productRepository.findAllByPriceBetween(pageable, Double.parseDouble(filterParam), Double.parseDouble(startRange), Double.parseDouble(endRange));
                    return getProductResponse(filteredProducts);
                }
                case "categoryName": {
                    Page<Product> filteredProducts = productRepository.findAllByCategory_CategoryName(pageable, filterParam.toLowerCase());
                    return getProductResponse(filteredProducts);
                }
                case "growthHabit": {
                    Page<Product> filteredProducts = productRepository.findAllByGrowthHabitContains(pageable, filterParam.toLowerCase());
                    return getProductResponse(filteredProducts);
                }
                case "lightLevel": {
                    Page<Product> filteredProducts = productRepository.findAllByLightLevelContains(pageable, filterParam.toLowerCase());
                    return getProductResponse(filteredProducts);
                }
                case "waterRequirement": {
                    Page<Product> filteredProducts = productRepository.findAllByWaterRequirementsContains(pageable, filterParam.toLowerCase());
                    return getProductResponse(filteredProducts);
                }
                case "productType": {
                    Page<Product> filteredProducts = productRepository.findAllByProductType(pageable, ProductType.valueOf(filterParam.toUpperCase()));
                    return getProductResponse(filteredProducts);
                }
                default:
                    Page<Product> products = productRepository.findAll(pageable);
                    return getProductResponse(products);
            }
        }
    }

    @Override
    public int getAvailableProductsCountByType(ProductType productType) {
        return productRepository.getAvailableProductCountByType(productType);
    }

    @Override
    public int getSoldProductsCountByType(ProductType productType) {
        return productRepository.getSoldProductCountByType(productType);
    }


    private ProductResponseDTO getProductResponse(Page<Product> products){
        List<Product> productList = products.getContent();
        List<ProductDTO> content = productList.stream().map(product -> mapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        System.out.println(content);

        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .content(content)
                .pageNo(products.getNumber())
                .totalPages(products.getTotalPages())
                .pageSize(products.getSize())
                .totalElements(products.getTotalElements())
                .last(products.isLast())
                .build();


        return productResponseDTO;

    }

    @Transactional
    @Override
    public String processPaymentAndUpdateProductQuantities(OrderResponseDTO orders) {
        List<Product> products = orders.getProductList();

        for(Product product : products){
            if (product.getQuantity() > 0) {
                product.setQuantity(product.getQuantity() - 1);

                product.setQuantitySold(product.getQuantitySold() + 1);

                productRepository.save(product);
            }
        }
        return "Payment Successfull";

    }

    private User getLoggedInUser(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(name, name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
