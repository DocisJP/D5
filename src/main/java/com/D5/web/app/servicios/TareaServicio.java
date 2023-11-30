package com.D5.web.app.servicios;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.repositorios.TareaRepositorio;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;


@Service
public class TareaServicio {

	@Autowired
    TareaRepositorio tareaRepositorio;


    @Transactional
    public void crear(String nombreTarea, String descripcion, Boolean estado, Date fechaInicio, Date fechaFinalizacion) throws MyException {
    	Tarea tarea = new Tarea();

    	valida(nombreTarea, descripcion, estado, fechaInicio, fechaFinalizacion);
        
   
        tarea.setDescripcion(descripcion);
        tarea.setEstado(estado);
        tarea.setNombreTarea(nombreTarea);

        tareaRepositorio.save(tarea);

    }

    @Transactional
     public Tarea modificar(Tarea algunaEntidad) {
        return tareaRepositorio.saveAndFlush(algunaEntidad);

    }

    @Transactional
    public void eliminar(Tarea algunaEntidad) {
        tareaRepositorio.delete(algunaEntidad);

    }

    @Transactional
    public Boolean cambiarEstado(Tarea tarea) {
        Tarea existente = tareaRepositorio.findById(tarea.getId())
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
        existente.setEstado(!existente.getEstado());
        tareaRepositorio.save(existente);
        return existente.getEstado();
    }

    @Transactional(readOnly = true)
    public List<Tarea> listarTareas() {
        return tareaRepositorio.findAll();
    }

    
    public Tarea buscarPorId(String id) {
        Optional<Tarea> resultado = tareaRepositorio.findById(id);
        if (resultado.isPresent()) {
            return resultado.get();
        } else {
            throw new IllegalArgumentException("Proyecto no encontrado con el ID: " + id);
        }
    }

    
    public void valida(String nombreTarea, String descripcion, Boolean estado, Date fechaInicio, Date fechaFinalizacion) throws MyException {
        if (estado == null) {
            throw new MyException("Estado invalido");
        }
        if (descripcion.isBlank()) {
            throw new MyException("La descripcion no puede ser espacios");
        }
        if (fechaInicio.before(Date.from(Instant.now())) || fechaInicio.equals(fechaFinalizacion)) {
            throw new MyException("Fecha de inicio no puede ser anterior al día de hoy");
        }
        if (fechaInicio.after(fechaFinalizacion)) {
            throw new MyException("Fecha de inicio no puede ser posterior a la de finalización");
        }

    }

    	
    public Map<String, Object> verDetalle(Tarea tarea) {
        Map<String, Object> detalles = new HashMap<>();
        detalles.put("nombre", tarea.getDescripcion());
        detalles.put("detalle", tarea.getEstado());
        detalles.put("horarioDeInicio", tarea.getNombreTarea());
        detalles.put("proyecto", tarea.getProyecto());
        detalles.put("participantes", tarea.getFechaInicio());
        detalles.put("participantes", tarea.getFechaFinalizacion());

        return detalles;
    }

  




}
