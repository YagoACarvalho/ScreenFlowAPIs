package com.CTRLTELA.CtrlTela.controllers;

import com.CTRLTELA.CtrlTela.common.login.AuthContext;
import com.CTRLTELA.CtrlTela.dtos.screens.ScreenCreateRequest;
import com.CTRLTELA.CtrlTela.dtos.screens.ScreenResponse;
import com.CTRLTELA.CtrlTela.services.ScreenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/screens")
public class ScreenController {

    private final ScreenService service;

    public ScreenController(ScreenService service) {

        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid ScreenCreateRequest dto){
        var tenantId = AuthContext.tenantId();
        System.out.println("DTO name=" + dto.name() + " | location=" + dto.location());
        var newScreen = service.create(tenantId, dto);
        return ResponseEntity.status(201).body(newScreen);
    }


    @GetMapping
    public ResponseEntity<List<ScreenResponse>> list(){
        var tenantId = AuthContext.tenantId();
        var list = service.list(tenantId);
        return ResponseEntity.ok(list);
    }
}
