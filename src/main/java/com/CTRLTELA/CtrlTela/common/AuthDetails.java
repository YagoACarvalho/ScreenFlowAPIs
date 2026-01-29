package com.CTRLTELA.CtrlTela.common;

import java.util.UUID;

public record AuthDetails(
        UUID tenantId,
        String role
) {
}
