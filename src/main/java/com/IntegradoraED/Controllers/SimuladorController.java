package com.IntegradoraED.Controllers;

import com.IntegradoraED.Model.Cliente;
import com.IntegradoraED.Model.Llamada;
import com.IntegradoraED.Service.AtencionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * SimuladorController
 * Este es el controlador principal de la aplicación web.
 * Se encarga de recibir todas las peticiones HTTP (clics del usuario, formularios),
 * llamar al servicio (AtencionService) para procesar los datos y devolver las
 * vistas HTML (index, clientes, ajustes, etc.) con la información actualizada.
 */
@Controller
public class SimuladorController {

    // CONEXIÓN DE LA LOGICA
    @Autowired
    private AtencionService service;




    private <T> List<T> asegurarLista(List<T> list) {
        return (list == null) ? new ArrayList<>() : list;
    }

    /**
     * Creamos la lista de los clientes
     */
    private List<Cliente> validarClientes(List<Cliente> original) {
        List<Cliente> limpia = new ArrayList<>();
        if (original != null) {
            for (Cliente c : original) {
                // Solo pasa el filtro si el objeto existe y tiene nombre
                if (c != null && c.getNombre() != null && !c.getNombre().isEmpty()) {
                    limpia.add(c);
                }
            }
        }
        return limpia;
    }


    // VISTAS PRINCIPALES (GET MAPPINGS)


    /**
     * Ruta Principal
     * Carga toda la información necesaria para el panel de control: tickets, agentes, auditoría.
     */
    @GetMapping("/")
    public String dashboard(Model model) {
        // Obtenemos las listas de tickets actuales desde el servicio
        List<Llamada> tickets = service.getTicketsPendientes();


        // Esto imprime en la terminal del servidor un reporte rápido de tickets de "Soporte".
        // Tiene validaciones de nulos para no romper la ejecución si hay datos incompletos.
        if(tickets != null) {
            int contadorSoporte = 0;
            System.out.println("----- REPORTE -----");
            for (Llamada t : tickets) {
                if(t != null && t.getNombreCliente() != null) {
                    System.out.println("Ticket: " + t.getId());
                    // Contamos cuántos problemas son de tipo 'Soporte'
                    if (t.getTipoProblema() != null && t.getTipoProblema().contains("Soporte")) {
                        contadorSoporte++;
                    }
                }
            }
            System.out.println("Soporte Total: " + contadorSoporte);
        }

        // se envian los datos al html
        // Usamos asegurarLista() para todo. Así si algo es null, se envía una lista vacía []
        // 'Model' es el objeto que transporta los datos desde Java hacia el HTML.
        model.addAttribute("tickets", asegurarLista(tickets));
        model.addAttribute("agentes", asegurarLista(service.getAgentes()));
        model.addAttribute("auditoria", asegurarLista(service.getAuditoria()));
        model.addAttribute("archivo", asegurarLista(service.getArchivo()));

        // Inicializamos la variable 'busqueda' vacía para evitar errores en el input de búsqueda
        model.addAttribute("busqueda", "");

        // Enviamos la lista de clientes ya limpia de nulos
        model.addAttribute("listaClientes", validarClientes(service.getTodosLosClientes()));

        // Retorna el archivo "index.html"
        return "index";
    }

    /**
     * Ruta: "/clientes"
     * Pantalla de clientes
     */
    @GetMapping("/clientes")
    public String verClientes(Model model) {
        model.addAttribute("listaClientes", validarClientes(service.getTodosLosClientes()));
        return "clientes";
    }

    /**
     * Ruta: "/ajustes"
     * Muestra la pantalla de configuración del sistema.
     */
    @GetMapping("/ajustes")
    public String verAjustes(Model model) { return "ajustes"; }

    // -------------------------------------------------------------------------
    // ACCIONES DE TICKETS (POST MAPPINGS)
    // -------------------------------------------------------------------------

    /**
     * Crea un ticket manualmente desde el formulario del dashboard.
     * Recibe los parámetros del form y llama al servicio.
     */
    @PostMapping("/ticket/nuevo")
    public String nuevoTicket(@RequestParam String cliente, @RequestParam String asunto, @RequestParam(required = false) String email, @RequestParam(required = false) String telefono) {
        service.agregarTicketManual(cliente, asunto, email, telefono);
        return "redirect:/"; // Recarga la página principal
    }

    /**
     * Genera un ticket aleatorio (simulación).
     */
    @PostMapping("/ticket/auto")
    public String ticketAuto() { service.generarTicketAutomatico(); return "redirect:/"; }

    /**
     * Intenta resolver el siguiente ticket en la cola (FIFO o Prioridad según servicio).
     * Si no hay agentes o tickets, captura el mensaje de error y lo envía a la vista.
     */
    @PostMapping("/ticket/resolver")
    public String resolver(RedirectAttributes redirectAttributes) {
        String resultado = service.resolverTicket();
        // Si el servicio devuelve un String que empieza con "ERROR", lo mostramos al usuario
        if (resultado.startsWith("ERROR")) redirectAttributes.addFlashAttribute("errorAgentes", resultado);
        return "redirect:/";
    }


    // GESTIÓN DE CLIENTES (CRUD)


    /**
     * Registra un nuevo cliente en la base de datos/lista.
     */
    @PostMapping("/cliente/crear")
    public String crearClienteDb(@RequestParam String nombre, @RequestParam(required = false) String email, @RequestParam(required = false) String telefono) {
        service.registrarClienteNuevo(nombre, email, telefono);
        return "redirect:/clientes";
    }

    /**
     * Elimina un cliente basándose en su ID único.
     * Los id empiezan desde el 1000
     */
    @PostMapping("/cliente/eliminar")
    public String eliminarCliente(@RequestParam long id) { service.eliminarClientePorId(id); return "redirect:/clientes"; }

    /**
     * Carga el formulario de edición para un cliente específico.
     * Busca al cliente por ID y lo pone en el modelo para rellenar los inputs.
     */
    @GetMapping("/cliente/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable long id, Model model) {
        Cliente cliente = service.buscarClientePorId(id);
        if (cliente != null) { model.addAttribute("cliente", cliente); return "editar-cliente"; }
        return "redirect:/clientes";
    }

    /**
     * Procesa los cambios enviados desde el formulario de edición.
     * Actualiza los datos del objeto cliente encontrado.
     */
    @PostMapping("/cliente/actualizar")
    public String actualizarCliente(@RequestParam long id, @RequestParam String nombre, @RequestParam String empresa, @RequestParam String email, @RequestParam String telefono) {
        Cliente c = service.buscarClientePorId(id);
        // Solo actualiza si encuentra al cliente (protección contra IDs inválidos)
        if(c != null) { c.setNombre(nombre); c.setEmpresa(empresa); c.setEmail(email); c.setTelefono(telefono); }
        return "redirect:/clientes";
    }


    // LÓGICA DE BÚSQUEDA


    /**
     * Busca clientes por nombre (búsqueda lineal).
     * Recibe el texto 'query' desde la barra de búsqueda.
     * el cliente solo se puede buscar por nombre especifico
     */
    @PostMapping("/buscar")
    public String buscar(@RequestParam String query, Model model) {
        List<Cliente> todos = service.getTodosLosClientes();
        List<Cliente> encontrados = new ArrayList<>();

        // Algoritmo de búsqueda: Recorre toda la lista y compara nombres
        if (todos != null) {
            for (Cliente c : todos) {
                // Validación para evitar NullPointer si un cliente está corrupto
                if (c != null && c.getNombre() != null && query != null) {
                    // .contains() verifica si el nombre tiene el texto buscado (ignorando mayúsculas/minúsculas)
                    if (c.getNombre().toLowerCase().contains(query.toLowerCase())) {
                        encontrados.add(c);
                    }
                }
            }
        }

        // Si no se encuentra nada, muestra mensaje de error y devuelve la lista completa original
        if (encontrados.isEmpty()) {
            model.addAttribute("mensajeError", "no hay cliente existiendo bro");
            model.addAttribute("listaClientes", validarClientes(todos));
        } else {
            // Si encuentra coincidencias, muestra solo esos clientes
            model.addAttribute("listaClientes", encontrados);
        }

        return "clientes";
    }


    // OTROS METODOS (AGENTES, AUDITORÍA, AJUSTES)


    /**
     * Contrata un nuevo operador (aumenta capacidad de atención).
     * podemos agregar
     */
    @PostMapping("/agente/nuevo")
    public String nuevoAgente(@RequestParam String nombre) { service.contratarAgente(nombre); return "redirect:/"; }

    /**
     * Despide a un operador por su índice en la lista (pila/cola de agentes).
     */
    @PostMapping("/agente/baja")
    public String bajaAgente(@RequestParam int indice) { service.despedirAgente(indice); return "redirect:/"; }

    /**
     * Elimina el último registro de la auditoría (pila de historial).
     */
    @PostMapping("/auditoria/borrar")
    public String borrarAuditoria() { service.limpiarUltimaAuditoria(); return "redirect:/"; }

    /**
     * Borra un dato específico del archivo histórico.
     */
    @PostMapping("/archivo/borrar")
    public String borrarArchivo(@RequestParam String dato) { service.borrarDeArchivo(dato); return "redirect:/"; }

    /**
     * Reinicio total del sistema (borra colas, pilas y reinicia contadores).
     */
    @PostMapping("/ajustes/reset")
    public String resetearTodo() { service.reiniciarSistema(); return "redirect:/"; }
}