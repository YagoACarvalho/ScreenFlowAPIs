package com.CTRLTELA.CtrlTela.dtos.screens;

import com.CTRLTELA.CtrlTela.domain.Screen;
import com.CTRLTELA.CtrlTela.enums.ScreenStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScreenResponse(
        UUID id,
        String name,
        String location,
        ScreenStatus status,
        LocalDateTime createdAt
) {

    public static ScreenResponse from(Screen screen) {
     return new ScreenResponse(
             screen.getId(),
             screen.getName(),
             screen.getLocation(),
             screen.getStatus(),
             screen.getCreatedAt()
     );
    }


}
