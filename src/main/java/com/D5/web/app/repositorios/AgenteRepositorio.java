package com.D5.web.app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.D5.web.app.entidades.Agente;

public interface AgenteRepositorio extends JpaRepository<Agente, String> {

}
