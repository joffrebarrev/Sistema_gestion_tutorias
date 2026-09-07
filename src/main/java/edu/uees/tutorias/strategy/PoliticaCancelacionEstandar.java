package edu.uees.tutorias.strategy;

import edu.uees.tutorias.domain.Reserva;

/**
 * Politica por defecto: cancela sin ninguna anotacion adicional. Se aplica
 * cuando la cancelacion ocurre en una ventana de tiempo "normal" (ni muy
 * anticipada ni demasiado tardia).
 */
public class PoliticaCancelacionEstandar implements PoliticaCancelacion {

    @Override
    public String aplicar(Reserva reserva, String motivo) {
        reserva.cancelar(motivo);
        return motivo;
    }
}
