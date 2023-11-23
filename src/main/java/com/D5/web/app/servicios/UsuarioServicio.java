package com.D5.web.app.servicios;

import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.enumerador.Role;
import com.D5.web.app.exepciones.MyException;
import com.D5.web.app.repositorios.IServicioGeneral;
import com.D5.web.app.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UsuarioServicio implements IServicioGeneral <Usuario>, UserDetailsService{

    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    
	public void agregarUsuario(String nombre,String apellido, String email,String password, String password2, Long dni,Integer telefono) throws MyException{
            
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

	@Override
	public void modificar(Usuario algunaEntidad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar(Usuario algunaEntidad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cambiarEstado(Usuario algunaEntidad) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void crear(Usuario algunaEntidad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registrar(Usuario algunaEntidad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visualizar(Usuario dashBoardoProyectoReunion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accederPerfil(Usuario algunClienteoAgente) {
		// TODO Auto-generated method stub
		
	}

	public void valida(String password, String password2)throws MyException {
		 if (!password.equals(password2)) {
            throw new MyException("los passwords deben ser iguales ");
        }
		
	}

    @Override
    public void agregar(Usuario algunaEntidad) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void valida(Usuario algunError) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
