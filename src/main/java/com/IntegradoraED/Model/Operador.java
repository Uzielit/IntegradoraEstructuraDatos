package com.IntegradoraED.Model;

public class Operador {
    private long id;
    private String nombre;
    private boolean disponible;

    public Operador(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.disponible = true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
