package br.com.nsfatima.calendario.application.usecase.evento;

import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CreateEventoRecorrenciaUseCase {

    public Map<String, Object> execute(UUID eventoId, String frequencia, int intervalo) {
        return Map.of(
                "id", UUID.randomUUID().toString(),
                "eventoBaseId", eventoId.toString(),
                "frequencia", frequencia,
                "intervalo", intervalo);
    }
}
