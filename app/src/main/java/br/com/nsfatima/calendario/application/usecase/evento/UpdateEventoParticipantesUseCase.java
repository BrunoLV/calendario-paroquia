package br.com.nsfatima.calendario.application.usecase.evento;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UpdateEventoParticipantesUseCase {

    public Map<String, Object> execute(UUID eventoId, List<UUID> participantes) {
        return Map.of(
                "eventoId", eventoId.toString(),
                "participantes", participantes);
    }
}
