package com.IntegradoraED.EstructurasDatos;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Colas
 * -------------------
 * Implementación de una estructura de datos lineal tipo FIFO (First In, First Out).
 * Primero en entrar primero en salir
 *
 * @param <T> Tipo genérico (puede ser Ticket, Llamada, Cliente, etc.)
 */

public class Colas<T> {

    // Puntero al inicio de la fila (el próximo a ser atendido)
    private Nodo<T> enfrente;

    // Puntero al final de la fila (donde se forman los nuevos)
    private Nodo<T> finalDeLaFila;

    // Contador para saber el tamaño de la cola rápidamente
    private int cuantosHay;

    /**
     * Constructor: Inicializa la cola vacía.
     */
    public Colas() {
        this.enfrente = null;
        this.finalDeLaFila = null;
        this.cuantosHay = 0;
    }

    /**
     * Formar
     * Agrega un nuevo elemento al FINAL de la fila.
     * @param valor El objeto a guardar.
     */
    public void formar(T valor) {
        Nodo<T> nuevoNodo = new Nodo<>(valor);

        if (estaVacia()) {
            // Si la fila está vacía, el nuevo es tanto el primero como el último
            enfrente = nuevoNodo;
            finalDeLaFila = nuevoNodo;
        } else {
            // Si ya hay gente, enlazamos el último actual con el nuevo
            finalDeLaFila.setProximo(nuevoNodo);
            // Y actualizamos el puntero 'final' al nuevo nodo
            finalDeLaFila = nuevoNodo;
        }
        cuantosHay++;
    }

    /**
     * Método que tebdmos cliente
     * Saca y devuelve el elemento que está al FRENTE de la fila.
     * @return El objeto atendido o null si está vacía.
     */
    public T atender() {
        if (estaVacia()) return null; // Validación de seguridad

        // Guardamos el valor antes de eliminar el nodo para poder retornarlo
        T valorAtendido = enfrente.getValor();

        // Movemos el puntero de frente al siguiente en la fila
        enfrente = enfrente.getProximo();

        // Caso especial: Si al atender al único que había la fila queda vacía,
        // también debemos limpiar el puntero 'final'
        if (enfrente == null) finalDeLaFila = null;

        cuantosHay--;
        return valorAtendido;
    }

    // --- NUEVO: BUSCAR ---
    /**
     * Recorre la cola sin modificarla para ver si un elemento existe.
     * Esto solo es para la barra de busqueda y solo funcioa con nombres
     */
    public boolean existe(String criterio) {
        Nodo<T> actual = enfrente;
        while (actual != null) {
            // Convertimos a string para buscar por nombre o ID usando .contains()
            if (actual.getValor().toString().contains(criterio)) {
                return true;
            }
            actual = actual.getProximo();
        }
        return false;
    }

    /**
     * verifica si esta esta vacia
     */
    public boolean estaVacia() { return cuantosHay == 0; }

    /**
     * Este es nuestro metodo para conectar con nuestro html
     * Aqui las estructuras dinámicas como la cola de Nodos, es dificil de leer para nuestro html entonces
     * utilizamos lass lista estándar de Java (ArrayList) solo para visualización.
     *
     */
    public List<T> conectorHtml() {
        List<T> listaParaVista = new ArrayList<>();
        Nodo<T> actual = enfrente;

        // Recorremos desde el frente hasta el final copiando los valores
        while (actual != null) {
            listaParaVista.add(actual.getValor());
            actual = actual.getProximo();
        }
        return listaParaVista;
    }
}