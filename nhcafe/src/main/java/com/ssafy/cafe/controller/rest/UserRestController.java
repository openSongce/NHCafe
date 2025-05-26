package com.ssafy.cafe.controller.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cafe.model.dto.OrderInfo;
import com.ssafy.cafe.model.dto.User;
import com.ssafy.cafe.model.service.OrderService;
import com.ssafy.cafe.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/rest/user")
@CrossOrigin("*")
@Tag(name = "user controller", description = "사용자 로그인등 사용자 기능을 정의한다.")
public class UserRestController {
	
	
	@Autowired
	UserService service;
	
	
	@Autowired
	OrderService oService;
	
	private static final Logger log = LoggerFactory.getLogger(UserRestController.class);
	
	

	
	@PostMapping("/number")
	@Operation(
		    summary = "번호 기반 로그인 및 자동 등록",
		    description = "사용자가 번호를 보내면, 이미 존재하면 기존 사용자 정보를, 없으면 새로 등록한 후 정보를 반환한다."
		)
	public int loginOrRegister(@RequestBody User user, HttpServletResponse response) throws UnsupportedEncodingException {
	    String number = user.getNumber();
	    User result = service.loginOrRegister(number);

	    if (result != null) {
	        String cookieValue = URLEncoder.encode(result.getNumber(), "UTF-8");
	        Cookie c = new Cookie("loginNumber", cookieValue);
	        c.setMaxAge(60 * 60 * 24 * 7);
	        c.setPath("/");
	        response.addCookie(c);
	    }

	    return result.getStamps();
	}
	
	


}
