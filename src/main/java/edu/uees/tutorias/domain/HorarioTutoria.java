package edu.uees.tutorias.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Franja horaria que un {@link Docente} publica para una {@link Asignatura}.
 *
 * <p>Es composicion, no asociacion debil: un HorarioTutoria no tiene
 * sentido fuera del contexto de "docente que ofrece tutoria de una
 * asignatura en una fecha"; nace siempre a traves de
 * {@link Docente#publicarHorario}.</p>
 *
 * <p>La regla de negocio "un horario no puede aceptar mas reservas que su
 * capacidad" se protege aqui mismo, dentro del propio horario, y no en
 * {@code ServicioReservas}. Esto evita que la regla de disponibilidad se
 * duplique o se olvide en otro punto del sistema (alta cohesion: el objeto
 * que conoce su capacidad es el unico responsable de decidir si esta
 * disponible).</p>
 */
public class HorarioTutoria {

    private final String id;
    private final Docente docente;
    private final Asignatura asignatura;
    private final LocalDateTime inicio;
    private final LocalDateTime fin;
    private final int capacidad;
    private int cuposOcupados;

    HorarioTutoria(String id, Docente docente, Asignatura asignatura,
                    LocalDateTime inicio, LocalDateTime fin, int capacidad) {
        this.id = Objects.requireNonNull(id, "id no puede ser nulo");
        this.docente = Objects.requireNonNull(docente, "docente no puede ser nulo");
        this.asignatura = Objects.requireNonNull(asignatura, "asignatura no puede ser nula");
        if (inicio == null || fin == null || !fin.isAfter(inicio)) {
            throw new IllegalArgumentException("El rango horario es invalido");
        }
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero");
        }
        this.inicio = inicio;
        this.fin = fin;
        this.capacidad = capacidad;
        this.cuposOcupados = 0;
    }

    public String getId() {
        return id;
    }

    public Docente getDocente() {
        return docente;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public int getCuposOcupados() {
        return cuposOcupados;
    }

    public boolean estaDisponible() {
        return cuposOcupados < capacidad;
    }

    /**
     * Ocupa un cupo del horario. Solo debe invocarse cuando ya existe una
     * {@link Reserva} valida asociada; por eso el metodo es de paquete
     * (visibilidad restringida a {@code edu.uees.tutorias.domain}) y solo
     * {@link Reserva} lo utiliza, evitando que un cupo se ocupe sin una
     * reserva real detras (encapsulamiento del invariante).
     */
    void ocuparCupo() {
        if (!estaDisponible()) {
            throw new IllegalStateException("El horario " + id + " ya no tiene cupos disponibles");
        }
        cuposOcupados++;
    }

    /**
     * Libera un cupo, por ejemplo cuando una reserva asociada se cancela.
     */
    void liberarCupo() {
        if (cuposOcupados > 0) {
            cuposOcupados--;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HorarioTutoria that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return asignatura + " con " + docente.getNombre() + " [" + inicio + " - " + fin + "]";
    }
}
