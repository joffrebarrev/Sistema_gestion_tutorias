package edu.uees.tutorias.strategy;

import edu.uees.tutorias.domain.Reserva;

/**
 * Strategy: regla de negocio que decide QUE CONSECUENCIA tiene cancelar
 * una reserva (ademas de la transicion de estado, que Reserva ya protege
 * por si misma).
 *
 * <p><b>Problema real que resuelve:</b> no toda cancelacion es igual. Si
 * un estudiante cancela con mucha anticipacion, no deberia haber ninguna
 * consecuencia; si cancela faltando muy poco para el horario, es
 * razonable dejar constancia de una cancelacion tardia (por ejemplo, para
 * una futura politica de penalizaciones). Antes de este incremento no
 * existia esta regla; agregarla como un {@code if/else} dentro de
 * {@code ServicioReservasImpl.cancelarReserva(...)} mezclaria la
 * orquestacion del caso de uso con una regla de negocio que puede seguir
 * creciendo (por ejemplo, politicas distintas por tipo de asignatura o de
 * estudiante en el futuro). Strategy aisla esa regla variable en clases
 * intercambiables; {@code Reserva} sigue siendo la unica responsable de
 * que la transicion de estado en si sea valida (SRP: Strategy decide la
 * consecuencia, Reserva decide si el cambio de estado es legal).</p>
 */
public interface PoliticaCancelacion {

    /**
     * Aplica la cancelacion sobre la reserva (delegando siempre en
     * {@link Reserva#cancelar(String)} para la transicion de estado) y
     * devuelve el motivo final registrado, que puede incluir una anotacion
     * propia de la politica (por ejemplo, la marca de "cancelacion
     * tardia").
     */
    String aplicar(Reserva reserva, String motivo);
}
