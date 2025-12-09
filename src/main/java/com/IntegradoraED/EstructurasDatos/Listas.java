package com.IntegradoraED.EstructurasDatos;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Listas (Arreglo Dinámico)
 * -------------------------------
 * Implementación personalizada de una lista que crece automáticamente.
 * A diferencia de un arreglo normal (ej: int[5]) que tiene tamaño fijo,
 * esta estructura detecta cuando se llena y duplica su capacidad.
 *
 * @param <T> Tipo genérico (puede guardar cualquier objeto).
 */

public class Listas<T> {

    // El "contenedor" real. Es un arreglo de Objetos.
    private Object[] elementosGuardados;

    // Contador de cuántos elementos REALES ha guardado el usuario.
    // Diferente a elementosGuardados.length (que es la capacidad total).
    private int cuantosHay;

    // Capacidad inicial del arreglo (empieza con hueco para 10 cosas).
    private static final int inico = 10;

    public Listas() {
        this.elementosGuardados = new Object[inico];
        this.cuantosHay = 0;
    }

    /**
     * MÉTODO CLAVE: Redimensionamiento (Resizing)
     * Verifica si el arreglo está lleno. Si lo está:
     * 1. Calcula el doble de tamaño.
     * 2. Crea un arreglo nuevo más grande.
     * 3. Copia todos los datos del viejo al nuevo.
     * 4. Reemplaza la referencia.
     */
    private void asegurarEspacio() {
        if (cuantosHay == elementosGuardados.length) {
            int nuevoEspacio = elementosGuardados.length * 2;
            Object[] estanteNuevo = new Object[nuevoEspacio];
            // Copia manual de elementos
            for (int i = 0; i < cuantosHay; i++) {
                estanteNuevo[i] = elementosGuardados[i];
            }
            elementosGuardados = estanteNuevo;
        }
    }

    // Operaciones Públicas

    /**
     * Agrega un elemento al final de la lista.
     * Antes de guardar, siempre verifica si hay espacio.
     */
    public void agregar(T elemento) {
        asegurarEspacio();
        elementosGuardados[cuantosHay] = elemento;
        cuantosHay++;
    }

    /**
     * Obtiene el elemento en una posición específica (acceso directo).
     * Valida que la posición solicitada exista.
     */
    public T obtener(int posicion) {
        if (posicion < 0 || posicion >= cuantosHay) {
            throw new IndexOutOfBoundsException("No valido :  " + posicion);
        }
        // Casteo necesario porque el arreglo guarda 'Object' pero retornamos 'T'
        return (T) elementosGuardados[posicion];
    }

    /**
     * Búsqueda lineal: Recorre el arreglo uno por uno para ver si encuentra el objeto.
     * @return El índice donde está el objeto, o -1 si no existe.
     */
    public int buscarElemento(T elemento) {
        for(int i = 0; i < cuantosHay; i++) {
            if(elementosGuardados[i].equals(elemento)) {
                return i; // Encontrado en la posición i
            }
        }
        return -1;
    }

    /**
     * Elimina un elemento por su índice.
     * Este método es costoso porque requiere "Desplazamiento" (Shifting):
     * Si borras el elemento 0, el 1 debe moverse al 0, el 2 al 1, etc.
     */
    public T eliminarElemento(int posicion) {
        if (posicion < 0 || posicion >= cuantosHay) {
            throw new IndexOutOfBoundsException("No valido : " + posicion);
        }
        T elementoEliminado = (T) elementosGuardados[posicion];

        // Movemos todos los de la derecha una posición a la izquierda para tapar el hueco
        for (int i = posicion; i < cuantosHay - 1; i++) {
            elementosGuardados[i] = elementosGuardados[i + 1];
        }
        cuantosHay--; // Reducimos el contador
        elementosGuardados[cuantosHay] = null; // Limpiamos la referencia sobrante al final
        return elementoEliminado;
    }

    public int getCuantosHay() {
        return cuantosHay;
    }

    public boolean estaVacia() {
        return cuantosHay == 0;
    }

    // CONECTOR PARA EL HTML:
    /**
     * Convierte esta estructura personalizada en una List de Java estándar.
     * Necesario para que Thymeleaf pueda iterar los datos en el frontend.
     */
    public List<T> conectorHtml() {
        List<T> listaParaVista = new ArrayList<>();
        for (int i = 0; i < cuantosHay; i++) {
            listaParaVista.add((T) elementosGuardados[i]);
        }
        return listaParaVista;
    }
}