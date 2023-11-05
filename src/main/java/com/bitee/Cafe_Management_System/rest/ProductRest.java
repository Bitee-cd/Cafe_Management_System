package com.bitee.Cafe_Management_System.rest;

import com.bitee.Cafe_Management_System.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/product")
public interface ProductRest {
    @PostMapping(path="/add")
    ResponseEntity<String> addNewProduct(@RequestBody(required = true) Map<String,String> requestMap);

    @GetMapping()
    ResponseEntity<List<ProductWrapper>> getAllProducts();

    @PostMapping(path="/update")
    ResponseEntity<String> updateProduct(@RequestBody(required = true) Map<String,String> requestMap);

    @DeleteMapping(path="/delete/{id}")
    ResponseEntity<String> deleteProduct(@PathVariable(required = true) Integer id);

    @PostMapping(path="/update-status")
    ResponseEntity<String> updateStatus(@RequestBody(required = true) Map<String,String> requestMap);

    @GetMapping(path="/category/{id}")
    ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable(required = true) Integer id );

    @GetMapping(path="/{id}")
    ResponseEntity<List<ProductWrapper>> getProductById(@PathVariable(required = true) Integer id );
}
