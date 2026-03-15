package br.com.nsfatima.calendario.infrastructure.persistence.repository;

import br.com.nsfatima.calendario.infrastructure.persistence.entity.EventoEnvolvidoEntity;
import br.com.nsfatima.calendario.infrastructure.persistence.entity.EventoEnvolvidoEntity.Key;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoEnvolvidoJpaRepository extends JpaRepository<EventoEnvolvidoEntity, Key> {
}
