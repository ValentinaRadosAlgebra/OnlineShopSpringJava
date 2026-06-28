package hr.algebra.onlineshop.listener;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
public class SessionListener implements HttpSessionListener {

    private static final String IP_ADDRESS_MESSAGE = "IP Address: ";

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        String ipAddr = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();

        log.info(IP_ADDRESS_MESSAGE + ipAddr);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        log.info("Session destroyed");
    }
}
