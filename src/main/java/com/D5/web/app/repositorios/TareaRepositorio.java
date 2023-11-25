package com.D5.web.app.repositorios;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.D5.web.app.entidades.Tarea;

@Repository
public interface TareaRepositorio extends JpaRepository<Tarea, String>{

}
