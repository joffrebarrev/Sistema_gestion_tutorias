package edu.uees.tutorias.strategy;

import edu.uees.tutorias.domain.Reserva;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Contexto del Strategy: decide QUE politica corresponde segun la
 * anticipacion con la que se cancela, comparando el momento actual contra
 * el inicio del {@code HorarioTutoria} de la reserva. Es el unico lugar
 * del sistema que conoce las tres implementaciones concretas de
 * {@link PoliticaCancelacion}; {@code ServicioReservasImpl} solo conoce la
 * interfaz.
 */
public final class SelectorPoliticaCancelacion {

    private static final long HORAS_ANTICIPACION = 24;
    private static final long HORAS_TARDIA = 2;

    private SelectorPoliticaCancelacion() {
    }

    public static PoliticaCancelacion seleccionar(Reserva reserva) {
        return seleccionar(reserva, LocalDateTime.now());
    }

    /** Sobrecarga usada por las pruebas para fijar el "ahora" y no depender del reloj del sistema. */
    public static PoliticaCancelacion seleccionar(Reserva reserva, LocalDateTime ahora) {
        Duration restante = Duration.between(ahora, reserva.getHorario().getInicio());
        long horas = restante.toHours();
        if (horas >= HORAS_ANTICIPACION) {
            return new PoliticaCancelacionAnticipada();
        }
        if (horas < HORAS_TARDIA) {
            return new PoliticaCancelacionTardia();
        }
        return new PoliticaCancelacionEstandar();
    }
}
