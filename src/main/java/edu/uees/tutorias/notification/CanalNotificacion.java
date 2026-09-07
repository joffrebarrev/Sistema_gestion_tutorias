package edu.uees.tutorias.notification;

/**
 * Canales de notificacion soportados. Se usa como parametro para
 * seleccionar la {@link NotificadorFactory} adecuada sin que quien la usa
 * (por ejemplo {@code App}) conozca las clases concretas de cada canal.
 */
public enum CanalNotificacion {
    EMAIL,
    CONSOLA,
    SMS
}
