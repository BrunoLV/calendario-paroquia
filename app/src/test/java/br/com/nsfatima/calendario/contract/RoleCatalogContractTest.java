package br.com.nsfatima.calendario.contract;

import br.com.nsfatima.calendario.domain.policy.AuthorizationPolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class RoleCatalogContractTest {

    private final AuthorizationPolicy policy = new AuthorizationPolicy();

    @Test
    void shouldRejectSecretaryOutsideCouncil() {
        assertFalse(policy.isRoleAllowed("PASTORAL", "secretario"));
    }
}
