package com.CTRLTELA.CtrlTela.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="security.jwt")
public record JwtProperties (
        String secret,
        Integer accessTokenMinutes) {

}
