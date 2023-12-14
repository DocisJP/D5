package com.D5.web.app.controladores;

import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.enumerador.Rol;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.servicios.EmailServicio;
import com.D5.web.app.servicios.ProyectoServicio;
import com.D5.web.app.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/")
@SessionAttributes("usuario")
public class VistaPrincipal {

    @Autowired
    UsuarioServicio usuarioServicio;
    
    @Autowired
    EmailServicio emailServicio;

    @Autowired
    ProyectoServicio proyectoServicio;
    
    @GetMapping("/")
    public String index() {

        return "index.html";
    }

       @GetMapping("/lista")
    public String lista(Model model){
        //agrego la lista para que cargue los usuarios
         List<Usuario> usuarios = usuarioServicio.listaUsuarios();
         
        model.addAttribute("usuarios", usuarios);

        return "lista_usuarios.html";
        
    }
    
         @GetMapping("/panel")
    public String perfil(Model model){

        return "panel_perfil.html";
        
    }
    
    @GetMapping("/registrar")
    public String registrar(ModelMap model) {

         model.addAttribute("roles", Rol.values());
        return "registro.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, 
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

        try {
            usuarioServicio.agregarUsuario(nombre, apellido, email, password, password2, dni, telefono, direccion,rol,
                    empresa, archivo);
            return "index.html";
        } catch (MyException e) {

            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "registro.html";
        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required=false) String error,ModelMap modelo){
    
        if(error != null){
        
            modelo.put("error", "Usuario o Contraseña inválidos");
        }
        
        return "login.html";
    }
    
       @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        //agrego metodo para dar aviso al loguear el admin
        int contador = usuarioServicio.Inactivos();
        int contador2 = proyectoServicio.Inactivos();
        if (contador > 0) {
            modelo.put("Aviso", "Usuarios: " + contador);
          
        }
       if (contador2 > 0) {
            modelo.put("Registro", "Proyectos: " + contador2);
          
        }
        return "principal.html";
    }
  

    
         @GetMapping("/solicitar")
    public String solicitarRegistro(Model model){
        model.addAttribute("roles", Rol.values());
        return "solicitud_registro.html";
        
    }

    @PostMapping("/contactar")
    public String contactar(@RequestParam String contactoEmail,@RequestParam String contactoMensaje) {
        
        emailServicio.enviarCorreo(contactoEmail,contactoMensaje); 
        return "index";

    }
}
