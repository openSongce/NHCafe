package com.ssafy.cafe.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.cafe.model.dao.ProductDao;
import com.ssafy.cafe.model.dto.Product;

@Service
public class ProductServiceImpl implements ProductService{


    @Autowired
    private ProductDao pDao;

   
    
    
    @Override
    public List<Product> getProductList() {
        return pDao.selectAll();
    }

	@Override
	public Product getProductDesc(Integer productId) {
		return pDao.selectWithDesc(productId);
	}

	@Override
	public List<Product> getProductListByType(String type) {
	    return pDao.selectProductByType(type);
	}

    
    
  
}
