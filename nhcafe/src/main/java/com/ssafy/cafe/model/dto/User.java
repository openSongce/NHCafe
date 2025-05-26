package com.ssafy.cafe.model.dto;

import java.util.ArrayList;
import java.util.List;

public class User {
 
	private String number;
    private Integer stamps;
    private List<Stamp> stampList = new ArrayList<>();
    
    

    public User(String number, Integer stamps) {
        this.number = number;
        this.stamps = stamps;
    }
    public User() {
    	
    }
    
    public String getNumber() {
 		return number;
 	}

 	public void setNumber(String number) {
 		this.number = number;
 	}

 	public Integer getStamps() {
 		return stamps;
 	}

 	public void setStamps(Integer stamps) {
 		this.stamps = stamps;
 	}

 	public List<Stamp> getStampList() {
 		return stampList;
 	}

 	public void setStampList(List<Stamp> stampList) {
 		this.stampList = stampList;
 	}
 	
 	@Override
	public String toString() {
		return "User [number=" + number + ", stamps=" + stamps + ", stampList=" + stampList + "]";
	}
  
    
    
}