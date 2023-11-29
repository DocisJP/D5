package com.D5.web.app.repositorios;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.D5.web.app.entidades.Tarea;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface TareaRepositorio extends JpaRepository<Tarea, String>{
 
    
	@Query("SELECT t FROM Tarea t WHERE t.usuario.email = :email")
    List<Tarea> findTareasByUsuarioEmail(String email);
  
    List<Tarea> findByNombreTarea(String nombreTarea);
}
