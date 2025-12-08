package com.IntegradoraED.EstructurasDatos;
import java.util.ArrayList;
import java.util.List;

public class ArbolBinario<T extends Comparable<T>> {
    private NodoArbol<T> raiz;

    private class NodoArbol<T> {
        T dato;
        NodoArbol<T> izquierda, derecha;
        public NodoArbol(T dato) { this.dato = dato; }
    }

    public void insertar(T dato) { raiz = insertarRecursivo(raiz, dato); }

    private NodoArbol<T> insertarRecursivo(NodoArbol<T> actual, T dato) {
        if (actual == null) return new NodoArbol<>(dato);
        if (dato.compareTo(actual.dato) < 0) actual.izquierda = insertarRecursivo(actual.izquierda, dato);
        else if (dato.compareTo(actual.dato) > 0) actual.derecha = insertarRecursivo(actual.derecha, dato);
        return actual;
    }

    // --- NUEVO: BUSCAR ---
    public boolean buscar(T dato) { return buscarRecursivo(raiz, dato); }

    private boolean buscarRecursivo(NodoArbol<T> actual, T dato) {
        if (actual == null) return false;
        if (dato.compareTo(actual.dato) == 0) return true;
        return dato.compareTo(actual.dato) < 0
                ? buscarRecursivo(actual.izquierda, dato)
                : buscarRecursivo(actual.derecha, dato);
    }

    // --- NUEVO: ELIMINAR ---
    public void eliminar(T dato) { raiz = eliminarRecursivo(raiz, dato); }

    private NodoArbol<T> eliminarRecursivo(NodoArbol<T> actual, T dato) {
        if (actual == null) return null;
        if (dato.compareTo(actual.dato) < 0) actual.izquierda = eliminarRecursivo(actual.izquierda, dato);
        else if (dato.compareTo(actual.dato) > 0) actual.derecha = eliminarRecursivo(actual.derecha, dato);
        else {
            if (actual.izquierda == null) return actual.derecha;
            if (actual.derecha == null) return actual.izquierda;
            actual.dato = encontrarMinimo(actual.derecha);
            actual.derecha = eliminarRecursivo(actual.derecha, actual.dato);
        }
        return actual;
    }

    private T encontrarMinimo(NodoArbol<T> actual) {
        T minv = actual.dato;
        while (actual.izquierda != null) {
            minv = actual.izquierda.dato;
            actual = actual.izquierda;
        }
        return minv;
    }

    public List<T> obtenerListaOrdenada() {
        List<T> lista = new ArrayList<>();
        recorrerInOrden(raiz, lista);
        return lista;
    }

    private void recorrerInOrden(NodoArbol<T> nodo, List<T> lista) {
        if (nodo != null) {
            recorrerInOrden(nodo.izquierda, lista);
            lista.add(nodo.dato);
            recorrerInOrden(nodo.derecha, lista);
        }
    }
    public boolean estaVacio() { return raiz == null; }
}