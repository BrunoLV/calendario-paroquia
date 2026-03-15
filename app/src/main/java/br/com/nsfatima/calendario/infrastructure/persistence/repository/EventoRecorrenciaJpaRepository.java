package br.com.nsfatima.calendario.infrastructure.persistence.repository;

import java.util.UUID;
import br.com.nsfatima.calendario.infrastructure.persistence.entity.EventoRecorrenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRecorrenciaJpaRepository extends JpaRepository<EventoRecorrenciaEntity, UUID> {
}
