package com.ssafy.cafe.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.cafe.model.dto.Order;
import com.ssafy.cafe.model.dto.OrderDetail;
import com.ssafy.cafe.model.dto.OrderDetailInfo;






@Mapper
public interface OrderDetailDao {
    /**
     * 주문상세정보를 입력한다. (make order에서)
     * 
     * @param detail
     * @return
     */
    int insert(OrderDetail detail);
    
    
    /**
     * orderId가 같은 데이터를 오름차순으로 반환
     * 
     * @param orderId
     * @return
     */
	List<OrderDetailInfo> getOrderDetailInfo(Integer orderId);
}
