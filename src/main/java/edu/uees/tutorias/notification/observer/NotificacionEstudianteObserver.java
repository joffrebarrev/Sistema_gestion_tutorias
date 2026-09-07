package edu.uees.tutorias.notification.observer;

import edu.uees.tutorias.domain.EstadoReserva;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.observer.ObservadorReserva;
import edu.uees.tutorias.notification.Notificador;

import java.util.Objects;

/**
 * Observador concreto que avisa al {@link Reserva#getEstudiante() estudiante}
 * de la reserva cada vez que esta cambia de estado.
 *
 * <p>Reemplaza las llamadas que antes hacia {@code ServicioReservasImpl}
 * directamente a {@code notificador.notificar(estudiante, ...)} despues de
 * cada operacion. La logica de "que mensaje redactar para el estudiante
 * segun el estado" ahora vive en un solo lugar (aqui), no repartida en
 * cuatro metodos del servicio.</p>
 */
public class NotificacionEstudianteObserver implements ObservadorReserva {

    private final Notificador notificador;

    public NotificacionEstudianteObserver(Notificador notificador) {
        this.notificador = Objects.requireNonNull(notificador, "notificador no puede ser nulo");
    }

    @Override
    public void actualizar(Reserva reserva, EstadoReserva estadoAnterior) {
        boolean esReprogramacion = reserva.getEstado() == EstadoReserva.PENDIENTE && estadoAnterior != null;
        String mensaje;
        if (esReprogramacion) {
            mensaje = "Tu reserva fue reprogramada a " + reserva.getHorario() + ". Debe confirmarse nuevamente.";
        } else {
            mensaje = switch (reserva.getEstado()) {
                case PENDIENTE -> "Tu solicitud de tutoria de " + nombreAsignatura(reserva)
                        + " con " + nombreDocente(reserva) + " quedo PENDIENTE de confirmacion.";
                case CONFIRMADA -> "Tu reserva de " + nombreAsignatura(reserva) + " fue CONFIRMADA.";
                case CANCELADA -> "Tu reserva de " + nombreAsignatura(reserva)
                        + " fue CANCELADA. Motivo: " + reserva.getMotivoCancelacion();
                case COMPLETADA -> "Tu tutoria de " + nombreAsignatura(reserva) + " fue marcada como COMPLETADA.";
            };
        }
        notificador.notificar(reserva.getEstudiante(), mensaje);
    }

    private String nombreAsignatura(Reserva reserva) {
        return reserva.getHorario().getAsignatura().getNombre();
    }

    private String nombreDocente(Reserva reserva) {
        return reserva.getHorario().getDocente().getNombre();
    }
}
