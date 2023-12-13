package com.D5.web.app.controladores;

import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Reunion;
import com.D5.web.app.entidades.Tarea; 
import com.D5.web.app.entidades.Usuario; 
import com.D5.web.app.servicios.ProyectoServicio;
import com.D5.web.app.servicios.ReunionServicio;
import com.D5.web.app.servicios.TareaServicio;
import com.D5.web.app.servicios.UsuarioServicio; 
import jakarta.servlet.http.HttpSession;
 

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
    ReunionServicio reunionServicio;

    @Autowired
    ProyectoServicio proyectoServicio;

    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/panel")
    public String panelControl(ModelMap model) {
        List<Proyecto> listado = proyectoServicio.listarProyectos();
        model.addAttribute("proyectos", listado);
        return "panel_proyecto.html";
    }
 
 
    @GetMapping("/listaProyectos/{id}")
    public String listaProyectos(@PathVariable String id,ModelMap model) {
        List<Proyecto> listado = proyectoServicio.listarProyectosPorIdUsuario(id);
        model.addAttribute("proyectos", listado);
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
    public String contactarAgente(HttpSession session,@ModelAttribute Proyecto proyecto,String titulo,String detalle,String idAgente, RedirectAttributes redirectAttrs) {
        System.out.println(titulo);
        System.out.println(detalle);
        System.out.println(idAgente);
        System.out.println(proyecto.getId());
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
         
            return "redirect:/proyecto/listaProyectos/" + logueado.getId();
         
    }
 

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        return "formulario_proyecto.html";
    }

    @PostMapping("/registro")
    public String registrarProyecto(@ModelAttribute Proyecto proyecto, RedirectAttributes redirectAttrs) {
        System.out.print(redirectAttrs);
        System.out.print(proyecto);
        try {
            Proyecto proyectoGuardado = proyectoServicio.crear(proyecto);
            redirectAttrs.addFlashAttribute("exito", "El proyecto fue creado con éxito");
            return "redirect:/proyecto/detalle/" + proyectoGuardado.getId();
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
            return "redirect:/proyecto/registro";
        }
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
    public String trabajosDeProyecto(ModelMap modelo) {
           List<Proyecto> proyectos = proyectoServicio.listarProyectos();
           modelo.addAttribute("proyectos", proyectos);
        return "panel_trabajo_agente.html";
    }
    /*Mock para probar una cosa...*/
    @GetMapping("/buscar/{id}")
    public String trabajosDeProyecto(ModelMap modelo, @PathVariable(required=false) String id) {
        List<Proyecto> proyectos = proyectoServicio.listarProyectos();
        Proyecto proyectoElegido;
        if (id == null) {
           proyectoElegido = new Proyecto();
        } else {
           proyectoElegido = proyectoServicio.buscarPorId(id);
        }
        
        List<Reunion> reuniones = reunionServicio.obtenerReunionesPorProyecto(id);
        List<Tarea> tareas = tareaServicio.obtenerTareasPorProyecto(id);

        modelo.addAttribute("reuniones", reuniones);
        modelo.addAttribute("proyectos", proyectos);
        modelo.addAttribute("proyectoElegido", proyectoElegido);
        modelo.addAttribute("tareas", tareas);
        return "panel_trabajo_agente.html";
    }

    @PostMapping("/buscar")
    public String trabajo(@RequestParam String proyectoId){
    
        return "redirect:/proyecto/buscar/" + proyectoId;
    }
}
