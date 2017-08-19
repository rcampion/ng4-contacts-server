package com.rkc.zds.service;

import com.rkc.zds.config.security.hmac.HmacRequester;
import com.rkc.zds.dto.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Hmac Requester service
 * Created by Michael DESIGAUD on 16/02/2016.
 */
@Service
public class DefaultHmacRequester implements HmacRequester{

	@Autowired
    private UserService userService;
	
    @Override
    public Boolean canVerify(HttpServletRequest request) {
        return request.getRequestURI().contains("/api") && !request.getRequestURI().contains("/api/authenticate");
    }

    @Override
    public String getPublicSecret(String iss) {
        UserDto userDTO = userService.findById(Integer.valueOf(iss));
        if(userDTO != null){
            return userDTO.getPublicSecret();
        }
        return null;
    }
}
