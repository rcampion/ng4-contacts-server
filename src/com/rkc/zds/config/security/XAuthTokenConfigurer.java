package com.rkc.zds.config.security;

import com.rkc.zds.service.AuthenticationService;
import com.rkc.zds.service.SecurityService;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class XAuthTokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private AuthenticationService authenticationService;

    private SecurityService securityService;

//    public XAuthTokenConfigurer(AuthenticationService authenticationService){
//        this.authenticationService = authenticationService;
 //   }

    XAuthTokenConfigurer(SecurityService securityService){
        this.securityService = securityService;
    }
    
    @Override
    public void configure(HttpSecurity builder) throws Exception {
        XAuthTokenFilter xAuthTokenFilter = new XAuthTokenFilter(securityService);

        //Trigger this filter before SpringSecurity authentication validator
        builder.addFilterBefore(xAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
