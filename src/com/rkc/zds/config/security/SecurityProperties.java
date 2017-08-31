package com.rkc.zds.config.security;

//import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Security properties
 * Created by michael on 21/06/17.
 */
//@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

	private static SecurityProperties INSTANCE = new SecurityProperties();
	
	public static SecurityProperties getInstance(){
		return INSTANCE;
	}
	
    private Jwt jwt = new Jwt();
    private Hmac hmac = new Hmac();

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public Hmac getHmac() {
        return hmac;
    }

    public void setHmac(Hmac hmac) {
        this.hmac = hmac;
    }

    public static class Jwt {

        private String secret = "f09cce0a0753496b9baaae70b343a15f";

        private Integer maxAge = 1800;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public Integer getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(Integer maxAge) {
            this.maxAge = maxAge;
        }
    }

    public static class Hmac {

        private String secret = "e2aab888483944eb908a26572f26c5d5";

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }
    }
}
