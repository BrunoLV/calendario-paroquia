package br.com.nsfatima.calendario.integration.foundation;

import br.com.nsfatima.calendario.infrastructure.observability.AuditLogService;
import br.com.nsfatima.calendario.infrastructure.time.TimezoneConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FoundationInfrastructureTest {

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private TimezoneConfig timezoneConfig;

    @Test
    void contextLoadsWithFoundationalBeans() {
        assertNotNull(auditLogService);
        assertNotNull(timezoneConfig);
    }
}
