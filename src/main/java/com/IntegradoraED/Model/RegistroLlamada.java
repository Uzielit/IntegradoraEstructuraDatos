package com.IntegradoraED.Model;

import java.time.LocalDateTime;
//FUNCION QUE NO SÉ LLEGO A UTILIZAR, PENSABAMIS AGREGAR LOS ATRIBUTOS
public class RegistroLlamada {
    private String descripcion;
    private LocalDateTime timestamp;

    public RegistroLlamada(String descripcion) {
        this.descripcion = descripcion;
        this.timestamp = LocalDateTime.now();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
