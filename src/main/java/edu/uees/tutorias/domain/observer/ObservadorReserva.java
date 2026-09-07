package edu.uees.tutorias.domain.observer;

import edu.uees.tutorias.domain.EstadoReserva;
import edu.uees.tutorias.domain.Reserva;

/**
 * Puerto (Observer) para reaccionar a cambios de estado de una {@link Reserva}.
 *
 * <p>Vive junto al dominio, igual que {@code RepositorioReservas}, porque
 * describe una necesidad del propio dominio ("avisar cuando cambio de
 * estado"), no una decision de tecnologia. {@code Reserva} depende solo de
 * esta abstraccion (DIP): no conoce si quien reacciona es un correo, un
 * panel administrativo o un futuro sistema de metricas.</p>
 *
 * <p>Problema real que resuelve: antes de este incremento,
 * {@code ServicioReservasImpl} llamaba manualmente a {@code notificador
 * .notificar(...)} para el estudiante y para el docente despues de cada
 * operacion (reservar, confirmar, cancelar, reprogramar). Agregar un
 * tercer interesado -por ejemplo un panel administrativo- exigia modificar
 * ese metodo cada vez (violacion de OCP). Con Observer, {@code Reserva}
 * simplemente notifica a su lista de observadores; agregar un interesado
 * nuevo es registrar un observador mas, sin tocar {@code Reserva} ni
 * {@code ServicioReservasImpl}.</p>
 */
public interface ObservadorReserva {

    /**
     * Invocado por {@link Reserva} despues de un cambio de estado valido.
     *
     * @param reserva         la reserva que cambio de estado (ya en su
     *                        nuevo estado)
     * @param estadoAnterior  el estado que tenia antes del cambio (puede
     *                        ser {@code null} si el observador se registro
     *                        al momento de crear la reserva y se notifica
     *                        el estado inicial)
     */
    void actualizar(Reserva reserva, EstadoReserva estadoAnterior);
}
