package com.CTRLTELA.CtrlTela.common.login;

import com.CTRLTELA.CtrlTela.common.exception.UnauthorizedException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class AuthContext {
    private AuthContext() {}

    private static AuthDetails details() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;

        Object d = auth.getDetails();
        if (d == null) return null;

        // blindagem contra ClassCastException
        if (!(d instanceof AuthDetails ad)) return null;

        return ad;
    }

    public static UUID tenantId() {
        var d = details();
        if (d == null || d.tenantId() == null) throw new UnauthorizedException("Unauthorized");
        return d.tenantId();
    }

    public static String role() {
        var d = details();
        if (d == null || d.role() == null) throw new UnauthorizedException("Unauthorized");
        return d.role();
    }

    public static boolean isDevice() {
        return "DEVICE".equalsIgnoreCase(role());
    }

    public static UUID deviceId() {
        var d = details();
        if (d == null || d.deviceId() == null) throw new UnauthorizedException("DeviceId ausente no token");
        return d.deviceId();
    }

    public static UUID screenId() {
        var d = details();
        if (d == null || d.screenId() == null) throw new UnauthorizedException("ScreenId ausente no token");
        return d.screenId();
    }
}

