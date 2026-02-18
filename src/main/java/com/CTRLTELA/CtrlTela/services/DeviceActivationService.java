package com.CTRLTELA.CtrlTela.services;

import com.CTRLTELA.CtrlTela.common.exception.NotFoundException;
import com.CTRLTELA.CtrlTela.common.jwtFlow.TokenHash;
import com.CTRLTELA.CtrlTela.domain.ActivationCode;
import com.CTRLTELA.CtrlTela.domain.Device;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceActivateResponse;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceActivationRequest;
import com.CTRLTELA.CtrlTela.enums.DeviceStatus;
import com.CTRLTELA.CtrlTela.repositories.ActivationCodeRepository;
import com.CTRLTELA.CtrlTela.repositories.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeviceActivationService {


    private final DeviceRepository deviceRepository;
    private final ActivationCodeRepository activationCodeRepository;


    public DeviceActivationService(DeviceRepository deviceRepository, ActivationCodeRepository activationCodeRepository) {
        this.deviceRepository = deviceRepository;
        this.activationCodeRepository = activationCodeRepository;
    }

    @Transactional
    public DeviceActivateResponse activate (DeviceActivationRequest dto) {

        var now = LocalDateTime.now();

        // Captura ActivationCode
       ActivationCode code = activationCodeRepository.findById((dto.activationCode()))
               .orElseThrow(() -> new NotFoundException("Activation code inválido."));

       // Validações
       if (code.getUsedAt() != null) {
           throw  new IllegalArgumentException("Activation code já foi usado");
       }

       if (code.getExpiresAt().isBefore(now)) {
           throw new IllegalArgumentException("Activation code expirado");
       }

       UUID tenantId = code.getTenant().getId();
       UUID screenId = code.getScreen().getId();

       // Upsert por tenant + fingerprint
        String fingerprint = dto.deviceFingerprint();

        Device device = deviceRepository.findByTenantIdAndFingerprint(tenantId, fingerprint)
                .orElseGet(Device::new);

        device.setTenant(code.getTenant());
        device.setScreen(code.getScreen());
        device.setFingerprint(fingerprint);
        device.setStatus(DeviceStatus.ACTIVE);
        device.setLastSeenAt(now);

        // Refresh token
        String refreshTokenRaw = UUID.randomUUID() + "." + UUID.randomUUID();

        device.setRefreshTokenHash(TokenHash.sha256Base64(refreshTokenRaw));


        Device saved = deviceRepository.save(device);

        // Marca activation code como usado
        code.setUsedAt(now);
        activationCodeRepository.save(code);

        return new DeviceActivateResponse(
                saved.getId(),
                screenId,
                tenantId,
                refreshTokenRaw
        );
    }
}
