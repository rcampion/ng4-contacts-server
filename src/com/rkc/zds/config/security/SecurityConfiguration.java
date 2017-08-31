package com.rkc.zds.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import com.rkc.zds.config.security.hmac.HmacRequester;
import com.rkc.zds.config.security.hmac.HmacSecurityConfigurer;
import com.rkc.zds.service.AuthenticationService;
import com.rkc.zds.service.SecurityService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	@Qualifier("securityService")
	private SecurityService securityService;

	@Autowired
	@Qualifier("userDetailsService")
	UserDetailsService userDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private HmacRequester hmacRequester;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
        		.antMatchers("/api/logout")
                .antMatchers("/scripts/**/*.{js}")
                .antMatchers("/node_modules/**")
                .antMatchers("/assets/**")
                .antMatchers("*.{ico}")
                .antMatchers("/views/**/*.{html}");
                
                //.antMatchers("/app/**/*.{html}");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/api/authenticate").anonymous()
                    .antMatchers("/").anonymous()
                    .antMatchers("/favicon.ico").anonymous()
                    .antMatchers("/api/logout").anonymous()
                    .antMatchers("/api/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/contact").hasRole("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/api/contact").hasRole("ADMIN")
                    .antMatchers(HttpMethod.PUT, "/api/contact").hasRole("ADMIN")
                .and()
                .csrf()
                    .disable()
                    .headers()
                    .frameOptions().disable()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout()
                    .permitAll()
                .and()
                    .apply(authTokenConfigurer())
                .and()
                    .apply(hmacSecurityConfigurer());
//                .and().exceptionHandling().accessDeniedPage("/Access_Denied");
    }

    
//    webSecurityBuilder.ignoring()
 // ignore all URLs that start with /resources/ or /static/
//            .antMatchers("/resources/**", "/static/**");

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    private HmacSecurityConfigurer hmacSecurityConfigurer(){
        return new HmacSecurityConfigurer(securityService);
    }

    private XAuthTokenConfigurer authTokenConfigurer(){
        return new XAuthTokenConfigurer(securityService);
    }
    
	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}    
}
