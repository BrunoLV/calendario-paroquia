package br.com.nsfatima.calendario.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;
import br.com.nsfatima.calendario.infrastructure.persistence.entity.ObservacaoEventoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservacaoEventoJpaRepository extends JpaRepository<ObservacaoEventoEntity, UUID> {
    List<ObservacaoEventoEntity> findByEventoId(UUID eventoId);
}
