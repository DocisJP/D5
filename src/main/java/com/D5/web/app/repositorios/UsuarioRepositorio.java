package com.D5.web.app.repositorios;

import com.D5.web.app.entidades.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.D5.web.app.entidades.Usuario;
import com.D5.web.app.enumerador.Rol;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>, UsuarioCustomRepository {

    @Query("SELECT u FROM Usuario u WHERE u.dni = :dni")
    public Usuario buscarPorDni(@Param("dni") Integer dni);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario findByEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol")
    public List<Usuario> findByRol(@Param("rol") Rol rol);

    @Query("SELECT u FROM Usuario u WHERE LOWER( u.empresa) LIKE LOWER(CONCAT('%',:empresa, '%'))")
    public List<Usuario> buscarUsuarioPorNombreEmpresa(@Param("empresa") String empresa);

    @Query("SELECT u FROM Usuario u JOIN u.proyectos p WHERE p.id = :proyectoId")
    public List<Usuario> listarUsuariosPorProyectoId(@Param("proyectoId") String proyectoId);

    @Override
    default List<String> findNombresEmpresasByQuery(String query) {
        List<String> empresas = findAllByEmpresaContainingIgnoreCase(query)
                .stream()
                .map(Usuario::getEmpresa)
                .filter(Objects::nonNull) // Filtra empresas nulas si es necesario
                .distinct()
                .collect(Collectors.toList());

        return empresas;
    }

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol = Rol.AGENTE")
    Long countAllAgentes();

    List<Usuario> findAllByEmpresaContainingIgnoreCase(String empresa);

    @Query("SELECT DISTINCT p FROM Proyecto p JOIN p.usuarios u WHERE LOWER(u.empresa) LIKE LOWER(CONCAT('%', :nombreEmpresa, '%'))")
    List<Proyecto> findProyectosByNombreEmpresa(@Param("nombreEmpresa") String nombreEmpresa);

}
