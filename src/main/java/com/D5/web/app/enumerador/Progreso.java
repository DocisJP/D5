package com.D5.web.app.enumerador;

public enum Progreso {
    
    FINALIZADO("Finalizado"),
    PENDIENTE("Pendiente");
    
     private String descripcion;

    Progreso(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

