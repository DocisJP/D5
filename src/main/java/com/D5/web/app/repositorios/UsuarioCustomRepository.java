package com.D5.web.app.repositorios;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioCustomRepository {
    List<String> findNombresEmpresasByQuery(String query);
}
