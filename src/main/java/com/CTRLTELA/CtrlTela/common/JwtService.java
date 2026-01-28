package com.CTRLTELA.CtrlTela.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final JwtProperties props;

    public JwtService(JwtProperties props) {
        System.out.println("JWT minutes = " + props.accessTokenMinutes());
        this.props = props;
    }

    public String generatedAccessToken(String email, UUID tenantId, String role) {
        Instant now = Instant.now();
        System.out.println("JWT minutes = " + props.accessTokenMinutes());
        Instant exp = now.plus(props.accessTokenMinutes(), ChronoUnit.MINUTES);

        System.out.println("JWT now=" + now + " exp=" + exp);

        System.out.println("JWT secret len=" + (props.secret() == null ? "null" : props.secret().length()));
        System.out.println("JWT minutes=" + props.accessTokenMinutes());

        return Jwts.builder()
                .subject(email)
                .claim("tenantId", tenantId.toString())
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(signingKey())
                .compact();
    }

    private SecretKey signingKey() {

        return Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
    }

    public JwtPrincipal parseAndValidate(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String email = claims.getSubject();
        String tenantIdStr = claims.get("tenantId", String.class);
        String role = claims.get("role", String.class);

        return new JwtPrincipal(email, UUID.fromString(tenantIdStr), role);
    }


}
