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
import com.D5.web.app.enumerador.Progreso;
import com.D5.web.app.enumerador.Rol;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.servicios.ProyectoServicio;
import com.D5.web.app.servicios.ReunionServicio;
import com.D5.web.app.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
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
        List<Usuario> usuarios = new ArrayList<>();
        List<Usuario> participantes = new ArrayList<>();

        for (Reunion reunione : reuniones) {
            if (reunione.getHorarioDeFin().before(new Date())) {
                reunione.setProgreso(Progreso.FINALIZADO);
            } else {
                reunione.setProgreso(Progreso.PENDIENTE);
            }
        }

        //Ordeno las reuniones por fecha inicio
        Collections.sort(reuniones);
        
        for (Reunion reunion : reuniones) {
            // Agrega todos los participantes de la reunión actual a la lista general
            participantes.addAll(reunion.getUsuarios());
            // Añade el usuario de la reunión actual a la lista general de usuarios
            usuarios.add(reunion.getUsuario());
        }

        System.out.println("participantes " + participantes.toString());
        System.out.println("usuarios " + usuarios.toString());

        model.addAttribute("reuniones", reuniones);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("participantes", participantes);

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

    @GetMapping("/participantes/{id}")
    public String listadosUsuariosReunion(@PathVariable String id, ModelMap model) {
        Reunion reunion = reunionServicio.buscarPorId(id);

        List<Usuario> listaActual = reunion.getUsuarios();
        List<Usuario> participantes = usuarioServicio.listaUsuarios();
        model.addAttribute("listaActual", listaActual);
        model.addAttribute("participantes", participantes);
        model.addAttribute("reunion", reunion);
        return "agregar_participantes_reunion";
    }

    @PostMapping("/participantes")
    public String agregaUsuarioAReunion(@RequestParam String reunionId, @RequestParam String usuarioId, ModelMap modelo) {

        Usuario usuario = usuarioServicio.getOne(usuarioId);
        Reunion reunion = reunionServicio.buscarPorId(reunionId);

        try {
            List<Usuario> listado = reunion.getUsuarios();

            if (!listado.contains(usuario)) {
                listado.add(usuario);
                reunion.setUsuarios(listado);
            } else {
                reunion.setUsuarios(reunion.getUsuarios());
            }

            modelo.addAttribute("reunion", reunion);
            // Actualizar la lista de participantes después de agregar el usuario
            List<Usuario> listaActual = reunion.getUsuarios();
            modelo.addAttribute("listaActual", listaActual);

            reunionServicio.modificar(reunion);

            modelo.addAttribute("exito", "Usuario agregado");

        } catch (Exception e) {
            List<Usuario> listaActual = reunion.getUsuarios();
            List<Usuario> participantes = usuarioServicio.listaUsuarios();
            modelo.addAttribute("listaActual", listaActual);
            modelo.addAttribute("participantes", participantes);
            modelo.addAttribute("reunion", reunion);
            return "agregar_participantes_reunion";
        }
        return "redirect:/reunion/participantes/" + reunion.getId();
    }

    @PostMapping("/eliminar-participante")
    public String eliminarParticipante(@RequestParam String reunionId, @RequestParam String usuarioId) {
        // Lógica para eliminar al usuario de la lista de participantes en el proyecto
        Reunion reunion = reunionServicio.buscarPorId(reunionId);
        Usuario usuario = usuarioServicio.getOne(usuarioId);

        List<Usuario> listaActual = reunion.getUsuarios();
        listaActual.remove(usuario);

        reunion.setUsuarios(listaActual);
        reunionServicio.modificar(reunion);

        // Redirige a la página de participantes actualizada
        return "redirect:/reunion/participantes/" + reunion.getId();
    }

    @Transactional
    @GetMapping("/registrar")
    public String formularioReunion(Model model, HttpSession session) {
        Usuario usuarioEncargado = (Usuario) session.getAttribute("usuariosession");
        List<Proyecto> listadoProyecto = new ArrayList<>();
        Set<Proyecto> proyectos = new HashSet<>();
        Set<Usuario> usuarios = new HashSet<>();
        List<Usuario> listaUsuarios = new ArrayList();
        List<Proyecto> proyectosEnComun = new ArrayList<>();
        Reunion reunion = new Reunion();
        reunion.setProgreso(Progreso.PENDIENTE);

        // Si el rol es ADMIN, mostrar todos los usuarios y sus proyectos
        if (usuarioEncargado.getRol().equals(Rol.ADMIN)) {
            listaUsuarios = usuarioServicio.listarUsuarios();
            listadoProyecto = proyectoServicio.listarProyectos();
            proyectos.addAll(listadoProyecto);
            // Obtener proyectos de todos los usuarios
            usuarios.addAll(listaUsuarios);
            for (Usuario usuario : usuarios) {
                List<Proyecto> proyectosUsuario = proyectoServicio.listarProyectosPorIdUsuario(usuario.getId());
                proyectosEnComun.addAll(proyectosUsuario);
            }
        } else {
            // Si no es ADMIN, obtener proyectos vinculados al usuario encargado
            listadoProyecto = proyectoServicio.listarProyectosPorIdUsuario(usuarioEncargado.getId());
            proyectos.addAll(listadoProyecto);
            // Agregar todos los proyectos al usuario encargado
            proyectosEnComun.addAll(proyectos);
            for (Proyecto proyecto : proyectosEnComun) {
                List<Usuario> usuariosLista = usuarioServicio.listarUsuariosPorIdProyecto(proyecto.getId());
                usuarios.addAll(usuariosLista);
            }
        }

        model.addAttribute("reunion", new Reunion());
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("proyectosEnComun", proyectosEnComun);
        model.addAttribute("progreso", Progreso.values());
        return "formulario_reunion.html";
    }

    // Procesa el formulario y registra una nueva tarea
    @PostMapping("/registro")
    public String registrarReunion(
            @RequestParam String nombre,
            @RequestParam String detalle,
            @RequestParam Boolean estado,
            @RequestParam(required = false) Progreso progreso,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date horarioDeInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date horarioDeFin,
            @RequestParam String proyectoId,
            @RequestParam String usuarioDestinatarioId,
            ModelMap modelo, HttpSession session, RedirectAttributes redirectAttrs
    ) {
        Usuario usuarioEncargado = (Usuario) session.getAttribute("usuariosession");

        Usuario usuarioDestinatario = usuarioServicio.buscarUsuario(usuarioDestinatarioId);
        Proyecto proyectoAsociado = proyectoServicio.buscarPorId(proyectoId);

        List<Usuario> usuariosParticipantes = new ArrayList();

        usuariosParticipantes.add(usuarioEncargado);
        usuariosParticipantes.add(usuarioDestinatario);
        try {

            Reunion reunionGuardada = reunionServicio.crear(
                    nombre,
                    detalle,
                    estado,
                    progreso,
                    horarioDeInicio,
                    horarioDeFin,
                    usuarioDestinatario,
                    usuarioEncargado,
                    usuariosParticipantes,
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

    @GetMapping("/detalleSolicitud/{id}")
    public String verDetalleSolicitud(@PathVariable String id, Model model) throws MyException {
        Reunion reunion = reunionServicio.buscarPorId(id);
        if (reunion == null) {
            return "redirect:/inicio";
        } else {
            model.addAttribute("reunion", reunion);
            return "detalle_solicitud_reunion";
        }
    }

    // Muestra la lista de reuniones
    @GetMapping("/lista")
    public String listarReuniones(Model model) {
        
        model.addAttribute("reuniones", reunionServicio.listaReuniones());
        return "panel_reunion.html";
    }

    @GetMapping("/listaReuniones/{id}")
    public String listaReuniones(@PathVariable String id, ModelMap model, RedirectAttributes redirectAttrs, HttpSession session) {

        // Obtener la información del usuario en sesión usando Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nombreUsuario = authentication.getName();

        List<Reunion> reuniones = reunionServicio.listarReunionesPorIdUsuario(id);

        for (Reunion reunione : reuniones) {
            if (reunione.getHorarioDeFin().before(new Date())) {
                reunione.setProgreso(Progreso.FINALIZADO);
            } else {
                reunione.setProgreso(Progreso.PENDIENTE);
            }
        }

        //Ordeno las reuniones por fecha inicio
        Collections.sort(reuniones);
        System.out.println("reunionnesessss" + reuniones.toString());
        System.out.println("nombre" + nombreUsuario);
        List<Usuario> usuarios = new ArrayList<>();
        List<Usuario> participantes = new ArrayList<>();

        for (Reunion reunion : reuniones) {
            // Agrega todos los participantes de la reunión actual a la lista general
            participantes.addAll(reunion.getUsuarios());
            // Añade el usuario de la reunión actual a la lista general de usuarios
            usuarios.add(reunion.getUsuario());
        }

        redirectAttrs.addFlashAttribute("exitoMensaje", "La reunion se creo correctamente.");

        System.out.println("participantes " + participantes.toString());
        System.out.println("usuarios " + usuarios.toString());

        model.addAttribute("reuniones", reuniones);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("participantes", participantes);
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "panel_reunion.html";
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
        if (horarioDeFin.after(new Date())) {
            reunion.setProgreso(Progreso.FINALIZADO);
        } else {
            reunion.setProgreso(Progreso.PENDIENTE);
        }

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

        return "redirect:/reunion/detalle/" + reunion.getId();
    }

    @GetMapping("/cambiarEstado/{id}")
    public String estadoReunion(@PathVariable String id, ModelMap modelo, HttpSession session) {
        Usuario usuarioEnSession = (Usuario) session.getAttribute("usuariosession");
        try {
            Reunion reunion = reunionServicio.buscarPorId(id);
            reunionServicio.cambiarEstado(reunion);
            modelo.addAttribute("exito", "se cambio el estado de la reunion");
        } catch (Exception e) {
            modelo.addAttribute("error", "No se pudo cambiar estado");
        }
        if (usuarioEnSession.getRol().equals(Rol.ADMIN)) {
            return "redirect:/reunion/panel";
        } else {
            return "redirect:/reunion/listaReuniones/" + usuarioEnSession.getId();
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarReunion(@PathVariable String id) {
        reunionServicio.eliminar(reunionServicio.buscarPorId(id));
        return "redirect:/reunion/panel";
    }

    @GetMapping("/solicitar")
    public String solicitarReunion(Model model, HttpSession session) {
        Usuario usuarioEnSession = (Usuario) session.getAttribute("usuariosession");
        Reunion reunion = new Reunion();
        reunion.setProgreso(Progreso.PENDIENTE);
        // Obtener los proyectos del usuario en sesión
        List<Proyecto> proyectos = proyectoServicio.listarProyectosPorIdUsuario(usuarioEnSession.getId());

        // Obtener los usuarios vinculados a los proyectos del usuario en sesión
        Set<Usuario> usuarios = new HashSet<>();

        for (Proyecto proyecto : proyectos) {
            List<Usuario> buscarUsuarios = usuarioServicio.listarUsuariosPorIdProyecto(proyecto.getId());

            System.out.println("BUSCAR USUARIOS " + buscarUsuarios.toString());

//            // Filtro los usuarios según el rol
//            List<Usuario> agentesDelProyecto = buscarUsuarios.stream()
//                    .filter(u -> u.getRol() == Rol.AGENTE)
//                    .collect(Collectors.toList());
//            List<Usuario> clientesDelProyecto = buscarUsuarios.stream()
//                    .filter(u -> u.getRol() == Rol.CLIENTE)
//                    .collect(Collectors.toList());
//
//            agentes.addAll(agentesDelProyecto);
//            clientes.addAll(clientesDelProyecto);
            usuarios.addAll(buscarUsuarios);
        }

        model.addAttribute("reunion", new Reunion());
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("progresos", Progreso.values());

        if(usuarioEnSession.getRol().equals(Rol.ADMIN)){
            
        proyectos = proyectoServicio.listarProyectos();
            for (Proyecto proyecto: proyectos) {
                 List<Usuario> buscarUsuarios = usuarioServicio.listarUsuariosPorIdProyecto(proyecto.getId());
                 usuarios.addAll(buscarUsuarios);
            }
        
        model.addAttribute("reunion", new Reunion());
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("progresos", Progreso.values());

        }
        
        return "solicitud_reunion.html";
    }

    @PostMapping("/solicitar")
    public String solicitarReunion(
            @RequestParam String nombre,
            @RequestParam(required = false) String detalle,
            @RequestParam Boolean estado,
            @RequestParam Progreso progreso,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date horarioDeInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date horarioDeFin,
            @RequestParam(required = false) String usuarioDestinatarioId,
            @RequestParam String proyectoId,
            ModelMap modelo, HttpSession session, RedirectAttributes redirectAttrs
    ) {

        Usuario usuarioSolicitante = (Usuario) session.getAttribute("usuariosession");

        Usuario usuarioDestinatario = usuarioServicio.buscarUsuario(usuarioDestinatarioId);

        Proyecto proyectoAsociado = proyectoServicio.buscarPorId(proyectoId);

        List<Usuario> usuariosParticipantes = new ArrayList();

        usuariosParticipantes.add(usuarioSolicitante);
        usuariosParticipantes.add(usuarioDestinatario);

        try {

            Reunion reunionGuardada = reunionServicio.crear(
                    nombre,
                    detalle,
                    estado,
                    progreso,
                    horarioDeInicio,
                    horarioDeFin,
                    usuarioDestinatario,
                    usuarioSolicitante,
                    usuariosParticipantes,
                    proyectoAsociado);

            if (reunionGuardada != null && reunionGuardada.getId() != null) {
                redirectAttrs.addFlashAttribute("exito", "La reunión fue solicitada con éxito");
                return "redirect:/reunion/detalleSolicitud/" + reunionGuardada.getId();
            } else {
                // Manejar el caso de que reunionuardada sea nula o no tenga ID
                modelo.addAttribute("error", "La reunion no pudo ser solicitada.");

                return "solicitud_reunion.html";
            }

        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());

            return "solicitud_reunion.html";
        }
    }
}
