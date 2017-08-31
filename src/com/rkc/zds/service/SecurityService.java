package com.rkc.zds.service;

import com.rkc.zds.config.security.SecurityProperties;
import com.rkc.zds.config.security.SecurityUtils;
import com.rkc.zds.config.security.WrappedRequest;
import com.rkc.zds.config.security.hmac.HmacException;
import com.rkc.zds.config.security.hmac.HmacSigner;
import com.rkc.zds.config.security.hmac.HmacToken;
import com.rkc.zds.config.security.hmac.HmacUtils;
import com.rkc.zds.dto.LoginDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Security service Perform jwt and hmac verification Created by michael on
 * 22/06/17.
 */
@Service
public class SecurityService {

	public static final Integer JWT_TTL = 20;
	private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

	@Autowired
	private UserDetailsService userDetailsService;

	// @Autowired
	private SecurityProperties securityProperties = SecurityProperties.getInstance();

	public SecurityService() {

	}

	// @Autowired
	public SecurityService(UserDetailsService userDetailsService, SecurityProperties securityProperties) {
		this.userDetailsService = userDetailsService;
		this.securityProperties = securityProperties;
	}

	/**
	 * Verify the JWT in current http request
	 * 
	 * @param request
	 *            current http request
	 * @throws HmacException
	 *             invalid hmac signature exception
	 * @throws ParseException
	 *             parsing exception
	 */
	public boolean verifyJwt(HttpServletRequest request) throws HmacException, ParseException {
		//Cookie jwtCookie = WebUtils.getCookie(request, AuthenticationService.ACCESS_TOKEN_COOKIE);
		Cookie jwtCookie = SecurityUtils.findJwtCookie(request);
		Assert.notNull(jwtCookie, "No jwt cookie found");

		String jwt = jwtCookie.getValue();
		String login = SecurityUtils.getJwtClaim(jwt, AuthenticationService.JWT_CLAIM_LOGIN);
		Assert.notNull(login, "No login found in JWT");

		Assert.isTrue(SecurityUtils.verifyJWT(jwt, securityProperties.getJwt().getSecret()),
				"The Json Web Token is invalid");

		Assert.isTrue(!SecurityUtils.isJwtExpired(jwt), "The Json Web Token is expired");

		this.authenticate(login);
		
		return true;
	}

	/**
	 * Verify the hmac request
	 * 
	 * @param request
	 *            current http request
	 * @return true if valid, false otherwise
	 * @throws HmacException
	 *             hmac exception
	 * @throws IOException
	 *             io exception
	 */
	public boolean verifyHmac(WrappedRequest request) throws HmacException, IOException {

		// Get Authentication header
		//Cookie jwtCookie = WebUtils.getCookie(request, AuthenticationService.ACCESS_TOKEN_COOKIE);

		Cookie jwtCookie = SecurityUtils.findJwtCookie(request);
		Assert.notNull(jwtCookie, "No jwt cookie found");

		String jwt = jwtCookie.getValue();

		if (jwt == null || jwt.isEmpty()) {
			throw new HmacException("The JWT is missing from the '" + HmacUtils.AUTHENTICATION + "' header");
		}

		String digestClient = request.getHeader(HmacUtils.X_DIGEST);

		if (digestClient == null || digestClient.isEmpty()) {
			throw new HmacException("The digest is missing from the '" + HmacUtils.X_DIGEST + "' header");
		}

		// Get X-Once header
		String xOnceHeader = request.getHeader(HmacUtils.X_ONCE);

		if (xOnceHeader == null || xOnceHeader.isEmpty()) {
			throw new HmacException("The date is missing from the '" + HmacUtils.X_ONCE + "' header");
		}

		String url = request.getRequestURL().toString();
		if (request.getQueryString() != null) {
			url += "?" + URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8.displayName());
		}

		String encoding = SecurityUtils.getJwtClaim(jwt, SecurityUtils.ENCODING_CLAIM_PROPERTY);

		// Get hmac secret from config
		String hmacSharedSecret = securityProperties.getHmac().getSecret();
		Assert.notNull(hmacSharedSecret, "Secret key cannot be null");

		String message;
		if ("POST".equals(request.getMethod()) || "PUT".equals(request.getMethod())
				|| "PATCH".equals(request.getMethod())) {
			message = request.getMethod().concat(request.getBody()).concat(url).concat(xOnceHeader);
		} else {
			message = request.getMethod().concat(url).concat(xOnceHeader);
		}

		// Digest are calculated using a public shared secret
		String digestServer = SecurityUtils.encodeMac(hmacSharedSecret, message, encoding);
		logger.debug("HMAC JWT: {}", jwt);
		logger.debug("HMAC url digest: {}", url);
		logger.debug("HMAC Message server: {}", message);
		logger.debug("HMAC Secret server: {}", hmacSharedSecret);
		logger.debug("HMAC Digest server: {}", digestServer);
		logger.debug("HMAC Digest client: {}", digestClient);
/* TODO
		if (digestClient.equals(digestServer)) {
			logger.debug("Request is valid, digest are matching");
			return true;
		} else {
			logger.debug("Server message: " + message);
			throw new HmacException("Digest are not matching! Client: " + digestClient + " / Server: " + digestServer);
		}
*/
		return true;
	}

	/**
	 * Authentication for every request - Triggered by every http request except the
	 * authentication
	 * 
	 * @see fr.redfroggy.hmac.configuration.security.XAuthTokenFilter Set the
	 *      authenticated user in the Spring Security context
	 * @param username
	 *            username
	 */
	private void authenticate(String username) {
		UserDetails details = userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(details,
				details.getPassword(), details.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}
}
