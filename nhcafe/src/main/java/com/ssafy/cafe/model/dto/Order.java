package com.ssafy.cafe.model.dto;

import java.util.Date;
import java.util.List;

public class Order {
 

	private Integer id;
    private String userId;
    private Date orderTime;
    private Character completed;
    private Integer usedStamp;
    private Integer price;
    
    private List<OrderDetail> details ;
    
   


	public Order(Integer id, String userId, Date orderTime, Character complited,Integer usedStamp,Integer price) {
        this.id = id;
        this.userId = userId;
        this.orderTime = orderTime;
        this.completed = complited;
        this.usedStamp=usedStamp;
        this.price=price;
    }

    
    public Order() {}

	public Integer getUsedStamp() {
		return usedStamp;
	}


	public void setUsedStamp(Integer usedStamp) {
		this.usedStamp = usedStamp;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Character getCompleted() {
		return completed;
	}

	public void setCompleted(Character completed) {
		this.completed = completed;
	}

	public List<OrderDetail> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetail> details) {
		this.details = details;
	}
   public Integer getPrice() {
		return price;
	}


	public void setPrice(Integer price) {
		this.price = price;
	}
 @Override
	public String toString() {
		return "Order [id=" + id + ", userId=" + userId + ", orderTime=" + orderTime + ", completed=" + completed
				+ ", usedStamp=" + usedStamp + ", price=" + price + ", details=" + details + "]";
	}
    
}
