package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/**
 * Puerto de comunicacion de eventos relevantes hacia un {@link Usuario}.
 *
 * <p>{@code ServicioReservasImpl} depende de esta interfaz y no de un
 * canal concreto (correo, SMS, push). Esto aplica dos principios SOLID a
 * la vez:</p>
 * <ul>
 *   <li><b>DIP</b>: el servicio de reservas depende de la abstraccion
 *       Notificador, no de {@code NotificadorEmail}.</li>
 *   <li><b>OCP</b>: agregar un nuevo canal (por ejemplo SMS) implica
 *       crear una nueva clase que implemente Notificador, sin modificar
 *       ServicioReservasImpl ni ninguna otra clase existente.</li>
 * </ul>
 */
public interface Notificador {

    void notificar(Usuario destinatario, String mensaje);
}
