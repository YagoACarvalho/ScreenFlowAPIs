package com.CTRLTELA.CtrlTela.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(
        name = "activation_codes",
        indexes = {
                @Index(name = "idx_activation_codes_screen_id", columnList = "screen_id"),
                @Index(name = "idx_activation_codes_tenant_id", columnList = "tenant_id")
        }
)
public class ActivationCode {

    @Id
    @Column(name = "code", length = 12, nullable = false, updatable = false)
    @Size(min = 8, max = 12)
    private String code;

    // Tenant a qual esse código pertence
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    // Define tela que será ativada ao usar esse código
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;



}
