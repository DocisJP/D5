package com.D5.web.app.validadores;

import com.D5.web.app.entidades.Usuario;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UsuarioValidador implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Usuario.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Usuario usuario = (Usuario) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", "requerido.usuario.nombre");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "apellido", "requerido.usuario.nombre");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dni", "requerido.usuario.dni");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "requerido.usuario.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "requerido.usuario.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "telefono", "requerido.usuario.telefono");

        if (!usuario.getApellido().matches("[A-Z]")) {
            errors.rejectValue("apellido", "pattern.usuario.apellido");
        }
        if (!usuario.getNombre().matches("[A-Z]")) {
            errors.rejectValue("nombre", "pattern.usuario.nombre");
        }
        if (!usuario.getDni().toString().matches("[0-9]")) {
            errors.rejectValue("apellido", "pattern.usuario.dni");
        }
        if (!usuario.getEmpresa().matches("[A-Z]")) {
            errors.rejectValue("empresa", "pattern.usuario.identificador");
        }
    }

}
