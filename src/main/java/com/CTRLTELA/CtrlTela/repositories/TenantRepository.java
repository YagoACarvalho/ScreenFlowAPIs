package com.CTRLTELA.CtrlTela.repositories;

import com.CTRLTELA.CtrlTela.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {
}
