package com.flameking.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @description  对token进行扩展
 */
public class JWTToken implements AuthenticationToken {
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
