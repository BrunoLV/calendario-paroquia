package br.com.nsfatima.calendario.infrastructure.persistence.repository;

import java.util.UUID;
import br.com.nsfatima.calendario.infrastructure.persistence.entity.ProjetoEventoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoEventoJpaRepository extends JpaRepository<ProjetoEventoEntity, UUID> {
}
