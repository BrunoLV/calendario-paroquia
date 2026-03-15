package br.com.nsfatima.calendario.application.usecase.evento;

import java.util.UUID;
import br.com.nsfatima.calendario.api.dto.evento.CreateEventoRequest;
import br.com.nsfatima.calendario.api.dto.evento.EventoResponse;
import br.com.nsfatima.calendario.domain.service.EventoDomainService;
import org.springframework.stereotype.Service;

@Service
public class CreateEventoUseCase {

    private final EventoDomainService eventoDomainService;

    public CreateEventoUseCase(EventoDomainService eventoDomainService) {
        this.eventoDomainService = eventoDomainService;
    }

    public EventoResponse execute(CreateEventoRequest request) {
        eventoDomainService.validateEvento(request.inicio(), request.fim(), request.status(), request.adicionadoExtraJustificativa());
        return new EventoResponse(UUID.randomUUID(), request.titulo(), request.descricao(), request.inicio(), request.fim(), request.status());
    }
}
