package com.ssafy.cafe.model.service;

import java.util.List;

import com.ssafy.cafe.model.dto.Product;

public interface ProductService {
    /**
     * 모든 상품 정보를 반환한다.
     * @return
     */
    List<Product> getProductList();
    
    Product getProductDesc(Integer productId);
    
    
    List<Product> getProductListByType(String type);
    
}
