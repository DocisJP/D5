package com.D5.web.app.entidades;



import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.validation.constraints.NotBlank;



@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class Agente extends Usuario{
	
	@NotBlank
	private String cargo;
	
	@OneToMany(mappedBy= "agente")
	private List<Tarea> tareas;
	@OneToMany
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
