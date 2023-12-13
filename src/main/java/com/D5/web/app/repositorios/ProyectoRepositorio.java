package com.D5.web.app.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.D5.web.app.entidades.Proyecto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProyectoRepositorio extends JpaRepository<Proyecto, String> {

    @Query("SELECT p FROM Proyecto p WHERE p.estado = :estado")
    List<Proyecto> findByEstado(@Param("estado") Boolean estado);

    @Query("SELECT p FROM Proyecto p JOIN p.usuarios u WHERE u.id = :id")
    List<Proyecto> listarProyectosPorIdUsuario(@Param("id") String id);

}
