package com.CTRLTELA.CtrlTela.common.login;

import com.CTRLTELA.CtrlTela.common.jwtFlow.JwtService;
import com.CTRLTELA.CtrlTela.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository repo;

    public AuthController(AuthenticationManager authManager, JwtService jwtService, UserRepository repo) {
        this.authManager = authManager;
        this.jwtService = jwtService;

        this.repo = repo;
    }


    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {

        var authToken = new UsernamePasswordAuthenticationToken(req.email(), req.password());
        authManager.authenticate(authToken);

        var user = repo.findByEmail(req.email()).orElseThrow();

        String jwt = jwtService.generatedAccessToken(
                user.getEmail(),
                user.getTenant().getId(),
                user.getRole().name()
        );

        return new LoginResponse(jwt);
    }
}
