package br.com.nsfatima.calendario.integration.eventos;

import br.com.nsfatima.calendario.domain.policy.CalendarIntegrityPolicy;
import br.com.nsfatima.calendario.domain.service.EventoDomainService;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AddedExtraValidationIntegrationTest {

    private final EventoDomainService service = new EventoDomainService(new CalendarIntegrityPolicy());

    @Test
    void shouldRejectAddedExtraWithoutJustification() {
        assertThrows(IllegalArgumentException.class, () ->
                service.validateEvento(
                        Instant.parse("2026-03-16T10:00:00Z"),
                        Instant.parse("2026-03-16T11:00:00Z"),
                        "ADICIONADO_EXTRA",
                        ""));
    }
}
