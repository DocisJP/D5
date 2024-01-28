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

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {

        // Obtener métricas clave
        List<Proyecto> listadoP = proyectoServicio.listarProyectos();
        List<Usuario> listadoU = usuarioServicio.listarUsuarios();

        System.out.println("LISTADO PROYECTOS" + listadoP.size());

        long totalProyectos = listadoP.size();
        long totalClientes = listadoU.stream().filter(u -> u.getRol().CLIENTE.equals(u.getRol())).count();
        long totalAgentes = listadoU.stream().filter(u -> u.getRol().AGENTE.equals(u.getRol())).count();
        long usuariosActivos = listadoU.stream().filter(Usuario::getEstado).count();
        long proyectosRecientes = listadoP.stream().filter(proyectoServicio::esProyectoReciente).count();
        long inProgressProjects = listadoP.stream().filter(p -> p.getProgreso().PENDIENTE.equals(p.getProgreso())).count();
        long proyectosFinalizados = listadoP.stream().filter(p -> p.getProgreso().FINALIZADO.equals(p.getProgreso())).count();

        System.out.println("PROYECTOS PENDIENTE " + inProgressProjects);
        System.out.println("PROYECTOS FINALIZADOS " + proyectosFinalizados);

        model.addAttribute("totalProjects", totalProyectos);
        model.addAttribute("totalClients", totalClientes);
        model.addAttribute("totalAgents", totalAgentes);
        model.addAttribute("activeClients", usuariosActivos);
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

        System.out.println("userId " + userId);

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

        System.out.println("Mostrando detalles para el usuario con ID: " + userId);
        System.out.println("Mostrando proyectos para el usuario con ID: " + proyectosTotalesDelUsu);
        System.out.println("Mostrando proyectos para el usuario con ID: " + proyectosEnProgresoDelUsu);
        System.out.println("Mostrando proyectos para el usuario con ID: " + proyectosFinalizadosDelUsu);

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
    public ResponseEntity<Map<String, Object>> buscarByProjectName(@RequestBody Map<String, String> requestBody) {

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

            System.out.println("Sugerencias de nombres de proyectos: " + sugerenciasNombresProyectos);
            System.out.println("Termino de búsqueda: '" + nombreProyecto + "'");
            System.out.println("Empresas encontradas: " + empresas);
            System.out.println("Usuarios encontrados: " + usuarios);
            System.out.println("Reuniones encontradas: " + reuniones);
            System.out.println("Tareas encontradas: " + tareas);
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

    /* @GetMapping("/buscarUsuarioYProyectos")
    public String buscarUsuarioYProyectosPorNombreEmpresa(@RequestParam(required = false) String nombreEmpresa, Model model) {
        List<Usuario> listaUsuarios = Collections.emptyList();
        List<Proyecto> proyectos = Collections.emptyList();

        if (nombreEmpresa != null && !nombreEmpresa.isEmpty()) {
            listaUsuarios = usuarioServicio.buscarUsuarioPorNombreEmpresa(nombreEmpresa);
            proyectos = usuarioServicio.buscarProyectosPorNombreEmpresa(nombreEmpresa);
            System.out.println("Termino de búsqueda: '" + nombreEmpresa + "'");
            System.out.println("Usuarios encontrados: " + listaUsuarios);
            System.out.println("Proyectos encontrados: " + proyectos);
        } else {
            System.out.println("No hay nombre de empresa.");
        }

        model.addAttribute("nombreEmpresa", nombreEmpresa);
        model.addAttribute("listadoUsuarios", listaUsuarios);
        model.addAttribute("proyectos", proyectos);

        return "nuevo_dashboard";
    }*/
    @GetMapping("/sugerirNombresEmpresas")
    @ResponseBody
    public List<String> sugerirNombresEmpresas(@RequestParam String query) {
        return usuarioServicio.findNombresEmpresasByQuery(query);
    }

    @PostMapping("/buscarUsuarioYProyectos")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> buscarUsuarioYProyectosPorNombreEmpresa(@RequestBody Map<String, String> requestBody) {

        String nombreEmpresa = requestBody.get("nombreEmpresa");
        List<String> listaUsuarios = Collections.emptyList();
        List<String> proyectos = Collections.emptyList();
        List<String> sugerenciasNombresEmpresas = Collections.emptyList();

        
         System.out.println("Termino de búsqueda1: '" + nombreEmpresa + "'");
            System.out.println("Usuarios encontrados: " + listaUsuarios);
            System.out.println("Proyectos encontrados: " + proyectos);
            
        if (nombreEmpresa != null && !nombreEmpresa.isEmpty()) {
            listaUsuarios = usuarioServicio.buscarUsuarioPorNombreEmpresa(nombreEmpresa);
            proyectos = usuarioServicio.buscarProyectosPorNombreEmpresa(nombreEmpresa);
 System.out.println("Termino de búsqueda2: '" + nombreEmpresa + "'");
            System.out.println("Usuarios encontrados: " + listaUsuarios);
            System.out.println("Proyectos encontrados: " + proyectos);
            // Obtener sugerencias de nombres de empresas
            sugerenciasNombresEmpresas = usuarioServicio.findNombresEmpresasByQuery(nombreEmpresa);

            System.out.println("Termino de búsqueda3: '" + nombreEmpresa + "'");
            System.out.println("Usuarios encontrados: " + listaUsuarios);
            System.out.println("Proyectos encontrados: " + proyectos);
        } else {
            System.out.println("No hay nombre de empresa.");
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("nombreEmpresa", nombreEmpresa);
        responseData.put("proyectos", proyectos);
        responseData.put("usuarios", listaUsuarios);
        responseData.put("sugerenciasNombresEmpresas", sugerenciasNombresEmpresas);

        return ResponseEntity.ok(responseData);
    }

}
