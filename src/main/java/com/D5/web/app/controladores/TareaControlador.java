package com.D5.web.app.controladores;

import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.servicios.TareaServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/tarea")
public class TareaControlador {


    @Autowired
    private TareaServicio tareaServicio;

     @GetMapping("/panel")
    public String panel() {
        
        return "panel_pendientes.html";
    }
    
    // Muestra el formulario para agregar una nueva tarea
    @GetMapping("/registrar")
    public String formularioRegistrar(Model model) {
        model.addAttribute("tarea", new Tarea());
        return "formulario_tarea";
    }

    // Procesa el formulario y registra una nueva tarea
    @PostMapping("/registro")
    public String registrarTarea(@Valid @ModelAttribute("tarea") Tarea tarea, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            return "formulario_tarea";
        }

        try {
            Tarea tareaGuardada = tareaServicio.crear(
                    tarea.getNombreTarea(), 
                    tarea.getDescripcion(), 
                    tarea.getEstado(), 
                    tarea.getFechaInicio(), 
                    tarea.getFechaFinalizacion());

            if (tareaGuardada != null && tareaGuardada.getId() != null) {
                redirectAttrs.addFlashAttribute("exito", "Tarea creada con éxito!");
                return "redirect:/tarea/detalle/" + tareaGuardada.getId();
            } else {
                // Manejar el caso de que tareaGuardada sea nula o no tenga ID
                redirectAttrs.addFlashAttribute("error", "La tarea no pudo ser creada.");
                return "formulario_tarea";
            }
        } catch (MyException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
            return "formulario_tarea";
        }
    }

    
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable String id, Model model) throws MyException {
        Tarea tarea = tareaServicio.buscarPorId(id);
        if (tarea == null) {
        	return "redirect:/index";
        }
        else {
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

}
