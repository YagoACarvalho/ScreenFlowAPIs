package com.CTRLTELA.CtrlTela.domain;

import com.CTRLTELA.CtrlTela.enums.DeviceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(
        name = "devices",
        indexes = {
                @Index(name = "idx_devices_tenant_id", columnList = "tenant_id"),
                @Index(name = "idx_devices_screen_id", columnList = "screen_id"),
                @Index(name = "idx_devices_status", columnList = "status")
        }
)
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Device pertence a uma conta
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    // Device esta vinculado a uma tela(Screen)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @Column(name = "refresh_token_hash", nullable = false, length = 255)
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeviceStatus status;

    // Atualiza a cada heartbeat
    @Column(name = "last_seen_at")
    private LocalDateTime lastSeenAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "fingerprint", nullable = false, length = 64)
    private String fingerprint;


    public void markSeen(LocalDateTime now) {
        this.lastSeenAt = now;
    }

}
