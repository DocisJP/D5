package com.D5.web.app.entidades;


import jakarta.persistence.Entity;
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
public class Cliente extends Usuario{
	
	@NotBlank
	private String empresa;
	@NotBlank
	private String cuil;
	@NotBlank
	private String direccionFiscal;
	@NotBlank
	private String profesion;
	
	
}
