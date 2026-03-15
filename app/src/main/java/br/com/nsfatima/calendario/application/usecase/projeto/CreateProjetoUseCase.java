package br.com.nsfatima.calendario.application.usecase.projeto;

import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CreateProjetoUseCase {

    public Map<String, Object> create(String nome, String descricao) {
        return Map.of(
                "id", UUID.randomUUID().toString(),
                "nome", nome,
                "descricao", descricao);
    }
}
