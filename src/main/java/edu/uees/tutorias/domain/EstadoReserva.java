package edu.uees.tutorias.domain;

/**
 * Estados posibles del ciclo de vida de una {@link Reserva}.
 *
 * <p>Las transiciones validas entre estados se protegen dentro de
 * {@code Reserva}, no aqui: el enum solo enumera los valores posibles.</p>
 */
public enum EstadoReserva {
    PENDIENTE,
    CONFIRMADA,
    CANCELADA,
    COMPLETADA
}
