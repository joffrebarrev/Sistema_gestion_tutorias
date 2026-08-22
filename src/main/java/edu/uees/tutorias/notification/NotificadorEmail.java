package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/**
 * Implementacion de {@link Notificador} que simula el envio de un correo
 * electronico. En una version futura, esta clase seria la unica que
 * cambiaria si se integra un proveedor real de correo (SendGrid, SMTP,
 * etc.); el resto del sistema no se ve afectado porque solo conoce la
 * interfaz {@link Notificador}.
 */
public class NotificadorEmail implements Notificador {

    @Override
    public void notificar(Usuario destinatario, String mensaje) {
        System.out.printf("[EMAIL a %s <%s>] %s%n",
                destinatario.getNombre(), destinatario.getEmail(), mensaje);
    }
}
