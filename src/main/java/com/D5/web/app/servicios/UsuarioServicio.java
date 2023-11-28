package com.D5.web.app.servicios;


import org.springframework.stereotype.Service;

import com.D5.web.app.entidades.Imagen;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.enumerador.Rol;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.repositorios.UsuarioRepositorio;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;
    
    @Transactional
    public void agregarUsuario(String nombre, String apellido, String email, String password, String password2, Long dni, Integer telefono, String direccion, String empresa, MultipartFile archivo) throws MyException {

        valida(password, password2);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setDni(dni);
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);
        usuario.setEmpresa(empresa);
        
        Imagen imagen = imagenServicio.guardar(archivo);
        
        usuario.setImagen(imagen);
        
        usuario.setRol(Rol.USER);
        usuario.setEstado(Boolean.TRUE);

        usuarioRepositorio.save(usuario);

    }

    @Transactional
    public void modificar(String idUsuario, String nombre, String apellido, String email, String password, String password2, Long dni, Integer telefono, MultipartFile archivo, String direccion, String empresa) throws MyException {

        valida(password, password2);
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        
        if (respuesta.isPresent()) {
            
            Usuario usuario = respuesta.get();
            
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setDni(dni);
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);
        usuario.setEmpresa(empresa);
        
        String idImagen = null;
            
            if (usuario.getImagen() != null) {
                
                idImagen = usuario.getImagen().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            
        
        usuario.setImagen(imagen);
        
        usuario.setRol(Rol.USER);
        usuario.setEstado(Boolean.TRUE);
        
        usuarioRepositorio.save(usuario);
        }

    }
    
    public Usuario buscarUsuario(String id){
    
    	/*
	        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
	        
	        if (respuesta.isPresent()) {
	            
	            Usuario usuario = respuesta.get();
	            
	            return usuario;
	            
	        } else { 
	            
	            return null;
	        
	        }
    	 * 
    	 */
    	return usuarioRepositorio.findById(id).orElse(null);
    
    }

    public Usuario buscarporEmail(String email) {
    	return usuarioRepositorio.findbyEmail(email);
    }
    
    public void eliminar(Usuario usuario) {
        
        usuarioRepositorio.delete(usuario);
       

    }

    
    public void cambiarEstado(Usuario usuario) {
      
        usuario.setEstado(Boolean.FALSE);
        
        /*
         *  usuario.setEstado(!usuario.getEstado());
        	usuarioRepositorio.save(usuario);
         */
    }

//    public void agregarProyecto(Proyecto proyecto, Usuario usuario){
//    
//     List<Proyecto> proyectos = usuario.getProyectoLista();
//     proyectos.add(proyecto);
//     usuario.setProyectoLista(proyectos);
//     usuarioRepositorio.save(usuario);
//    }
        
    public void valida(String password, String password2) throws MyException {
        if (!password.equals(password2)) {
            throw new MyException("los passwords deben ser iguales ");
        }

    }

    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
    Usuario usuario = usuarioRepositorio.findbyEmail(email);
    
      if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new  SimpleGrantedAuthority(usuario.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);

        } else {

            return null;

        }
    
    }

        
    
   
}
