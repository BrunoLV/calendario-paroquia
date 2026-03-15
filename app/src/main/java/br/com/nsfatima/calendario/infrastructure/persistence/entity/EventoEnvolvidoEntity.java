package br.com.nsfatima.calendario.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "eventos_envolvidos", schema = "calendario")
@IdClass(EventoEnvolvidoEntity.Key.class)
public class EventoEnvolvidoEntity {

    @Id
    @Column(name = "evento_id")
    private UUID eventoId;

    @Id
    @Column(name = "organizacao_id")
    private UUID organizacaoId;

    @Column(name = "papel_participacao", length = 64)
    private String papelParticipacao;

    public UUID getEventoId() {
        return eventoId;
    }

    public void setEventoId(UUID eventoId) {
        this.eventoId = eventoId;
    }

    public UUID getOrganizacaoId() {
        return organizacaoId;
    }

    public void setOrganizacaoId(UUID organizacaoId) {
        this.organizacaoId = organizacaoId;
    }

    public String getPapelParticipacao() {
        return papelParticipacao;
    }

    public void setPapelParticipacao(String papelParticipacao) {
        this.papelParticipacao = papelParticipacao;
    }

    public static class Key implements Serializable {
        private UUID eventoId;
        private UUID organizacaoId;

        public Key() {
        }

        public Key(UUID eventoId, UUID organizacaoId) {
            this.eventoId = eventoId;
            this.organizacaoId = organizacaoId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Key key)) {
                return false;
            }
            return Objects.equals(eventoId, key.eventoId) && Objects.equals(organizacaoId, key.organizacaoId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(eventoId, organizacaoId);
        }
    }
}
