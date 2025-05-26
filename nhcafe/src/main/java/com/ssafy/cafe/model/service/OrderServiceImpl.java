package com.ssafy.cafe.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.cafe.model.dao.OrderDao;
import com.ssafy.cafe.model.dao.OrderDetailDao;
import com.ssafy.cafe.model.dao.StampDao;
import com.ssafy.cafe.model.dao.UserDao;
import com.ssafy.cafe.model.dto.Order;
import com.ssafy.cafe.model.dto.OrderDetail;
import com.ssafy.cafe.model.dto.OrderDetailInfo;
import com.ssafy.cafe.model.dto.OrderInfo;
import com.ssafy.cafe.model.dto.Stamp;
import com.ssafy.cafe.model.dto.User;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao oDao;
    
    @Autowired
    OrderDetailDao dDao;
    
    @Autowired
    StampDao sDao;
    
    @Autowired
    UserDao uDao;
    
    @Override
    @Transactional
    public int makeOrder(Order order) {
        oDao.insert(order);
        // t_order 테이블에 injection
        int id = order.getId();
        String userId = order.getUserId();
        int quantity = 0;

        // t_order_detail 테이블에 있는만큼 injection
        for(OrderDetail detail:order.getDetails()) {
        	detail.setOrderId(id);
        	dDao.insert(detail);
        	quantity += detail.getQuantity();
        }
        
        // 누적된 quantity만큼 stamp 객체 생성 및 t_stamp 테이블에 injection
        Stamp stamp = new Stamp(userId, id, quantity);
        sDao.insert(stamp);
        
        // userId가 같은 user를 받아온 후 stamp를 갱신함
        User user = uDao.selectById(userId);
        
        int usedStamp = order.getUsedStamp() == null ? 0 : order.getUsedStamp();
        int updatedStamp = user.getStamps() + quantity - usedStamp;
        if (updatedStamp < 0) updatedStamp = 0;
        user.setStamps(updatedStamp);
        uDao.updateStamp(user);
        
        
        //stamp할인가격계산
        int discount = usedStamp * 100;
        order.setPrice(discount);
        
        
//        user.setStamps(user.getStamps() + quantity);
//        uDao.updateStamp(user);
//        
        return id;
    }

    @Override
    public Order getOrderDetails(Integer orderId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Order> getOrderByUser(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateOrder(Order order) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public OrderInfo getOrderInfo(Integer orderId) {
    	OrderInfo orderInfo = oDao.selectOrderInfo(orderId);
    	List<OrderDetailInfo> details = dDao.getOrderDetailInfo(orderId);
    	orderInfo.setDetails(details);
    	
    	int totalPrice=0;
    	for(OrderDetailInfo detail:details) {
    		totalPrice+=detail.getSumPrice();
    	}
    	
    	orderInfo.setTotalPrice(totalPrice);
    	
        return orderInfo;
    }

  


}
