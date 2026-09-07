package edu.uees.tutorias.notification.observer;

import edu.uees.tutorias.domain.EstadoReserva;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.observer.ObservadorReserva;
import edu.uees.tutorias.notification.Notificador;

import java.util.Objects;

/**
 * Observador concreto que avisa al {@link edu.uees.tutorias.domain.Docente}
 * dueno del horario cuando la reserva asociada cambia de estado.
 *
 * <p>Al docente solo le interesan los eventos de creacion y cancelacion
 * (necesita saber si tiene una solicitud nueva o si una ya no se
 * realizara); a diferencia del estudiante, no recibe aviso de CONFIRMADA
 * ni de COMPLETADA porque el ya sabe que confirmo/completo la sesion. Esta
 * diferencia de "quien reacciona a que" es exactamente lo que Observer
 * permite modelar sin condicionales en Reserva ni en el servicio.</p>
 */
public class NotificacionDocenteObserver implements ObservadorReserva {

    private final Notificador notificador;

    public NotificacionDocenteObserver(Notificador notificador) {
        this.notificador = Objects.requireNonNull(notificador, "notificador no puede ser nulo");
    }

    @Override
    public void actualizar(Reserva reserva, EstadoReserva estadoAnterior) {
        boolean esReprogramacion = reserva.getEstado() == EstadoReserva.PENDIENTE && estadoAnterior != null;
        if (esReprogramacion) {
            notificador.notificar(reserva.getHorario().getDocente(),
                    "La reserva de " + reserva.getEstudiante().getNombre()
                            + " se reprogramo a " + reserva.getHorario() + ".");
            return;
        }
        switch (reserva.getEstado()) {
            case PENDIENTE -> notificador.notificar(reserva.getHorario().getDocente(),
                    "Tienes una nueva solicitud de tutoria de " + reserva.getEstudiante().getNombre()
                            + " para " + reserva.getHorario().getAsignatura().getNombre() + ".");
            case CANCELADA -> notificador.notificar(reserva.getHorario().getDocente(),
                    "La reserva de " + reserva.getEstudiante().getNombre() + " fue cancelada. Motivo: "
                            + reserva.getMotivoCancelacion());
            default -> { /* CONFIRMADA y COMPLETADA no requieren aviso al docente */ }
        }
    }
}
