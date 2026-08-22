package edu.uees.tutorias.service;

import edu.uees.tutorias.domain.Asignatura;
import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.EstadoReserva;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioTutoria;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.repository.RepositorioReservas;
import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorConsola;
import edu.uees.tutorias.persistence.RepositorioReservasEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas de los casos de uso principales. Se inyectan implementaciones
 * en memoria/consola de los puertos (RepositorioReservas, Notificador),
 * lo que solo es posible porque ServicioReservasImpl depende de
 * abstracciones (DIP) y no de clases concretas.
 */
class ServicioReservasImplTest {

    private ServicioReservas servicioReservas;
    private Docente docente;
    private Estudiante estudiante;
    private Asignatura asignatura;

    @BeforeEach
    void setUp() {
        RepositorioReservas repositorioReservas = new RepositorioReservasEnMemoria();
        Notificador notificador = new NotificadorConsola();
        servicioReservas = new ServicioReservasImpl(repositorioReservas, notificador);

        docente = new Docente("D1", "Ing. Carla Zambrano", "carla.zambrano@uees.edu.ec", "Computacion");
        estudiante = new Estudiante("E1", "Joffre Barre", "joffre.barre@uees.edu.ec", "Telecomunicaciones");
        asignatura = new Asignatura("UCOM0310", "Diseno de Software");
    }

    @Test
    void reservarYConfirmarCambiaElEstadoCorrectamente() {
        HorarioTutoria horario = docente.publicarHorario("H1", asignatura,
                LocalDateTime.of(2026, 8, 25, 15, 0),
                LocalDateTime.of(2026, 8, 25, 16, 0), 1);

        Reserva reserva = servicioReservas.reservarTutoria(estudiante, horario);
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());

        servicioReservas.confirmarReserva(reserva.getId());
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
    }

    @Test
    void noPermiteReservarUnHorarioSinCupos() {
        HorarioTutoria horario = docente.publicarHorario("H2", asignatura,
                LocalDateTime.of(2026, 8, 25, 15, 0),
                LocalDateTime.of(2026, 8, 25, 16, 0), 1);

        Estudiante otroEstudiante = new Estudiante("E2", "Maria Paz", "maria.paz@uees.edu.ec", "Sistemas");

        servicioReservas.reservarTutoria(estudiante, horario);
        assertFalse(horario.estaDisponible());

        assertThrows(IllegalStateException.class,
                () -> servicioReservas.reservarTutoria(otroEstudiante, horario));
    }

    @Test
    void cancelarLiberaElCupoDelHorario() {
        HorarioTutoria horario = docente.publicarHorario("H3", asignatura,
                LocalDateTime.of(2026, 8, 25, 15, 0),
                LocalDateTime.of(2026, 8, 25, 16, 0), 1);

        Reserva reserva = servicioReservas.reservarTutoria(estudiante, horario);
        servicioReservas.cancelarReserva(reserva.getId(), "Cambio de disponibilidad del estudiante");

        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        assertEquals(0, horario.getCuposOcupados());
    }
}
