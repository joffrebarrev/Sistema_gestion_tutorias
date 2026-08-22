package edu.uees.tutorias.service;

import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioTutoria;
import edu.uees.tutorias.domain.Reserva;

import java.util.List;

/**
 * Casos de uso del sistema de tutorias relacionados con reservas.
 *
 * <p>Separar esta interfaz de {@code ServicioReservasImpl} permite que
 * quien consuma el servicio (por ejemplo un futuro controlador REST o la
 * clase {@code App}) dependa solo del contrato, no de la implementacion
 * (DIP), y facilita crear implementaciones alternativas para pruebas.</p>
 */
public interface ServicioReservas {

    Reserva reservarTutoria(Estudiante estudiante, HorarioTutoria horario);

    void confirmarReserva(String idReserva);

    void cancelarReserva(String idReserva, String motivo);

    void reprogramarReserva(String idReserva, HorarioTutoria nuevoHorario);

    List<Reserva> obtenerReservasDe(Estudiante estudiante);
}
