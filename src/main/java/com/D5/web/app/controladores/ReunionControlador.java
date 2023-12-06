package com.D5.web.app.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.qos.logback.core.model.Model;

@Controller
@RequestMapping("/reunion")
public class ReunionControlador {

    @GetMapping("/panel")
    public String panelReunion(Model model)
    {
        return "calendario_reuniones.html";
    }
    
    
}
