package hr.algebra.onlineshop.controller.rest;

import hr.algebra.onlineshop.dto.authentication.AuthRequestDTO;
import hr.algebra.onlineshop.dto.authentication.JwtResponseDTO;
import hr.algebra.onlineshop.dto.authentication.RegisterDTO;
import hr.algebra.onlineshop.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
public class AuthRESTController {
    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponseDTO login(@RequestBody AuthRequestDTO dto) {
        return authService.login(dto);
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterDTO dto) {
        authService.register(dto);
    }
}
