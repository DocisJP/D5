package com.D5.web.app.controladores;

import com.D5.web.app.entidades.Imagen;
import com.D5.web.app.entidades.Proyecto;

import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.enumerador.Rol;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.servicios.ImagenServicio;
import com.D5.web.app.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
public class UsuarioControlador {

    @Autowired
    UsuarioServicio usuarioServicio;
    
    @Autowired
    ImagenServicio imagenServicio;

    @GetMapping("/panel/{id}")
    public String panelPerfil(@PathVariable String id, Model model) {
        Usuario usuario = usuarioServicio.buscarUsuario(id);
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Rol.values());
        return "panel_perfil.html";

    }

    @PostMapping("/modificar")
    public String modificarPerfil(@ModelAttribute("usuario") Usuario usuario, MultipartFile archivo, RedirectAttributes redirectAttrs) {

 
        try {
            
            if (archivo != null &&  !archivo.isEmpty()) {
                Imagen imagen = imagenServicio.guardar(archivo);
                usuario.setImagen(imagen); 
            }  
            usuarioServicio.modificar(usuario, archivo);

            redirectAttrs.addFlashAttribute("exito", "Usuario actualizado con éxito");
            return "redirect:/perfil/panel/" + usuario.getId();
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/perfil/panel/" + usuario.getId();
    }
}
