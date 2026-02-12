package com.CTRLTELA.CtrlTela.common.login;

import java.util.UUID;

public record AuthDetails(
        UUID tenantId,
        String role,
        UUID deviceId,
        UUID screenId
) {
}
