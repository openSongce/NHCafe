package com.ssafy.cafe.model.service;

import com.ssafy.cafe.model.dto.User;


public interface UserService {
	

	public User loginOrRegister(String number);
    
    /**
     * 해당 번호가 이미 사용 중인지를 반환한다.
     * @param id
     * @return
     */
    public boolean isUsedId(String number);

    /**
     * 번호 에 해당하는 User 정보를 반환한다.
     * 
     * @param id
     * @return
     * 조회된 User 정보를 반환한다.
     */
    public User selectUser(String number);
    
    
    
    
    
    boolean updateStamp(User user);


}
