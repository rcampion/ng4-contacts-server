package com.rkc.zds.config.security;

import com.rkc.zds.dto.UserDto;
import com.rkc.zds.repository.UserRepository;
import com.rkc.zds.config.security.hmac.HmacException;
import com.rkc.zds.config.security.hmac.HmacSigner;
import com.rkc.zds.config.security.hmac.HmacUtils;
import com.rkc.zds.controller.AuthenticationController;
import com.rkc.zds.service.AuthenticationService;
import com.rkc.zds.service.SecurityService;
import com.rkc.zds.service.UserService;

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
import java.io.IOException;
import java.text.ParseException;

public class XAuthTokenFilter extends GenericFilterBean{

    private SecurityService securityService;

    private AuthenticationService authenticationService;

//    public XAuthTokenFilter(AuthenticationService authenticationService){
//       this.authenticationService = authenticationService;
//    }
    XAuthTokenFilter(SecurityService securityService){
        this.securityService = securityService;
     }
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (!request.getRequestURI().contains("/api") || request.getRequestURI().contains("/api/authenticate")){
            filterChain.doFilter(request, response);
        } else {

            try {
                this.securityService.verifyJwt(request);

                filterChain.doFilter(request,response);
            } catch (HmacException | ParseException e) {
                e.printStackTrace();
                response.setStatus(403);
            }
        }

    }
/*    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (!request.getRequestURI().contains("/api") || request.getRequestURI().contains("/api/authenticate")){
            filterChain.doFilter(request, response);
        } else {

            try {

                Cookie jwtCookie = SecurityUtils.findJwtCookie(request);
                Assert.notNull(jwtCookie,"No jwt cookie found");

                String jwt = jwtCookie.getValue();
                String login = HmacSigner.getJwtClaim(jwt, AuthenticationService.JWT_CLAIM_LOGIN);
                Assert.notNull(login,"No login found in JWT");
                
                //UserDto userDTO = userRepository.findByUserName(login);
                //UserDto userDTO = this.userService.findByUserName(login);
                UserDto userDTO = this.authenticationService.findByUserName(login);
                
                Assert.notNull(userDTO,"No user found with login: "+login);

                Assert.isTrue(HmacSigner.verifyJWT(jwt,userDTO.getPrivateSecret()),"The Json Web Token is invalid");

                Assert.isTrue(!HmacSigner.isJwtExpired(jwt),"The Json Web Token is expired");

                String csrfHeader = request.getHeader(AuthenticationService.CSRF_CLAIM_HEADER);
                Assert.notNull(csrfHeader,"No csrf header found");

                String jwtCsrf = HmacSigner.getJwtClaim(jwt, AuthenticationService.CSRF_CLAIM_HEADER);
                Assert.notNull(jwtCsrf,"No csrf claim found in jwt");

                //Check csrf token (prevent csrf attack)
                Assert.isTrue(jwtCsrf.equals(csrfHeader));

                this.authenticationService.tokenAuthentication(login);
                filterChain.doFilter(request,response);
            } catch (HmacException | ParseException e) {
                e.printStackTrace();
                response.setStatus(403);
            }
        }

    }*/
}
