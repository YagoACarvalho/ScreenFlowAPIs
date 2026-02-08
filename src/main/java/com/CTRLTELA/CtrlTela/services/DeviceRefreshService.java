package com.CTRLTELA.CtrlTela.services;

import com.CTRLTELA.CtrlTela.common.jwtFlow.JwtService;
import com.CTRLTELA.CtrlTela.domain.Device;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceRefreshRequest;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceRefreshResponse;
import com.CTRLTELA.CtrlTela.enums.DeviceStatus;
import com.CTRLTELA.CtrlTela.repositories.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

        device.setLastSeenAt(LocalDateTime.now());

        String acessToken = jwtService.generateDeviceAcessToken(
                device.getId(),
                device.getTenant().getId(),
                device.getScreen().getId()
        );

        return new DeviceRefreshResponse(
                acessToken,
                device.getId(),
                device.getScreen().getId(),
                device.getTenant().getId()
        );

    }
}
