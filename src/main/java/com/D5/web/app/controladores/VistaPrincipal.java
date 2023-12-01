package com.D5.web.app.controladores;


import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	public String registrar() {

		return "registro.html";
	}

	@PostMapping("/registro")
	public String registro(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String email,
			@RequestParam String password, @RequestParam String password2, @RequestParam Long dni,
			@RequestParam Long telefono, @RequestParam String direccion, @RequestParam String empresa,
			MultipartFile imagen) throws MyException {

		try {
			usuarioServicio.agregarUsuario(nombre, apellido, email, password, password2, dni, telefono, direccion,
					empresa, (MultipartFile) imagen);
			return "index.html";
		} catch (MyException e) {

			return "index.html";
		}

	}
	
	@PreAuthorize("hasAnyRole('Role_USER', 'ROLE_ADMIN')")
	@GetMapping("/inicio")
	public String inicio(HttpSession session){

	    Usuario logueado = (Usuario) session.getAttribute("usuariosession");
	    
	    if (logueado.getRol().toString().equals("ADMIN")) {
	        return "redirect:/admin/dashboard";
	    }
	    
	    return "principal.html";
	
}
}
