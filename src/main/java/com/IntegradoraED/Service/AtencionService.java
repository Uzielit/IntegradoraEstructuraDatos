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

/**
 * AtencionService
 * ---------------
 * Esta clase contiene toda la Lógica de Negocio.
 * Es el puente entre lo que el usuario ve (Controlador/HTML) y cómo se guardan los datos (Estructuras).
 *
 * AQUÍ OCURRE LA MAGIA DE LA INTEGRACIÓN DE ESTRUCTURAS:
 * 1. Los Tickets entran a una COLA (FIFO).
 * 2. Los Agentes están en una LISTA (Dinámica).
 * 3. Al resolver, se guarda registro en una PILA (Auditoría LIFO).
 * 4. Y el historial permanente se va a un ÁRBOL BINARIO (Ordenado).
 */

@Service
public class AtencionService {

    // --- ESTRUCTURAS DE DATOS (INSTANCIAS) ---
    // Usamos la Cola para atender en orden de llegada (Justicia: FIFO)
    private Colas<Llamada> colaTickets = new Colas<>();

    // Usamos Lista para gestionar el personal (fácil de recorrer y editar)
    private Listas<Operador> directorioAgentes = new Listas<>();

    // Usamos Pila para la auditoría porque queremos ver lo MÁS RECIENTE arriba (LIFO)
    private Pilas<String> pilaAuditoria = new Pilas<>();

    // Usamos Árbol para el archivo histórico para mantenerlo ordenado y buscar rápido
    private ArbolBinario<String> archivoHistorico = new ArbolBinario<>();

    // Base de datos simulada de clientes en memoria
    private Listas<Cliente> baseDatosClientes = new Listas<>();

    // Contadores para generar IDs únicos
    private long ticketCounter = 1000;
    private long clienteManualCounter = 5000;

    /**
     * Constructor:
     * datos predeterminados para que haya operadores
     */
    public AtencionService() {
        // Inicializamos agentes por defecto
        directorioAgentes.agregar(new Operador(101, "A. Martínez (Caja 1)"));
        directorioAgentes.agregar(new Operador(102, "C. López (Supervisor)"));
    }

    // -------------------------------------------------------------------------
    // --- MÓDULO TICKETS (GESTIÓN DE LA COLA) ---
    // -------------------------------------------------------------------------

    /**
     * Simulacion de datos, para que no haya rerigstro vacio
     */
    public void generarTicketAutomatico() {
        String[] empresas = {"TechCorp", "Hotel Real", "Consultores SA", "Logística Express", "Usuario Particular"};
        String[] asuntos = {"Pago de Servicio", "Reclamo", "Contratación", "Soporte Técnico"};
        Random r = new Random();

        // Selección aleatoria de datos
        String nombre = empresas[r.nextInt(empresas.length)];
        String asunto = asuntos[r.nextInt(asuntos.length)];
        String email = nombre.toLowerCase().replace(" ", "") + "@mail.com";
        String tel = "555-" + (1000 + r.nextInt(9000));

        // Creación del objeto y Enqueue (Formar en la cola)
        Llamada ticket = new Llamada(ticketCounter++, nombre, asunto, email, tel);
        colaTickets.formar(ticket);

        registrarAuditoria("Entrada: Ticket #" + ticket.getId() + " creado autom.");
    }

    /**
     * Crea un ticket con datos reales del formulario web.
     */
    public void agregarTicketManual(String clienteNombre, String asunto, String email, String telefono) {
        // Validaciones básicas para no guardar nulos
        String mailFinal = (email == null || email.trim().isEmpty()) ? "S/N" : email;
        String telFinal = (telefono == null || telefono.trim().isEmpty()) ? "S/N" : telefono;

        Llamada ticket = new Llamada(ticketCounter++, clienteNombre, asunto, mailFinal, telFinal);
        colaTickets.formar(ticket);

        registrarAuditoria("Entrada: Ticket #" + ticket.getId() + " manual.");
    }

    /**
     * MÉTODO PRINCIPAL DEL SISTEMA
     * Simula la resolución de un problema. Mueve datos entre TODAS las estructuras.
     * 1. Saca de la Cola (Dequeue).
     * 2. Guarda el Cliente en la Lista (BD).
     * 3. Registra en la Pila (Auditoría).
     * 4. Archiva en el Árbol (Historial).
     */
    public String resolverTicket() {
        // Validaciones: No se puede atender si no hay agentes o tickets
        if (directorioAgentes.estaVacia()) return "ERROR: No hay agentes activos. ¡Agregue personal!";
        if (colaTickets.estaVacia()) return "No hay tickets pendientes.";

        // 1. Atender (Sacar de la Cola)
        Llamada ticket = colaTickets.atender();

        // 2. Creamos y guardamos al cliente en la "Base de Datos" (Lista)
        Cliente clienteAtendido = new Cliente(
                ticket.getId(), // Reusamos el ID del ticket para vincularlos
                ticket.getNombreCliente(),
                "Cliente Atendido",
                ticket.getEmail(),
                ticket.getTelefono()
        );
        baseDatosClientes.agregar(clienteAtendido);

        // Preparamos el mensaje de log
        String log = "TKT-" + ticket.getId() + " | " + ticket.getNombreCliente() + " | FINALIZADO";

        // 3. Apilar en Auditoría (Lo más reciente arriba)
        pilaAuditoria.apilar("Resolución: " + log);

        // 4. Insertar en Árbol (Se ordena alfabéticamente/numéricamente)
        archivoHistorico.insertar(log);

        return "Ticket " + ticket.getId() + " atendido y archivado.";
    }

    // -------------------------------------------------------------------------
    // --- MÓDULO CLIENTES MANUALES (CRUD EN LISTA) ---
    // -------------------------------------------------------------------------

    public void registrarClienteNuevo(String nombre, String email, String telefono) {
        String emailFinal = (email == null || email.trim().isEmpty()) ? "S/N" : email;
        String telFinal = (telefono == null || telefono.trim().isEmpty()) ? "S/N" : telefono;

        baseDatosClientes.agregar(new Cliente(clienteManualCounter++, nombre, "Registro Manual", emailFinal, telFinal));
        registrarAuditoria("CRM: Cliente manual " + nombre + " registrado.");
    }

    // Retorna la lista convertida para el HTML
    public List<Cliente> getTodosLosClientes() { return baseDatosClientes.conectorHtml(); }

    /**
     * Búsqueda Lineal en la lista de clientes.
     */
    public Cliente buscarClientePorId(long id) {
        for (Cliente c : baseDatosClientes.conectorHtml()) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    /**
     * Eliminación en lista. Busca por ID y elimina por índice.
     */
    public void eliminarClientePorId(long id) {
        List<Cliente> lista = baseDatosClientes.conectorHtml();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == id) {
                try { baseDatosClientes.eliminarElemento(i); } catch (Exception e) {}
                break; // Rompe el ciclo una vez encontrado y eliminado
            }
        }
    }

    // -------------------------------------------------------------------------
    // --- MÓDULO AGENTES (GESTIÓN DE PERSONAL) ---
    // -------------------------------------------------------------------------

    public void contratarAgente(String nombre) {
        // Usamos el tiempo actual como ID único temporal
        directorioAgentes.agregar(new Operador(System.currentTimeMillis(), nombre));
        // LOG: Se registra en actividad reciente
        registrarAuditoria("Registrado : Nuevo operador - " + nombre);
    }

    public void despedirAgente(int indice) {
        try {
            // Recuperamos el objeto eliminado para saber su nombre antes de que desaparezca
            Operador agenteEliminado = directorioAgentes.eliminarElemento(indice);
            if (agenteEliminado != null) {
                // LOG: Se registra con el nombre específico
                registrarAuditoria(": Operador Despedido  - " + agenteEliminado.getNombre());
            }
        } catch (Exception e) {
            registrarAuditoria(" Error al eliminar operador");
        }
    }

    // -------------------------------------------------------------------------
    // --- OTROS MÓDULOS (AUDITORÍA, BÚSQUEDA, RESET) ---
    // -------------------------------------------------------------------------

    // Elimina el evento más reciente (Undo/Deshacer)
    public void limpiarUltimaAuditoria() { if(!pilaAuditoria.estaVacia()) pilaAuditoria.desapilar(); }

    // Elimina un nodo específico del árbol
    public void borrarDeArchivo(String dato) { archivoHistorico.eliminar(dato); }

    /**
     * BUSCADOR GLOBAL:
     * Busca el texto 'query' en TODAS las estructuras de datos disponibles.
     * Retorna un string indicando en qué lugar se encontró primero.
     */
    public String buscarEnSistema(String query) {
        if(query == null || query.trim().isEmpty()) return "";

        // Verifica en Cola, Pila, Árbol y Lista
        if(colaTickets.existe(query)) return " SALA DE ESPERA";
        if(pilaAuditoria.existe(query)) return " AUDITORÍA";
        if(archivoHistorico.buscar(query)) return " HISTÓRICO";

        // Búsqueda en lista de clientes (manual)
        for(Cliente c : baseDatosClientes.conectorHtml()){
            if(c.getNombre().toLowerCase().contains(query.toLowerCase())) return "📍 CLIENTES";
        }
        return "⚠️ No encontrado: " + query;
    }

    /**
     * Reinicio de fábrica: Crea nuevas instancias vacías de todas las estructuras.
     */
    public void reiniciarSistema() {
        colaTickets = new Colas<>();
        pilaAuditoria = new Pilas<>();
        archivoHistorico = new ArbolBinario<>();
        // Nota: No se borran los agentes ni clientes en este método específico según la lógica actual
    }

    // Método privado para facilitar el registro de logs con fecha y hora
    private void registrarAuditoria(String accion) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        pilaAuditoria.apilar("[" + LocalDateTime.now().format(dtf) + "] " + accion);
    }

    // --- GETTERS (CONECTORES AL FRONTEND) ---
    // Estos métodos convierten tus estructuras complejas en Listas simples de Java
    // para que Thymeleaf pueda dibujarlas en las tablas HTML.
    public List<Llamada> getTicketsPendientes() { return colaTickets.conectorHtml(); }
    public List<Operador> getAgentes() { return directorioAgentes.conectorHtml(); }
    public List<String> getAuditoria() { return pilaAuditoria.conectorHtml(); }
    public List<String> getArchivo() { return archivoHistorico.obtenerListaOrdenada(); }
}