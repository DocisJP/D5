package com.D5.web.app.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Usuario;


@Repository
public interface ProyectoRepositorio extends JpaRepository<Proyecto, String>{ 

	List<Proyecto> findByEstado(Boolean estado);
 
	@Query("SELECT DISTINCT u.empresa FROM Proyecto p JOIN p.usuarios u WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
	List<String> findEmpresasByProjectName(@Param("nombre") String nombre);

}
