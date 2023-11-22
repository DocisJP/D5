package com.D5.web.app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.D5.web.app.entidades.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{

}
