package com.CTRLTELA.CtrlTela.repositories;

import com.CTRLTELA.CtrlTela.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {

    Optional<Device> findByTenantIdAndFingerprint(UUID tenantId, String fingerprint);

    boolean existsByTenantIdAndFingerprint(UUID tenantId, String fingerprint);

    List<Device> findAllByTenantIdOrderByCreatedAtDesc(UUID tenantId);

    List<Device> findAllByTenantIdAndScreenId(UUID tenantId, UUID screenId);

    Optional<Device> findByIdAndTenantId(UUID deviceId, UUID tenantId);

    Optional<Device> findByRefreshTokenHash(String refreshTokenHash);

    Optional<Device> findByIdAndTenantIdAndScreenId(UUID id, UUID tenantId, UUID screenId);

    List<Device> findAllByTenantIdAndScreenIdOrderByCreatedAtDesc(UUID tenantId, UUID screenId);

}
