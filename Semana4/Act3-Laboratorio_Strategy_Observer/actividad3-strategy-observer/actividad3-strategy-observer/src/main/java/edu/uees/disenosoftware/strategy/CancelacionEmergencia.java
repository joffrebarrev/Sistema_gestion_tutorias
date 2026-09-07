package edu.uees.disenosoftware.strategy;

import edu.uees.disenosoftware.domain.Reserva;

public class CancelacionEmergencia implements PoliticaCancelacion {
    private final String justificacion;

    public CancelacionEmergencia(String justificacion) {
        if (justificacion == null || justificacion.isBlank()) {
            throw new IllegalArgumentException("Se requiere una justificación válida para la emergencia.");
        }
        this.justificacion = justificacion;
    }

    @Override
    public boolean puedeCancelar(Reserva reserva) {
        System.out.println("Cancelación por emergencia justificada: " + justificacion);
        return reserva.minutosRestantes() >= 15;
    }
}