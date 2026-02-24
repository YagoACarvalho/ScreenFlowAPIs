package com.CTRLTELA.CtrlTela.dtos.DeviceActivation;

import com.CTRLTELA.CtrlTela.domain.Device;
import com.CTRLTELA.CtrlTela.enums.DeviceStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeviceListItem(
        UUID id,
        UUID screenId,
        String fingerprint,
        DeviceStatus status,
        LocalDateTime lastSeenAt,
        LocalDateTime createdAt

) {

    public static DeviceListItem from(Device d) {
        return new DeviceListItem(
                d.getId(),
                d.getScreen().getId(),
                d.getFingerprint(),
                d.getStatus(),
                d.getLastSeenAt(),
                d.getCreatedAt()
        );
    }

}
