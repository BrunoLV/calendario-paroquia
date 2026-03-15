package br.com.nsfatima.calendario.infrastructure.observability;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogService.class);

    public void log(String actor, String action, String target, String result, Map<String, Object> metadata) {
        LOGGER.info(
                "audit actor={} action={} target={} result={} metadata={}",
                actor,
                action,
                target,
                result,
                metadata);
    }
}
