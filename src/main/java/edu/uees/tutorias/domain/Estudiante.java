package edu.uees.tutorias.domain;

/**
 * Estudiante que puede solicitar tutorias.
 *
 * <p>Herencia valida: un Estudiante ES-UN Usuario y no sobrescribe ni
 * debilita ningun comportamiento heredado (respeta LSP: en cualquier
 * lugar donde se espera un Usuario, un Estudiante puede sustituirlo sin
 * romper el comportamiento, por ejemplo dentro de {@code Notificador}).</p>
 */
public class Estudiante extends Usuario {

    private final String carrera;

    public Estudiante(String id, String nombre, String email, String carrera) {
        super(id, nombre, email);
        if (carrera == null || carrera.isBlank()) {
            throw new IllegalArgumentException("La carrera del estudiante no puede estar vacia");
        }
        this.carrera = carrera;
    }

    public String getCarrera() {
        return carrera;
    }

    @Override
    public String getRol() {
        return "Estudiante";
    }
}
