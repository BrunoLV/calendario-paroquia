package br.com.nsfatima.calendario.infrastructure.observability;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CadastroEventoMetricsPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CadastroEventoMetricsPublisher.class);

    public void publishCadastroTempo(Duration duracao) {
        LOGGER.info("metric cadastro_evento_tempo_ms={}", duracao.toMillis());
    }
}
