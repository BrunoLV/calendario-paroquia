package br.com.nsfatima.calendario.domain.policy;

import java.util.Locale;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationPolicy {

    public boolean isRoleAllowed(String organizationType, String role) {
        String normalizedOrg = organizationType == null ? "" : organizationType.toUpperCase(Locale.ROOT);
        String normalizedRole = role == null ? "" : role.toLowerCase(Locale.ROOT);

        return switch (normalizedOrg) {
            case "PASTORAL", "LAICATO" -> Set.of("coordenador", "vice-coordenador", "membro").contains(normalizedRole);
            case "CLERO" -> Set.of("paroco", "vigario", "padre").contains(normalizedRole);
            case "CONSELHO" -> Set.of("coordenador", "vice-coordenador", "secretario", "membro").contains(normalizedRole);
            default -> false;
        };
    }
}
