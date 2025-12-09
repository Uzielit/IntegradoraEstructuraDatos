package com.IntegradoraED.Model;

public class Cliente {
    private long id;
    private String nombre;
    private String empresa;
    private String email;
    private String telefono;
    private String estatus;

    public Cliente(long id, String nombre, String empresa, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.empresa = empresa;
        this.email = email;
        this.telefono = telefono;
        this.estatus = "Activo";
    }

    // --- GETTERS ---
    public long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmpresa() { return empresa; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getEstatus() { return estatus; }

    // --- SETTERS (Aquí faltaba el setId) ---
    public void setId(long id) { this.id = id; } // <--- ¡ESTE ERA EL FALTANTE!

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEstatus(String estatus) { this.estatus = estatus; }
}