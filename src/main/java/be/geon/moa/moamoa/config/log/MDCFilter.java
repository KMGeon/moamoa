package be.geon.moa.moamoa.config.log;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MDCFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            String transactionId = generateTransactionId();
            MDC.put("transactionId", transactionId);
            MDC.put("request_id", getClientIp(servletRequest));
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.clear();
        }
    }



    private String generateTransactionId() {
        return UUID.randomUUID().toString().split("-")[0];
    }

    private String getClientIp(ServletRequest request) {
        String clientIp = request.getRemoteAddr();

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String xForwardedFor = httpServletRequest.getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                clientIp = xForwardedFor.split(",")[0].trim();
            }
        }

        return clientIp;
    }


}