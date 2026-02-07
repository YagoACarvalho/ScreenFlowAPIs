package com.CTRLTELA.CtrlTela.dtos.DeviceActivation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record DeviceActivationRequest(
        @NotBlank
        String activationCode,
        @NotBlank
        @Size(max = 64)
        String deviceFingerprint
) {
}
