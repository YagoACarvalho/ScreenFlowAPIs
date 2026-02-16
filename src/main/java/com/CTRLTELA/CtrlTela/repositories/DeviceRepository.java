package com.CTRLTELA.CtrlTela.repositories;

import com.CTRLTELA.CtrlTela.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {

    Optional<Device> findByTenantIdAndFingerprint(UUID tenantId, String fingerprint);

    boolean existsByTenantIdAndFingerprint(UUID tenantId, String fingerprint);

    Optional<Device> findByRefreshToken(String refreshTokenHash);

}
