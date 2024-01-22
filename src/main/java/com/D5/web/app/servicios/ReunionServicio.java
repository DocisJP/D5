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
import com.D5.web.app.enumerador.Progreso;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.repositorios.ReunionRepositorio;
import com.D5.web.app.repositorios.UsuarioRepositorio;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;
import org.springframework.ui.Model;

@Service
public class ReunionServicio {

    @Autowired
    private ReunionRepositorio reunionRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ProyectoServicio proyectoServicio;

    //plan b
      @Transactional
    public Reunion crear(String nombre, String detalle, Boolean estado, Progreso progreso, Date horarioDeInicio, Date horarioDeFin, Usuario usuarioDestinatario, Usuario usuario, List<Usuario> usuariosParticipantes, Proyecto proyecto) throws MyException {

        Reunion reunion = new Reunion();

        reunion.setDetalle(detalle);
        reunion.setNombre(nombre);
        reunion.setEstado(false);
        reunion.setProgreso(Progreso.PENDIENTE);
        reunion.setHorarioDeInicio(horarioDeInicio);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate = dateFormat.format(horarioDeInicio);
        reunion.setHorarioDeInicioFormatt(formattedDate);
        reunion.setHorarioDeFin(horarioDeFin);
        String formattedDateFin = dateFormat.format(horarioDeFin);
        reunion.setHorarioDeFinFormatt(formattedDateFin);
        reunion.setUsuarioDestinatario(usuarioDestinatario);
        reunion.setUsuario(usuario);
        reunion.setUsuarios(usuariosParticipantes);
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

    // Verificar si los horarios son null antes de comparar
    if (reunion.getHorarioDeInicio() == null || reunion.getHorarioDeFin() == null) {
        throw new IllegalArgumentException("Debe ingresar un horario válido.");
    }

    // Verificar si los horarios son válidos
    if (!Objects.equals(reunion.getHorarioDeFin(), reunion.getHorarioDeInicio()) &&
            reunion.getHorarioDeFin().before(reunion.getHorarioDeInicio())) {
        throw new IllegalArgumentException("Debe ingresar un horario de finalización válido.");
    }

    // Puedes manejar la lógica específica si los horarios pueden ser null según tu aplicación
    if (!Objects.equals(reunion.getHorarioDeInicio(), reunion.getHorarioDeFin()) &&
            reunion.getHorarioDeInicio().after(reunion.getHorarioDeFin())) {
        throw new IllegalArgumentException("Debe ingresar un horario de inicio válido.");
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

            if (reunion.getEstado().toString().equalsIgnoreCase("FALSE")) {
                contador++;

            }
        }
        return contador;

    }

    public List<Reunion> listarReunionesPorIdUsuario(String id) {
        return reunionRepositorio.listarReunionesPorIdUsuario(id);
    }

}
