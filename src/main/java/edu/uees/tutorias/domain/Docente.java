package edu.uees.tutorias.domain;

import java.time.LocalDateTime;

/**
 * Docente que publica y administra horarios disponibles para tutoria.
 *
 * <p>Docente es responsable de crear {@link HorarioTutoria} validos (fecha
 * futura, asignatura no nula, capacidad positiva). Esta responsabilidad se
 * deja aqui -y no en un HorarioTutoria construido "a mano" desde cualquier
 * parte del sistema- para que la unica forma de publicar un horario sea a
 * traves del docente dueno de ese horario (alta cohesion: la clase agrupa
 * el comportamiento relacionado con "publicar disponibilidad").</p>
 */
public class Docente extends Usuario {

    private final String departamento;

    public Docente(String id, String nombre, String email, String departamento) {
        super(id, nombre, email);
        if (departamento == null || departamento.isBlank()) {
            throw new IllegalArgumentException("El departamento del docente no puede estar vacio");
        }
        this.departamento = departamento;
    }

    public String getDepartamento() {
        return departamento;
    }

    /**
     * Publica un nuevo horario disponible para tutoria.
     */
    public HorarioTutoria publicarHorario(String idHorario, Asignatura asignatura,
                                           LocalDateTime inicio, LocalDateTime fin,
                                           int capacidad) {
        return new HorarioTutoria(idHorario, this, asignatura, inicio, fin, capacidad);
    }

    @Override
    public String getRol() {
        return "Docente";
    }
}
