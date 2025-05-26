package com.ssafy.cafe.model.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.cafe.model.dto.User;


@Mapper
public interface UserDao {
	
	
	User login(String number);
	
	
    /**
     * 사용자 정보를 추가한다.
     * @param user
     * @return
     */
    int insert(User user); //registerUser

    /**
     * 사용자의 Stamp 정보를 수정한다.
     * @param user
     * @return
     */
    int updateStamp(User user); //upDateUser
    
    /**
     * 사용자 정보를 조회한다.
     * @param userId
     * @return
     */

    User selectById(String userId); //getUserInfo

  
    
    
}
