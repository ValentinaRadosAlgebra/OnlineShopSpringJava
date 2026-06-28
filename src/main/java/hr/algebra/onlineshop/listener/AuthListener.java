package hr.algebra.onlineshop.listener;

import hr.algebra.onlineshop.dto.LoginHistoryDTO;
import hr.algebra.onlineshop.service.CartService;
import hr.algebra.onlineshop.service.LoginHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.ZoneId;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthListener {

    private final LoginHistoryService loginHistoryService;
    private final CartService cartService;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void onAuthSuccess(AuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        String username = auth.getName();

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();

        String ipAddress = getClientIp(request);

        loginHistoryService.saveLogin(username, ipAddress);

        //websocket
        messagingTemplate.convertAndSend("/topic/login-history",
                new LoginHistoryDTO(username, java.time.LocalDateTime.now(ZoneId.of("UTC")), ipAddress));

        cartService.mergeSessionCartToDatabase(username);
    }

    private String getClientIp(HttpServletRequest request) {

        String[] headerCandidates = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String header : headerCandidates) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0];
            }
        }

        return request.getRemoteAddr();
    }
}
