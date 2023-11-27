package com.D5.web.app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.D5.web.app.entidades.Proyecto;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProyectoRepositorio extends JpaRepository<Proyecto, String> {

    @Query("SELECT p FROM Proyecto_usuarios pu, Proyecto p,Usuario u "
            + "WHERE p.id=pu.proyecto_id "
            + "AND pu.usuario_id=u.id "
            + "AND u.email = :email")
    public List<Proyecto> findTareaByUsuario(@Param("email") String email);
}
