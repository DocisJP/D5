package com.D5.web.app.servicios;

import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.repositorios.IServicioGeneral;
import com.D5.web.app.repositorios.ProyectoRepositorio;
import java.util.Date;

public class ProyectoServicio implements IServicioGeneral<Proyecto> {

    Proyecto proyecto;
    ProyectoRepositorio proyectoRepositorio;
    public void agregar(String nombre, String detalleProyecto, Date fechaInicio, Date fechaFinalizacion) {
        proyecto= new Proyecto(null, nombre, detalleProyecto, fechaInicio, fechaFinalizacion, null, null, null);
        proyectoRepositorio.save(proyecto);

    }

    @Override
    public void agregar(Proyecto algunaEntidad) {
        // TODO Auto-generated method stub

    }

    @Override
    public void modificar(Proyecto algunaEntidad) {
        // TODO Auto-generated method stub

    }

    @Override
    public void eliminar(Proyecto algunaEntidad) {
        // TODO Auto-generated method stub

    }

    @Override
    public void cambiarEstado(Proyecto algunaEntidad) {
        // TODO Auto-generated method stub

    }

    @Override
    public void crear(Proyecto algunaEntidad) {
        // TODO Auto-generated method stub

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

    @Override
    public void valida(Proyecto algunError) {
        // TODO Auto-generated method stub

    }

}
