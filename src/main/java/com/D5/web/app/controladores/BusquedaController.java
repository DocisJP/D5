package com.D5.web.app.controladores;

import com.D5.web.app.entidades.Usuario;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.D5.web.app.servicios.ProyectoServicio;
import com.D5.web.app.servicios.UsuarioServicio;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BusquedaController {

    @Autowired
    private ProyectoServicio proyectoServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/sugerirNombresProyectos")
    @ResponseBody
    public List<String> sugerirNombresProyectos(@RequestParam String query) {
        return proyectoServicio.findNombresProyectosByQuery(query);
    }

    @GetMapping("/sugerirNombresEmpresas")
    @ResponseBody
    public List<String> sugerirNombresEmpresas(@RequestParam String query) {
        return usuarioServicio.findNombresEmpresasByQuery(query);
    }

    @GetMapping("/buscar")
    public String buscarEmpresasByProjectName(@RequestParam(required = false) String nombreProyecto, Model model) {
        List<String> empresas = Collections.emptyList();
        List<String> sugerenciasNombresProyectos = Collections.emptyList();

        if (nombreProyecto != null && !nombreProyecto.isEmpty()) {
            empresas = proyectoServicio.findEmpresasByProjectName(nombreProyecto);

            // Obtener sugerencias de nombres de proyectos
            sugerenciasNombresProyectos = proyectoServicio.findNombresProyectosByQuery(nombreProyecto);
            System.out.println("Sugerencias de nombres de proyectos: " + sugerenciasNombresProyectos);

            System.out.println("Termino de búsqueda: '" + nombreProyecto + "'");
            System.out.println("Empresas encontradas: " + empresas);
        } else {
            System.out.println("No hay nombre de proyecto.");
        }

        model.addAttribute("nombreProyecto", nombreProyecto);
        model.addAttribute("empresas", empresas);
        model.addAttribute("sugerenciasNombresProyectos", sugerenciasNombresProyectos);

        return "lista_empresas";
    }

    @GetMapping("/buscarUsuario")
    public String buscarUsuarioPorNombreEmpresa(@RequestParam(required = false) String nombreEmpresa, Model model) {
        List<Usuario> usuarios = Collections.emptyList();

        if (nombreEmpresa != null && !nombreEmpresa.isEmpty()) {
            usuarios = usuarioServicio.buscarUsuarioPorNombreEmpresa(nombreEmpresa);
            System.out.println("Termino de búsqueda: '" + nombreEmpresa + "'");
            System.out.println("Usuarios encontrados: " + usuarios);
        } else {
            System.out.println("No hay nombre de empresa.");
        }
        model.addAttribute("nombreEmpresa", nombreEmpresa);
        model.addAttribute("usuarios", usuarios);
        return "lista_empresas";
    }

}
