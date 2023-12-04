package com.D5.web.app.controladores;


import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/")
@SessionAttributes("usuario")
public class VistaPrincipal {

    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index() {

        return "index.html";
    }

    @GetMapping("/registrar")
    public String registrar() {

        return "registro.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, @RequestParam Long dni,
            @RequestParam Long telefono, @RequestParam String direccion, @RequestParam String empresa,
            MultipartFile archivo, ModelMap modelo) throws MyException {

        try {
            usuarioServicio.agregarUsuario(nombre, apellido, email, password, password2, dni, telefono, direccion,
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
    
    @PreAuthorize("hasAnyRole('Role_USER', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        return "principal.html";

    }
    
    //Agrego Perfil
//      @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
//    @GetMapping("/perfil")
//    public String perfil(ModelMap modelo , HttpSession session){
//    
//        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
//        
//        modelo.put("usuario", usuario);
//        return "usuario_modificar.html";
//    }
//    
//     @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
//    @GetMapping("/perfil/{id}")
//    public String perfil(ModelMap modelo , HttpSession session,@PathVariable String id){
//    
//        Usuario usuario = usuarioService.getOne(id);
//        modelo.addAttribute("usuario", usuario);
//        
//        return "usuario_modificar.html";
//    }
//    
//    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
//    @PostMapping("/perfil/{id}")
//    public String actualizar(MultipartFile archivo,@PathVariable String id,
//            @RequestParam String nombre,@RequestParam String email,
//            @RequestParam String password, @RequestParam String password2,ModelMap modelo){
//    
//        try {
//            usuarioService.actualizar(archivo, id, nombre, email, password, password2);
//            
//            modelo.put("exito", "Usuario actualizado correctamente");
//            
//            return "inicio.html";
//                  
//        } catch (Exception e) {
//            
//            modelo.put("error", e.getMessage());
//            modelo.put("nombre", nombre);
//            modelo.put("email", email);
//            
//            return "usuario_modificar.html";
//        }
//    
//    }
}