package com.rkc.zds.service;

import com.rkc.zds.config.security.SecurityUser;
import com.rkc.zds.dto.AuthorityDto;
import com.rkc.zds.dto.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
//import fr.redfroggy.hmac.mock.MockUsers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Hmac user details service
 * Created by Michael DESIGAUD on 15/02/2016.
 */
@Component
public class HmacUserDetailsService implements UserDetailsService{
    @Autowired
    private UserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    	UserDto userDTO = userService.findByUserName(username);

    	if (userDTO == null) {
            throw new UsernameNotFoundException("User "+username+" not found");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if(!userDTO.getAuthorities().isEmpty()){
            for(AuthorityDto authority : userDTO.getAuthorities()){
                authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
            }
        }

        return new SecurityUser(userDTO.getId(),userDTO.getLogin(),userDTO.getPassword(),userDTO.getProfile(),authorities);
    }
}
