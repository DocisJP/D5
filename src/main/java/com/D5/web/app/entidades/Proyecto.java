package com.D5.web.app.entidades;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;



@Entity
public class Proyecto {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @NotBlank
    private String nombre;
    
    @NotBlank
    private String detalleProyecto;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date fechaInicio;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date fechaFinalizacion;
    
 
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Agente> equipo;
    
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reunion> listaReuniones;
    
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarea> tareas;

    
    public Proyecto() {
        this.equipo = new ArrayList<>();
        this.listaReuniones = new ArrayList<>();
        this.tareas = new ArrayList<>();
    }

    
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDetalleProyecto() {
		return detalleProyecto;
	}

	public void setDetalleProyecto(String detalleProyecto) {
		this.detalleProyecto = detalleProyecto;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFinalizacion() {
		return fechaFinalizacion;
	}

	public void setFechaFinalizacion(Date fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}

	public List<Agente> getEquipo() {
		return equipo;
	}

	public void setEquipo(List<Agente> equipo) {
		this.equipo = equipo;
	}

	public List<Reunion> getListaReuniones() {
		return listaReuniones;
	}

	public void setListaReuniones(List<Reunion> listaReuniones) {
		this.listaReuniones = listaReuniones;
	}

	public List<Tarea> getTareas() {
		return tareas;
	}

	public void setTareas(List<Tarea> tareas) {
		this.tareas = tareas;
	}
	
	
}
