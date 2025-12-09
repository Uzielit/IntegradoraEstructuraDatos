package com.IntegradoraED.EstructurasDatos;
import java.util.ArrayList;
import java.util.List;


 //@param <T> Tipo genérico (puede guardar Strings, Auditorias, Agentes, etc.)


public class Pilas<T> {

    // Puntero que siempre mira al elemento de la "cima" (el último agregado)
    private Nodo<T> elementoDeArriba;

    // Contador para saber el tamaño de la pila rápidamente
    private int cuantosHay;

    /**
     * Constructor: Inicializa la pila vacía.
     */
    public Pilas() {
        this.elementoDeArriba = null;
        this.cuantosHay = 0;
    }

    /**
     * Método Push (Apilar)
     * Coloca un nuevo elemento ENCIMA de todos los demás.
     * Operación rápida O(1) porque no necesita recorrer nada, solo pone encima.
     */
    public void apilar(T valor) {
        Nodo<T> nuevoNodo = new Nodo<>(valor);

        // Si ya había elementos, el nuevo nodo debe "pisar" o apuntar al antiguo de arriba
        if (!estaVacia()) nuevoNodo.setProximo(elementoDeArriba);

        // Ahora el "elemento de arriba" es oficialmente el nuevo nodo
        elementoDeArriba = nuevoNodo;
        cuantosHay++;
    }

    /**
     * Método Pop (Desapilar)
     * Quita y devuelve el elemento que está hasta ARRIBA (Cima).
     * @return El objeto quitado o null si está vacía.
     */
    public T desapilar() {
        if (estaVacia()) return null; // Seguridad

        // Guardamos el valor de la cima para retornarlo después
        T valorQuitado = elementoDeArriba.getValor();

        // El puntero baja un nivel: ahora la cima es el que estaba debajo
        elementoDeArriba = elementoDeArriba.getProximo();

        cuantosHay--;
        return valorQuitado;
    }

    // --- NUEVO: BUSCAR ---
    /**
     * Método Peek/Search (Buscar sin borrar)
     * Recorre la pila desde arriba hacia abajo buscando coincidencias de texto.
     */
    public boolean existe(String criterio) {
        Nodo<T> actual = elementoDeArriba;
        // Recorrido lineal
        while (actual != null) {
            // Usamos .toString() y .contains() para búsqueda flexible
            if (actual.getValor().toString().contains(criterio)) return true;
            actual = actual.getProximo(); // Bajar al siguiente nivel
        }
        return false;
    }

    // Verifica si el contador es 0
    public boolean estaVacia() { return cuantosHay == 0; }

    /**
     * Método conector para el HTML.
     * Convierte la Pila de Nodos en una Lista simple para mostrarla en pantalla.
     * El orden será: Índice 0 = El de más arriba (el más reciente).
     */
    public List<T> conectorHtml() {
        List<T> listaParaVista = new ArrayList<>();
        Nodo<T> actual = elementoDeArriba;

        // Recorre de arriba a abajo copiando los valores
        while (actual != null) {
            listaParaVista.add(actual.getValor());
            actual = actual.getProximo();
        }
        return listaParaVista;
    }
}