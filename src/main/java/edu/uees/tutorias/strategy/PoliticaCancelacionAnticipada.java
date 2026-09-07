package edu.uees.tutorias.strategy;

import edu.uees.tutorias.domain.Reserva;

/**
 * Politica para cancelaciones con amplia anticipacion (24 horas o mas
 * antes del horario). Dejan constancia explicita de que no corresponde
 * ninguna penalizacion, para que quede documentado en el motivo.
 */
public class PoliticaCancelacionAnticipada implements PoliticaCancelacion {

    @Override
    public String aplicar(Reserva reserva, String motivo) {
        String motivoFinal = motivo + " (cancelacion anticipada, sin penalizacion)";
        reserva.cancelar(motivoFinal);
        return motivoFinal;
    }
}
