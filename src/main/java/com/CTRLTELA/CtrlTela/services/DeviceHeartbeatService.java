package com.CTRLTELA.CtrlTela.services;

import com.CTRLTELA.CtrlTela.common.exception.NotFoundException;
import com.CTRLTELA.CtrlTela.common.exception.UnauthorizedException;
import com.CTRLTELA.CtrlTela.domain.Device;
import com.CTRLTELA.CtrlTela.enums.DeviceStatus;
import com.CTRLTELA.CtrlTela.repositories.DeviceRepository;
import jakarta.validation.constraints.Null;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeviceHeartbeatService {

    private final DeviceRepository deviceRepository;

    public DeviceHeartbeatService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Transactional
    public void heartbeat(UUID tenantId, UUID deviceId, UUID screenId) {

        if (deviceId == null) {
            throw new UnauthorizedException("Token não é de device");
        }

        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new NotFoundException("Device não encontrado"));

        if(!device.getTenant().getId().equals(tenantId)) {
            throw new UnauthorizedException("Device não pertence ao tenant");
        }

        if(!device.getScreen().getId().equals(screenId)) {
            throw new UnauthorizedException("Screen do device não confere");
        }

        if(device.getStatus() != DeviceStatus.ACTIVE){

        }
    }
}
