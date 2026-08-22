package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/**
 * Implementacion de {@link Notificador} usada para pruebas y para la
 * demostracion en consola ({@code App}). Evidencia el cumplimiento de
 * OCP: se agrego este canal sin tocar {@code Notificador},
 * {@code NotificadorEmail} ni {@code ServicioReservasImpl}.
 */
public class NotificadorConsola implements Notificador {

    @Override
    public void notificar(Usuario destinatario, String mensaje) {
        System.out.printf("[CONSOLA -> %s (%s)] %s%n",
                destinatario.getNombre(), destinatario.getRol(), mensaje);
    }
}
