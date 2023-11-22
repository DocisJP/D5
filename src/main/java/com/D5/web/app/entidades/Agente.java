package com.D5.web.app.entidades;

import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class Agente extends Usuario{
	
	@NotBlank
	private String cargo;
	
	@ManyToOne
	private List<Tarea> tareas;
	
	private List<Usuario> contactos;
	
	private List<String> notas;
	
	
}
