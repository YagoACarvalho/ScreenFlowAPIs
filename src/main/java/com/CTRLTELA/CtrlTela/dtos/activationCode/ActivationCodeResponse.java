package com.CTRLTELA.CtrlTela.dtos.activationCode;

import com.CTRLTELA.CtrlTela.domain.ActivationCode;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivationCodeResponse(

        String code,
        UUID screenId,
        LocalDateTime expiresAt,
        LocalDateTime usedAt,
        LocalDateTime createdAt
) {

  public static ActivationCodeResponse from(ActivationCode ac) {
        return new ActivationCodeResponse(
                ac.getCode(),
                ac.getScreen().getId(),
                ac.getExpiresAt(),
                ac.getUsedAt(),
                ac.getCreatedAt()
        );
    }
}
