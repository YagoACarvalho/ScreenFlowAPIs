package com.CTRLTELA.CtrlTela.services;

import com.CTRLTELA.CtrlTela.dtos.DeviceActivation.DeviceListItem;
import com.CTRLTELA.CtrlTela.repositories.DeviceRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class DeviceQueryService {

    private final DeviceRepository repository;

    public DeviceQueryService(DeviceRepository repository) {
        this.repository = repository;
    }

    public List<DeviceListItem> list(UUID tenantId ,UUID screenId) {

        var devices = (screenId == null)
                ? repository.findAllByTenantIdOrderByCreatedAtDesc(tenantId)
                : repository.findAllByTenantIdAndScreenIdOrderByCreatedAtDesc(tenantId, screenId);

        return devices.stream().map(DeviceListItem::from).toList();
    }
}
