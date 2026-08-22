package edu.uees.tutorias.domain;

import java.util.Objects;

/**
 * Materia sobre la que se ofrece tutoria (por ejemplo "Algoritmos" o
 * "Base de Datos"). Es un objeto de valor simple: se identifica por su
 * codigo y no tiene comportamiento propio mas alla de exponer sus datos.
 */
public class Asignatura {

    private final String codigo;
    private final String nombre;

    public Asignatura(String codigo, String nombre) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El codigo de la asignatura no puede estar vacio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la asignatura no puede estar vacio");
        }
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Asignatura that)) return false;
        return codigo.equals(that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return nombre + " (" + codigo + ")";
    }
}
