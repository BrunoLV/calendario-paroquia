package br.com.nsfatima.calendario.infrastructure.persistence.repository;

import java.util.UUID;
import br.com.nsfatima.calendario.infrastructure.persistence.entity.EventoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoJpaRepository extends JpaRepository<EventoEntity, UUID> {
}
