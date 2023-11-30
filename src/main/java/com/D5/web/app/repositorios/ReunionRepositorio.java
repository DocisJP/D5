package com.D5.web.app.repositorios;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.D5.web.app.entidades.Reunion;

@Repository
public interface ReunionRepositorio extends JpaRepository<Reunion, String> {

	List<Reunion> findByNombre(String nombre);

    List<Reunion> findByHorarioDeInicioBetween(Date inicio, Date fin);
}
