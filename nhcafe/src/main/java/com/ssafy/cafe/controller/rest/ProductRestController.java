package com.ssafy.cafe.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cafe.model.dto.Product;
import com.ssafy.cafe.model.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;


@RestController
@RequestMapping("/rest/product")
@CrossOrigin("*")
public class ProductRestController {

	@Autowired
	ProductService pService;
	
	@GetMapping
	@Operation(summary="전체 상품의 목록을 반환한다.")
	public ResponseEntity<List<Product>> getProduct(){
		List<Product> list = pService.getProductList();
		
		return new ResponseEntity<List<Product>>(list, HttpStatus.OK);
	}
	
	
	
	
	@GetMapping("/{productId}")
	@Operation(summary="해당 ID의 상품정보와 설명을 반환한다")
	public ResponseEntity<Product> getProductDesc(@PathVariable Integer productId) {
		Product product=pService.getProductDesc(productId);
		
		
		return new ResponseEntity<>(product,HttpStatus.OK);
	}
	

}