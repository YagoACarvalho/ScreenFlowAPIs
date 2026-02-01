package com.CTRLTELA.CtrlTela.dtos.activationCode;

import java.time.LocalDateTime;

public record ActivationCodeUseResponse(
        String code,
        LocalDateTime usedAt
) {
}
