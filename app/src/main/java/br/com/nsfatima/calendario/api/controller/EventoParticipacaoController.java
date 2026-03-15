package br.com.nsfatima.calendario.api.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import br.com.nsfatima.calendario.application.usecase.evento.ClearEventoParticipantesUseCase;
import br.com.nsfatima.calendario.application.usecase.evento.CreateEventoRecorrenciaUseCase;
import br.com.nsfatima.calendario.application.usecase.evento.UpdateEventoParticipantesUseCase;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/eventos")
public class EventoParticipacaoController {

    private final UpdateEventoParticipantesUseCase updateEventoParticipantesUseCase;
    private final ClearEventoParticipantesUseCase clearEventoParticipantesUseCase;
    private final CreateEventoRecorrenciaUseCase createEventoRecorrenciaUseCase;

    public EventoParticipacaoController(
            UpdateEventoParticipantesUseCase updateEventoParticipantesUseCase,
            ClearEventoParticipantesUseCase clearEventoParticipantesUseCase,
            CreateEventoRecorrenciaUseCase createEventoRecorrenciaUseCase) {
        this.updateEventoParticipantesUseCase = updateEventoParticipantesUseCase;
        this.clearEventoParticipantesUseCase = clearEventoParticipantesUseCase;
        this.createEventoRecorrenciaUseCase = createEventoRecorrenciaUseCase;
    }

    @PutMapping("/{eventoId}/participantes")
    public Map<String, Object> putParticipantes(@PathVariable UUID eventoId, @RequestBody Map<String, List<UUID>> payload) {
        return updateEventoParticipantesUseCase.execute(
                eventoId,
                payload.getOrDefault("organizacoesParticipantes", List.of()));
    }

    @DeleteMapping("/{eventoId}/participantes")
    public Map<String, Object> clearParticipantes(@PathVariable UUID eventoId) {
        return clearEventoParticipantesUseCase.execute(eventoId);
    }

    @PutMapping("/{eventoId}/recorrencia")
    public Map<String, Object> createRecorrencia(@PathVariable UUID eventoId, @RequestBody Map<String, Object> payload) {
        String frequencia = String.valueOf(payload.getOrDefault("frequencia", "SEMANAL"));
        int intervalo = Integer.parseInt(String.valueOf(payload.getOrDefault("intervalo", 1)));
        return createEventoRecorrenciaUseCase.execute(eventoId, frequencia, intervalo);
    }
}
