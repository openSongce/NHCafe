package com.ssafy.cafe.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.cafe.model.dao.UserDao;
import com.ssafy.cafe.model.dto.User;




@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    
    private OrderService orderService;
   
 

    @Override
    public User loginOrRegister(String number) {
        User user = userDao.login(number);
        
        if (user == null) {
            User newUser = new User();
            newUser.setNumber(number);
            newUser.setStamps(-1);  // 초기 적립
            userDao.insert(newUser);
            return userDao.selectById(number); // 등록 후 다시 조회해서 반환
        }
        
        
        return user;
    }

    @Override
    public boolean isUsedId(String number) {
    	User user=userDao.selectById(number);
    	if(user!=null) { // id를 사용하는 user가 있다면
    		return true;
    	}
 
        return false;
    }

    @Override
    public User selectUser(String number) {
        return userDao.selectById(number);
    }

    @Override
    public boolean updateStamp(User user) {
        int result = userDao.updateStamp(user);
        return result > 0;  // 성공 여부를 boolean으로 반환
    }

}
