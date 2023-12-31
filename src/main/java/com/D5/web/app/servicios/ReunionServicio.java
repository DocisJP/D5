package com.D5.web.app.servicios;

import com.D5.web.app.entidades.Proyecto;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.D5.web.app.entidades.Reunion;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.repositorios.ReunionRepositorio;
import jakarta.transaction.Transactional;
import java.util.ArrayList;

@Service
public class ReunionServicio {

    @Autowired
    private ReunionRepositorio reunionRepositorio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    //plan b
    @org.springframework.transaction.annotation.Transactional
    public Reunion crear(String nombre, String detalle, Boolean estado, Date horarioDeInicio, Date horarioDeFin, Usuario usuario, Proyecto proyecto) throws MyException {
        Reunion reunion = new Reunion();
        Usuario usuarioEncargado = usuario;

        reunion.setDetalle(detalle);
        reunion.setUsuario(usuario);
        reunion.setNombre(nombre);
        reunion.setEstado(estado);
        reunion.setHorarioDeInicio(horarioDeInicio);
        reunion.setHorarioDeFin(horarioDeFin);
        reunion.setProyecto(proyecto);
        return reunionRepositorio.save(reunion);
    }

    @org.springframework.transaction.annotation.Transactional
    public Reunion modificar(Reunion algunaEntidad) {

        validar(algunaEntidad);

        return reunionRepositorio.saveAndFlush(algunaEntidad);
    }

    @org.springframework.transaction.annotation.Transactional
    public void eliminar(Reunion algunaEntidad) {
        reunionRepositorio.delete(algunaEntidad);
    }
//    @Transactional
//    public void crear(String nombre, Date horarioInicio, Boolean estado, List<Usuario> participantes, String detalle) {
//        Reunion reunion = new Reunion();
//        reunion.setNombre(nombre);
//        reunion.setDetalle(detalle);
//        reunion.setHorarioDeInicio(horarioInicio);
//        reunion.setParticipantes(participantes);
//        reunion.setEstado(true);
//
//        System.out.println("Reunión a guardar: " + reunion);  // Imprime información para depuración
//
//        validar(reunion);
//        reunionRepositorio.save(reunion);
//    }
//
//    @Transactional
//    public Reunion modificar(Reunion reunion) {
//        validar(reunion);
//        return reunionRepositorio.save(reunion);
//    }
//
//    @Transactional
//    public void eliminar(Reunion reunion) {
//        reunionRepositorio.delete(reunion);
//    }

    @Transactional
    public Boolean cambiarEstado(Reunion reunion) {
        Reunion existente = reunionRepositorio.findById(reunion.getId()).orElseThrow(() -> new IllegalArgumentException("Reunion no encontrada"));
        existente.setEstado(!existente.getEstado());
        reunionRepositorio.save(existente);
        return existente.getEstado();
    }

    public Reunion buscarPorId(String id) {
        Optional<Reunion> resultado = reunionRepositorio.findById(id);
        if (resultado.isPresent()) {
            return resultado.get();
        } else {
            throw new IllegalArgumentException("Reunion no encontrado con el ID: " + id);
        }
    }

    public Map<String, Object> verDetalle(Reunion reunion) {
        Map<String, Object> detalles = new HashMap();
        detalles.put("nombre", reunion.getNombre());
        detalles.put("detalle", reunion.getDetalle());
        detalles.put("horarioDeInicio", reunion.getHorarioDeInicio());
        detalles.put("proyecto", reunion.getProyecto());
        detalles.put("usuario", reunion.getUsuario());

        return detalles;
    }

    private void validar(Reunion reunion) {

        if (reunion.getNombre() == null || reunion.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la reunión es obligatorio.");
        }
        if (reunion.getDetalle() == null || reunion.getDetalle().isEmpty()) {
            throw new IllegalArgumentException("El detalle falta.");
        }
        if (reunion.getHorarioDeFin().before(reunion.getHorarioDeInicio())) {
            throw new IllegalArgumentException("Debe ingresar un horario de finalización valido");
        }
        if (reunion.getHorarioDeInicio().after(reunion.getHorarioDeFin())) {
            throw new IllegalArgumentException("Debe ingresar un horario de inicio valido");
        }
        if (reunion.getHorarioDeInicio() == null || reunion.getHorarioDeFin() == null) {
            throw new IllegalArgumentException("Debe ingresar un horario valido");
        }

    }

    @Transactional
    public List<Reunion> listaReuniones() {
        return reunionRepositorio.findAll();
    }

    //************Probando panel trabajo**********
    public List<Reunion> obtenerReunionesPorProyecto(String proyectoId) {

        return reunionRepositorio.buscarPorIdProyecto(proyectoId);
    }
    //****************************************************

    @Transactional
    public int Inactivos() {

        Integer contador = 0;

        List<Reunion> reuniones = new ArrayList();

        reuniones = reunionRepositorio.findAll();

        for (Reunion reunion : reuniones) {

            if (!reunion.getEstado()) {
                contador++;
            }
        }
        return contador;

    }

}
