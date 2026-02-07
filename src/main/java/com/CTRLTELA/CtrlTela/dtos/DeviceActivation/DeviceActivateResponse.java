package com.CTRLTELA.CtrlTela.dtos.DeviceActivation;

import java.util.UUID;

public record DeviceActivateResponse(
        UUID deviceId,
        UUID screenId,
        UUID tenanId,
        String refreshToken
) {
}
