package com.CTRLTELA.CtrlTela.common.jwtFlow;

import java.util.UUID;

public record JwtPrincipal(
        String email,
        UUID tenantId,
        String role
) {
}
