package com.IntegradoraED.Model;

import java.time.LocalDateTime;

public class Llamada {
    private long id;
    private String nombreCliente;
    private String tipoProblema;
    private LocalDateTime horaLlegada;

    public Llamada(long id, String nombreCliente, String tipoProblema) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.tipoProblema = tipoProblema;
        this.horaLlegada = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTipoProblema() {
        return tipoProblema;
    }

    public void setTipoProblema(String tipoProblema) {
        this.tipoProblema = tipoProblema;
    }

    public LocalDateTime getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(LocalDateTime horaLlegada) {
        this.horaLlegada = horaLlegada;
    }
}
