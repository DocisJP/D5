package com.D5.web.app.controladores;


import com.D5.web.app.entidades.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
public class UsuarioControlador {
    
    @GetMapping("/panel")
    public String panelPerfil(Model model){
        return "panel_perfil.html";
        
    }

    //Agrego get y post para la muestra del html falta la logica
    @GetMapping("/modificar/{id}")
    public String modificarUsuario(@PathVariable String id, Model model) {
    return "usuario_modificar.html";
    }
      
  @PostMapping("/modificar")
    public String modificarProyecto(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttrs) {
        
         return "redirect:/lista" + usuario.getId();
    }
}
