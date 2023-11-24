package com.D5.web.app.repositorios;


import org.springframework.stereotype.Repository;

@Repository
public interface TareaRepositorio {

import com.D5.web.app.entidades.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TareaRepositorio extends JpaRepository<Tarea, String>{


}
