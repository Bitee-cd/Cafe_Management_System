package com.bitee.Cafe_Management_System.Category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("api/v1/category")
public interface CategoryRest {
    @GetMapping(path = "")
    ResponseEntity<List<Category>> getAllCategories(@RequestParam(required = false) String filterValue);

    @GetMapping(path="{id}")
    ResponseEntity<Category> getSingleCategory(@PathVariable String id);

    @PostMapping(path = "/add")
    ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String, String> requestMap);


    @PutMapping (path = "{id}")
    ResponseEntity<String> updateCategory(
            @PathVariable String id,
            @RequestBody(required = true) Map<String, String> requestMap);

    ResponseEntity<String> updateCategory(Map<String, String> requestMap);
}
