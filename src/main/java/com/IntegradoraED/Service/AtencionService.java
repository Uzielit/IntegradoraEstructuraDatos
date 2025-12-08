package com.IntegradoraED.Service;

import com.IntegradoraED.EstructurasDatos.*;
import com.IntegradoraED.Model.Cliente;
import com.IntegradoraED.Model.Llamada;
import com.IntegradoraED.Model.Operador;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class AtencionService {

    // --- ESTRUCTURAS DE DATOS ---
    private Colas<Llamada> colaTickets = new Colas<>();
    private Listas<Operador> directorioAgentes = new Listas<>();
    private Pilas<String> pilaAuditoria = new Pilas<>();
    private ArbolBinario<String> archivoHistorico = new ArbolBinario<>();
    private Listas<Cliente> baseDatosClientes = new Listas<>();

    // Contadores
    private long ticketCounter = 1000;
    private long clienteIdCounter = 5000;

    public AtencionService() {
        // Datos Iniciales
        directorioAgentes.agregar(new Operador(101, "A. Martínez (Caja 1)"));
        directorioAgentes.agregar(new Operador(102, "C. López (Supervisor)"));
        directorioAgentes.agregar(new Operador(103, "R. Diaz (Soporte)"));

        registrarClienteCompleto("Juan Pérez", "TechCorp", "juan.p@techcorp.com", "555-0101");
        registrarClienteCompleto("Hotel Real", "Grupo Real", "admin@hotelreal.com", "555-0202");
    }

    // ==========================================
    // MÓDULO TICKETS (COLA) - ¡MODIFICADO!
    // ==========================================

    // Método para generar tickets automáticos (Random)
    public void generarTicketAutomatico() {
        String[] empresas = {"TechCorp", "Hotel Real", "Consultores SA", "Logística Express", "Usuario Particular"};
        String[] asuntos = {"Pago de Servicio", "Reclamo", "Contratación", "Soporte Técnico"};
        Random r = new Random();
        Llamada ticket = new Llamada(ticketCounter++, empresas[r.nextInt(empresas.length)], asuntos[r.nextInt(asuntos.length)]);
        colaTickets.formar(ticket);
        registrarAuditoria("Entrada: Ticket #" + ticket.getId() + " creado autom.");
    }

    // Método manual: Recibe datos completos para registrar al cliente también
    public void agregarTicketManual(String clienteNombre, String asunto, String email, String telefono) {
        // 1. Crear Ticket en Cola
        Llamada ticket = new Llamada(ticketCounter++, clienteNombre, asunto);
        colaTickets.formar(ticket);

        // 2. Verificar si el cliente existe
        boolean existe = false;
        for(Cliente c : baseDatosClientes.conectorHtml()) {
            if(c.getNombre().equalsIgnoreCase(clienteNombre)) {
                existe = true;
                break;
            }
        }

        // 3. Si no existe, lo registramos con los datos del formulario
        if(!existe) {
            String mailFinal = (email == null || email.isEmpty()) ? "no-mail@sistema.com" : email;
            String telFinal = (telefono == null || telefono.isEmpty()) ? "S/N" : telefono;

            registrarClienteCompleto(clienteNombre, "Particular", mailFinal, telFinal);
            registrarAuditoria("CRM: Cliente nuevo registrado desde Sala Espera (" + clienteNombre + ")");
        }

        registrarAuditoria("Entrada: Ticket #" + ticket.getId() + " manual.");
    }

    public String resolverTicket() {
        if (colaTickets.estaVacia()) return "No hay tickets pendientes.";
        Llamada ticket = colaTickets.atender();
        String log = "TKT-" + ticket.getId() + " | " + ticket.getNombreCliente() + " | FINALIZADO";
        pilaAuditoria.apilar("Resolución: " + log);
        archivoHistorico.insertar(log);
        return "Ticket " + ticket.getId() + " resuelto.";
    }

    // ==========================================
    // MÓDULO CLIENTES (CRUD)
    // ==========================================
    public void registrarClienteCompleto(String nombre, String empresa, String email, String tel) {
        baseDatosClientes.agregar(new Cliente(clienteIdCounter++, nombre, empresa, email, tel));
    }

    // Este es para cuando creas cliente desde el módulo "Clientes" (formulario simple)
    public void registrarClienteNuevo(String nombre) {
        for(Cliente c : baseDatosClientes.conectorHtml()) {
            if(c.getNombre().equalsIgnoreCase(nombre)) return;
        }
        registrarClienteCompleto(nombre, "Particular", nombre.toLowerCase().replace(" ",".")+"@mail.com", "Pendiente");
        registrarAuditoria("CRM: Nuevo cliente registrado (" + nombre + ")");
    }

    public List<Cliente> getTodosLosClientes() {
        return baseDatosClientes.conectorHtml();
    }

    public Cliente buscarClientePorId(long id) {
        for (Cliente c : baseDatosClientes.conectorHtml()) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public void eliminarClientePorId(long id) {
        List<Cliente> lista = baseDatosClientes.conectorHtml();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == id) {
                try {
                    baseDatosClientes.eliminarElemento(i);
                    registrarAuditoria("CRM: Cliente eliminado (ID: " + id + ")");
                } catch (Exception e) {}
                break;
            }
        }
    }

    // ==========================================
    // OTROS MÓDULOS (AGENTES, AUDITORÍA, ETC)
    // ==========================================
    public void contratarAgente(String nombre) {
        directorioAgentes.agregar(new Operador(System.currentTimeMillis(), nombre));
        registrarAuditoria("RRHH: Alta de " + nombre);
    }
    public void despedirAgente(int indice) {
        try { directorioAgentes.eliminarElemento(indice); registrarAuditoria("RRHH: Baja de personal"); } catch (Exception e) {}
    }
    public void limpiarUltimaAuditoria() { if(!pilaAuditoria.estaVacia()) pilaAuditoria.desapilar(); }
    public void borrarDeArchivo(String dato) { archivoHistorico.eliminar(dato); registrarAuditoria("Admin: Registro eliminado del archivo"); }

    public String buscarEnSistema(String query) {
        if(query == null || query.trim().isEmpty()) return "";
        if(colaTickets.existe(query)) return "📍 Encontrado en: SALA DE ESPERA (Pendiente)";
        if(pilaAuditoria.existe(query)) return "📍 Encontrado en: AUDITORÍA RECIENTE";
        if(archivoHistorico.buscar(query)) return "📍 Encontrado en: ARCHIVO HISTÓRICO";
        for(Object op : directorioAgentes.conectorHtml()){
            if(op.toString().contains(query)) return "📍 Encontrado en: CAJEROS / AGENTES";
        }
        for(Cliente c : baseDatosClientes.conectorHtml()){
            if(c.getNombre().toLowerCase().contains(query.toLowerCase())) return "📍 Encontrado en: BASE DE DATOS CLIENTES";
        }
        return "⚠️ No se encontraron resultados para: " + query;
    }

    public void reiniciarSistema() {
        colaTickets = new Colas<>();
        pilaAuditoria = new Pilas<>();
        archivoHistorico = new ArbolBinario<>();
        registrarAuditoria("⚠️ SISTEMA REINICIADO POR ADMINISTRADOR");
    }

    private void registrarAuditoria(String accion) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        pilaAuditoria.apilar("[" + LocalDateTime.now().format(dtf) + "] " + accion);
    }

    // Getters Views
    public List<Llamada> getTicketsPendientes() { return colaTickets.conectorHtml(); }
    public List<Operador> getAgentes() { return directorioAgentes.conectorHtml(); }
    public List<String> getAuditoria() { return pilaAuditoria.conectorHtml(); }
    public List<String> getArchivo() { return archivoHistorico.obtenerListaOrdenada(); }
}