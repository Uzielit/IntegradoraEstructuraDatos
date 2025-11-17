package com.IntegradoraED.EstructurasDatos;

import java.util.ArrayList;
import java.util.List;

public class Listas<T> {

    private Object[] elementosGuardados;
    private int cuantosHay;

    private static final int inico = 10;

    public Listas() {
        this.elementosGuardados = new Object[inico];
        this.cuantosHay = 0;
    }


    private void asegurarEspacio() {
        if (cuantosHay == elementosGuardados.length) {
            int nuevoEspacio = elementosGuardados.length * 2;
            Object[] estanteNuevo = new Object[nuevoEspacio];
            for (int i = 0; i < cuantosHay; i++) {
                estanteNuevo[i] = elementosGuardados[i];
            }
            elementosGuardados = estanteNuevo;
        }
    }

    // Operaciones Públicas

    public void agregar(T elemento) {
        asegurarEspacio();
        elementosGuardados[cuantosHay] = elemento;
        cuantosHay++;
    }

    public T obtener(int posicion) {
        if (posicion < 0 || posicion >= cuantosHay) {
            throw new IndexOutOfBoundsException("No valido :  " + posicion);
        }
        return (T) elementosGuardados[posicion];
    }

    public int buscarElemento(T elemento) {
        for(int i = 0; i < cuantosHay; i++) {
            if(elementosGuardados[i].equals(elemento)) {
                return i; // Encontrado en la posición i
            }
        }
        return -1;
    }

    public T eliminarElemento(int posicion) {
        if (posicion < 0 || posicion >= cuantosHay) {
            throw new IndexOutOfBoundsException("No valido : " + posicion);
        }
        T elementoEliminado = (T) elementosGuardados[posicion];

        // Movemos todos los de la derecha una posición a la izquierda
        for (int i = posicion; i < cuantosHay - 1; i++) {
            elementosGuardados[i] = elementosGuardados[i + 1];
        }
        cuantosHay--;
        elementosGuardados[cuantosHay] = null;
        return elementoEliminado;
    }

    public int getCuantosHay() {
        return cuantosHay;
    }

    public boolean estaVacia() {
        return cuantosHay == 0;
    }

    // CONECTOR PARA EL HTML:
    public List<T> conectorHtml() {
        List<T> listaParaVista = new ArrayList<>();
        for (int i = 0; i < cuantosHay; i++) {
            listaParaVista.add((T) elementosGuardados[i]);
        }
        return listaParaVista;
    }
}
