package com.D5.web.app.controladores;

import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.enumerador.Rol;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.servicios.ProyectoServicio;
import com.D5.web.app.servicios.TareaServicio;
import com.D5.web.app.servicios.UsuarioServicio;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tarea")
public class TareaControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ProyectoServicio proyectoServicio;

    @Autowired
    private TareaServicio tareaServicio;

    @GetMapping("/panel")
    public String panel(Model model) {
     model.addAttribute("tareas", tareaServicio.listarTareas());
        return "panel_pendientes.html";
    }

    // Muestra el formulario para agregar una nueva tarea
    @GetMapping("/registrar")
    public String formularioRegistrar(Model model) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios().stream().filter(usuario -> usuario.getRol().equals(Rol.AGENTE)).collect(Collectors.toList());;
        List<Proyecto> proyectos = proyectoServicio.listarProyectos();
        model.addAttribute("tarea", new Tarea());
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("proyectos", proyectos);
        return "formulario_tarea";
    }

    // Procesa el formulario y registra una nueva tarea
    @PostMapping("/registro")
    public String registrarTarea(
            @RequestParam String nombreTarea,
            @RequestParam String descripcion,
            @RequestParam Boolean estado,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date fechaInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date fechaFinalizacion,
            @RequestParam String usuarioId,
            @RequestParam String proyectoId,
            ModelMap modelo
    ) {
        Usuario usuarioEncargado = usuarioServicio.buscarUsuario(usuarioId);
        Proyecto proyectoAsociado = proyectoServicio.buscarPorId(proyectoId);
        try {

            Tarea tareaGuardada = tareaServicio.crear(
                    nombreTarea,
                    descripcion,
                    estado,
                    fechaInicio,
                    fechaFinalizacion,
                    usuarioEncargado,
                    proyectoAsociado);

            if (tareaGuardada != null && tareaGuardada.getId() != null) {
                modelo.addAttribute("exito", "La tarea pudo ser creada.");
                return "redirect:/tarea/detalle/" + tareaGuardada.getId();
            } else {
                // Manejar el caso de que tareaGuardada sea nula o no tenga ID
                modelo.addAttribute("error", "La tarea no pudo ser creada.");

                return "formulario_tarea";
            }
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "formulario_tarea";
        }
    }

    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable String id, Model model) throws MyException {
        Tarea tarea = tareaServicio.buscarPorId(id);
        if (tarea == null) {
            return "redirect:/index";
        } else {
            model.addAttribute("tarea", tarea);
            return "detalle_tarea";
        }
    }

    // Muestra la lista de tareas
    @GetMapping("/lista")
    public String listarTareas(Model model) {
        model.addAttribute("tareas", tareaServicio.listarTareas());
        return "lista_tareas";
    }

    //Agregue post y get para las vistas falta la logica
    @GetMapping("/modificar/{id}")
    public String modificarTarea(@PathVariable String id, Model model) {
        Tarea tarea = tareaServicio.buscarPorId(id);
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        List<Proyecto> proyectos = proyectoServicio.listarProyectos();
        model.addAttribute("tarea", tarea);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("proyectos", proyectos);

        return "tarea_modificar.html";
    }

    @PostMapping("/modificar")
    public String modificarProyecto(
            @RequestParam String id,
            @RequestParam String nombreTarea,
            @RequestParam String descripcion,
            @RequestParam Boolean estado,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date fechaInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date fechaFinalizacion,
            @RequestParam String usuarioId,
            @RequestParam String proyectoId,
            ModelMap modelo
    ) {

        Tarea aModificar = new Tarea();
        aModificar.setId(id);
        aModificar.setDescripcion(descripcion);
        aModificar.setEstado(estado);
        aModificar.setNombreTarea(nombreTarea);
        aModificar.setUsuario(usuarioServicio.buscarUsuario(usuarioId));
        aModificar.setProyecto(proyectoServicio.buscarPorId(proyectoId));
        aModificar.setFechaInicio(fechaInicio);
        aModificar.setFechaFinalizacion(fechaFinalizacion);

        Tarea tarea = tareaServicio.modificar(aModificar);

        return "redirect:/tarea/detalle/" + tarea.getId();
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarTarea(@PathVariable String id) {
        tareaServicio.eliminar(tareaServicio.buscarPorId(id));
        return "redirect:/tarea/panel";
    }
}
