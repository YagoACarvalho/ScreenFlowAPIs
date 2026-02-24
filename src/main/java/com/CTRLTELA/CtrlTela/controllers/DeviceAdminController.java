package com.CTRLTELA.CtrlTela.controllers;

import com.CTRLTELA.CtrlTela.common.exception.UnauthorizedException;
import com.CTRLTELA.CtrlTela.common.login.AuthContext;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceListItem;
import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceRevokeResponse;
import com.CTRLTELA.CtrlTela.services.DeviceQueryService;
import com.CTRLTELA.CtrlTela.services.DeviceRevokeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
public class DeviceAdminController {

    private final DeviceRevokeService service;
    private final DeviceQueryService queryService;

    public DeviceAdminController(DeviceRevokeService service, DeviceQueryService queryService) {
        this.service = service;
        this.queryService = queryService;
    }

    @PostMapping("/{deviceId}/revoke")
    public ResponseEntity<DeviceRevokeResponse> revoke(@PathVariable UUID deviceId) {
        UUID tenantId = AuthContext.tenantId();

        var resp = service.revoke(tenantId, deviceId);
        return ResponseEntity.ok(resp);
    }




}
