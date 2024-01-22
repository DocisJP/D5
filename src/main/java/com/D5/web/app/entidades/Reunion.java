package com.D5.web.app.entidades;

import com.D5.web.app.enumerador.Progreso;
import java.util.Date;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reunion")
public class Reunion implements Comparable<Reunion> {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    private String detalle;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date horarioDeInicio;

    private String horarioDeInicioFormatt;

    //Agregue horario fin de la reunion
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date horarioDeFin;

    private String horarioDeFinFormatt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_destinatario_id")
    private Usuario usuarioDestinatario;

    @ManyToMany
    @JoinTable(
            name = "reunion_usuarios",
            joinColumns = @JoinColumn(name = "reunion_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private List<Usuario> usuarios = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id")
    private Proyecto proyecto;

    private Boolean estado;

    @Enumerated(EnumType.STRING)
    private Progreso progreso;

    public Progreso getProgreso() {
        return progreso;
    }

    public void setProgreso(Progreso progreso) {
        this.progreso = progreso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public String getId() {
        return id;
    }

    public String getHorarioDeInicioFormatt() {
        return horarioDeInicioFormatt;
    }

    public void setHorarioDeInicioFormatt(String horarioDeInicioFormatt) {
        this.horarioDeInicioFormatt = horarioDeInicioFormatt;
    }

    public String getHorarioDeFinFormatt() {
        return horarioDeFinFormatt;
    }

    public void setHorarioDeFinFormatt(String horarioDeFinFormatt) {
        this.horarioDeFinFormatt = horarioDeFinFormatt;
    }

    //Getter and setter
    public Date getHorarioDeFin() {
        return horarioDeFin;
    }

    public void setHorarioDeFin(Date horarioDeFin) {
        this.horarioDeFin = horarioDeFin;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Date getHorarioDeInicio() {
        return horarioDeInicio;
    }

    public void setHorarioDeInicio(Date horarioDeInicio) {
        this.horarioDeInicio = horarioDeInicio;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario getUsuarioDestinatario() {
        return usuarioDestinatario;
    }

    public void setUsuarioDestinatario(Usuario usuarioDestinatario) {
        this.usuarioDestinatario = usuarioDestinatario;
    }

    @Override
    public String toString() {
        return "Reunion{" + "id=" + id + ", nombre=" + nombre + ", detalle=" + detalle + ", horarioDeInicio=" + horarioDeInicio + ", horarioDeFin=" + horarioDeFin + ", usuario=" + usuario + ", usuarios=" + usuarios + ", proyecto=" + proyecto + ", estado=" + estado + ", progreso=" + progreso + '}';
    }

    @Override
    public int compareTo(Reunion otraReunion) {
        return this.horarioDeInicio.compareTo(otraReunion.getHorarioDeInicio());
    }

}
