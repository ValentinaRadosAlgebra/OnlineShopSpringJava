package hr.algebra.onlineshop.service;

import hr.algebra.onlineshop.dto.authentication.AuthRequestDTO;
import hr.algebra.onlineshop.dto.authentication.JwtResponseDTO;
import hr.algebra.onlineshop.dto.authentication.RefreshTokenDTO;
import hr.algebra.onlineshop.dto.authentication.RegisterDTO;
import hr.algebra.onlineshop.model.RefreshToken;
import hr.algebra.onlineshop.model.Role;
import hr.algebra.onlineshop.model.User;
import hr.algebra.onlineshop.repo.RoleRepository;
import hr.algebra.onlineshop.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public JwtResponseDTO login(AuthRequestDTO request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername());
        RefreshToken refresh = refreshTokenService.create(user.getUsername());

        return new JwtResponseDTO(token, refresh.getToken());
    }

    @Override
    public void register(RegisterDTO dto) {
        User user = new User();
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );

        Role customerRole = roleRepository
                .findByName("USER")
                .orElseThrow();

        user.setRoles(List.of(customerRole));

        userRepository.save(user);
    }

    @Override
    public RefreshTokenDTO refreshToken(String token) {
        RefreshToken rt =
                refreshTokenService.verify(token);

        String accessToken =
                jwtService.generateToken(
                        rt.getUserInfo().getUsername()
                );

        return new RefreshTokenDTO(accessToken);
    }
}