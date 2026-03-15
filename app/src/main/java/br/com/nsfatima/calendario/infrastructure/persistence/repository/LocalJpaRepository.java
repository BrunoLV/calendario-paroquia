package br.com.nsfatima.calendario.infrastructure.persistence.repository;

import java.util.UUID;
import br.com.nsfatima.calendario.infrastructure.persistence.entity.LocalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalJpaRepository extends JpaRepository<LocalEntity, UUID> {
}
