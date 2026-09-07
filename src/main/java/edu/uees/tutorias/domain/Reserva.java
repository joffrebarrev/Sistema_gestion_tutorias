package edu.uees.tutorias.domain;

import edu.uees.tutorias.domain.observer.ObservadorReserva;

import java.util.ArrayList;
import java.util.List;
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
 *
 * <p><b>Incremento 1 (Ae3) - patron Observer:</b> Reserva actua como
 * <i>Subject</i>. Mantiene una lista de {@link ObservadorReserva} y les
 * avisa despues de cada cambio de estado valido. Antes, era
 * {@code ServicioReservasImpl} quien decidia "a mano" a quien avisar
 * (estudiante y docente, siempre los mismos dos, siempre del mismo modo).
 * Ahora esa responsabilidad se invierte: Reserva no sabe quien la escucha
 * ni como reacciona (solo conoce la abstraccion ObservadorReserva), lo que
 * mantiene la coherencia con el resto del dominio (que ya protegia sus
 * propias reglas) y aplica OCP: sumar un interesado nuevo no modifica esta
 * clase.</p>
 */
public class Reserva {

    private final String id;
    private final Estudiante estudiante;
    private HorarioTutoria horario;
    private EstadoReserva estado;
    private String motivoCancelacion;
    private final List<ObservadorReserva> observadores = new ArrayList<>();

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

    /**
     * Registra un observador que sera notificado en cada cambio de estado
     * futuro de esta reserva. Quien ensambla el caso de uso (por ejemplo
     * {@code ServicioReservasImpl}) decide cuantos y cuales observadores
     * registrar; Reserva solo conoce la abstraccion.
     */
    public void agregarObservador(ObservadorReserva observador) {
        observadores.add(Objects.requireNonNull(observador, "observador no puede ser nulo"));
    }

    /**
     * Notifica explicitamente el estado actual a los observadores
     * registrados, con {@code estadoAnterior = null}. Se usa una sola vez,
     * justo despues de crear la reserva y registrar sus observadores, para
     * avisar del estado inicial PENDIENTE sin necesidad de una transicion
     * previa.
     */
    public void notificarCreacion() {
        notificarObservadores(null);
    }

    private void notificarObservadores(EstadoReserva estadoAnterior) {
        for (ObservadorReserva observador : observadores) {
            observador.actualizar(this, estadoAnterior);
        }
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
        EstadoReserva anterior = estado;
        estado = EstadoReserva.CONFIRMADA;
        notificarObservadores(anterior);
    }

    /** Cancela la reserva y libera el cupo del horario asociado. */
    public void cancelar(String motivo) {
        if (estado == EstadoReserva.CANCELADA || estado == EstadoReserva.COMPLETADA) {
            throw new IllegalStateException(
                    "Una reserva " + estado + " no puede cancelarse");
        }
        EstadoReserva anterior = estado;
        horario.liberarCupo();
        estado = EstadoReserva.CANCELADA;
        this.motivoCancelacion = motivo;
        notificarObservadores(anterior);
    }

    /** Marca la tutoria como realizada. Solo aplica sobre una reserva confirmada. */
    public void completar() {
        if (estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException(
                    "Solo una reserva CONFIRMADA puede completarse (estado actual: " + estado + ")");
        }
        EstadoReserva anterior = estado;
        estado = EstadoReserva.COMPLETADA;
        notificarObservadores(anterior);
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
        EstadoReserva anterior = estado;
        horario.liberarCupo();
        nuevoHorario.ocuparCupo();
        this.horario = nuevoHorario;
        this.estado = EstadoReserva.PENDIENTE;
        notificarObservadores(anterior);
    }

    @Override
    public String toString() {
        return "Reserva{id='" + id + "', estudiante=" + estudiante.getNombre()
                + ", horario=" + horario + ", estado=" + estado + "}";
    }
}
