package br.com.nsfatima.calendario.application.usecase.evento;

import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ClearEventoParticipantesUseCase {

    public Map<String, Object> execute(UUID eventoId) {
        return Map.of(
                "eventoId", eventoId.toString(),
                "participantes", java.util.List.of());
    }
}
