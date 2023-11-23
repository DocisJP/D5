package com.D5.web.app.controladores;

import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class VistaPrincipal {
    
    @Autowired
    UsuarioServicio usuarioServicio;

        @GetMapping("/")
public String index() {
return "index.html";
}

@GetMapping("/registrar")
public String registrar(){

return "form_registro.html";
}

@PostMapping("/registro")
public String registro(@RequestParam String nombre,@RequestParam String apellido, @RequestParam String email,@RequestParam String password,@RequestParam String password2, @RequestParam Long dni, @RequestParam Integer telefono)throws MatchException{

    try {
        usuarioServicio.agregarUsuario(nombre, apellido, email, password, password2, dni, telefono);
        return "index.html";
    } catch (MyException e) {
        
        return "index.html";
    }

}
}
