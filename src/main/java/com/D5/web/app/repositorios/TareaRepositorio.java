package com.D5.web.app.repositorios;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.D5.web.app.entidades.Tarea;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface TareaRepositorio extends JpaRepository<Tarea, String>{

    
    @Query("SELECT t FROM Tarea t,Usuario u WHERE t.usuario_id=u.id AND u.email = :email")
    public List<Tarea> findTareaByUsuario(@Param("email") String email);
}
