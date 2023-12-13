package com.D5.web.app.repositorios;

import com.D5.web.app.entidades.Proyecto;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.D5.web.app.entidades.Reunion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ReunionRepositorio extends JpaRepository<Reunion, String> {

    List<Reunion> findByNombre(String nombre);

    List<Reunion> findByHorarioDeInicioBetween(Date inicio, Date fin);
    
    @Query("SELECT r FROM Reunion r WHERE r.proyecto.id = :id")
    List<Reunion> buscarPorIdProyecto(@Param("id") String id);

}
