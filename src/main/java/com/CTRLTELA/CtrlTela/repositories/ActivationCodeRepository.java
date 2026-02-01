package com.CTRLTELA.CtrlTela.repositories;

import com.CTRLTELA.CtrlTela.domain.ActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface ActivationCodeRepository extends JpaRepository<ActivationCode, UUID> {
    // checa a unicidade do code
    boolean existsByCode(String code);

    // lista códes do tenant
    List<ActivationCode> findAllByTenantIdOrderByCreatedAtDesc(UUID tenantId);

    Optional<ActivationCode> findByCodeAndTenantId(String code, UUID tenantId);


}
