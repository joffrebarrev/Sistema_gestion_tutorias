package edu.uees.disenosoftware.observer;

import edu.uees.disenosoftware.domain.Reserva;

public class PanelEstudianteObserver implements ReservaObserver {
    @Override
    public void actualizar(Reserva reserva) {
        System.out.println("Panel Estudiante: Notificación de actualización -> " + reserva.getId() + " es " + reserva.getEstado());
    }
}