package br.com.nsfatima.calendario.domain.service;

import java.time.Instant;
import br.com.nsfatima.calendario.domain.policy.CalendarIntegrityPolicy;
import org.springframework.stereotype.Service;

@Service
public class EventoDomainService {

    private final CalendarIntegrityPolicy calendarIntegrityPolicy;

    public EventoDomainService(CalendarIntegrityPolicy calendarIntegrityPolicy) {
        this.calendarIntegrityPolicy = calendarIntegrityPolicy;
    }

    public void validateEvento(Instant inicio, Instant fim, String status, String justificativa) {
        calendarIntegrityPolicy.validateInterval(inicio, fim);
        if ("ADICIONADO_EXTRA".equalsIgnoreCase(status) && (justificativa == null || justificativa.isBlank())) {
            throw new IllegalArgumentException("ADICIONADO_EXTRA exige justificativa");
        }
    }
}
