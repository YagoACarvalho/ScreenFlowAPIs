package com.CTRLTELA.CtrlTela.services;

import com.CTRLTELA.CtrlTela.common.exception.NotFoundException;
import com.CTRLTELA.CtrlTela.domain.Device;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceRevokeResponse;
import com.CTRLTELA.CtrlTela.enums.DeviceStatus;
import com.CTRLTELA.CtrlTela.repositories.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeviceRevokeService {

    private final DeviceRepository deviceRepository;

    public DeviceRevokeService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Transactional
    public DeviceRevokeResponse revoke(UUID tenantId, UUID deviceId) {

        Device device = deviceRepository.findByIdAndTenantId(deviceId,tenantId)
                .orElseThrow(() -> new NotFoundException("Device não encontrado"));

        if (device.getStatus() == DeviceStatus.REVOKED) {
            // Idempotente: já revogado -> retorna ok
            return new DeviceRevokeResponse(device.getId(), tenantId, device.getStatus());
        }

        device.setStatus(DeviceStatus.REVOKED);

        // Mata refresh token (impede refresh futuro)
        device.setRefreshTokenHash("REVOKED-" + UUID.randomUUID());

        // Zera lastSeen pra sinalizar que foi derrubado
        device.setLastSeenAt(null);

        return new DeviceRevokeResponse(device.getId(), tenantId, device.getStatus());

    }
}
