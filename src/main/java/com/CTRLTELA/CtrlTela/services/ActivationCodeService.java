package com.CTRLTELA.CtrlTela.services;

import com.CTRLTELA.CtrlTela.common.login.ActivationCodeGenerator;
import com.CTRLTELA.CtrlTela.common.exception.NotFoundException;
import com.CTRLTELA.CtrlTela.domain.ActivationCode;
import com.CTRLTELA.CtrlTela.dtos.activationCode.ActivationCodeCreateRequest;
import com.CTRLTELA.CtrlTela.dtos.activationCode.ActivationCodeResponse;
import com.CTRLTELA.CtrlTela.repositories.ActivationCodeRepository;
import com.CTRLTELA.CtrlTela.repositories.ScreenRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class ActivationCodeService {

    private static final int MAX_ATTEMPTS = 10;
    private static final int DEFAULT_EXPIRES_MINUTES = 10;
    private static final int CODE_LENGTH = 10;

    private final ActivationCodeRepository activationCodeRepository;
    private final ScreenRepository screenRepository;
    private final ActivationCodeGenerator generator;

    public ActivationCodeService(ActivationCodeRepository activationCodeRepository, ScreenRepository screenRepository, ActivationCodeGenerator generator) {
        this.activationCodeRepository = activationCodeRepository;
        this.screenRepository = screenRepository;
        this.generator = generator;
    }


    public ActivationCodeResponse create(UUID tenantId, ActivationCodeCreateRequest req) {

        var screen = screenRepository.findByIdAndTenantId(req.screenId(), tenantId)
                .orElseThrow(() -> new NotFoundException("Screen não encontrada"));

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {

            String code = generator.generate(CODE_LENGTH);
            int minutes = (req.expiresInMinutes() == null || req.expiresInMinutes() <= 0) ? DEFAULT_EXPIRES_MINUTES : req.expiresInMinutes();




            ActivationCode entity = new ActivationCode();
            entity.setCode(code);
            entity.setTenant(screen.getTenant());
            entity.setScreen(screen);
            entity.setExpiresAt(LocalDateTime.now().plusMinutes(minutes));




            try {
                ActivationCode saved = activationCodeRepository.saveAndFlush(entity);
                return ActivationCodeResponse.from(saved);

            } catch (DataIntegrityViolationException ex) {
                if (isUniqueViolation(ex)) continue;
                throw ex;
            }

        }

        // só cai aqui se falhar todas as tentativas
        throw new IllegalStateException(
                "Falha ao gerar código único após " + MAX_ATTEMPTS + " tentativas"
        );
    }

    private boolean isUniqueViolation(DataIntegrityViolationException ex) {
        Throwable t = ex;
        while (t != null) {
            if (t instanceof SQLException sqlEx) {
                return "23505".equals(sqlEx.getSQLState()); // Postgres unique_violation
            }
            t = t.getCause();
        }
        return false;
    }
}
