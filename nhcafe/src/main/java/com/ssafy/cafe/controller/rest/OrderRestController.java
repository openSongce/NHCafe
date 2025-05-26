package com.ssafy.cafe.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cafe.model.dto.Order;
import com.ssafy.cafe.model.dto.OrderDetail;
import com.ssafy.cafe.model.dto.OrderInfo;
import com.ssafy.cafe.model.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/rest/order")
@CrossOrigin("*")
public class OrderRestController {
	
    @Autowired
    private OrderService oService;

    @PostMapping
    @Operation(summary="order 객체를 저장하고 추가된 Order의 id를 반환한다.", 
            description="아래 형태로 입력하면 주문이 입력된다.<br>"
        		+ "'010-1111-1111' 고객이 1번 상품(아메리카노)를 2개 주문함.<br>"
        		+ "<pre>{<br>"
        		+ "  \"completed\": \"N\",<br>"
        		+ "  \"details\": [<br>"
        		+ "    {<br>"
        		+ "      \"productId\": 1,<br>"
        		+ "      \"quantity\": 2<br>"
        		+ "    }<br>"
        		+ "  ],<br>"
        		+ "  \"orderTable\": \"앱주문\",<br>"
        		+ "  \"userId\": \"010-1111-1111\"<br>"
        		+ "}</pre>" 
        		+ "OrderDetail 이 없으면 실패 (-1을 리턴)<br>"
        		+ "주문건수가 음수이거나, 주문건수가 15건 초과면 실패 (-2를 리턴)")
    public int makeOrder(@RequestBody Order order) {
    	if(order.getDetails().isEmpty()) return -1;
    	for(OrderDetail detail:order.getDetails()) {
    		if(detail.getQuantity() <= 0 || detail.getQuantity() > 15) return -2;
    	}
    	
    	return oService.makeOrder(order);
    }
    
    @GetMapping("/{orderId}")
    @Operation(summary="{orderId}에 해당하는 주문의 상세 내역을 목록 형태로 반환한다. "
    		+ "이 정보는 사용자 정보 화면의 주문 내역 조회에서 사용된다.")
    public OrderInfo getOrderInfo(@PathVariable Integer orderId){
    	return oService.getOrderInfo(orderId);
    }
    
    
    
    
}
