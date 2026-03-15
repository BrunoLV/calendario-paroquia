package br.com.nsfatima.calendario.application.usecase.observacao;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ListObservacoesUseCase {

    public List<Map<String, Object>> execute(UUID eventoId) {
        return List.of(Map.of(
                "eventoId", eventoId.toString(),
                "tipo", "NOTA",
                "conteudo", "Historico append-only"));
    }
}
