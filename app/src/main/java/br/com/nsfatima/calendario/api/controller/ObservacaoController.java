package br.com.nsfatima.calendario.api.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import br.com.nsfatima.calendario.application.usecase.observacao.CreateObservacaoUseCase;
import br.com.nsfatima.calendario.application.usecase.observacao.ListObservacoesUseCase;
import br.com.nsfatima.calendario.infrastructure.observability.ObservacaoAuditPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/eventos/{eventoId}/observacoes")
public class ObservacaoController {

    private final CreateObservacaoUseCase createObservacaoUseCase;
    private final ListObservacoesUseCase listObservacoesUseCase;
    private final ObservacaoAuditPublisher observacaoAuditPublisher;

    public ObservacaoController(
            CreateObservacaoUseCase createObservacaoUseCase,
            ListObservacoesUseCase listObservacoesUseCase,
            ObservacaoAuditPublisher observacaoAuditPublisher) {
        this.createObservacaoUseCase = createObservacaoUseCase;
        this.listObservacoesUseCase = listObservacoesUseCase;
        this.observacaoAuditPublisher = observacaoAuditPublisher;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@PathVariable UUID eventoId, @RequestBody Map<String, String> request) {
        Map<String, Object> response = createObservacaoUseCase.execute(
                eventoId,
                UUID.fromString(request.getOrDefault("usuarioId", "00000000-0000-0000-0000-000000000001")),
                request.getOrDefault("tipo", "NOTA"),
                request.getOrDefault("conteudo", ""));
        observacaoAuditPublisher.publish("system", "create-observacao", eventoId.toString(), "success");
        return response;
    }

    @GetMapping
    public List<Map<String, Object>> list(@PathVariable UUID eventoId) {
        return listObservacoesUseCase.execute(eventoId);
    }
}
