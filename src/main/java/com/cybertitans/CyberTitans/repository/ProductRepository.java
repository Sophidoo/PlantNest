package com.cybertitans.CyberTitans.repository;

import com.cybertitans.CyberTitans.enums.ProductType;
import com.cybertitans.CyberTitans.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.quantity > 0 AND LOWER(CONCAT(p.productId, p.productName, p.productType, p.quantity, p.image, p.description, p.growthHabit, p.lightLevel, p.waterRequirement, p.productPrice,p.category.categoryName)) LIKE %?1%")
    Page<Product> findAllByFilterParam (Pageable pageable, String filterParam);

    @Query("SELECT p FROM Product p WHERE p.quantity > 0 AND LOWER(p.growthHabit) LIKE %?1%")
    Page<Product> findAllByGrowthHabitContains (Pageable pageable, String growthHabit);
    List<Product> findByProductIdIn(List<Long> productIds);
    Page<Product> findAllByProductType (Pageable pageable, ProductType productType);
    List<Product> findAllByProductType (ProductType productType);
    @Query("SELECT SUM(p.quantitySold) FROM Product p WHERE p.productType = :productType")
    int getTotalQuantitySold(ProductType productType);
    @Query("SELECT p FROM Product p WHERE p.quantity > 0 AND LOWER(p.lightLevel) LIKE %?1%")
    Page<Product> findAllByLightLevelContains (Pageable pageable, String lightLevel);

    @Query("SELECT p FROM Product p WHERE p.quantity > 0 AND LOWER(p.waterRequirement) LIKE %?1%")
    Page<Product> findAllByWaterRequirementsContains (Pageable pageable, String waterRequirement);

    @Query("SELECT p FROM Product p WHERE p.quantity > 0 AND LOWER(p.productName) LIKE %?1%")
    Page<Product> findAllByProductNameContains (Pageable pageable, String productName);

    @Query("SELECT p FROM Product  p WHERE p.quantity > 0 AND  :filterParam BETWEEN :startRange AND :endRange")
    Page<Product> findAllByPriceBetween (Pageable pageable, double filterParam, double startRange,double endRange);

    @Query("SELECT p FROM Product p WHERE p.quantity > 0 AND LOWER(p.category.categoryName) =:categoryName")
    Page<Product> findAllByCategory_CategoryName(Pageable pageable, @Param("categoryName") String categoryName);

    @Query("SELECT SUM(p.quantity) FROM Product p WHERE p.quantity > 0 AND p.productType = :productType")
    Integer getAvailableProductCountByType(@Param("productType") ProductType productType);

    @Query("SELECT SUM(p.quantitySold) FROM Product p WHERE p.quantity > 0 AND p.productType = :productType")
    Integer getSoldProductCountByType(@Param("productType") ProductType productType);

}
