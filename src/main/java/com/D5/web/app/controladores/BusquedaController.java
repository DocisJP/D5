package com.D5.web.app.controladores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.D5.web.app.servicios.ProyectoServicio;

@Controller
public class BusquedaController {

	@Autowired
    private ProyectoServicio proyectoServicio;

	@GetMapping("/buscar")
	public String buscarEmpresasByProjectName(@RequestParam(required = false) String nombreProyecto, Model model) {
	    List<String> empresas = Collections.emptyList();
	    if (nombreProyecto != null && !nombreProyecto.isEmpty()) {
	        empresas = proyectoServicio.findEmpresasByProjectName(nombreProyecto);
	        System.out.println("Termino de busqueda: '" + nombreProyecto + "'");
	        System.out.println("Empresas encontradas: " + empresas);
	    } else {
	        System.out.println("No hay nombre de proyecto.");
	    }
	    model.addAttribute("nombreProyecto", nombreProyecto);
	    model.addAttribute("empresas", empresas);
	    return "lista_empresas";
	}



}
