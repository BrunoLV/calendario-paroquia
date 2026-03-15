package br.com.nsfatima.calendario.infrastructure.security;

import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ExternalMembershipReader {

    public Set<String> findRolesByUserAndOrganization(UUID userId, UUID organizationId) {
        return Set.of();
    }
}
