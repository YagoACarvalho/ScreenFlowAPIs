package com.CTRLTELA.CtrlTela.services;

import com.CTRLTELA.CtrlTela.common.exception.NotFoundException;
import com.CTRLTELA.CtrlTela.domain.Screen;
import com.CTRLTELA.CtrlTela.domain.Tenant;
import com.CTRLTELA.CtrlTela.dtos.screens.ScreenCreateRequest;
import com.CTRLTELA.CtrlTela.dtos.screens.ScreenResponse;
import com.CTRLTELA.CtrlTela.enums.ScreenStatus;
import com.CTRLTELA.CtrlTela.repositories.ScreenRepository;
import com.CTRLTELA.CtrlTela.repositories.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final TenantRepository tenantRepository;

    public ScreenService(ScreenRepository screenRepository, TenantRepository tenantRepository) {
        this.screenRepository = screenRepository;
        this.tenantRepository = tenantRepository;
    }

    @Transactional
    public ScreenResponse create(UUID tenantId, ScreenCreateRequest req) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new NotFoundException("Tenant não encontrado: " + tenantId ));

        Screen screen = new Screen();
        screen.setTenant(tenant);
        screen.setName(req.name());
        screen.setLocation(req.location());
        screen.setStatus(ScreenStatus.ACTIVE);

       Screen saved = screenRepository.saveAndFlush(screen);
        System.out.println("createdAt after save = " + saved.getCreatedAt());
        return ScreenResponse.from(saved);
    }

    public List<ScreenResponse> list(UUID tenantId) {
        if (!tenantRepository.existsById(tenantId)) {
            throw new NotFoundException("Tenant não encontrado: " + tenantId);
        }

        var screens = screenRepository.findAllByTenant_Id(tenantId);

        if (screens.isEmpty()) {
            throw new NotFoundException("Não há telas para este tenant");
        }
        return screens.stream()
                .map(ScreenResponse::from)
                .toList();

    }

}
