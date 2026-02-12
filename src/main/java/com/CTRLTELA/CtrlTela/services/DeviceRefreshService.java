package com.CTRLTELA.CtrlTela.services;

import com.CTRLTELA.CtrlTela.common.exception.UnauthorizedException;
import com.CTRLTELA.CtrlTela.common.jwtFlow.JwtService;
import com.CTRLTELA.CtrlTela.common.jwtFlow.TokenHash;
import com.CTRLTELA.CtrlTela.domain.Device;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceRefreshRequest;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceRefreshResponse;
import com.CTRLTELA.CtrlTela.enums.DeviceStatus;
import com.CTRLTELA.CtrlTela.repositories.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeviceRefreshService {

    private final DeviceRepository deviceRepository;
    private final JwtService jwtService;


    public DeviceRefreshService(DeviceRepository deviceRepository, JwtService jwtService) {
        this.deviceRepository = deviceRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public DeviceRefreshResponse refresh(DeviceRefreshRequest req) {

        Device device = deviceRepository.findByRefreshToken(req.refreshToken())
                .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido"));

        // Confere fingerprint (MVP)
        if (!device.getFingerprint().equals(req.deviceFingerprint())) {
            throw new IllegalArgumentException("Fingerprint não confere");
        }

        if (device.getStatus() != DeviceStatus.ACTIVE) {
            throw new IllegalArgumentException("Device inativo");
        }

        String provideHash = TokenHash.sha256Base64(req.refreshToken());
        if (!provideHash.equals(device.getRefreshToken())) {
            throw new UnauthorizedException("Refresh token inválido");
        }

        device.setLastSeenAt(LocalDateTime.now());

        UUID tenantId = device.getTenant().getId();
        UUID screenId = device.getScreen().getId();
        UUID deviceId = device.getId();

        String accessToken = jwtService.generateDeviceAcessToken(deviceId, tenantId, screenId);

        String newRefreshRaw = UUID.randomUUID() + "." + UUID.randomUUID();
        device.setRefreshToken(TokenHash.sha256Base64(newRefreshRaw));

        deviceRepository.save(device);

        return new DeviceRefreshResponse(
                accessToken,
                device.getId(),
                device.getScreen().getId(),
                device.getTenant().getId()
        );

    }
}
