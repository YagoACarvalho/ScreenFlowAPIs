package com.CTRLTELA.CtrlTela.dtos.DeviceActivation;

import jakarta.validation.constraints.NotBlank;

public record DeviceRefreshRequest(
        @NotBlank
        String deviceFingerprint,
        @NotBlank
        String refreshToken
) {
}
