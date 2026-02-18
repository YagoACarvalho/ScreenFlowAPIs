package com.CTRLTELA.CtrlTela.dtos.DeviceActivation;

import com.CTRLTELA.CtrlTela.enums.DeviceStatus;

import java.util.UUID;

public record DeviceRevokeResponse(
        UUID deviceId,
        UUID tenantId,
        DeviceStatus status
) {
}
