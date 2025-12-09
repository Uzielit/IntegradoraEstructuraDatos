package com.IntegradoraED.EstructurasDatos;
import java.util.ArrayList;
import java.util.List;






public class ArbolBinario<T extends Comparable<T>> {

    // El nodo principal desde donde empieza todo el árbol.
    private NodoArbol<T> raiz;

    /**
     * Clase interna NodoArbol
     * Representa cada "caja" o elemento dentro del árbol.
     * Guarda el dato y dos flechas (punteros): una a la izquierda y otra a la derecha.
     */
    private class NodoArbol<T> {
        T dato;
        NodoArbol<T> izquierda, derecha;
        public NodoArbol(T dato) { this.dato = dato; }
    }

    // -------------------------------------------------------------------------
    // --- RECURSIVIDAD 1: INSERCIÓN ---
    // -------------------------------------------------------------------------

    /**
     * Método público que inicia el proceso de guardar un dato nuevo.
     */
    public void insertar(T dato) { raiz = insertarRecursivo(raiz, dato); }

    /**
     * Método recursivo para encontrar el lugar correcto del nuevo dato.
     * Lógica:
     * 1. Si el espacio está vacío, se crea el nodo aquí.
     * 2. Si el dato es MENOR que el actual, se manda a la IZQUIERDA.
     * 3. Si el dato es MAYOR que el actual, se manda a la DERECHA.
     */
    private NodoArbol<T> insertarRecursivo(NodoArbol<T> actual, T dato) {
        if (actual == null) return new NodoArbol<>(dato); // Caso base: encontramos un hueco

        // Compara los datos para decidir el camino
        if (dato.compareTo(actual.dato) < 0)
            actual.izquierda = insertarRecursivo(actual.izquierda, dato);
        else if (dato.compareTo(actual.dato) > 0)
            actual.derecha = insertarRecursivo(actual.derecha, dato);

        return actual;
    }

    // -------------------------------------------------------------------------
    // --- RECURSIVIDAD 2: BÚSQUEDA ---
    // -------------------------------------------------------------------------

    /**
     * Usada cuando das click en la lupa de búsqueda en el HTML.
     * Retorna 'true' si encuentra algo parecido al dato.
     */
    public boolean buscar(T dato) { return buscarRecursivo(raiz, dato); }

    /**
     * Busca recursivamente en todo el árbol.
     * Nota: Usa .contains() para permitir búsquedas parciales (ej: buscar "Juan" encuentra "Juan Perez").
     */
    private boolean buscarRecursivo(NodoArbol<T> actual, T dato) {
        if (actual == null) return false; // Llegamos al final y no encontramos nada

        // Si el dato del nodo contiene el texto que buscamos, retornamos true
        if (actual.dato.toString().contains(dato.toString())) return true;

        // Llamada recursiva: Busca tanto en la izquierda como en la derecha
        // El operador || significa que si lo encuentra en cualquier lado, es válido.
        return buscarRecursivo(actual.izquierda, dato) || buscarRecursivo(actual.derecha, dato);
    }

    // -------------------------------------------------------------------------
    // --- RECURSIVIDAD 3: ELIMINACIÓN ---
    // -------------------------------------------------------------------------

    /**
     * Método público para borrar un elemento del árbol.
     */
    public void eliminar(T dato) { raiz = eliminarRecursivo(raiz, dato); }

    /**
     * El método más complejo. Elimina un nodo y reestructura el árbol para que no se rompa.
     * Maneja 3 casos:
     * 1. El nodo no tiene hijos (es hoja): Se borra simple.
     * 2. El nodo tiene 1 hijo: El hijo sube a ocupar su lugar.
     * 3. El nodo tiene 2 hijos: Busca el "heredero" (el menor del lado derecho) para reemplazarlo.
     */
    private NodoArbol<T> eliminarRecursivo(NodoArbol<T> actual, T dato) {
        if (actual == null) return null;

        // Primero buscamos el nodo a eliminar navegando el árbol
        if (dato.compareTo(actual.dato) < 0) actual.izquierda = eliminarRecursivo(actual.izquierda, dato);
        else if (dato.compareTo(actual.dato) > 0) actual.derecha = eliminarRecursivo(actual.derecha, dato);
        else {
            // ¡Lo encontramos! Ahora a eliminarlo:

            // Caso 1 y 2: Solo un hijo o ninguno
            if (actual.izquierda == null) return actual.derecha;
            if (actual.derecha == null) return actual.izquierda;

            // Caso 3: Dos hijos. Buscamos el sucesor (el valor más pequeño de la rama derecha)
            actual.dato = encontrarMinimo(actual.derecha);
            // Eliminamos el duplicado del sucesor en la rama derecha
            actual.derecha = eliminarRecursivo(actual.derecha, actual.dato);
        }
        return actual;
    }

    /**
     * Ayudante para la eliminación. Baja todo a la izquierda posible para encontrar el valor menor.
     */
    private T encontrarMinimo(NodoArbol<T> actual) {
        T minv = actual.dato;
        while (actual.izquierda != null) {
            minv = actual.izquierda.dato;
            actual = actual.izquierda;
        }
        return minv;
    }

    // -------------------------------------------------------------------------
    // --- RECURSIVIDAD 4: RECORRIDO (Convertir a Lista) ---
    // -------------------------------------------------------------------------

    /**
     * Convierte la estructura de Árbol compleja en una Lista simple (List<T>)
     * para que pueda ser mostrada en la tabla del HTML fácilmente.
     */
    public List<T> obtenerListaOrdenada() {
        List<T> lista = new ArrayList<>();
        recorrerInOrden(raiz, lista);
        return lista;
    }

    /**
     * Recorrido In-Order (Izquierda -> Raíz -> Derecha).
     * Esto garantiza que la lista resultante salga ordenada alfabética o numéricamente.
     */
    private void recorrerInOrden(NodoArbol<T> nodo, List<T> lista) {
        if (nodo != null) {
            // 1. Visitar hijo izquierdo
            recorrerInOrden(nodo.izquierda, lista);
            // 2. Guardar el dato actual
            lista.add(nodo.dato);
            // 3. Visitar hijo derecho
            recorrerInOrden(nodo.derecha, lista);
        }
    }

    // Verifica si el árbol no tiene datos
    public boolean estaVacio() { return raiz == null; }
}