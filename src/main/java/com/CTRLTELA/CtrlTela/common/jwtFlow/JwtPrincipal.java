package com.CTRLTELA.CtrlTela.common.jwtFlow;

import java.util.UUID;

public record JwtPrincipal(
        String email,
        String subject,
        UUID tenantId,
        String role,
        UUID deviceId,
        UUID screenId
) {
}
