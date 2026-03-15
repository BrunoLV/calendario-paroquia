package br.com.nsfatima.calendario.integration.eventos;

import br.com.nsfatima.calendario.application.usecase.aprovacao.CreateSolicitacaoAprovacaoUseCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AprovacaoHorarioIntegrationTest {

    @Test
    void shouldCreateApprovalRequest() {
        CreateSolicitacaoAprovacaoUseCase useCase = new CreateSolicitacaoAprovacaoUseCase();
        assertNotNull(useCase.create("evento-1", "ALTERACAO_HORARIO"));
    }
}
