package com.D5.web.app.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Reunion;
import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.repositorios.IServicioGeneral;
import com.D5.web.app.repositorios.ProyectoRepositorio;
//import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ProyectoServicio implements IServicioGeneral<Proyecto> {

    private final ProyectoRepositorio proyectoRepositorio;

    @Autowired
    public ProyectoServicio(ProyectoRepositorio proyectoRepositorio) {
        this.proyectoRepositorio = proyectoRepositorio;
    }

    @Override
    @Transactional
    public void agregar(Proyecto proyecto) {
        proyectoRepositorio.save(proyecto);
    }

    @Override
    @Transactional
    public void modificar(Proyecto proyecto) {
        Proyecto existente = proyectoRepositorio.findById(proyecto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
        
        existente.setFechaInicio(null);
        existente.setFechaFinalizacion(null);
        existente.setNombre(proyecto.getNombre());
        existente.setDetalleProyecto(proyecto.getDetalleProyecto());
        existente.setFechaInicio(proyecto.getFechaInicio());
        existente.setFechaFinalizacion(proyecto.getFechaFinalizacion());
        

        
        actualizarReuniones(existente, proyecto.getListaReuniones());
        actualizarTareas(existente, proyecto.getTareas());

        proyectoRepositorio.save(existente);
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


    @Override
    @Transactional
    public void eliminar(Proyecto proyecto) {
        proyectoRepositorio.delete(proyecto);
    }

    @Override
    public void cambiarEstado(Proyecto proyecto) {
        Proyecto existente = proyectoRepositorio.findById(proyecto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
        existente.setEstado(!existente.getEstado());
        proyectoRepositorio.save(existente);
    }


    @Override
    public void registrar(Proyecto proyecto) {
        proyectoRepositorio.save(proyecto);
    }

    @Override
    public void valida(Proyecto proyecto) {
        if (proyecto.getNombre() == null || proyecto.getNombre().isEmpty() || proyecto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del proyecto es requerido");
        }
    }

    @Override
    public void visualizar(Proyecto proyecto) {
        proyectoRepositorio.findById(proyecto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
    }

    @Override
    public void accederPerfil(Proyecto proyecto) {
        proyectoRepositorio.findById(proyecto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
    }
    
    @Override
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


