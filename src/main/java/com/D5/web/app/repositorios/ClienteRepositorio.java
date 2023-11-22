package com.D5.web.app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.D5.web.app.entidades.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, String>{

}
