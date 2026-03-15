package br.com.nsfatima.calendario.api.dto.evento;

import java.time.Instant;

public record UpdateEventoRequest(
        String titulo,
        String descricao,
        Instant inicio,
        Instant fim,
        String status,
        String adicionadoExtraJustificativa,
        String canceladoMotivo) {
}
