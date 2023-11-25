package com.D5.web.app.servicios;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.D5.web.app.entidades.Agente;

import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.repositorios.IServicioGeneral;
import com.D5.web.app.repositorios.TareaRepositorio;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TareaServicio implements IServicioGeneral<Tarea> {

	@Autowired
    TareaRepositorio tareaRepositorio;


    @Transactional
    public void agregar(String nombreTarea, String descripcion, Boolean estado, Agente agente, Date fechaInicio, Date fechaFinalizacion) throws MyException {
    	Tarea tarea = new Tarea();

    	valida(nombreTarea, descripcion, estado, fechaInicio, fechaFinalizacion);
        
    	tarea.setAgente(agente);
        tarea.setDescripcion(descripcion);
        tarea.setEstado(estado);
        tarea.setFechaInicio(fechaInicio);
        tarea.setFechaFinalizacion(fechaFinalizacion);
        tarea.setNombreTarea(nombreTarea);

        tareaRepositorio.save(tarea);

    }

    @Transactional
    @Override
    public void modificar(Tarea algunaEntidad) {
        tareaRepositorio.saveAndFlush(algunaEntidad);

    }

    @Transactional
    @Override
    public void eliminar(Tarea algunaEntidad) {
        tareaRepositorio.delete(algunaEntidad);

    }

    @Transactional
    @Override
    public void cambiarEstado(Tarea algunaEntidad) {
        Optional<Tarea> respuesta = tareaRepositorio.findById(algunaEntidad.getId());

        if (respuesta.isPresent()) {
            if (respuesta.get().getEstado()) {
                respuesta.get().setEstado(Boolean.FALSE);
            } else {
                respuesta.get().setEstado(Boolean.TRUE);
            }
        }

    }

    @Transactional(readOnly = true)
    public List<Tarea> listarTareas() {
        return tareaRepositorio.findAll();
    }

    @Transactional
    public Tarea getOne(String id) {
        return tareaRepositorio.getReferenceById(id);
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

    @Override
    public void crear(Tarea algunaEntidad) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void registrar(Tarea algunaEntidad) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void accederPerfil(Tarea algunClienteoAgente) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void visualizar(Tarea dashBoardoProyectoReunion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void agregar(Tarea algunaEntidad) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void valida(Tarea algunError) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
