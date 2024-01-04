package com.D5.web.app.controladores;

import com.D5.web.app.entidades.Nota;
import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Reunion;
import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.enumerador.Progreso;
import com.D5.web.app.enumerador.Rol;
import com.D5.web.app.repositorios.ProyectoRepositorio;
import com.D5.web.app.servicios.EmailServicio;
import com.D5.web.app.servicios.NotaServicio;
import com.D5.web.app.servicios.ProyectoServicio;
import com.D5.web.app.servicios.ReunionServicio;
import com.D5.web.app.servicios.TareaServicio;
import com.D5.web.app.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/proyecto")
public class ProyectoControlador {

    @Autowired
    TareaServicio tareaServicio;

    @Autowired
    NotaServicio notaServicio;

    @Autowired
    ReunionServicio reunionServicio;

    @Autowired
    ProyectoServicio proyectoServicio;

    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    private EmailServicio emailServicio;

    @GetMapping("/panel")
    public String panelControl(ModelMap model) {

        List<Proyecto> proyectos = proyectoServicio.listarProyectos();
        
        for (Proyecto proyecto : proyectos) {
            if(proyecto.getFechaFinalizacion().before(new Date())){
            proyecto.setProgreso(Progreso.FINALIZADO);
            }else{
            proyecto.setProgreso(Progreso.PENDIENTE);
            }
        }
        //Ordeno los proyectos por fecha inicio
        Collections.sort(proyectos);
        model.addAttribute("proyectos", proyectos);
        return "panel_proyecto.html";
    }

    @GetMapping("/lista/tareas")
    public String listaTareas(ModelMap model) {

        List<Tarea> listado = tareaServicio.listarTareas();
        model.addAttribute("tareas", listado);
        return "listado_tareas";
    }

    @GetMapping("/listaProyectos/{id}")

    public String listaProyectos(@PathVariable String id, ModelMap model, RedirectAttributes redirectAttrs) {
        List<Proyecto> proyectos = proyectoServicio.listarProyectosPorIdUsuario(id);

        for (Proyecto proyect : proyectos) {
            if (proyect.getFechaFinalizacion().before(new Date())) {
                proyect.setProgreso(Progreso.FINALIZADO);
            } else {
                proyect.setProgreso(Progreso.PENDIENTE);
            }
        }

        //Ordeno las reuniones por fecha inicio
        Collections.sort(proyectos);
        model.addAttribute("proyectos", proyectos);

        redirectAttrs.addFlashAttribute("exitoMensaje", "Proyectos cargados correctamente.");

        return "panel_proyecto.html";
    }

//
//    @GetMapping("/lista/tareas")
//    public String listaTareas(ModelMap model) {
//
//        List<Tarea> listado = tareaServicio.listarTareas();
//        model.addAttribute("tareas", listado);
//        return "listado_tareas";
//    }
    @GetMapping("/reuniones/{id}")
    public String verListaReuniones(@PathVariable String id, ModelMap model) {
        Proyecto proyecto = proyectoServicio.buscarPorId(id);
        List<Reunion> listado = proyecto.getListaReuniones();
        model.addAttribute("reuniones", listado);
        return "listado_reuniones";
    }

    @GetMapping("/contactar/{id}")
    public String contactar(@PathVariable String id, ModelMap model) {
        Proyecto proyecto = proyectoServicio.buscarPorId(id);
        List<Usuario> agentes = proyectoServicio.getAgentes(proyecto);
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("agentes", agentes);
        return "panel_contacto_agente";
    }

    @PostMapping("/contactar")
    public String contactarAgente(HttpSession session, @ModelAttribute Proyecto proyecto, String asunto, String mensaje, String idAgente, RedirectAttributes redirectAttrs, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        emailServicio.enviarCorreo(proyecto, idAgente, logueado, asunto, mensaje);
        System.out.println("Correo enviado correctamente");
        redirectAttrs.addFlashAttribute("exitoMensaje", "El correo se envió correctamente.");

        return "redirect:/proyecto/contactar/" + proyecto.getId();

    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        Proyecto nuevoProyecto = new Proyecto();
        nuevoProyecto.setEstado(Boolean.FALSE);
        nuevoProyecto.setProgreso(Progreso.PENDIENTE);

        model.addAttribute("proyecto", nuevoProyecto);
        model.addAttribute("progresos", Progreso.values());
        return "formulario_proyecto.html";
    }

    @PostMapping("/registro")
    public String registrarProyecto(@ModelAttribute Proyecto proyecto, HttpSession session, RedirectAttributes redirectAttrs) {
        try {

            Usuario usuarioEnSession = (Usuario) session.getAttribute("usuariosession");

            // Si el usuario en sesión no es un "ADMIN", agregarlo como participante
            if (!Rol.ADMIN.equals(usuarioEnSession.getRol())) {
                List<Usuario> usuarios = new ArrayList<>();
                usuarios.add(usuarioEnSession);
                proyecto.setUsuarios(usuarios);
            }

            // Guardar el proyecto
            Proyecto proyectoGuardado = proyectoServicio.crear(proyecto);

            redirectAttrs.addFlashAttribute("exito", "El proyecto fue guardado con éxito");
            return "redirect:/proyecto/detalle/" + proyectoGuardado.getId();
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
            return "redirect:/proyecto/registro";
        }
    }

    @GetMapping("/participantes/{id}")
    public String listadosUsuariosProyecto(@PathVariable String id, ModelMap model) {
        Proyecto proyecto = proyectoServicio.buscarPorId(id);

        List<Usuario> listaActual = proyecto.getUsuarios();
        List<Usuario> participantes = usuarioServicio.listaUsuarios();
        model.addAttribute("listaActual", listaActual);
        model.addAttribute("participantes", participantes);
        model.addAttribute("proyecto", proyecto);
        return "agregar_participantes";
    }

    @PostMapping("/participantes")
    public String agregaUsuarioAproyecto(@RequestParam String proyectoId, @RequestParam String usuarioId, ModelMap modelo) {

        Usuario usuario = usuarioServicio.getOne(usuarioId);
        Proyecto proyecto = proyectoServicio.buscarPorId(proyectoId);

        try {
            List<Usuario> listado = proyecto.getUsuarios();

            if (!listado.contains(usuario)) {
                listado.add(usuario);
                proyecto.setUsuarios(listado);

                // Resto del código...  
            } else {
                proyecto.setUsuarios(proyecto.getUsuarios());
            }

            modelo.addAttribute("proyecto", proyecto);
            // Actualizar la lista de participantes después de agregar el usuario
            List<Usuario> listaActual = proyecto.getUsuarios();
            modelo.addAttribute("listaActual", listaActual);

            proyectoServicio.modificar(proyecto);

            modelo.addAttribute("exito", "Usuario agregado");

        } catch (Exception e) {
            List<Usuario> listaActual = proyecto.getUsuarios();
            List<Usuario> participantes = usuarioServicio.listaUsuarios();
            modelo.addAttribute("listaActual", listaActual);
            modelo.addAttribute("participantes", participantes);
            modelo.addAttribute("proyecto", proyecto);
            return "agregar_participantes";
        }
        return "redirect:/proyecto/participantes/" + proyecto.getId();
    }

    @PostMapping("/eliminar-participante")
    public String eliminarParticipante(@RequestParam String proyectoId, @RequestParam String usuarioId) {
        // Lógica para eliminar al usuario de la lista de participantes en el proyecto
        Proyecto proyecto = proyectoServicio.buscarPorId(proyectoId);
        Usuario usuario = usuarioServicio.getOne(usuarioId);

        List<Usuario> listaActual = proyecto.getUsuarios();
        listaActual.remove(usuario);

        proyecto.setUsuarios(listaActual);
        proyectoServicio.modificar(proyecto);

        // Redirige a la página de participantes actualizada
        return "redirect:/proyecto/participantes/" + proyecto.getId();
    }

    @GetMapping("/lista")
    public String listarProyectos(ModelMap model) {
        List<Proyecto> listado = proyectoServicio.listarProyectos();
        model.addAttribute("proyectos", listado);
        return "panel_proyecto";

    }

    @GetMapping("/detalle/{id}")
    public String mostrarDetalles(@PathVariable String id, Model model) {
        Proyecto proyecto = proyectoServicio.buscarPorId(id);
        if (proyecto == null) {
            return "redirect:/proyecto/lista";
        }
        model.addAttribute("proyecto", proyecto);

        return "detalle_proyecto";

    }

    @GetMapping("/modificar/{id}")
    public String mostrarFormularioModificar(@PathVariable String id, Model model) {
        Proyecto proyecto = proyectoServicio.buscarPorId(id);
        model.addAttribute("progresos", Progreso.values());
        if (proyecto == null) {
            // Manejar el caso en que el proyecto no se encuentre
            return "redirect:/proyecto/lista";
        }
        model.addAttribute("proyecto", proyecto);
        return "proyecto_modificar.html";
    }

    @PostMapping("/modificar")
    public String modificarProyecto(@ModelAttribute Proyecto proyecto, RedirectAttributes redirectAttrs) {
        try {
            proyectoServicio.modificar(proyecto);

            redirectAttrs.addFlashAttribute("exito", "Proyecto actualizado con éxito");
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/proyecto/detalle/" + proyecto.getId();
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProyecto(@PathVariable String id, RedirectAttributes redirectAttrs) {
        try {

            proyectoServicio.eliminarPorId(id);

            redirectAttrs.addFlashAttribute("exito", "Proyecto eliminado con éxito");
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/proyecto/lista";
    }


    /*Mock para probar una cosa...*/
    @GetMapping("/trabajo")
    public String trabajosDeProyecto(ModelMap modelo, HttpSession session) {
        List<Proyecto> proyectosTodos = proyectoServicio.listarProyectos();
        Usuario usuarioEnSession = (Usuario) session.getAttribute("usuariosession");

        List<Proyecto> proyectos = proyectoServicio.listarProyectosPorIdUsuario(usuarioEnSession.getId());

        modelo.addAttribute("proyectos", proyectos);
        return "panel_trabajo_agente.html";
    }

    @PostMapping("/buscar")
    public String trabajo(@RequestParam String proyectoId) {

        return "redirect:/proyecto/buscar/" + proyectoId;
    }

    /*Mock para probar una cosa...*/
    @GetMapping("/buscar/{id}")
    public String trabajosDeProyecto(ModelMap modelo, @PathVariable(required = false) String id, HttpSession session) {
        List<Proyecto> proyectosTodos = proyectoServicio.listarProyectos();
        Usuario usuarioEnSession = (Usuario) session.getAttribute("usuariosession");

        List<Proyecto> proyectos = proyectoServicio.listarProyectosPorIdUsuario(usuarioEnSession.getId());

        Proyecto proyectoElegido;
        if (id == null) {
            proyectoElegido = new Proyecto();
        } else {
            proyectoElegido = proyectoServicio.buscarPorId(id);
        }

        List<Nota> notas = notaServicio.listarNotasPorIdProyecto(id);
        List<Reunion> reuniones = reunionServicio.obtenerReunionesPorProyecto(id);
        List<Tarea> tareas = tareaServicio.obtenerTareasPorProyecto(id);

        // Ordenar las listas por fecha de inicio
        Collections.sort(notas);
        Collections.sort(reuniones);
        Collections.sort(tareas);

        modelo.addAttribute("reuniones", reuniones);
        modelo.addAttribute("notas", notas);
        modelo.addAttribute("proyectos", proyectos);
        modelo.addAttribute("proyectoElegido", proyectoElegido);
        modelo.addAttribute("tareas", tareas);

        return "panel_trabajo_agente.html";
    }

}
