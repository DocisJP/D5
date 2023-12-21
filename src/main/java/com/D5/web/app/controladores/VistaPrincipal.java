package com.D5.web.app.controladores;

import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.enumerador.Rol;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.servicios.EmailServicio;
import com.D5.web.app.servicios.ProyectoServicio;
import com.D5.web.app.servicios.ReunionServicio;
import com.D5.web.app.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    ReunionServicio reunionServicio;

    @GetMapping("/")
    public String index() {

        return "index.html";
    }

    @GetMapping("/lista")
    public String lista(Model model, HttpSession session) {

        Usuario enSession = (Usuario) session.getAttribute("usuariosession");

        //agrego la lista para que cargue los usuarios
        List<Usuario> usuarios = usuarioServicio.listaUsuarios();
        List<Usuario> agentes = usuarios.stream()
                .filter(u -> u.getRol() == Rol.AGENTE)
                .collect(Collectors.toList());

        List<Usuario> clientes = usuarios.stream()
                .filter(u -> u.getRol() == Rol.CLIENTE)
                .collect(Collectors.toList());

        List<Usuario> administradores = usuarios.stream()
                .filter(u -> u.getRol() == Rol.ADMIN)
                .collect(Collectors.toList());
        List<Usuario> agentesVinculados = new ArrayList<>();
        List<Usuario> clientesVinculados = new ArrayList<>();
          // Obtener los proyectos del usuario en sesión
        List<Proyecto> proyectos = proyectoServicio.listarProyectosPorIdUsuario(enSession.getId());
//
//            // Obtener los agentes y clientes vinculados a los proyectos del usuario en sesión
        for (Proyecto proyecto : proyectos) {
            List<Usuario> buscarAgentes = usuarioServicio.listarUsuariosPorIdProyecto(proyecto.getId());
            List<Usuario> buscarClientes = usuarioServicio.listarUsuariosPorIdProyecto(proyecto.getId());

            Set<Usuario> agentesVinculadosSet = new HashSet<>();
            Set<Usuario> clientesVinculadosSet = new HashSet<>();

            List<Usuario> agentesDelProyecto = buscarAgentes.stream()
                    .filter(u -> u.getRol() == Rol.AGENTE)
                    .collect(Collectors.toList());
            List<Usuario> clientesDelProyecto = buscarClientes.stream()
                    .filter(u -> u.getRol() == Rol.CLIENTE)
                    .collect(Collectors.toList());

            // Agregar usuarios únicos a los conjuntos
            agentesVinculadosSet.addAll(agentesDelProyecto);
            clientesVinculadosSet.addAll(clientesDelProyecto);
            // Convertir conjuntos a listas
            agentesVinculados = new ArrayList<>(agentesVinculadosSet);
            clientesVinculados = new ArrayList<>(clientesVinculadosSet);

        }
          
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("agentes", agentes);
        model.addAttribute("clientes", clientes);
        model.addAttribute("administradores", administradores);
        model.addAttribute("agentesVinculados", agentesVinculados);
        model.addAttribute("clientesVinculados", clientesVinculados);

        return "lista_usuarios.html";

    }

    @GetMapping("/panel")
    public String perfil(Model model) {

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
            MultipartFile archivo, ModelMap modelo, HttpSession session) throws MyException {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        try {
            usuarioServicio.agregarUsuario(nombre, apellido, email, password, password2, dni, telefono, direccion, rol,
                    empresa, archivo);
            if (Rol.ADMIN.equals(logueado.getRol())) {
                return "redirect:/inicio";
            } else {
                return "index.html";
            }
        } catch (MyException e) {

            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "registro.html";
        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {

            modelo.put("error", "Usuario o Contraseña inválidos");
        }

        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        //agrego metodo para dar aviso al loguear el admin
        int contadorUsuariosInactivos = usuarioServicio.Inactivos();
        int contadorProyectosPendientes = proyectoServicio.Inactivos();
        int contadorReunionesPendientes = reunionServicio.Inactivos();

        if (contadorUsuariosInactivos > 0) {

            modelo.put("avisoUsuario", "Hay usuarios sin registrar");
        }
        if (contadorProyectosPendientes > 0) {

            modelo.put("avisoProyecto", "Hay proyectos para revisar");
        }
        if (contadorReunionesPendientes > 0) {

            modelo.put("avisoReunion", "Hay reuniones para agendar");
        }

        modelo.put("usuario", logueado);

        return "principal.html";
    }

    @GetMapping("/solicitar")
    public String solicitarRegistro(Model model) {
        model.addAttribute("roles", Rol.values());
        return "solicitud_registro.html";

    }

    @PostMapping("/contactar")
    public String contactar(@RequestParam String contactoEmail, @RequestParam String contactoMensaje) {

        emailServicio.enviarCorreo(contactoEmail, contactoMensaje);
        return "index";

    }
}
