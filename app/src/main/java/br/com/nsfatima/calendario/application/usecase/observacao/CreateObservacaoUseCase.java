package br.com.nsfatima.calendario.application.usecase.observacao;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CreateObservacaoUseCase {

    public Map<String, Object> execute(UUID eventoId, UUID usuarioId, String tipo, String conteudo) {
        return Map.of(
                "id", UUID.randomUUID().toString(),
                "eventoId", eventoId.toString(),
                "usuarioId", usuarioId.toString(),
                "tipo", tipo,
                "conteudo", conteudo,
                "criadoEm", Instant.now().toString());
    }
}
