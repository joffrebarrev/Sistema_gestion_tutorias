package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/**
 * Implementacion de {@link Notificador} que simula el envio de un SMS.
 * Se agrega en este incremento como evidencia de que el Factory Method
 * heredado de Ae2 sigue cumpliendo su proposito: este canal se sumo sin
 * modificar {@code Notificador}, {@code NotificadorEmail},
 * {@code NotificadorConsola} ni {@code ServicioReservasImpl} (OCP).
 */
public class NotificadorSms implements Notificador {

    @Override
    public void notificar(Usuario destinatario, String mensaje) {
        System.out.printf("[SMS a %s] %s%n", destinatario.getNombre(), mensaje);
    }
}
