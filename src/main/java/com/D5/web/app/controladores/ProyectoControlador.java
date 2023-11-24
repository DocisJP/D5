/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package com.D5.web.app.controladores;

import com.D5.web.app.entidades.Agente;
import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Reunion;
import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.servicios.ProyectoServicio;
import com.D5.web.app.servicios.ReunionServicio;
import com.D5.web.app.servicios.TareaServicio;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author pc
 */
@Controller
@RequestMapping("/proyecto")
public class ProyectoControlador {

    TareaServicio tareaServicio;
    ReunionServicio reunionServicio;

    ProyectoServicio proyectoServicio;

    @GetMapping("/lista/tareas")
    public String listaTareas(ModelMap model) {

        List<Tarea> listado = tareaServicio.listarTareas();
        model.addAttribute("tareas", listado);
        return "listado_tareas";
    }

    @GetMapping("/reuniones/{proyecto}")
    public String verListaReuniones(@PathVariable Proyecto proyecto, ModelMap model) {

        List<Reunion> listado = proyectoServicio.verReuniones(proyecto);
        model.addAttribute("reuniones", listado);
        return "listado_reuniones";
    }

    @PostMapping("/regitrar")

    public String registrarProyecto(@RequestParam String nombre, @RequestParam String detalleProyecto, @RequestParam Date fechaInicio, @RequestParam Date fechaFinalizacion, @RequestParam List<Agente> equipo, @RequestParam List<Reunion> listaReuniones, @RequestParam List<Tarea> tareas, ModelMap modelo) {

        try {
            proyectoServicio.crear(nombre, detalleProyecto, fechaInicio, fechaFinalizacion, equipo, listaReuniones, tareas);
            modelo.put("exito", "La entidad se a creado con éxito");

        } catch (MyException ex) {
            modelo.addAttribute("nombre", nombre);
            modelo.addAttribute("detalleProyecto", detalleProyecto);
            modelo.addAttribute("fechaInicio", fechaInicio);
            modelo.addAttribute("fechaFinalizacion", fechaFinalizacion);
            modelo.addAttribute("equipo", equipo);
            modelo.addAttribute("listaReuniones", listaReuniones);
            modelo.addAttribute("tareas", tareas);
                    
                    modelo.put("error", ex.getMessage());
                    return "proyecto_registro";
        }

        return "redirect:/";
    }
}
