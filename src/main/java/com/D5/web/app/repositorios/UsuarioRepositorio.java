package com.D5.web.app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.D5.web.app.entidades.Usuario;
import java.util.List;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
    
	@Query("SELECT u FROM Usuario u WHERE u.dni = :dni")
    public Usuario buscarPorDni(@Param("dni")Integer dni);
	
	@Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario findbyEmail(@Param("email")String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.role = :role")
    public List<Usuario> findbyRole(@Param("role") String role);
}
