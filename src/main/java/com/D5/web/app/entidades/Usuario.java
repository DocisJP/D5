package com.D5.web.app.entidades;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.D5.web.app.enumerador.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {
	
	//Esto es parte del formulario

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@NotBlank
	private String nombre;

	@NotBlank
	private String apellido;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	private Long dni;

	@NotBlank
	private Integer telefono;
	
	
	//Esto no es parte del formulario
	private Boolean estado;

	private Role role;

	private List<Proyecto> proyectoLista;
	
}
