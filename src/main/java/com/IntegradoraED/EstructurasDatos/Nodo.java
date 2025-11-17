package com.IntegradoraED.EstructurasDatos;

public class Nodo<T> {

    private T valor;

    private Nodo<T> proximo;

    public Nodo(T valor) {
        this.valor = valor;
        this.proximo = null;
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    public Nodo<T> getProximo() {
        return proximo;
    }

    public void setProximo(Nodo<T> proximo) {
        this.proximo = proximo;
    }
}
