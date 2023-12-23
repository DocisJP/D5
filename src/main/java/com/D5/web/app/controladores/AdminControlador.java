package com.D5.web.app.controladores;

import com.D5.web.app.servicios.ProyectoServicio;
import com.D5.web.app.servicios.ReunionServicio;
import com.D5.web.app.servicios.TareaServicio;
import com.D5.web.app.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    ProyectoServicio ProyectoServicio;

    @Autowired
    UsuarioServicio UsuarioServicio;

    @Autowired
    ReunionServicio reunionServicio;

    @Autowired
    TareaServicio tareaServicio;

    @GetMapping("/dashboard")
    public String panelAdministrativo(ModelMap modelo) {

        
        
        return "dashboard.html";

    }

}
