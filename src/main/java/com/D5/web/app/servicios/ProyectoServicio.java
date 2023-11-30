package com.D5.web.app.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Reunion;
import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.repositorios.ProyectoRepositorio;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ProyectoServicio {

	@Autowired
    private ProyectoRepositorio proyectoRepositorio;


    
    @Transactional
    public Proyecto modificar(Proyecto proyecto) {
        Proyecto existente = proyectoRepositorio.findById(proyecto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        existente.setNombre(proyecto.getNombre());
        existente.setDetalleProyecto(proyecto.getDetalleProyecto());
        existente.setFechaInicio(proyecto.getFechaInicio());
        existente.setFechaFinalizacion(proyecto.getFechaFinalizacion());
        existente.setEstado(proyecto.getEstado());

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
    public void eliminar(Proyecto proyecto) {
        proyectoRepositorio.delete(proyecto);
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
    }

    
    
    public List<Proyecto> visualizar() {
        return proyectoRepositorio.findAll();
    }


    
    public void accederPerfil(Proyecto proyecto) {
        proyectoRepositorio.findById(proyecto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
    }
    
    
    @Transactional
    public void crear(Proyecto proyecto) {
        valida(proyecto); 
        establecerRelaciones(proyecto); 
        proyectoRepositorio.save(proyecto);
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

}


