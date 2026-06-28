package hr.algebra.onlineshop.service;

import hr.algebra.onlineshop.dto.LoginHistoryDTO;
import hr.algebra.onlineshop.model.LoginHistory;
import hr.algebra.onlineshop.repo.LoginHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {

    private final LoginHistoryRepository repository;

    @Override
    public void saveLogin(String username, String ipAddress) {

        LoginHistory log = new LoginHistory();
        log.setUsername(username);
        log.setLoginTime(LocalDateTime.now(ZoneId.of("UTC")));
        log.setIpAddress(ipAddress);

        repository.save(log);
    }

    @Override
    public List<LoginHistoryDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<LoginHistoryDTO> findByUsername(String username) {
        return repository.findByUsername(username)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private LoginHistoryDTO toDTO(LoginHistory log) {
        return new LoginHistoryDTO(
                log.getUsername(),
                log.getLoginTime(),
                log.getIpAddress()
        );
    }
}
