package com.D5.web.app.repositorios;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 
import com.D5.web.app.entidades.Reunion; 
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
 

@Repository
public interface ReunionRepositorio extends JpaRepository<Reunion, String> {

 
    @Query("SELECT r FROM Reunion r WHERE r.nombre = :nombre")
    List<Reunion> findByNombre(@Param("nombre") String nombre);

    @Query("SELECT r FROM Reunion r WHERE r.horarioDeInicio BETWEEN :inicio AND :fin")
    List<Reunion> findByHorarioDeInicioBetween(@Param("inicio") Date inicio, @Param("fin") Date fin);
//
//    @Query("SELECT r FROM Reunion r WHERE r.proyecto_id = :idProyecto")
//    List<Reunion> buscarPorIdProyecto(@Param("idProyecto") String idProyecto);
 
}
