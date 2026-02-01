package com.CTRLTELA.CtrlTela.controllers;

import com.CTRLTELA.CtrlTela.common.AuthContext;
import com.CTRLTELA.CtrlTela.dtos.activationCode.ActivationCodeCreateRequest;
import com.CTRLTELA.CtrlTela.dtos.activationCode.ActivationCodeResponse;
import com.CTRLTELA.CtrlTela.services.ActivationCodeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/activation-codes")
public class ActivationCodeController {

    private final ActivationCodeService service;

    public ActivationCodeController(ActivationCodeService service) {
        this.service = service;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid ActivationCodeCreateRequest dto) {
        UUID tenantId = AuthContext.tenantId();
        var created = service.create(tenantId, dto);
        return ResponseEntity.status(201).body(created);
    }
}
