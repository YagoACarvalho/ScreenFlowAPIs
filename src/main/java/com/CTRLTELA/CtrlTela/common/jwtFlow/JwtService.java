package com.CTRLTELA.CtrlTela.common.jwtFlow;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final JwtProperties props;

    public JwtService(JwtProperties props) {

        this.props = props;
        if (props.secretBase64() == null || props.secretBase64().isBlank()) {
            throw new IllegalStateException("security.jwt.secret-base64 não configurado");
        }
    }

    public String generatedAccessToken(String email, UUID tenantId, String role) {
        Instant now = Instant.now();

        Instant exp = now.plus(props.accessTokenMinutes(), ChronoUnit.MINUTES);


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

        byte[] keyBytes = java.util.Base64.getDecoder().decode(props.secretBase64());
        return  io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
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

    public String generateDeviceAcessToken(UUID deviceId, UUID tenantId, UUID screenId) {
        Instant now = Instant.now();
        Instant exp = now.plus(props.accessTokenMinutes(), ChronoUnit.MINUTES);

        return Jwts.builder()
                .subject("device:" + deviceId)
                .claim("tenantId", tenantId.toString())
                .claim("deviceId",  deviceId.toString())
                .claim("screenId", screenId.toString())
                .claim("role", "DEVICE")
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(signingKey())
                .compact();
    }


}
