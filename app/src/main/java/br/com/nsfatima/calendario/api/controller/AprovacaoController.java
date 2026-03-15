package br.com.nsfatima.calendario.api.controller;

import java.util.Map;
import br.com.nsfatima.calendario.application.usecase.aprovacao.CreateSolicitacaoAprovacaoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/aprovacoes")
public class AprovacaoController {

    private final CreateSolicitacaoAprovacaoUseCase createSolicitacaoAprovacaoUseCase;

    public AprovacaoController(CreateSolicitacaoAprovacaoUseCase createSolicitacaoAprovacaoUseCase) {
        this.createSolicitacaoAprovacaoUseCase = createSolicitacaoAprovacaoUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@RequestBody Map<String, String> request) {
        return createSolicitacaoAprovacaoUseCase.create(
                request.getOrDefault("eventoId", "unknown"),
                request.getOrDefault("tipoSolicitacao", "ALTERACAO_HORARIO"));
    }
}
