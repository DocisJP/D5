/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.D5.web.app;

import com.D5.web.app.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author pc
 */
@Configuration
@EnableWebSecurity
public class SeguridadWeb {

    @Autowired
    public UsuarioServicio usuarioServicio;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public SecurityFilterChain filtroCadena(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(autoriza -> autoriza
                                                    .requestMatchers("/admin/*")
                                                    .hasRole("ADMIN")
                                                    .requestMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll())
                                .formLogin(autoriza -> autoriza.loginPage("/login")
                                                    .loginProcessingUrl("/logincheck")
                                                    .usernameParameter("email")
                                                    .passwordParameter("password")
                                                    .defaultSuccessUrl("/inicio")
                                                    .permitAll())
                                .logout(autoriza -> autoriza
                                                    .logoutUrl("/logout")
                                                    .logoutSuccessUrl("/login")
                                                    .permitAll())
                                .csrf(csrfCustomizer -> csrfCustomizer
                                                    .disable())
                                .build();

    }
}