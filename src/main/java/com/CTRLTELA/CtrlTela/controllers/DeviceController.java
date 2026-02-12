package com.CTRLTELA.CtrlTela.controllers;

import com.CTRLTELA.CtrlTela.common.login.AuthContext;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceActivateResponse;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceActivationRequest;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceRefreshRequest;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceRefreshResponse;
import com.CTRLTELA.CtrlTela.services.DeviceActivationService;
import com.CTRLTELA.CtrlTela.services.DeviceRefreshService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/device")
public class DeviceController {

    private final DeviceActivationService service;
    private final DeviceRefreshService refreshService;

    public DeviceController(DeviceActivationService service, DeviceRefreshService refreshService) {
        this.service = service;
        this.refreshService = refreshService;
    }

    @PostMapping("/activate")
    public ResponseEntity<DeviceActivateResponse> activate(@RequestBody @Valid DeviceActivationRequest req) {
        var code = service.activate(req);
        return ResponseEntity.ok(code);
    }

    @PostMapping("/refresh")
    public ResponseEntity<DeviceRefreshResponse> refresh(@RequestBody @Valid DeviceRefreshRequest req) {
        var resp = refreshService.refresh(req);
        return ResponseEntity.ok(resp);
    }

    /* @PostMapping("/heartbeat")
    public ResponseEntity<Void> heartbeat() {
        UUID tenantId = AuthContext.tenantId();
        UUID deviceId = AuthContext.deviceId();
        UUID screenId = AuthContext.screenId();

        service.heartbeat(tenantId, deviceId, screenId);
        return ResponseEntity.noContent().build();
    } */

}
