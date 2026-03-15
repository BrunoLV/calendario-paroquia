package br.com.nsfatima.calendario.infrastructure.observability;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ObservacaoAuditPublisher {

    private final AuditLogService auditLogService;

    public ObservacaoAuditPublisher(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    public void publish(String actor, String action, String target, String result) {
        auditLogService.log(actor, action, target, result, Map.of());
    }
}
