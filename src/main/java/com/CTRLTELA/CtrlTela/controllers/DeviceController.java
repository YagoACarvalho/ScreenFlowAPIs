package com.CTRLTELA.CtrlTela.controllers;

import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceActivateResponse;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceActivationRequest;
import com.CTRLTELA.CtrlTela.services.DeviceActivationService;
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

    public DeviceController(DeviceActivationService service) {
        this.service = service;
    }

    @PostMapping("activate")
    public ResponseEntity<DeviceActivateResponse> activate(@RequestBody @Valid DeviceActivationRequest req) {
        var code = service.activate(req);
        return ResponseEntity.ok(code);
    }

}
