package com.rkc.zds.config.security.hmac;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

import com.rkc.zds.config.security.XAuthTokenFilter;
import com.rkc.zds.service.SecurityService;

/**
 * Hmac Security filter configurer
 * Created by Michael DESIGAUD on 16/02/2016.
 */
public class HmacSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private SecurityService securityService;

    public HmacSecurityConfigurer(SecurityService securityService){
        this.securityService = securityService;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        HmacSecurityFilter hmacSecurityFilter = new HmacSecurityFilter(securityService);

        //Trigger this filter before SpringSecurity authentication validator
        builder.addFilterBefore(hmacSecurityFilter, XAuthTokenFilter.class);
    }
}

