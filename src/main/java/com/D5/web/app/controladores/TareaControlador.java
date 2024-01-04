package com.D5.web.app.controladores;

import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.enumerador.Progreso;
import com.D5.web.app.enumerador.Rol;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.servicios.ProyectoServicio;
import com.D5.web.app.servicios.TareaServicio;
import com.D5.web.app.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        
        List<Tarea> tareas = tareaServicio.listarTareas();
        Collections.sort(tareas);
        
        model.addAttribute("tareas", tareas);
        return "panel_pendientes.html";
    }

    @GetMapping("/registrar/{id}")
    public String formularioRegistrar(@PathVariable String id, Model model, HttpSession session) {
        
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        List<Proyecto> proyectosAsociadosUsuarioSession = proyectoServicio.listarProyectosPorIdUsuario(id);
        Set<Usuario> usuariosAsociados = new HashSet<>();

        for (Proyecto proyecto : proyectosAsociadosUsuarioSession) {
            // Obtener la lista de usuarios asociados a cada proyecto
            List<Usuario> usuariosProyecto = usuarioServicio.listarUsuariosPorIdProyecto(proyecto.getId());
            System.out.println("usuariosProyecto " + usuariosProyecto.toString());
            // Filtrar usuarios por el rol de AGENTE y agregarlos al conjunto
            usuariosProyecto.stream()
                    .filter(usuario -> usuario.getRol().equals(Rol.AGENTE))
                    .forEach(usuariosAsociados::add);
        }
        for (Usuario usuariosAsociado : usuariosAsociados) {
            System.out.println("usuariosProyectoAsociados " + usuariosAsociado.getNombre());
        }

        // Verificar si hay al menos un agente en la lista
        if (!usuariosAsociados.isEmpty()&& !logueado.getRol().equals(Rol.ADMIN)) {
            List<Proyecto> proyectos = proyectoServicio.listarProyectosPorIdUsuario(id);

            model.addAttribute("tarea", new Tarea());
            model.addAttribute("proyectos", proyectos);
            model.addAttribute("usuarios", usuariosAsociados);

            return "formulario_tarea";
        } else if (logueado.getRol().equals(Rol.ADMIN)){
            List<Proyecto> proyectos = proyectoServicio.listarProyectos();
            List<Usuario> usuariosProyecto = new ArrayList();
            for (Proyecto proyecto : proyectos) {
                usuariosProyecto = usuarioServicio.listarUsuariosPorIdProyecto(proyecto.getId());
                usuariosProyecto.stream()
                    .filter(usuario -> usuario.getRol().equals(Rol.AGENTE))
                    .forEach(usuariosAsociados::add);
                System.out.println("usuarios del proyecto "+usuariosProyecto.toString());
            }
                         
            model.addAttribute("tarea", new Tarea());
            model.addAttribute("proyectos", proyectos);
            model.addAttribute("usuarios", usuariosAsociados);
            
            return "formulario_tarea";
        } else{
         return "redirect:/inicio";
        }
    }

    // Procesa el formulario y registra una nueva tarea
    @PostMapping("/registro")
    public String registrarTarea(
            @RequestParam String nombreTarea,
            @RequestParam String descripcion,
            @RequestParam Boolean estado,
            @RequestParam(required = false) Progreso progreso,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date fechaInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date fechaFinalizacion,
            @RequestParam String usuarioId,
            @RequestParam String proyectoId,
            ModelMap modelo, RedirectAttributes redirectAttrs
    ) {
        Usuario usuarioEncargado = usuarioServicio.buscarUsuario(usuarioId);
        Proyecto proyectoAsociado = proyectoServicio.buscarPorId(proyectoId);
        try {

            Tarea tareaGuardada = tareaServicio.crear(
                    nombreTarea,
                    descripcion,
                    estado,
                    progreso,
                    fechaInicio,
                    fechaFinalizacion,
                    usuarioEncargado,
                    proyectoAsociado);

            if (tareaGuardada != null && tareaGuardada.getId() != null) {

                redirectAttrs.addFlashAttribute("exito", "La tarea fue creada con Exito");

                return "redirect:/tarea/detalle/" + tareaGuardada.getId();

            } else {
                // Manejar el caso de que tareaGuardada sea nula o no tenga ID
                modelo.addAttribute("error", "La tarea no pudo ser creada.");

                return "formulario_tarea";
            }
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());

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

    @GetMapping("/lista/{idUsuario}")
    public String listarTareas(Model model, @PathVariable String idUsuario, RedirectAttributes redirectAttrs, HttpSession session) {

        // Obtener la información del usuario en sesión usando Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nombreUsuario = authentication.getName();

        // Buscar los proyectos asociados al usuario
        List<Proyecto> proyectosUsuario = proyectoServicio.listarProyectosPorIdUsuario(idUsuario);
        List<Tarea> tareasDelProyecto = new ArrayList();
        
        //Busco las tareas de los proyectos asociados al usuario
        for (Proyecto proyecto : proyectosUsuario) {
           tareasDelProyecto = tareaServicio.obtenerTareasPorProyecto(proyecto.getId());
        }
        
        // si su fecha de fin es anterior al dia de hoy seteo el progreso en finalizada
        for (Tarea tarea : tareasDelProyecto) {
            if(tarea.getFechaFinalizacion().before(new Date())){
                tarea.setProgreso(Progreso.FINALIZADO);
            } else {
                tarea.setProgreso(Progreso.PENDIENTE);
            }
        }
        
        // Lista para almacenar todas las tareas de los proyectos del usuario
        List<Tarea> tareas = new ArrayList<>();

        // Iterar sobre los proyectos del usuario y agregar las tareas al listado
        for (Proyecto proyecto : proyectosUsuario) {
            // Buscar las tareas solo si hay tareas asociadas al proyecto
            List<Tarea> tareasProyecto = tareaServicio.buscarPorProyectoId(proyecto.getId());

            // Verificar si hay tareas antes de agregarlas a la lista
            if (!tareasProyecto.isEmpty()) {
                tareas.addAll(tareasProyecto);
            }
        }
        
        Collections.sort(tareas);

        model.addAttribute("tareas", tareas);
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "lista_tareas";
    }

    //Agregue post y get para las vistas falta la logica
    @GetMapping("/modificar/{id}")
    public String modificarTarea(@PathVariable String id, Model model, HttpSession session) {
        
        Tarea tarea = tareaServicio.buscarPorId(id);
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        List<Proyecto> proyectosAsociadosUsuarioSession = proyectoServicio.listarProyectosPorIdUsuario(logueado.getId());
        Set<Usuario> usuariosAsociados = new HashSet<>();

        for (Proyecto proyecto : proyectosAsociadosUsuarioSession) {
            // Obtener la lista de usuarios asociados a cada proyecto
            List<Usuario> usuariosProyecto = usuarioServicio.listarUsuariosPorIdProyecto(proyecto.getId());
            System.out.println("usuariosProyecto " + usuariosProyecto.toString());
            // Filtrar usuarios por el rol de AGENTE y agregarlos al conjunto
            usuariosProyecto.stream()
                    .filter(usuario -> usuario.getRol().equals(Rol.AGENTE))
                    .forEach(usuariosAsociados::add);
        }
        for (Usuario usuariosAsociado : usuariosAsociados) {
            System.out.println("usuariosProyectoAsociados " + usuariosAsociado.getNombre());
        }

        // Verificar si hay al menos un agente en la lista
        if (!usuariosAsociados.isEmpty() && !logueado.getRol().equals(Rol.ADMIN)) {
            List<Proyecto> proyectos = proyectoServicio.listarProyectosPorIdUsuario(logueado.getId());

            model.addAttribute("tarea", tarea);
            model.addAttribute("proyectos", proyectos);
            model.addAttribute("usuarios", usuariosAsociados);
            model.addAttribute("usuariosession", logueado);
            model.addAttribute("progresos", Progreso.values()); 

            return "tarea_modificar.html";
        } else if (logueado.getRol().equals(Rol.ADMIN)){
            List<Proyecto> proyectos = proyectoServicio.listarProyectos();
            List<Usuario> usuariosProyecto = new ArrayList();
            for (Proyecto proyecto : proyectos) {
                usuariosProyecto = usuarioServicio.listarUsuariosPorIdProyecto(proyecto.getId());
                usuariosProyecto.stream()
                    .filter(usuario -> usuario.getRol().equals(Rol.AGENTE))
                    .forEach(usuariosAsociados::add);
                System.out.println("usuarios del proyecto "+usuariosProyecto.toString());
            }
            model.addAttribute("progresos", Progreso.values());           
            model.addAttribute("tarea", tarea);
            model.addAttribute("proyectos", proyectos);
            model.addAttribute("usuarios", usuariosAsociados);
            model.addAttribute("usuariosession", logueado);
            
            return "tarea_modificar.html";
        } else{
         return "redirect:/inicio";
        }
    }

    @PostMapping("/modificar")
    public String modificarProyecto(
            @RequestParam String id,
            @RequestParam String nombreTarea,
            @RequestParam String descripcion,
            @RequestParam Boolean estado,
            @RequestParam(required = false) Progreso progreso,
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
        aModificar.setProgreso(progreso);
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
