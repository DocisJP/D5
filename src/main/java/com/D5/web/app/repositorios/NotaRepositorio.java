package com.D5.web.app.repositorios;

import com.D5.web.app.entidades.Nota;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaRepositorio extends JpaRepository<Nota,String>{
    
    @Query("SELECT n FROM Nota n WHERE n.usuario.id = :id")
    List<Nota> buscarNotasPorUsuarioId(@Param("id") String id);
    
    @Query("SELECT n FROM Nota n WHERE n.proyecto.id = :id")
    List<Nota> buscarNotasPorProyectoId(@Param("id") String id);

    
}
