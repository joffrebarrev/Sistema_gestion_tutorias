package edu.uees.disenosoftware.observer;

import edu.uees.disenosoftware.domain.Reserva;

public class EmailObserver implements ReservaObserver {
    @Override
    public void actualizar(Reserva reserva) {
        System.out.println("Email: nuevo estado " + reserva.getEstado() + " para la reserva " + reserva.getId());
    }
}
