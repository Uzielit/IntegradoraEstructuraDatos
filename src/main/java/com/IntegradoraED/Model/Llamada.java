package com.IntegradoraED.Model;

import java.time.LocalDateTime;

public class Llamada {
    private long id;
    private String nombreCliente;
    private String tipoProblema;
    // Estos campos transportan la info hasta que se atiende
    private String email;
    private String telefono;
    private LocalDateTime horaLlegada;

    public Llamada(long id, String nombreCliente, String tipoProblema, String email, String telefono) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.tipoProblema = tipoProblema;
        this.email = email;
        this.telefono = telefono;
        this.horaLlegada = LocalDateTime.now();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getTipoProblema() { return tipoProblema; }
    public void setTipoProblema(String tipoProblema) { this.tipoProblema = tipoProblema; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDateTime getHoraLlegada() { return horaLlegada; }
    public void setHoraLlegada(LocalDateTime horaLlegada) { this.horaLlegada = horaLlegada; }
}