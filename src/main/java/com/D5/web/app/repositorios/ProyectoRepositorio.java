package com.D5.web.app.repositorios;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.D5.web.app.entidades.Proyecto;

@Repository
public interface ProyectoRepositorio extends JpaRepository<Proyecto, String>{


}
