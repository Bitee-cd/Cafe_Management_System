package com.bitee.Cafe_Management_System.dao;

import com.bitee.Cafe_Management_System.model.Product;
import com.bitee.Cafe_Management_System.wrapper.ProductWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductDao extends JpaRepository<Product,Integer> {
List<ProductWrapper> getAllProduct();

    @Modifying
    @Transactional
    Integer updateProductStatus(@Param("status") String status,@Param("id") int id);

   List<ProductWrapper> getByCategoryId(@Param("id") Integer id);

    List<ProductWrapper> getProductById(@Param("id") Integer id);
}
