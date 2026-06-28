package hr.algebra.onlineshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginHistoryDTO {
    private String username;
    private LocalDateTime loginTime;
    private String ipAddress;
}
