package com.D5.web.app.controladores;

import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Reunion;
import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.enumerador.Progreso;
import com.D5.web.app.enumerador.Rol;
import com.D5.web.app.servicios.ProyectoServicio;
import com.D5.web.app.servicios.ReunionServicio;
import com.D5.web.app.servicios.TareaServicio;
import com.D5.web.app.servicios.UsuarioServicio;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin(origins = "http://localhost:8080")
@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    ProyectoServicio proyectoServicio;

    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    ReunionServicio reunionServicio;

    @Autowired
    TareaServicio tareaServicio;

//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/dashboard")
//    public String panelAdministrativo(ModelMap modelo) {
//
//        return "dashboard.html";
//
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/buscar")
//    public String busquedaAdmin(@RequestParam String busqueda, ModelMap modelo) {
//        List<String> listaProyectos = proyectoServicio.findEmpresasByProjectName(busqueda);
//        List<String> empresas = proyectoServicio.findNombresProyectosByQuery(busqueda);
//        List<Usuario> listaUsuarios = usuarioServicio.buscarUsuarioPorNombreEmpresa(busqueda);
//
//        modelo.addAttribute("listaProyectos", listaProyectos);
//        modelo.addAttribute("empresas", empresas);
//        modelo.addAttribute("listaUsuarios", listaUsuarios);
//
//        return "dashboard.html";
//
//   }
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {

        // Obtener métricas clave
        List<Proyecto> listadoP = proyectoServicio.listarProyectos();
        List<Usuario> listadoU = usuarioServicio.listarUsuarios();

        long totalProyectos = listadoP.size();
        long totalClientes = listadoU.stream().filter(u -> Rol.CLIENTE.equals(u.getRol())).count();
        long totalAgentes = listadoU.stream().filter(u -> Rol.AGENTE.equals(u.getRol())).count();
        long clientesActivos = listadoU.stream().filter(Usuario::getEstado).count();
        long proyectosRecientes = listadoP.stream().filter(proyectoServicio::esProyectoReciente).count();
        long inProgressProjects = listadoP.stream().filter(p -> (Progreso.PENDIENTE).equals(p.getProgreso())).count();
        long proyectosFinalizados = listadoP.stream().filter(p -> (Progreso.FINALIZADO).equals(p.getProgreso())).count();

        model.addAttribute("totalProjects", totalProyectos);
        model.addAttribute("totalClients", totalClientes);
        model.addAttribute("totalAgents", totalAgentes);
        model.addAttribute("activeClients", clientesActivos);
        model.addAttribute("recentProjects", proyectosRecientes);
        model.addAttribute("inProgressProjects", inProgressProjects);
        model.addAttribute("proyectosFinalizados", proyectosFinalizados);
        model.addAttribute("listaUsuarios", listadoU);
        model.addAttribute("cuantosAgentes", usuarioServicio.buscarPorRol(Rol.AGENTE).stream().count());

        return "nuevo_dashboard";
    }

    @PostMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> adminDashboardPoste(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");

        Usuario usuario = new Usuario();
        long proyectosTotalesDelUsu = 0;
        long proyectosEnProgresoDelUsu = 0;
        long proyectosFinalizadosDelUsu = 0;
        long reunionesTotales = 0;
        long reunionesEnProgreso = 0;
        long reunionesFinalizados = 0;
        long tareasTotales = 0;
        long tareasEnProgreso = 0;
        long tareasFinalizados = 0;

        if (userId != null) {
            usuario = usuarioServicio.buscarUsuario(userId);

            List<Proyecto> proyectos = proyectoServicio.listarProyectosPorIdUsuario(userId);
            List<Reunion> reuniones = reunionServicio.listarReunionesPorIdUsuario(userId);
            List<Tarea> tareas = tareaServicio.listarTareasPorIdUsuario(userId);

            proyectosTotalesDelUsu = proyectos.size();
            proyectosEnProgresoDelUsu = proyectos.stream().filter(p -> Progreso.PENDIENTE.equals(p.getProgreso())).count();
            proyectosFinalizadosDelUsu = proyectos.stream().filter(p -> Progreso.FINALIZADO.equals(p.getProgreso())).count();

            reunionesTotales = reuniones.size();
            reunionesEnProgreso = reuniones.stream().filter(r -> Progreso.PENDIENTE.equals(r.getProgreso())).count();
            reunionesFinalizados = reuniones.stream().filter(r -> Progreso.FINALIZADO.equals(r.getProgreso())).count();

            tareasTotales = tareas.size();
            tareasEnProgreso = tareas.stream().filter(t -> Progreso.PENDIENTE.equals(t.getProgreso())).count();
            tareasFinalizados = tareas.stream().filter(t -> Progreso.FINALIZADO.equals(t.getProgreso())).count();
        }

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "proyectosTotales", proyectosTotalesDelUsu,
                "proyectosEnProgreso", proyectosEnProgresoDelUsu,
                "proyectosFinalizados", proyectosFinalizadosDelUsu,
                "reunionesTotales", reunionesTotales,
                "reunionesEnProgreso", reunionesEnProgreso,
                "reunionesFinalizados", reunionesFinalizados,
                "tareasTotales", tareasTotales,
                "tareasEnProgreso", tareasEnProgreso,
                "tareasFinalizados", tareasFinalizados
        ));
    }

    @GetMapping("/usuarios/ver/{id}")
    public String getUsuarioDetails(@PathVariable String id, Model model) {
        Usuario usuario = usuarioServicio.buscarUsuario(id);
        model.addAttribute("usuario", usuario);

        System.out.println("Mostrando detalles para el usuario con ID: " + id);
        // Retorna directamente la vista "nuevo_dashboard" (sin redirección)
        return "nuevo_dashboard";
    }

    @GetMapping("/sugerirNombresProyectos")
    @ResponseBody
    public List<String> sugerirNombresProyectos(@RequestParam String query) {
        return proyectoServicio.findNombresProyectosByQuery(query);
    }

    @GetMapping("/devolverAgentes")
    @ResponseBody
    public List<HashMap<String, String>> devolverAgentes() {
        return usuarioServicio.buscarPorRol(Rol.AGENTE).stream()
                .map(usuario -> {
                    // Retrieve project count from ProyectoServicio
                    String cuantosProyectos = Long.toString(proyectoServicio.listarProyectosPorIdUsuario(usuario.getId()).stream().count());

                    HashMap<String, String> map = new HashMap<>();
                    map.put(usuario.getNombre(), cuantosProyectos); // nombres es la llave, valor son los proyectos.
                    return map;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/buscar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> buscarEmpresasByProjectName(@RequestBody Map<String, String> requestBody) {

        String nombreProyecto = requestBody.get("nombreProyecto");
        List<String> empresas = Collections.emptyList();
        List<String> usuarios = Collections.emptyList();
        List<String> reuniones = Collections.emptyList();
        List<String> tareas = Collections.emptyList();
        List<String> sugerenciasNombresProyectos = Collections.emptyList();

        if (nombreProyecto != null && !nombreProyecto.isEmpty()) {
            empresas = proyectoServicio.findEmpresasByProjectName(nombreProyecto);
            usuarios = proyectoServicio.findUsuariosByProjectName(nombreProyecto);
            reuniones = proyectoServicio.findReunionesByProjectName(nombreProyecto);
            tareas = proyectoServicio.findTareasByProjectName(nombreProyecto);

            // Obtener sugerencias de nombres de proyectos
            sugerenciasNombresProyectos = proyectoServicio.findNombresProyectosByQuery(nombreProyecto);

        } else {
            System.out.println("No hay nombre de proyecto.");
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("nombreProyecto", nombreProyecto);
        responseData.put("empresas", empresas);
        responseData.put("usuarios", usuarios);
        responseData.put("reuniones", reuniones);
        responseData.put("tareas", tareas);
        responseData.put("sugerenciasNombresProyectos", sugerenciasNombresProyectos);

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/buscarUsuarioYProyectos")
    public String buscarUsuarioYProyectosPorNombreEmpresa(@RequestParam(required = false) String nombreEmpresa, Model model) {
        List<Usuario> listaUsuarios = Collections.emptyList();
        List<Proyecto> proyectos = Collections.emptyList();

        if (nombreEmpresa != null && !nombreEmpresa.isEmpty()) {
            listaUsuarios = usuarioServicio.buscarUsuarioPorNombreEmpresa(nombreEmpresa);
            proyectos = usuarioServicio.buscarProyectosPorNombreEmpresa(nombreEmpresa);
        } else {
            System.out.println("No hay nombre de empresa.");
        }

        model.addAttribute("nombreEmpresa", nombreEmpresa);
        model.addAttribute("listadoUsuarios", listaUsuarios);
        model.addAttribute("proyectos", proyectos);

        return "nuevo_dashboard";
    }

    @GetMapping("/gestion-empresas")
    public String showClients(Model model) {
        // Lógica para obtener y agregar datos al modelo
        // Puedes usar servicios para interactuar con la base de datos y obtener la información necesaria

        return "nuevo_dashboard"; // Este es el nombre de tu archivo HTML sin la extensión
    }

}
