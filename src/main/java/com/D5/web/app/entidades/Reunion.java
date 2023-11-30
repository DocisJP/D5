package com.D5.web.app.entidades;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.List;


@Entity
@Table(name = "reunion")
public class Reunion {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    
    @ManyToOne
    private Proyecto proyecto;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date horarioDeInicio;

    private String detalle;

    private Boolean estado;

    // Una reunión puede tener varios participantes
    @ManyToMany
    @JoinTable(
            name = "reunion_participantes",
            joinColumns = @JoinColumn(name = "reunion_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
 
    private List<Usuario> participantes = new ArrayList<Usuario>(); 
    
    private String nombre;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Date getHorarioDeInicio() {
        return horarioDeInicio;
    }

    public void setHorarioDeInicio(Date horarioDeInicio) {
        this.horarioDeInicio = horarioDeInicio;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

	public List<Usuario> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(List<Usuario> participantes) {
		this.participantes = participantes;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

   
    
    
}
