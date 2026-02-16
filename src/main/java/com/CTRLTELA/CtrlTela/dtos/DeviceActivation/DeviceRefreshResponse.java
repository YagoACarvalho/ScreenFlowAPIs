package com.CTRLTELA.CtrlTela.dtos.DeviceActivation;

import java.util.UUID;

public record DeviceRefreshResponse(
        String accessToken,
        String refreshToken,
        UUID deviceId,
        UUID screenId,
        UUID tenantId
) {
}
