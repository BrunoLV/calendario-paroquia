package br.com.nsfatima.calendario.application.usecase.evento;

import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GetTaxaEventosExtraUseCase {

    public Map<String, Object> execute(String periodo) {
        return Map.of(
                "periodo", periodo,
                "taxaEventosAdicionadosExtra", 0.0);
    }
}
