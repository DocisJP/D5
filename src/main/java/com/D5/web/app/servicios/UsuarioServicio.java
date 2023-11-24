package com.D5.web.app.servicios;

import com.D5.web.app.entidades.Proyecto;
import org.springframework.stereotype.Service;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.enumerador.Role;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.repositorios.UsuarioRepositorio;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void agregarUsuario(String nombre, String apellido, String email, String password, String password2, Long dni, Integer telefono) throws MyException {

        valida(password, password2);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setDni(dni);
        usuario.setTelefono(telefono);
        usuario.setRole(Role.CLIENTE);
        usuario.setEstado(Boolean.TRUE);

        usuarioRepositorio.save(usuario);

    }

    @Transactional
    public void modificar(String idUsuario, String nombre, String apellido, String email, String password, String password2, Long dni, Integer telefono) throws MyException {

        valida(password, password2);
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        
        if (respuesta.isPresent()) {
            
            Usuario usuario = respuesta.get();
            
             usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setDni(dni);
        usuario.setTelefono(telefono);
        usuario.setRole(Role.CLIENTE);
        usuario.setEstado(Boolean.TRUE);
        
        usuarioRepositorio.save(usuario);
        }

    }
    
    public Usuario buscarUsuario(String id){
    
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            
            Usuario usuario = respuesta.get();
            
            return usuario;
            
        } else { 
            
            return null;
        
        }
    
    }

    
    public void eliminar(Usuario usuario) {
        
        usuarioRepositorio.delete(usuario);
       

    }

    
    public void cambiarEstado(Usuario usuario) {
      
        usuario.setEstado(Boolean.FALSE);
    }

    public void agregarProyecto(Proyecto proyecto, Usuario usuario){
    
     List<Proyecto> proyectos = usuario.getProyectoLista();
     proyectos.add(proyecto);
     usuario.setProyectoLista(proyectos);
     usuarioRepositorio.save(usuario);
    }
        
    public void valida(String password, String password2) throws MyException {
        if (!password.equals(password2)) {
            throw new MyException("los passwords deben ser iguales ");
        }

    }

    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

        
   
}
