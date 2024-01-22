package com.D5.web.app.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Reunion;
import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.enumerador.Progreso;
import com.D5.web.app.enumerador.Rol;
import com.D5.web.app.repositorios.ProyectoRepositorio;
import jakarta.validation.ValidationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ProyectoServicio {

    @Autowired
    private ProyectoRepositorio proyectoRepositorio;

    @Transactional
    public Proyecto crear(Proyecto proyecto) {
        try {
            if (proyecto.getEstado() == null) {
                proyecto.setEstado(true);
            }
            valida(proyecto);
            establecerRelaciones(proyecto);
            return proyectoRepositorio.save(proyecto);
        } catch (ValidationException e) {
            throw new IllegalArgumentException("Error de validación: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalStateException("Error al crear el proyecto: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Proyecto modificar(Proyecto proyecto) {
        Proyecto existente = proyectoRepositorio.findById(proyecto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        existente.setNombre(proyecto.getNombre());
        existente.setDetalleProyecto(proyecto.getDetalleProyecto());
        existente.setFechaInicio(proyecto.getFechaInicio());
        existente.setFechaFinalizacion(proyecto.getFechaFinalizacion());
        existente.setEstado(proyecto.getEstado());
        existente.setProgreso(proyecto.getProgreso());
        actualizarReuniones(existente, proyecto.getListaReuniones());
        actualizarTareas(existente, proyecto.getTareas());

        return proyectoRepositorio.save(existente);
    }

    private void actualizarReuniones(Proyecto existente, List<Reunion> nuevasReuniones) {
        existente.getListaReuniones().clear();
        if (nuevasReuniones != null) {
            existente.getListaReuniones().addAll(nuevasReuniones);
        }
    }

    private void actualizarTareas(Proyecto existente, List<Tarea> nuevasTareas) {
        existente.getTareas().clear();
        if (nuevasTareas != null) {
            existente.getTareas().addAll(nuevasTareas);
        }
    }

    @Transactional
    public void eliminarPorId(String id) {
        proyectoRepositorio.findById(id)
                .ifPresent(proyectoRepositorio::delete);
    }

    public Boolean cambiarEstado(Proyecto proyecto) {
        Proyecto existente = proyectoRepositorio.findById(proyecto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
        existente.setEstado(!existente.getEstado());
        proyectoRepositorio.save(existente);
        return existente.getEstado();
    }

    public void registrar(Proyecto proyecto) {
        proyectoRepositorio.save(proyecto);
    }

    private void valida(Proyecto proyecto) {
        if (proyecto.getNombre() == null || proyecto.getNombre().isEmpty() || proyecto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del proyecto es requerido");
        }
        if (proyecto.getDetalleProyecto() == null) {
            throw new IllegalArgumentException("El detalle del producto no puede ser nulo ");
        }
        if (proyecto.getFechaInicio().before(Date.from(Instant.now())) || proyecto.getFechaInicio().equals(proyecto.getFechaFinalizacion())) {
            throw new IllegalArgumentException("Fecha de inicio no puede ser anterior al día de hoy");
        }
        if (proyecto.getFechaInicio().after(proyecto.getFechaFinalizacion())) {
            throw new IllegalArgumentException("Fecha de inicio no puede ser posterior a la de finalización");
        }
        if (proyecto.getFechaFinalizacion().before(proyecto.getFechaInicio())) {
            throw new IllegalArgumentException("Fecha de finalización no puede ser previa a la fecha de inicio");
        }
    }

    public List<Proyecto> visualizar() {
        return proyectoRepositorio.findAll();
    }

    public void verDetalle(Proyecto proyecto) {
        proyectoRepositorio.findById(proyecto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
    }

    private void establecerRelaciones(Proyecto proyecto) {
        for (Reunion reunion : proyecto.getListaReuniones()) {
            reunion.setProyecto(proyecto);
        }
        for (Tarea tarea : proyecto.getTareas()) {
            tarea.setProyecto(proyecto);
        }

    }

    public Proyecto buscarPorId(String id) {
        Optional<Proyecto> resultado = proyectoRepositorio.findById(id);
        if (resultado.isPresent()) {
            return resultado.get();
        } else {
            throw new IllegalArgumentException("Proyecto no encontrado con el ID: " + id);
        }
    }

    public List<Proyecto> listarProyectos() {

        List<Proyecto> proyectos = proyectoRepositorio.findAll();
        for (Proyecto proyect : proyectos) {
            if (proyect.getFechaFinalizacion().before(new Date())) {
                System.out.println("fecha fin IF" + proyect.getFechaFinalizacion() + proyect.getProgreso());
                proyect.setProgreso(Progreso.FINALIZADO);
            } else {
                proyect.setProgreso(Progreso.PENDIENTE);
                System.out.println("fecha fin ELSE" + proyect.getFechaFinalizacion() + proyect.getProgreso());
            }
            proyectoRepositorio.save(proyect);
        }

        return proyectos;
    }

    public List<Proyecto> listarProyectosEnProgreso() {
        return proyectoRepositorio.findProyectoByProgreso(Progreso.PENDIENTE);
    }

    public List<Proyecto> listarProyectosPorIdUsuario(String id) {
        return proyectoRepositorio.listarProyectosPorIdUsuario(id);
    }

    public List<Usuario> getAgentes(Proyecto proyecto) {
        List<Usuario> agentes = new ArrayList<>();
        for (Usuario usuario : proyecto.getUsuarios()) {
            if (usuario.getRol().equals(Rol.AGENTE)) {
                agentes.add(usuario);
            }
        }
        return agentes;
    }

    @Transactional
    public Integer Inactivos() {

        Integer contador = 0;

        List<Proyecto> proyectos = new ArrayList();

        proyectos = proyectoRepositorio.findAll();

        for (Proyecto proyecto : proyectos) {

            if (proyecto.getEstado().toString().equalsIgnoreCase("FALSE")) {
                contador++;

            }
        }
        return contador;

    }

    public List<String> findEmpresasByProjectName(String projectName) {
        return proyectoRepositorio.findEmpresasByProjectName(projectName);
    }

    public List<String> findNombresProyectosByQuery(String query) {
        // Realiza una búsqueda en el repositorio de proyectos por nombres que coincidan con la consulta
        return proyectoRepositorio.findNombresByNombreContainingIgnoreCase(query);
    }

    public List<String> findUsuariosByProjectName(String nombreProyecto) {
       return proyectoRepositorio.findUsuariosByProyectName(nombreProyecto);
    }

    public List<String> findReunionesByProjectName(String nombreProyecto) {
       return proyectoRepositorio.findReunionesByProyectName(nombreProyecto); }

    public List<String> findTareasByProjectName(String nombreProyecto) {
     return proyectoRepositorio.findTareasByProyectName(nombreProyecto); }
    
    
  public boolean esProyectoReciente(Proyecto proyecto) {
    // Obtener la fecha de referencia hace 6 meses
    Calendar calendario = Calendar.getInstance();
    calendario.add(Calendar.MONTH, -6);
    Date fechaHaceSeisMeses = calendario.getTime();

    // Verificar si la fecha de inicio del proyecto es posterior a la fecha hace 6 meses
    return proyecto.getFechaInicio().after(fechaHaceSeisMeses);
}

}
