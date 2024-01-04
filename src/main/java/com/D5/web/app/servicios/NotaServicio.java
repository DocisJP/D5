package com.D5.web.app.servicios;

import com.D5.web.app.entidades.Nota;
import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.repositorios.NotaRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotaServicio {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private NotaRepositorio notaRepositorio;

    @Transactional
    public Nota crear(String descripcion, Date fecha, Usuario usuario, Proyecto proyecto) throws MyException {
        Nota nota = new Nota();
        Usuario usuarioEncargado = usuario;

        nota.setDetalleNota(descripcion);
        nota.setFecha(fecha);
        nota.setProyecto(proyecto);
        nota.setUsuario(usuario);
        return notaRepositorio.save(nota);
    }

    @Transactional
    public Nota modificar(Nota algunaEntidad) {

        return notaRepositorio.saveAndFlush(algunaEntidad);
    }

    @Transactional
    public void eliminar(Nota algunaEntidad) {
        notaRepositorio.delete(algunaEntidad);
    }

        public List<Nota> listarNotas() {
        return notaRepositorio.findAll();
    }

    public List<Nota> listarNotasPorIdUsuario(String id) {
        return notaRepositorio.buscarNotasPorUsuarioId(id);
    }

     public List<Nota> listarNotasPorIdProyecto(String id) {
        return notaRepositorio.buscarNotasPorProyectoId(id);
    }
     
         public Nota buscarPorId(String id) {
        Optional<Nota> resultado = notaRepositorio.findById(id);
        if (resultado.isPresent()) {
            return resultado.get();
        } else {
            throw new IllegalArgumentException("Nota no encontrada con el ID: " + id);
        }
    }
}
