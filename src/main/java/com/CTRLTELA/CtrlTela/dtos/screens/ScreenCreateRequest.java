package com.CTRLTELA.CtrlTela.dtos.screens;

import jakarta.validation.constraints.NotBlank;

public record ScreenCreateRequest(
        @NotBlank
        String name,
        @NotBlank
        String location
) {
}
