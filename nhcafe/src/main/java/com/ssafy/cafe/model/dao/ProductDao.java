package com.ssafy.cafe.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.cafe.model.dto.Product;




@Mapper
public interface ProductDao {
	
	/**
     * 상품정보를 조회한다. 
     * 
     * @return
     */
	Product selectbyId(Integer productId);
    
    /**
     * 모든 상품정보를 조회한다. 
     * 
     * @return
     */
    List<Product> selectAll();
    
    /**
     * ID에 해당하는 상품의 설명과 함꼐 반환
     * 
     * @param productId
     * @return
     */
    Product selectWithDesc(Integer productId);
    
    
    
    List<Product> selectProductByType(String type);
}
