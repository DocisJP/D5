package com.D5.web.app.servicios;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.D5.web.app.entidades.Agente;

import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Reunion;
import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.repositorios.IServicioGeneral;
import com.D5.web.app.repositorios.ProyectoRepositorio;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProyectoServicio implements IServicioGeneral<Proyecto> {
	

    @Autowired
    ProyectoRepositorio proyectoRepositorio;

    @Transactional
    public void agregar(String nombre, String detalleProyecto, Date fechaInicio, Date fechaFinalizacion) {
        //Proyecto proyecto = new Proyecto(null, nombre, detalleProyecto, fechaInicio, fechaFinalizacion, null, null, null);
        //proyectoRepositorio.save(proyecto);

    }

    public List<Reunion> verReuniones(Proyecto proyecto) {
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(proyecto.getId());
        List<Reunion> contenido = new ArrayList<>();
        if (respuesta.isPresent()) {
            contenido = respuesta.get().getListaReuniones();
        }
        return contenido;

    }

    public List<Tarea> verTareas(Proyecto proyecto) {
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(proyecto.getId());
        List<Tarea> lista = new ArrayList<>();
        if (respuesta.isPresent()) {
            lista = respuesta.get().getTareas();
        }
        return lista;
    }

    @Override
    public void agregar(Proyecto algunaEntidad) {
        // TODO Auto-generated method stub

    }

    @Override
    public void modificar(Proyecto algunaEntidad) {
        proyectoRepositorio.saveAndFlush(algunaEntidad);
    }

    @Override
    public void eliminar(Proyecto algunaEntidad) {
        proyectoRepositorio.delete(algunaEntidad);
    }

    @Override
    public void cambiarEstado(Proyecto algunaEntidad) {
    }

    @Transactional
    public void crear(String nombre, String detalleProyecto, Date fechaInicio, Date fechaFinalizacion, List<Agente> equipo, List<Reunion> listaReuniones, List<Tarea> tareas) throws MyException {
        //valida(nombre, detalleProyecto, fechaInicio, fechaFinalizacion, equipo, listaReuniones, tareas);
       // Proyecto proyecto = new Proyecto(null, nombre, detalleProyecto, fechaInicio, fechaFinalizacion, equipo, listaReuniones, tareas);
       // proyectoRepositorio.saveAndFlush(proyecto);
    }

    @Override
    public void registrar(Proyecto algunaEntidad) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visualizar(Proyecto dashBoardoProyectoReunion) {
        // TODO Auto-generated method stub

    }

    @Override
    public void accederPerfil(Proyecto algunClienteoAgente) {
        // TODO Auto-generated method stub

    }

    public void valida(String nombre, String detalleProyecto, Date fechaInicio, Date fechaFinalizacion, List<Agente> equipo, List<Reunion> listaReuniones, List<Tarea> tareas) throws MyException {
        if (nombre.isEmpty() || nombre == null) {
            throw new MyException("nombre vacío o nulo");

        }
        
        if (detalleProyecto.isBlank() || detalleProyecto == null) {
            throw new MyException("detalle vacío o nulo");

        }
        if (fechaInicio.before(Date.from(Instant.now())) || fechaInicio.equals(fechaFinalizacion)) {
            throw new MyException("Fecha de inicio no puede ser anterior al día de hoy");
        }
        if (fechaInicio.after(fechaFinalizacion)) {
            throw new MyException("Fecha de inicio no puede ser posterior a la de finalización");
        }

    }

    @Override
    public void crear(Proyecto algunaEntidad) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void valida(Proyecto algunError) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
