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
import org.springframework.web.bind.annotation.RequestParam;
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
    public String modificarPerfil(@RequestParam String id,@RequestParam String nombre, 
            @RequestParam String apellido,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2,
            @RequestParam Long dni,
            @RequestParam Long telefono,
            @RequestParam String direccion, 
            @RequestParam(required = false) Rol rol,
            @RequestParam String empresa,          
            MultipartFile archivo, ModelMap modelo) throws MyException {
        
        if(password==password2){
            try {
                
                Usuario usuario = new Usuario();
                usuario.setId(id);
                usuario.setNombre(nombre);
                usuario.setApellido(apellido);
                usuario.setEmail(email);
                usuario.setPassword(password);
                usuario.setDni(dni);
                usuario.setTelefono(telefono);
                usuario.setDireccion(direccion);
                usuario.setRol(rol);
                usuario.setEmpresa(empresa);
                Imagen imagen = imagenServicio.guardar(archivo);
                usuario.setImagen(imagen);
                
                usuarioServicio.modificar(usuario);
                
                modelo.addAttribute("exito", "Usuario actualizado con éxito");
                
                return "redirect:/perfil/panel/" + id; 
            } catch (MyException m) {
                
                modelo.addAttribute("error", m.getMessage());
            }       
        }
        return "redirect:/";
    }
}