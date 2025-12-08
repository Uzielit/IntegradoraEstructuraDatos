package com.IntegradoraED.EstructurasDatos;
import java.util.ArrayList;
import java.util.List;

public class Colas<T> {
    private Nodo<T> enfrente;
    private Nodo<T> finalDeLaFila;
    private int cuantosHay;

    public Colas() {
        this.enfrente = null;
        this.finalDeLaFila = null;
        this.cuantosHay = 0;
    }

    public void formar(T valor) {
        Nodo<T> nuevoNodo = new Nodo<>(valor);
        if (estaVacia()) {
            enfrente = nuevoNodo;
            finalDeLaFila = nuevoNodo;
        } else {
            finalDeLaFila.setProximo(nuevoNodo);
            finalDeLaFila = nuevoNodo;
        }
        cuantosHay++;
    }

    public T atender() {
        if (estaVacia()) return null;
        T valorAtendido = enfrente.getValor();
        enfrente = enfrente.getProximo();
        if (enfrente == null) finalDeLaFila = null;
        cuantosHay--;
        return valorAtendido;
    }

    // --- NUEVO: BUSCAR ---
    public boolean existe(String criterio) {
        Nodo<T> actual = enfrente;
        while (actual != null) {
            // Convertimos a string para buscar por nombre o ID
            if (actual.getValor().toString().contains(criterio)) {
                return true;
            }
            actual = actual.getProximo();
        }
        return false;
    }

    public boolean estaVacia() { return cuantosHay == 0; }

    public List<T> conectorHtml() {
        List<T> listaParaVista = new ArrayList<>();
        Nodo<T> actual = enfrente;
        while (actual != null) {
            listaParaVista.add(actual.getValor());
            actual = actual.getProximo();
        }
        return listaParaVista;
    }
}