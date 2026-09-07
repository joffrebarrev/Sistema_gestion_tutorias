package edu.uees.disenosoftware.observer;

import edu.uees.disenosoftware.domain.Reserva;

public class PanelDocenteObserver implements ReservaObserver {
    @Override
    public void actualizar(Reserva reserva) {
        System.out.println("Panel Docente: Estado de la tutoría " + reserva.getId() + " modificado a " + reserva.getEstado());
    }
}