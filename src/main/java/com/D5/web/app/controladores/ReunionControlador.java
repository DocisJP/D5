package com.D5.web.app.controladores;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.D5.web.app.entidades.Reunion;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.servicios.ReunionServicio;
import com.D5.web.app.servicios.UsuarioServicio;


@Controller
@RequestMapping("/reunion")
public class ReunionControlador {

    @Autowired
    ReunionServicio reunionServicio;

    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/registro")
public String mostrarFormularioRegistro(Model model) {
    model.addAttribute("reunion", new Reunion());
    model.addAttribute("usuarios", usuarioServicio.listarUsuarios()); 
    return "calendario_reuniones"; 
}

    
    @PostMapping("/registro")
    public String registrarReunion(@ModelAttribute("reunion") Reunion reunion, 
                               @RequestParam List<String> participantes, 
                               RedirectAttributes redirectAttributes) {
    try {
        // Convertir los IDs de los usuarios en objetos Usuario
        List<Usuario> usuariosParticipantes = participantes.stream()
            .map(id -> usuarioServicio.buscarUsuario(id)) // Método para buscar usuario por ID
            .collect(Collectors.toList());

        reunion.setParticipantes(usuariosParticipantes);
        reunionServicio.crear(reunion.getNombre(), 
            reunion.getHorarioDeInicio(), 
            reunion.getEstado(),
            usuariosParticipantes,
            reunion.getDetalle());
        
        redirectAttributes.addFlashAttribute("exito", "Reunión creada con éxito");
        return "redirect:/reunion/calendario_reuniones";
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error al crear la reunión: " + e.getMessage());
        return "redirect:/reunion/registro";
    }
}


    
}
