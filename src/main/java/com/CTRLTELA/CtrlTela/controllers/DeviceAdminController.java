package com.CTRLTELA.CtrlTela.controllers;

import com.CTRLTELA.CtrlTela.common.exception.UnauthorizedException;
import com.CTRLTELA.CtrlTela.common.login.AuthContext;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceRevokeResponse;
import com.CTRLTELA.CtrlTela.services.DeviceRevokeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
public class DeviceAdminController {

    private final DeviceRevokeService service;

    public DeviceAdminController(DeviceRevokeService service) {
        this.service = service;
    }

    @PostMapping("/{deviceId}/revoke")
    public ResponseEntity<DeviceRevokeResponse> revoke(@PathVariable UUID deviceId) {
        UUID tenantId = AuthContext.tenantId();

        var resp = service.revoke(tenantId, deviceId);
        return ResponseEntity.ok(resp);
    }

}
