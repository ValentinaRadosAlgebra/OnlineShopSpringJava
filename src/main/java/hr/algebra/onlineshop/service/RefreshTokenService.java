package hr.algebra.onlineshop.service;

import hr.algebra.onlineshop.model.RefreshToken;
import hr.algebra.onlineshop.repo.RefreshTokenRepository;
import hr.algebra.onlineshop.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository repo;
    private final UserRepository userRepository;

    public RefreshToken create(String username) {

        repo.findByUserInfo_Username(username)
                .ifPresent(repo::delete);

        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusSeconds(600));
        token.setUserInfo(userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new RuntimeException("User not found")));

        return repo.save(token);
    }

    public RefreshToken verify(String token) {

        RefreshToken rt = repo.findByToken(token).orElseThrow();

        if (rt.getExpiryDate().isBefore(Instant.now())) {
            repo.delete(rt);
            throw new IllegalArgumentException("Expired");
        }

        return rt;
    }
}
