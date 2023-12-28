package com.D5.web.app.controladores;

import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.servicios.ProyectoServicio;
import com.D5.web.app.servicios.ReunionServicio;
import com.D5.web.app.servicios.TareaServicio;
import com.D5.web.app.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    ProyectoServicio proyectoServicio;

    @Autowired
    UsuarioServicio UsuarioServicio;

    @Autowired
    ReunionServicio reunionServicio;

    @Autowired
    TareaServicio tareaServicio;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public String panelAdministrativo(ModelMap modelo) {

        return "dashboard.html";

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/buscar")
    public String busquedaAdmin(@RequestParam String busqueda, ModelMap modelo) {
        List<String> listaProyectos = proyectoServicio.findEmpresasByProjectName(busqueda);
        List<String> empresas = proyectoServicio.buscaProyectos(busqueda);
        List<Usuario> listaUsuarios = UsuarioServicio.buscarUsuarioPorNombreEmpresa(busqueda);

        modelo.addAttribute("listaProyectos", listaProyectos);
        modelo.addAttribute("empresas", empresas);
        modelo.addAttribute("listaUsuarios", listaUsuarios);

        return "dashboard.html";

    }

}
