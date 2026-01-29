package com.CTRLTELA.CtrlTela.common;

import com.CTRLTELA.CtrlTela.common.Exception.UnauthorizedException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class AuthContext {
    private AuthContext() {}

    public static UUID tenantId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if ( auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("Not authenticated");
        }
        if (!(auth.getDetails() instanceof AuthDetails details)) {
            throw new UnauthorizedException("Missing auth details");
        }

        return details.tenantId();
    }

    public static String role() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if ( auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("Not authenticated");
        }
        if (!(auth.getDetails() instanceof AuthDetails details)) {
            throw new UnauthorizedException("Missing auth details");
        }
        return details.role();
    }
}
