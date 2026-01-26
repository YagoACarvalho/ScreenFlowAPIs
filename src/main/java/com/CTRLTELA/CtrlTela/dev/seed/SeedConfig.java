package com.CTRLTELA.CtrlTela.dev.seed;
import com.CTRLTELA.CtrlTela.domain.Tenant;
import com.CTRLTELA.CtrlTela.domain.User;
import com.CTRLTELA.CtrlTela.enums.TenantStatus;
import com.CTRLTELA.CtrlTela.enums.UserRole;
import com.CTRLTELA.CtrlTela.repositories.TenantRepository;
import com.CTRLTELA.CtrlTela.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Profile("dev")
@Configuration
public class SeedConfig {

    @Bean
    CommandLineRunner seed(UserRepository userRepository, TenantRepository tenantRepository, PasswordEncoder encoder) {
        return  args -> {
            if(userRepository.findByEmail("admin@demo.com").isPresent()) return;

            Tenant t = new Tenant();
            t.setName("Demo");
            t.setStatus(TenantStatus.ACTIVE);
            tenantRepository.save(t);

            User u = new User();
            u.setTenant(t);
            u.setEmail("admin@demo.com");
            u.setPasswordHash(encoder.encode("admin123"));
            u.setRole(UserRole.ADMIN);

            userRepository.save(u);
        };
    }
}
