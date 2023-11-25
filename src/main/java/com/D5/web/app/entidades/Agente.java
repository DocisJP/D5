package com.D5.web.app.entidades;



import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;



@Entity
@DiscriminatorValue("Agente")
public class Agente extends Usuario{
	
	@NotBlank
	private String cargo;
	
	@OneToMany(mappedBy= "agente")
	private List<Tarea> tareas;
	
	@ManyToMany
	@JoinTable(
			name = "agente_contactos", // Nombre de la tabla de unión
	        joinColumns = @JoinColumn(name = "agente_id"), // Columna en la tabla de unión para 'Agente'
	        inverseJoinColumns = @JoinColumn(name = "contacto_id") // Columna en la tabla de unión para 'Usuario'
	    )
	private List<Usuario> contactos;
	
	private List<String> notas;

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public List<Tarea> getTareas() {
		return tareas;
	}

	public void setTareas(List<Tarea> tareas) {
		this.tareas = tareas;
	}

	public List<Usuario> getContactos() {
		return contactos;
	}

	public void setContactos(List<Usuario> contactos) {
		this.contactos = contactos;
	}

	public List<String> getNotas() {
		return notas;
	}

	public void setNotas(List<String> notas) {
		this.notas = notas;
	}
	
}
