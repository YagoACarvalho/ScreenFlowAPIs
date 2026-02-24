package com.CTRLTELA.CtrlTela.controllers;

import com.CTRLTELA.CtrlTela.common.exception.UnauthorizedException;
import com.CTRLTELA.CtrlTela.common.login.AuthContext;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceListItem;
import com.CTRLTELA.CtrlTela.services.DeviceQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
public class DeviceQueryController {

    private final DeviceQueryService service;

    public DeviceQueryController(DeviceQueryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DeviceListItem>>list(@RequestParam(required = false)UUID screenId) {
        if(AuthContext.isDevice()) {
            throw new UnauthorizedException("Device não pode listar devices");
        }

        UUID tenantId = AuthContext.tenantId();
        var resp = service.list(tenantId, screenId);
        return ResponseEntity.ok(resp);
    }
}
