package com.D5.web.app.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.enumerador.Progreso;
import java.util.Date;

@Repository
public interface ProyectoRepositorio extends JpaRepository<Proyecto, String> {

    @Query("SELECT p FROM Proyecto p JOIN p.usuarios u WHERE u.email = :email")
    public List<Proyecto> findProyectoByUsuario(@Param("email") String email);

    @Query("SELECT p FROM Proyecto p WHERE p.estado = :estado")
    public List<Proyecto> findByEstado(@Param("estado") Boolean estado);

    @Query("SELECT p FROM Proyecto p JOIN p.usuarios u WHERE u.id = :id")
    List<Proyecto> listarProyectosPorIdUsuario(@Param("id") String id);

    @Query("SELECT DISTINCT u.empresa FROM Proyecto p JOIN p.usuarios u WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<String> findEmpresasByProjectName(@Param("nombre") String nombre);

    // Definir el método personalizado para buscar nombres de proyectos que coincidan con la consulta
    @Query("SELECT DISTINCT p.nombre FROM Proyecto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<String> findNombresByNombreContainingIgnoreCase(String query);

    List<Proyecto> findProyectoByProgreso(@Param("progreso") Progreso actual);
    
    @Query("SELECT DISTINCT CONCAT(u.nombre, ' ', u.apellido, ' (', u.rol, ')') FROM Proyecto p JOIN p.usuarios u WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombreProyecto, '%'))")
    List<String> findUsuariosByProyectName(@Param("nombreProyecto") String nombreProyecto);

    @Query("SELECT DISTINCT CONCAT(r.nombre, ' Fecha: ', r.horarioDeInicio, ' (', r.progreso, ')') FROM Proyecto p JOIN p.listaReuniones r WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombreProyecto, '%'))")
    List<String> findReunionesByProyectName(@Param("nombreProyecto") String nombreProyecto);

    @Query("SELECT DISTINCT CONCAT(t.nombreTarea, ' Fecha: ', t.fechaInicio, ' (', t.progreso, ')') FROM Proyecto p JOIN p.tareas t WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombreProyecto, '%'))")
    List<String> findTareasByProyectName(@Param("nombreProyecto") String nombreProyecto);

}
