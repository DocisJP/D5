package com.D5.web.app.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/perfil")
public class UsuarioControlador {
    
    @GetMapping("/panel")
    public String panelPerfil(Model model){
        return "panel_perfil.html";
        
    }

}
