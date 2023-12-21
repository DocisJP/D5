/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.D5.web.app.servicios;

import com.D5.web.app.entidades.Proyecto;
import com.D5.web.app.entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 *
 * @author Miguel
 */
@Service
public class EmailServicio {
    
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UsuarioServicio usuarioServicio;

    public void enviarCorreo(Proyecto proyecto,String idAgente,Usuario cliente,String asunto,String mensaje) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        Usuario agente=usuarioServicio.buscarUsuario(idAgente);
                mensaje="Sr./Sra agente "+agente.getApellido()+
                        "\n\nEl cliente: "+cliente.getApellido()+" "+cliente.getNombre()+
                        "con email: "+cliente.getEmail()+
                        "\nle acaba de dejar un mensaje con respecto al proyecto de nombre: "+proyecto.getNombre()+
                        "\n\n ------------------------\n \n"+mensaje+"\n\n ------------------------\n \n  Att: FiveDesign";
                

        
        mailMessage.setTo(agente.getEmail());
        mailMessage.setSubject(asunto);
        mailMessage.setText(mensaje);

        javaMailSender.send(mailMessage);
    }
    
    public void enviarCorreo(String contactoEmail, String contactomensaje) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        
        contactomensaje = "Saludos \n\nSe recibio un nuevo mensaje de contacto a responder al email: " + contactoEmail 
                + "\n\n ------------------------\n \n" + contactomensaje + "\n\n ------------------------\n \n  Att: FiveDesign";
        
        mailMessage.setTo("julidrz@hotmail.com.ar");
        mailMessage.setSubject("nuevo mensaje de contacto");
        mailMessage.setText(contactomensaje);

        javaMailSender.send(mailMessage);
    }
}
