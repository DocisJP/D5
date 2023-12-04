package com.D5.web.app.servicios;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.D5.web.app.entidades.Reunion;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.repositorios.ReunionRepositorio;
import jakarta.transaction.Transactional;


@Service
public class ReunionServicio {

    @Autowired
    private ReunionRepositorio reunionRepositorio;

    
    @Transactional
    public void crear(String nombre, Date horarioInicio, Boolean estado, List<Usuario> participantes, String detalle) 
    {
    	Reunion reunion = new Reunion();
    	
    	reunion.setNombre(nombre);
    	reunion.setDetalle(detalle);
    	reunion.setHorarioDeInicio(horarioInicio);
    	reunion.setParticipantes(participantes);
    	reunion.setEstado(true);
    	
    	validar(reunion);
    	reunionRepositorio.save(reunion);
    }
    
    @Transactional
    public Reunion modificar(Reunion reunion) {
    	validar(reunion);
        return reunionRepositorio.save(reunion);
    }

    
    @Transactional
    public void eliminar(Reunion reunion) {
        reunionRepositorio.delete(reunion);
    }

   
    @Transactional
    public Boolean cambiarEstado(Reunion reunion) {
        Reunion existente = reunionRepositorio.findById(reunion.getId())
                .orElseThrow(() -> new IllegalArgumentException("Reunión no encontrada"));
        existente.setEstado(!existente.getEstado());
        reunionRepositorio.save(existente);
        return existente.getEstado();
    }


   
    public Reunion buscarPorId(String id) {
        Optional<Reunion> resultado = reunionRepositorio.findById(id);
        if (resultado.isPresent()) {
            return resultado.get();
        } else {
            throw new IllegalArgumentException("Proyecto no encontrado con el ID: " + id);
        }
    }

    
    public Map<String, Object> verDetalle(Reunion reunion) {
        Map<String, Object> detalles = new HashMap<>();
        detalles.put("nombre", reunion.getNombre());
        detalles.put("detalle", reunion.getDetalle());
        detalles.put("horarioDeInicio", reunion.getHorarioDeInicio());
        detalles.put("proyecto", reunion.getProyecto());
        detalles.put("participantes", reunion.getParticipantes());

        return detalles;
    }


    
    private void validar(Reunion reunion) {
        if (reunion.getNombre() == null || reunion.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la reunión es obligatorio.");
        }
        if (reunion.getHorarioDeInicio() == null || reunion.getHorarioDeInicio().before(Date.from(Instant.now()))) {
        	throw new IllegalArgumentException("Debe ingresar un horario de inicio valido");
        }
        if (reunion.getId() == null) {
        	throw new IllegalArgumentException("Esta reunion no existe");
        }

    }

    
    public List<Reunion> listaReuniones() {
        return reunionRepositorio.findAll();
    }

	

}	