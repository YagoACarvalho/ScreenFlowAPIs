package com.CTRLTELA.CtrlTela.dtos.DeviceActivation;

import java.util.UUID;

public record DeviceRefreshResponse(
        String accessToken,
        UUID deviceId,
        UUID screenId,
        UUID tenantId
) {
}
