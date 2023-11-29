package com.D5.web.app.controladores;


import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Reunion;
import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.servicios.ProyectoServicio;
import com.D5.web.app.servicios.ReunionServicio;
import com.D5.web.app.servicios.TareaServicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/lista/tareas")
    public String listaTareas(ModelMap model) {

        List<Tarea> listado = tareaServicio.listarTareas();
        model.addAttribute("tareas", listado);
        return "listado_tareas";
    }

    
    @GetMapping("/reuniones/{id}")
    public String verListaReuniones(@PathVariable String id, ModelMap model) {
        Proyecto proyecto = proyectoServicio.buscarPorId(id);
        List<Reunion> listado = proyecto.getListaReuniones();
        model.addAttribute("reuniones", listado);
        return "listado_reuniones";
    }

    
    @PostMapping("/registrar")
    public String registrarProyecto(@ModelAttribute Proyecto proyecto, RedirectAttributes redirectAttrs) {
        try {
            proyectoServicio.crear(proyecto);
            redirectAttrs.addFlashAttribute("exito", "El proyecto fue creado con éxito");
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
            return "redirect:/proyecto_registro";
        }

        return "redirect:/";
    }
}
