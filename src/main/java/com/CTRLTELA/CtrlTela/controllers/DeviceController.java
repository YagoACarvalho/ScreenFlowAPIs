package com.CTRLTELA.CtrlTela.controllers;

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
        return ResponseEntity.ok(refreshService.refresh(req));
    }

}
