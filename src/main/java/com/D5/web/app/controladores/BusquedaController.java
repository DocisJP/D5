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

@Controller
public class BusquedaController {

    @Autowired
    private ProyectoServicio proyectoServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/buscar")
    public String buscarEmpresasByProjectName(@RequestParam(required = false) String nombreProyecto, Model model) {
        List<String> empresas = Collections.emptyList();
        List<String> proyectosEmpresa = Collections.emptyList();
        if (nombreProyecto != null && !nombreProyecto.isEmpty()) {
            empresas = proyectoServicio.findEmpresasByProjectName(nombreProyecto);
            String busqueda = nombreProyecto;
            proyectosEmpresa = proyectoServicio.buscaProyectos(busqueda);
        } else {
            System.out.println("No hay nombre de proyecto.");
        }
        model.addAttribute("nombreProyecto", nombreProyecto);
        model.addAttribute("empresas", empresas);
        model.addAttribute("proyectosEmpresa", proyectosEmpresa);
        return "lista_empresas";
    }

    @GetMapping("/buscarUsuario")
    public String buscarUsuarioPorNombreEmpresa(@RequestParam(required = false) String nombreEmpresa, Model model) {
        List<Usuario> usuarios = Collections.emptyList();
        if (nombreEmpresa != null && !nombreEmpresa.isEmpty()) {
            usuarios = usuarioServicio.buscarUsuarioPorNombreEmpresa(nombreEmpresa);
            System.out.println("Termino de busqueda: '" + nombreEmpresa + "'");
            System.out.println("Usuarios encontrados: " + usuarios);
        } else {
            System.out.println("No hay nombre de usuario.");
        }
        model.addAttribute("nombreEmpresa", nombreEmpresa);
        model.addAttribute("usuarios", usuarios);
        return "lista";
    }

}
