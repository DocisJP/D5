package com.D5.web.app.repositorios;


import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 
import com.D5.web.app.entidades.Reunion; 
import com.D5.web.app.entidades.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface ReunionRepositorio extends JpaRepository<Reunion, String> {


    List<Reunion> findByHorarioDeInicioBetween(Date inicio, Date fin);
    
    @Query("SELECT r FROM Reunion r WHERE r.proyecto.id = :id")
    List<Reunion> buscarPorIdProyecto(@Param("id") String id);

    @Query("SELECT r FROM Reunion r WHERE r.nombre = :nombre")
    List<Reunion> findByNombre(@Param("nombre") String nombre);

    @Query("SELECT r FROM Reunion r JOIN r.usuarios u WHERE u.id = :id")
    List<Reunion> listarReunionesPorIdUsuario(@Param("id") String id);

    @Query("SELECT r FROM Reunion r WHERE r.usuarioDestinatario = :usuario AND r.estado = false")
    List<Reunion> findAllReunionesByUsuarioDestinatario(Usuario usuario);
}
