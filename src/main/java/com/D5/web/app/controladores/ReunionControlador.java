package com.D5.web.app.controladores;

import com.D5.web.app.entidades.Proyecto;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.D5.web.app.entidades.Reunion;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.enumerador.Rol;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.servicios.ProyectoServicio;
import com.D5.web.app.servicios.ReunionServicio;
import com.D5.web.app.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reunion")
public class ReunionControlador {

    @Autowired
    ReunionServicio reunionServicio;

    @Autowired
    ProyectoServicio proyectoServicio;

    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/panel")
    public String panel(Model model) {
        List<Reunion> reuniones = reunionServicio.listaReuniones();

        model.addAttribute("reuniones", reuniones);
        return "panel_reunion.html";
    }

    @GetMapping("/calendario")
    public String mostrarCalendario(Model model) {
        List<Reunion> reuniones = reunionServicio.listaReuniones();
        Reunion reunion = new Reunion();
        model.addAttribute("reuniones", reuniones);
        model.addAttribute("reunion", reunion);
        return "calendario_reuniones.html";
    }

    @GetMapping("/registrar")
    public String formularioReunion(Model model) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        List<Proyecto> proyectos = proyectoServicio.listarProyectos();
        model.addAttribute("reunion", new Reunion());
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("proyectos", proyectos);
        return "formulario_reunion.html";
    }

    // Procesa el formulario y registra una nueva tarea
    @PostMapping("/registro")
    public String registrarReunion(
            @RequestParam String nombre,
            @RequestParam String detalle,
            @RequestParam Boolean estado,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date horarioDeInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date horarioDeFin,
            @RequestParam String usuarioId,
            @RequestParam String proyectoId,
            ModelMap modelo, RedirectAttributes redirectAttrs
    ) {
        Usuario usuarioEncargado = usuarioServicio.buscarUsuario(usuarioId);
        Proyecto proyectoAsociado = proyectoServicio.buscarPorId(proyectoId);
        try {

            Reunion reunionGuardada = reunionServicio.crear(
                    nombre,
                    detalle,
                    estado,
                    horarioDeInicio,
                    horarioDeFin,
                    usuarioEncargado,
                    proyectoAsociado);

            if (reunionGuardada != null && reunionGuardada.getId() != null) {
                redirectAttrs.addFlashAttribute("exito", "La reunión fue creada con éxito");
                return "redirect:/reunion/detalle/" + reunionGuardada.getId();
            } else {
                // Manejar el caso de que tareaGuardada sea nula o no tenga ID
                modelo.addAttribute("error", "La reunion no pudo ser creada.");

                return "formulario_reunion.html";
            }

        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());

            return "formulario_reunion.html";
        }
    }

    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable String id, Model model) throws MyException {
        Reunion reunion = reunionServicio.buscarPorId(id);
        if (reunion == null) {
            return "redirect:/inicio";
        } else {
            model.addAttribute("reunion", reunion);
            return "detalle_reunion";
        }
    }

    // Muestra la lista de tareas
    @GetMapping("/lista")
    public String listarTareas(Model model) {
        model.addAttribute("reuniones", reunionServicio.listaReuniones());
        return "lista_reuniones";
    }

    @GetMapping("/modificar/{id}")
    public String modificarReunion(@PathVariable String id, Model model) {
        Reunion reunion = reunionServicio.buscarPorId(id);
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        List<Proyecto> proyectos = proyectoServicio.listarProyectos();
        model.addAttribute("reunion", reunion);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("proyectos", proyectos);

        return "reunion_modificar.html";
    }

    @PostMapping("/modificar")
    public String modificarReunion(
            @RequestParam String id,
            @RequestParam String nombre,
            @RequestParam String detalle,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date horarioDeInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date horarioDeFin,
            @RequestParam String usuarioId,
            @RequestParam String proyectoId,
            Model model
    ) {
        Reunion reunion = reunionServicio.buscarPorId(id);

        reunion.setNombre(nombre);
        reunion.setDetalle(detalle);
        reunion.setHorarioDeInicio(horarioDeInicio);
        reunion.setHorarioDeFin(horarioDeFin);
        reunion.setUsuario(usuarioServicio.buscarUsuario(usuarioId));
        reunion.setProyecto(proyectoServicio.buscarPorId(proyectoId));
        reunion.setEstado(Boolean.TRUE);

        try {
            reunionServicio.modificar(reunion);
            model.addAttribute("exito", "La modificación fue aceptada");

        } catch (Exception e) {
            List<Usuario> usuarios = usuarioServicio.listarUsuarios();
            List<Proyecto> proyectos = proyectoServicio.listarProyectos();
            model.addAttribute("reunion", reunion);
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("proyectos", proyectos);
            model.addAttribute("error", e.getMessage());

            return "reunion_modificar.html";
        }

        return "redirect:/reunion/panel";
    }

    @GetMapping("/cambiarEstado/{id}")
    public String estadoReunion(@PathVariable String id, ModelMap modelo) {
        try {
            Reunion reunion = reunionServicio.buscarPorId(id);
            reunionServicio.cambiarEstado(reunion);
            modelo.addAttribute("exito", "se cambio el estado de la reunion");
        } catch (Exception e) {
            modelo.addAttribute("error", "No se pudo cambiar estado");
        }
        return "redirect:/reunion/panel";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarReunion(@PathVariable String id) {
        reunionServicio.eliminar(reunionServicio.buscarPorId(id));
        return "redirect:/reunion/panel";
    }

    @GetMapping("/solicitar")
    public String solicitarReunion(Model model, HttpSession session) {
        Usuario usuarioEnSession = (Usuario) session.getAttribute("usuariosession");

        // Obtener los proyectos del usuario en sesión
        List<Proyecto> proyectos = proyectoServicio.listarProyectosPorIdUsuario(usuarioEnSession.getId());

        // Obtener los agentes vinculados a los proyectos del usuario en sesión
        List<Usuario> agentes = new ArrayList<>();
        for (Proyecto proyecto : proyectos) {
            List<Usuario> buscarAgentes = usuarioServicio.listarUsuariosPorIdProyecto(proyecto.getId());

            // Filtro los agentes según el rol
            List<Usuario> agentesDelProyecto = buscarAgentes.stream()
                    .filter(u -> u.getRol() == Rol.AGENTE)
                    .collect(Collectors.toList());

            agentes.addAll(agentesDelProyecto);
        }

        model.addAttribute("reunion", new Reunion());
        model.addAttribute("agentes", agentes);
        model.addAttribute("proyectos", proyectos);
        return "solicitud_reunion.html";
    }

}
