package com.bitee.Cafe_Management_System.serviceImpl;

import com.bitee.Cafe_Management_System.JWT.JWTFilter;
import com.bitee.Cafe_Management_System.JWT.JwtUtil;
import com.bitee.Cafe_Management_System.constants.CafeConstants;
import com.bitee.Cafe_Management_System.dao.ProductDao;
import com.bitee.Cafe_Management_System.model.Category;
import com.bitee.Cafe_Management_System.model.Product;
import com.bitee.Cafe_Management_System.service.ProductService;
import com.bitee.Cafe_Management_System.utils.CafeUtils;
import com.bitee.Cafe_Management_System.wrapper.ProductWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
   @Autowired
    JwtUtil jwtUtil;

   @Autowired
    JWTFilter jwtFilter;
   @Autowired
    ProductDao productDao;
    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
               if(validateProductMap(requestMap,false)){
                    productDao.save(getProductFromMap(requestMap,false));
                    return CafeUtils.getResponseEntity("Product created successfully",HttpStatus.CREATED);
               }else return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }else return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try{
                return new ResponseEntity<>(productDao.getAllProduct(),HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
       try{
          if(jwtFilter.isAdmin()){
              if(validateProductMap(requestMap,true)){
                  Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
                  if(!optional.isEmpty()){
                      log.info("inside update product service");
                      Product newProduct = getProductFromMap(requestMap,true);
                      newProduct.setStatus(optional.get().getStatus());
                      productDao.save(newProduct);
                      return CafeUtils.getResponseEntity("Product Updated successfully",HttpStatus.OK);
                  } else return CafeUtils.getResponseEntity("Product doesn't exist",HttpStatus.BAD_REQUEST);
              }else return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
          }else return  CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
       }catch (Exception e){
           e.printStackTrace();
       }
       return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try{
            if(jwtFilter.isAdmin()) {
                Optional<Product> optional = productDao.findById(id);
                if (!optional.isEmpty()) {
                    productDao.deleteById(id);
                    return CafeUtils.getResponseEntity("product deleted successfully", HttpStatus.OK);
                } else return CafeUtils.getResponseEntity("Product doesn't exist", HttpStatus.NOT_FOUND);
            }else return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try{
          if (jwtFilter.isAdmin()){
              Optional optional= productDao.findById(Integer.parseInt(requestMap.get("id")));
              if(!optional.isEmpty()){
                  productDao.updateProductStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                  return CafeUtils.getResponseEntity("Product Status updated successfully",HttpStatus.OK);
              }else return CafeUtils.getResponseEntity("Product doesn't exist",HttpStatus.OK);
          }else return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try{
            return new ResponseEntity<>(productDao.getByCategoryId(id),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProductById(Integer id) {
        try{
            Optional<Product> optional = productDao.findById(id);
            if(!optional.isEmpty()) {
                return new ResponseEntity<>(productDao.getProductById(id), HttpStatus.OK);
            }else return  new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));
        Product product = new Product();
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        if(isAdd){
            product.setId(Integer.parseInt(requestMap.get("id")));

        }else{
           product.setStatus("true");
        }

                return product;
    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }else if(!validateId){
                return true;
            }
        }return false;
    }
}
