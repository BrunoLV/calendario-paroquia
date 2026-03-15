package br.com.nsfatima.calendario.application.usecase.metrics;

import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GetIndicadorRetrabalhoUseCase {

    public Map<String, Object> execute(String periodo) {
        return Map.of(
                "periodo", periodo,
                "indicadorRetrabalho", 0.0);
    }
}
