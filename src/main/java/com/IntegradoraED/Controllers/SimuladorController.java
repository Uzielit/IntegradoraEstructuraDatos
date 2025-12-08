package com.IntegradoraED.Controllers;

import com.IntegradoraED.Model.Cliente;
import com.IntegradoraED.Service.AtencionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SimuladorController {

    @Autowired
    private AtencionService service;
    private String resultadoBusqueda = "";

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("tickets", service.getTicketsPendientes());
        model.addAttribute("agentes", service.getAgentes());
        model.addAttribute("auditoria", service.getAuditoria());
        model.addAttribute("archivo", service.getArchivo());
        model.addAttribute("listaClientes", service.getTodosLosClientes()); // Clientes en Dashboard
        model.addAttribute("busqueda", resultadoBusqueda);
        resultadoBusqueda = "";
        return "index";
    }

    @GetMapping("/clientes")
    public String verClientes(Model model) {
        model.addAttribute("listaClientes", service.getTodosLosClientes());
        return "clientes";
    }

    @GetMapping("/ajustes")
    public String verAjustes(Model model) {
        return "ajustes";
    }

    // --- ACCIÓN MODIFICADA: Ticket con datos de cliente ---
    @PostMapping("/ticket/nuevo")
    public String nuevoTicket(@RequestParam String cliente,
                              @RequestParam String asunto,
                              @RequestParam(required = false) String email,
                              @RequestParam(required = false) String telefono) {
        service.agregarTicketManual(cliente, asunto, email, telefono);
        return "redirect:/";
    }

    @PostMapping("/ticket/auto")
    public String ticketAuto() {
        service.generarTicketAutomatico();
        return "redirect:/";
    }

    @PostMapping("/ticket/resolver")
    public String resolver() {
        service.resolverTicket();
        return "redirect:/";
    }

    @PostMapping("/cliente/crear")
    public String crearClienteDb(@RequestParam String nombre) {
        service.registrarClienteNuevo(nombre);
        return "redirect:/";
    }

    @PostMapping("/cliente/eliminar")
    public String eliminarCliente(@RequestParam long id) {
        service.eliminarClientePorId(id);
        return "redirect:/";
    }

    @GetMapping("/cliente/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable long id, Model model) {
        Cliente cliente = service.buscarClientePorId(id);
        if (cliente != null) {
            model.addAttribute("cliente", cliente);
            return "editar-cliente";
        }
        return "redirect:/";
    }

    @PostMapping("/cliente/actualizar")
    public String actualizarCliente(@RequestParam long id, @RequestParam String nombre, @RequestParam String empresa, @RequestParam String email, @RequestParam String telefono) {
        Cliente c = service.buscarClientePorId(id);
        if(c != null) {
            c.setNombre(nombre); c.setEmpresa(empresa); c.setEmail(email); c.setTelefono(telefono);
        }
        return "redirect:/";
    }

    @PostMapping("/buscar")
    public String buscar(@RequestParam String query) {
        resultadoBusqueda = service.buscarEnSistema(query);
        return "redirect:/";
    }

    @PostMapping("/agente/nuevo")
    public String nuevoAgente(@RequestParam String nombre) { service.contratarAgente(nombre); return "redirect:/"; }

    @PostMapping("/agente/baja")
    public String bajaAgente(@RequestParam int indice) { service.despedirAgente(indice); return "redirect:/"; }

    @PostMapping("/auditoria/borrar")
    public String borrarAuditoria() { service.limpiarUltimaAuditoria(); return "redirect:/"; }

    @PostMapping("/archivo/borrar")
    public String borrarArchivo(@RequestParam String dato) { service.borrarDeArchivo(dato); return "redirect:/"; }

    @PostMapping("/ajustes/reset")
    public String resetearTodo() { service.reiniciarSistema(); return "redirect:/"; }
}