package br.com.nsfatima.calendario.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "eventos", schema = "calendario")
public class EventoEntity extends BaseVersionedEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 160)
    private String titulo;

    @Column(length = 4000)
    private String descricao;

    @Column(name = "organizacao_responsavel_id", nullable = false)
    private UUID organizacaoResponsavelId;

    @Column(name = "inicio_utc", nullable = false)
    private Instant inicioUtc;

    @Column(name = "fim_utc", nullable = false)
    private Instant fimUtc;

    @Column(nullable = false, length = 32)
    private String status;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public UUID getOrganizacaoResponsavelId() {
        return organizacaoResponsavelId;
    }

    public void setOrganizacaoResponsavelId(UUID organizacaoResponsavelId) {
        this.organizacaoResponsavelId = organizacaoResponsavelId;
    }

    public Instant getInicioUtc() {
        return inicioUtc;
    }

    public void setInicioUtc(Instant inicioUtc) {
        this.inicioUtc = inicioUtc;
    }

    public Instant getFimUtc() {
        return fimUtc;
    }

    public void setFimUtc(Instant fimUtc) {
        this.fimUtc = fimUtc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
