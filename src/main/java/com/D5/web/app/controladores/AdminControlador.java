/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.D5.web.app.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author leandro
 */
@Controller
@RequestMapping("/admin")
public class AdminControlador {
    
       
    @GetMapping("/dashboard")
    public String panelAdministrativo(){
        
        return "panel.html";
    
    }
    
}
