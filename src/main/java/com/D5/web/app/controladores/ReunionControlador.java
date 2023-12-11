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
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.servicios.ProyectoServicio;
import com.D5.web.app.servicios.ReunionServicio;
import com.D5.web.app.servicios.UsuarioServicio;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;

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
//    @GetMapping("/registro")
//    public String mostrarFormularioRegistro(Model model) {
//        model.addAttribute("reunion", new Reunion());
//        model.addAttribute("usuarios", usuarioServicio.listarUsuarios());
//        return "formulario_reunion.html";
//    }
//
//    @PostMapping("/registro")
//    public String registrarReunion(@ModelAttribute("reunion") Reunion reunion,
//            @RequestParam String[] participantes,
//            RedirectAttributes redirectAttributes) {
//        System.out.println("Participantes seleccionados: " + Arrays.toString(participantes));
//
//        try {
//            // Convertir los IDs de los usuarios en objetos Usuario
//            List<Usuario> usuariosParticipantes = Arrays.stream(participantes)
//                    .map(id -> usuarioServicio.buscarUsuario(id))
//                    .collect(Collectors.toList());
//
//            reunion.setParticipantes(usuariosParticipantes);
//            reunionServicio.crear(reunion.getNombre(),
//                    reunion.getHorarioDeInicio(),
//                    reunion.getEstado(),
//                    usuariosParticipantes,
//                    reunion.getDetalle());
//
//            redirectAttributes.addFlashAttribute("exito", "Reunión creada con éxito");
//            return "redirect:/reunion/calendario_reuniones";
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", "Error al crear la reunión: " + e.getMessage());
//            return "redirect:/reunion/registro";
//        }
//    }
///Probando plan b
    // Muestra el formulario para agregar una nueva tarea

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
            ModelMap modelo
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
                modelo.addAttribute("exito", "La reunion pudo ser creada.");
                return "redirect:/reunion/detalle/" + reunionGuardada.getId();
            } else {
                // Manejar el caso de que tareaGuardada sea nula o no tenga ID
                modelo.addAttribute("error", "La reunion no pudo ser creada.");

                return "formulario_reunion.html";
            }
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
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

    //Agregue post y get para las vistas falta la logica
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

    @GetMapping("/eliminar/{id}")
    public String eliminarReunion(@PathVariable String id) {
        reunionServicio.eliminar(reunionServicio.buscarPorId(id));
        return "redirect:/reunion/panel";
    }
}
