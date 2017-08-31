package com.rkc.zds.config.security.hmac;

import com.rkc.zds.config.security.WrappedRequest;
import com.rkc.zds.service.AuthenticationService;
import com.rkc.zds.service.SecurityService;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Hmac verification filter Created by Michael DESIGAUD on 16/02/2016.
 */
public class HmacSecurityFilter extends GenericFilterBean {

	private static final Logger logger = LoggerFactory.getLogger(HmacSecurityFilter.class);

	@Autowired
	private SecurityService securityService;

	public HmacSecurityFilter(SecurityService securityService) {
		this.securityService = securityService;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		WrappedRequest wrappedRequest = new WrappedRequest(request);

		try {

			if (!request.getRequestURI().contains("/api") || request.getRequestURI().contains("/api/authenticate")) {
				filterChain.doFilter(wrappedRequest, response);
			}
			/*
			 * else { if(securityService.verifyHmac(wrappedRequest)) {
			 * //if(securityService.verifyJwt(wrappedRequest)) {
			 * filterChain.doFilter(wrappedRequest, response); } }
			 */
			else {
				if(securityService.verifyJwt(wrappedRequest)) {
					 filterChain.doFilter(wrappedRequest, response); 
				}
			}
		} catch (Exception e) {
			logger.debug("Error while generating hmac token");
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
			response.setStatus(403);
			response.getWriter().write(e.getMessage());
		}
	}
}
