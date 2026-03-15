package br.com.nsfatima.calendario.domain.policy;

import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class CalendarIntegrityPolicy {

    public void validateInterval(Instant inicioUtc, Instant fimUtc) {
        if (inicioUtc == null || fimUtc == null) {
            throw new IllegalArgumentException("inicio/fim devem ser informados");
        }
        if (!fimUtc.isAfter(inicioUtc)) {
            throw new IllegalArgumentException("fim deve ser maior que inicio");
        }
    }
}
