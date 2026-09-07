package edu.uees.tutorias.notification;

/**
 * Factory Method (revision de Ae2) para obtener un {@link Notificador}
 * segun el canal deseado, sin que el codigo cliente (por ejemplo
 * {@code App}) construya directamente {@code NotificadorEmail},
 * {@code NotificadorConsola} o {@code NotificadorSms} con {@code new}.
 *
 * <p><b>Por que se mantiene este patron en el Incremento 1:</b> el
 * problema que resolvia en Ae2 sigue existiendo tal cual en el dominio
 * real: elegir el canal de notificacion es una decision de configuracion
 * (que canal usar hoy) separada de la logica de negocio (que avisar).
 * Antes de este patron, quien ensamblaba la aplicacion necesitaria un
 * {@code if/else} o un {@code switch} sobre el canal cada vez que
 * necesitara un Notificador; con Factory Method, agregar un canal nuevo
 * (por ejemplo {@code SMS}, agregado en este mismo incremento) solo
 * requirio: una implementacion de {@link Notificador}, una factory
 * concreta y un valor de {@link CanalNotificacion} -sin tocar
 * {@code App} ni ninguna clase existente (OCP).</p>
 */
public abstract class NotificadorFactory {

    /** Metodo factory: cada subclase decide que implementacion concreta crear. */
    public abstract Notificador crearNotificador();

    /**
     * Punto de acceso unico: dado un canal, devuelve la factory concreta
     * correspondiente. Es el unico lugar del sistema que conoce la
     * correspondencia canal -> factory concreta.
     */
    public static NotificadorFactory obtenerFactory(CanalNotificacion canal) {
        return switch (canal) {
            case EMAIL -> new NotificadorEmailFactory();
            case CONSOLA -> new NotificadorConsolaFactory();
            case SMS -> new NotificadorSmsFactory();
        };
    }

    private static final class NotificadorEmailFactory extends NotificadorFactory {
        @Override
        public Notificador crearNotificador() {
            return new NotificadorEmail();
        }
    }

    private static final class NotificadorConsolaFactory extends NotificadorFactory {
        @Override
        public Notificador crearNotificador() {
            return new NotificadorConsola();
        }
    }

    private static final class NotificadorSmsFactory extends NotificadorFactory {
        @Override
        public Notificador crearNotificador() {
            return new NotificadorSms();
        }
    }
}
