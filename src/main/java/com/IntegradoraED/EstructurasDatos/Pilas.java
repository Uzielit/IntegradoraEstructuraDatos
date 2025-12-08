package com.IntegradoraED.EstructurasDatos;
import java.util.ArrayList;
import java.util.List;

public class Pilas<T> {
    private Nodo<T> elementoDeArriba;
    private int cuantosHay;

    public Pilas() {
        this.elementoDeArriba = null;
        this.cuantosHay = 0;
    }

    public void apilar(T valor) {
        Nodo<T> nuevoNodo = new Nodo<>(valor);
        if (!estaVacia()) nuevoNodo.setProximo(elementoDeArriba);
        elementoDeArriba = nuevoNodo;
        cuantosHay++;
    }

    public T desapilar() {
        if (estaVacia()) return null;
        T valorQuitado = elementoDeArriba.getValor();
        elementoDeArriba = elementoDeArriba.getProximo();
        cuantosHay--;
        return valorQuitado;
    }

    // --- NUEVO: BUSCAR ---
    public boolean existe(String criterio) {
        Nodo<T> actual = elementoDeArriba;
        while (actual != null) {
            if (actual.getValor().toString().contains(criterio)) return true;
            actual = actual.getProximo();
        }
        return false;
    }

    public boolean estaVacia() { return cuantosHay == 0; }

    public List<T> conectorHtml() {
        List<T> listaParaVista = new ArrayList<>();
        Nodo<T> actual = elementoDeArriba;
        while (actual != null) {
            listaParaVista.add(actual.getValor());
            actual = actual.getProximo();
        }
        return listaParaVista;
    }
}