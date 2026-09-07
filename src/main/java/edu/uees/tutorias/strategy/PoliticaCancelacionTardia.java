package edu.uees.tutorias.strategy;

import edu.uees.tutorias.domain.Reserva;

/**
 * Politica para cancelaciones tardias (menos de 2 horas antes del
 * horario). Registra explicitamente la cancelacion tardia en el motivo,
 * como base para una futura regla de penalizacion (fuera del alcance de
 * este incremento, pero ya soportada por el diseno: agregar la
 * penalizacion real solo requeriria modificar esta clase).
 */
public class PoliticaCancelacionTardia implements PoliticaCancelacion {

    @Override
    public String aplicar(Reserva reserva, String motivo) {
        String motivoFinal = motivo + " (cancelacion tardia: penalizacion aplicada)";
        reserva.cancelar(motivoFinal);
        return motivoFinal;
    }
}
