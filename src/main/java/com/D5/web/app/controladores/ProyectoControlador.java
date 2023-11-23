/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package com.D5.web.app.controladores;

import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.servicios.TareaServicio;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author pc
 */
@Controller
@RequestMapping("/proyecto")
public class ProyectoControlador {

    TareaServicio tareaServicio;

    @GetMapping("/lista/tareas")
    public String listaTareas(ModelMap model) {

        List<Tarea> listado = tareaServicio.listarTareas();
        model.addAttribute("tareas", listado);
        return "listado_tareas";
    }

}
