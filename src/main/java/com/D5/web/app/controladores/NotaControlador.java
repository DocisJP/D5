package com.D5.web.app.controladores;

import com.D5.web.app.entidades.Nota;
import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Tarea;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.enumerador.Rol;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.servicios.NotaServicio;
import com.D5.web.app.servicios.ProyectoServicio;
import jakarta.servlet.http.HttpSession;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/nota")
public class NotaControlador {

    @Autowired
    ProyectoServicio proyectoServicio;

    @Autowired
    NotaServicio notaServicio;

    @GetMapping("/registrar/{idProyecto}")
    public String registrarNota(@PathVariable String idProyecto, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Proyecto proyecto = proyectoServicio.buscarPorId(idProyecto);

        // Crear una nueva instancia de Nota y establecer el proyecto y usuario
        Nota nuevaNota = new Nota();
        nuevaNota.setProyecto(proyecto);
        nuevaNota.setUsuario(usuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("nota", nuevaNota);
        return "agregar_nota.html";
    }

    // Procesa el formulario y registra una nueva nota
    @PostMapping("/registro")
    public String registrarNota(@RequestParam String detalleNota,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date fecha,
            @RequestParam String usuarioId,
            @RequestParam String proyectoId,
            HttpSession session,
            RedirectAttributes redirectAttrs
    ) {
        Usuario usuarioEnSession = (Usuario) session.getAttribute("usuariosession");
        Proyecto proyectoAsociado = proyectoServicio.buscarPorId(proyectoId);

        try {
            Nota notaGuardada = notaServicio.crear(detalleNota, fecha, usuarioEnSession, proyectoAsociado);

            if (notaGuardada != null && notaGuardada.getId() != null) {
                redirectAttrs.addFlashAttribute("exito", "La nota fue creada con éxito");
                return "redirect:/nota/detalle/" + notaGuardada.getId();
            } else {
                // Manejar el caso de que notaGuardada sea nula o no tenga ID
                redirectAttrs.addFlashAttribute("error", "La nota no pudo ser creada.");
                return "redirect:/nota/registro";
            }
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
            return "redirect:/nota/registro";
        }
    }
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable String id, Model model) throws MyException {
        Nota nota = notaServicio.buscarPorId(id);
        if (nota == null) {
            return "redirect:/index";
        } else {
            model.addAttribute("nota", nota);
            return "detalle_nota";
        }
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarNota(@PathVariable String id) {
        notaServicio.eliminar(notaServicio.buscarPorId(id));
        return "redirect:/proyecto/trabajo";
    }
}
