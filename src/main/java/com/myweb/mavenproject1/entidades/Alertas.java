package com.myweb.mavenproject1.entidades;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "alertas")
public class Alertas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "documento_id")
    private Long documentoId;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "tipo_alerta")
    private String tipoAlerta;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Column(name = "fecha_notificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNotificacion;

    // Campos nuevos para control de vencimientos
    @Column(name = "fecha_limite_respuesta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaLimiteRespuesta;

    @Column(name = "dias_restantes")
    private Integer diasRestantes;

    @Column(name = "es_pqrs")
    private Boolean esPqrs = false;

    // Getters y Setters completos
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDocumentoId() { return documentoId; }
    public void setDocumentoId(Long documentoId) { this.documentoId = documentoId; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getTipoAlerta() { return tipoAlerta; }
    public void setTipoAlerta(String tipoAlerta) { this.tipoAlerta = tipoAlerta; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Date getFechaNotificacion() { return fechaNotificacion; }
    public void setFechaNotificacion(Date fechaNotificacion) { this.fechaNotificacion = fechaNotificacion; }

    public Date getFechaLimiteRespuesta() { return fechaLimiteRespuesta; }
    public void setFechaLimiteRespuesta(Date fechaLimiteRespuesta) { this.fechaLimiteRespuesta = fechaLimiteRespuesta; }

    public Integer getDiasRestantes() { return diasRestantes; }
    public void setDiasRestantes(Integer diasRestantes) { this.diasRestantes = diasRestantes; }

    public Boolean getEsPqrs() { return esPqrs; }
    public void setEsPqrs(Boolean esPqrs) { this.esPqrs = esPqrs; }
}