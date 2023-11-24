package com.D5.web.app.repositorios;


import org.springframework.stereotype.Repository;

@Repository
public interface ProyectoRepositorio {

import com.D5.web.app.entidades.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyectoRepositorio extends JpaRepository<Proyecto, String>{


}
