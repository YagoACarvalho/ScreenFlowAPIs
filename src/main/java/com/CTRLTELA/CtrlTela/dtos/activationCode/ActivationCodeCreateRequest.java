package com.CTRLTELA.CtrlTela.dtos.activationCode;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ActivationCodeCreateRequest(
        @NotNull
        UUID screenId,
        @Positive
                @Max(43200)
        Integer expiresInMinutes
) {
}
