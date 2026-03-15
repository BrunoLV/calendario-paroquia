package br.com.nsfatima.calendario.infrastructure.persistence.repository;

import java.util.UUID;
import br.com.nsfatima.calendario.infrastructure.persistence.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaJpaRepository extends JpaRepository<CategoriaEntity, UUID> {
}
