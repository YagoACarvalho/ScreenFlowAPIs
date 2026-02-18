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

        // Hash do refresh token recebido
        String providedHash = TokenHash.sha256Base64(req.refreshToken());

        // Busca pelo HASH (nunca pelo raw)
        Device device = deviceRepository.findByRefreshTokenHash(providedHash)
                .orElseThrow(() -> new UnauthorizedException("Refresh token inválido"));

        // Confere fingerprint (MVP)
        if (!device.getFingerprint().equals(req.deviceFingerprint())) {
            throw new UnauthorizedException("Fingerprint não confere");
        }

        // Status
        if (device.getStatus() != DeviceStatus.ACTIVE) {
            throw new UnauthorizedException("Device revogado");
        }

        // Atualiza lastseen
        device.setLastSeenAt(LocalDateTime.now());

        UUID tenantId = device.getTenant().getId();
        UUID screenId = device.getScreen().getId();
        UUID deviceId = device.getId();

        // Gera novo acess token curto
        String accessToken = jwtService.generateDeviceAcessToken(deviceId, tenantId, screenId);

        // Rotacionado refresh token (gera novo raw e salva hash)
        String newRefreshRaw = UUID.randomUUID() + "." + UUID.randomUUID();
        device.setRefreshTokenHash(TokenHash.sha256Base64(newRefreshRaw));

        deviceRepository.save(device);

        //Resposta devolve access + novo refresh RAW (o cliente precisa guardar o raw)
        return new DeviceRefreshResponse(
                accessToken,
                newRefreshRaw,
                deviceId,
                screenId,
                tenantId
        );

    }
}
