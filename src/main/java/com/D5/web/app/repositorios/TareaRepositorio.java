package com.D5.web.app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.D5.web.app.entidades.Tarea;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface TareaRepositorio extends JpaRepository<Tarea, String> {

    @Query("SELECT t FROM Tarea t WHERE t.usuario.email = :email")
    List<Tarea> findTareasByUsuarioEmail(@Param("email") String email);
    
    @Query("SELECT t FROM Tarea t WHERE t.usuario.id = :id")
    List<Tarea> findTareasByUsuarioId(@Param("id") String id);
    
    @Query("SELECT t FROM Tarea t WHERE t.proyecto.id = :id")
    List<Tarea> findTareasByProyectoId(@Param("id") String id);
    
    @Query("SELECT t FROM Tarea t WHERE t.nombreTarea = :nombreTarea")
    List<Tarea> findByNombreTarea(@Param("nombreTarea") String nombreTarea);

}
