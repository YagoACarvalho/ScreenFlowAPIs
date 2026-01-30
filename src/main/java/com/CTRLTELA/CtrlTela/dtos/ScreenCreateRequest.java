package com.CTRLTELA.CtrlTela.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ScreenCreateRequest(
        @NotBlank
        String name,
        @NotBlank
        String location
) {
}
