package hr.algebra.onlineshop.service;

import hr.algebra.onlineshop.dto.authentication.AuthRequestDTO;
import hr.algebra.onlineshop.dto.authentication.JwtResponseDTO;
import hr.algebra.onlineshop.dto.authentication.RefreshTokenDTO;
import hr.algebra.onlineshop.dto.authentication.RegisterDTO;

public interface AuthService {
    JwtResponseDTO login(AuthRequestDTO request);
    void register(RegisterDTO registerDTO);
    RefreshTokenDTO refreshToken(String token);
}
