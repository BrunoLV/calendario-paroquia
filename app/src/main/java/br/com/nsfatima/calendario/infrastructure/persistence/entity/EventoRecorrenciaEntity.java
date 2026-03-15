package br.com.nsfatima.calendario.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "eventos_recorrencia", schema = "calendario")
public class EventoRecorrenciaEntity extends BaseVersionedEntity {

    @Id
    private UUID id;

    @Column(name = "evento_base_id", nullable = false)
    private UUID eventoBaseId;

    @Column(nullable = false, length = 32)
    private String frequencia;

    @Column(nullable = false)
    private Integer intervalo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEventoBaseId() {
        return eventoBaseId;
    }

    public void setEventoBaseId(UUID eventoBaseId) {
        this.eventoBaseId = eventoBaseId;
    }

    public String getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }

    public Integer getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(Integer intervalo) {
        this.intervalo = intervalo;
    }
}
