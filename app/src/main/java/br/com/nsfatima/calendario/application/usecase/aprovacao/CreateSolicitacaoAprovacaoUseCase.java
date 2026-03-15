package br.com.nsfatima.calendario.application.usecase.aprovacao;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CreateSolicitacaoAprovacaoUseCase {

    public Map<String, Object> create(String eventoId, String tipoSolicitacao) {
        return Map.of(
                "id", UUID.randomUUID().toString(),
                "eventoId", eventoId,
                "tipoSolicitacao", tipoSolicitacao,
                "criadoEm", Instant.now().toString());
    }
}
