package edu.uees.tutorias.domain;

import java.util.Objects;

/**
 * Registra el encuentro entre un {@link Estudiante} y un
 * {@link HorarioTutoria}, junto con su estado a lo largo del tiempo.
 *
 * <p>Esta es la clase con mayor responsabilidad de negocio del dominio:
 * protege ella misma las transiciones validas de estado (confirmar,
 * cancelar, completar, reprogramar). Se decidio concentrar aqui la
 * maquina de estados -en lugar de dejar que {@code ServicioReservas}
 * decida "a mano" si un cambio de estado es valido- para mantener alta
 * cohesion: todo lo que necesita conocerse para saber si una reserva
 * puede cambiar de estado vive dentro de la propia Reserva.</p>
 */
public class Reserva {

    private final String id;
    private final Estudiante estudiante;
    private HorarioTutoria horario;
    private EstadoReserva estado;
    private String motivoCancelacion;

    public Reserva(String id, Estudiante estudiante, HorarioTutoria horario) {
        this.id = Objects.requireNonNull(id, "id no puede ser nulo");
        this.estudiante = Objects.requireNonNull(estudiante, "estudiante no puede ser nulo");
        this.horario = Objects.requireNonNull(horario, "horario no puede ser nulo");
        if (!horario.estaDisponible()) {
            throw new IllegalStateException("El horario " + horario.getId() + " no tiene cupos disponibles");
        }
        horario.ocuparCupo();
        this.estado = EstadoReserva.PENDIENTE;
    }

    public String getId() {
        return id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public HorarioTutoria getHorario() {
        return horario;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    /** Confirma una reserva pendiente. */
    public void confirmar() {
        if (estado != EstadoReserva.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo una reserva PENDIENTE puede confirmarse (estado actual: " + estado + ")");
        }
        estado = EstadoReserva.CONFIRMADA;
    }

    /** Cancela la reserva y libera el cupo del horario asociado. */
    public void cancelar(String motivo) {
        if (estado == EstadoReserva.CANCELADA || estado == EstadoReserva.COMPLETADA) {
            throw new IllegalStateException(
                    "Una reserva " + estado + " no puede cancelarse");
        }
        horario.liberarCupo();
        estado = EstadoReserva.CANCELADA;
        this.motivoCancelacion = motivo;
    }

    /** Marca la tutoria como realizada. Solo aplica sobre una reserva confirmada. */
    public void completar() {
        if (estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException(
                    "Solo una reserva CONFIRMADA puede completarse (estado actual: " + estado + ")");
        }
        estado = EstadoReserva.COMPLETADA;
    }

    /**
     * Reprograma la reserva a un nuevo horario: libera el cupo anterior,
     * ocupa el nuevo y vuelve a dejar la reserva en estado PENDIENTE
     * (debe confirmarse nuevamente).
     */
    public void reprogramar(HorarioTutoria nuevoHorario) {
        if (estado == EstadoReserva.CANCELADA || estado == EstadoReserva.COMPLETADA) {
            throw new IllegalStateException(
                    "Una reserva " + estado + " no puede reprogramarse");
        }
        Objects.requireNonNull(nuevoHorario, "nuevoHorario no puede ser nulo");
        if (!nuevoHorario.estaDisponible()) {
            throw new IllegalStateException("El horario " + nuevoHorario.getId() + " no tiene cupos disponibles");
        }
        horario.liberarCupo();
        nuevoHorario.ocuparCupo();
        this.horario = nuevoHorario;
        this.estado = EstadoReserva.PENDIENTE;
    }

    @Override
    public String toString() {
        return "Reserva{id='" + id + "', estudiante=" + estudiante.getNombre()
                + ", horario=" + horario + ", estado=" + estado + "}";
    }
}
