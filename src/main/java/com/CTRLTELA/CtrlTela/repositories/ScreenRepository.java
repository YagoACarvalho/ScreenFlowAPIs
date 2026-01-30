package com.CTRLTELA.CtrlTela.repositories;

import com.CTRLTELA.CtrlTela.domain.Screen;
import com.CTRLTELA.CtrlTela.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, UUID> {
    Optional<Tenant> findAllByTenantId(UUID tenantId);

    List<Screen> findAllByTenant_Id(UUID tenantId);

}
