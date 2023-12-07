package com.D5.web.app.servicios;


import org.springframework.stereotype.Service;

import com.D5.web.app.entidades.Imagen;
import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.enumerador.Rol;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.repositorios.UsuarioRepositorio;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List; 
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

    BCryptPasswordEncoder encode = new BCryptPasswordEncoder();

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void agregarUsuario(String nombre, String apellido, String email, String password, String password2, Long dni, Long telefono, 
            String direccion, String empresa, MultipartFile archivo) throws MyException {

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
        usuario.setRol(Rol.USER);
        usuario.setEstado(Boolean.TRUE);

        Imagen imagen = imagenServicio.guardar(archivo);

        usuario.setImagen(imagen);

        usuarioRepositorio.save(usuario);

    }

    @Transactional
    public void modificar(Usuario usuario) {
        Usuario existente = usuarioRepositorio.findById(usuario.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setEmail(usuario.getEmail());
        existente.setDni(usuario.getDni());
        existente.setDireccion(usuario.getDireccion());
        existente.setEmpresa(usuario.getEmpresa());
        existente.setTelefono(usuario.getTelefono());
        existente.setImagen(usuario.getImagen());
        existente.setRol(usuario.getRol());
 
        usuarioRepositorio.save(existente);
    }
 
    public Usuario buscarUsuario(String id) {

        return usuarioRepositorio.findById(id).orElse(null);

    }
 
    public Usuario buscarporEmail(String email) {
    	return usuarioRepositorio.findByEmail(email);
    }
    
    public List<Usuario> buscarPorRol(Rol rol) {
        return usuarioRepositorio.findByRol(rol);
    }

    @Transactional
    public void eliminar(Usuario usuario) {

        usuarioRepositorio.delete(usuario);

    }
   public Usuario getOne(String id){
    
        return usuarioRepositorio.getReferenceById(id);
    }
    
    public void cambiarEstado(Usuario usuario) {
        usuario.setEstado(!usuario.getEstado());
        usuarioRepositorio.save(usuario);

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
        
    Usuario usuario = usuarioRepositorio.findByEmail(email);
    
      if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList<GrantedAuthority>();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name());
            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);

        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}