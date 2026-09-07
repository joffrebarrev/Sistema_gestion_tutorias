package edu.uees.tutorias;

import edu.uees.tutorias.domain.Asignatura;
import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioTutoria;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.notification.CanalNotificacion;
import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorFactory;
import edu.uees.tutorias.persistence.RepositorioReservasEnMemoria;
import edu.uees.tutorias.domain.repository.RepositorioReservas;
import edu.uees.tutorias.service.ServicioReservas;
import edu.uees.tutorias.service.ServicioReservasImpl;

import java.time.LocalDateTime;

/**
 * Punto de entrada de demostracion del Incremento 1 (Ae3). Ensambla las
 * implementaciones concretas -incluyendo el canal de notificacion elegido
 * a traves del Factory Method revisado de Ae2- e inyecta las
 * abstracciones en {@link ServicioReservasImpl}. Recorre tres escenarios
 * para evidenciar los patrones integrados:
 * <ol>
 *   <li>Reserva -> confirmacion (Observer avisando a estudiante y
 *       docente en cada paso).</li>
 *   <li>Reserva -> cancelacion con mucha anticipacion (Strategy elige
 *       {@code PoliticaCancelacionAnticipada}).</li>
 *   <li>Reserva -> cancelacion faltando pocas horas (Strategy elige
 *       {@code PoliticaCancelacionTardia}).</li>
 * </ol>
 */
public class App {

    public static void main(String[] args) {
        RepositorioReservas repositorioReservas = new RepositorioReservasEnMemoria();

        // Factory Method (revision de Ae2): el canal se elige aqui, en un
        // unico lugar, sin que el resto del sistema conozca NotificadorConsola.
        Notificador notificador = NotificadorFactory.obtenerFactory(CanalNotificacion.CONSOLA).crearNotificador();
        ServicioReservas servicioReservas = new ServicioReservasImpl(repositorioReservas, notificador);

        Docente docente = new Docente("D1", "Ing. Carla Zambrano", "carla.zambrano@uees.edu.ec", "Computacion");
        Estudiante estudiante = new Estudiante("E1", "Joffre Barre", "joffre.barre@uees.edu.ec", "Telecomunicaciones");
        Asignatura asignatura = new Asignatura("UCOM0310", "Diseno de Software");

        LocalDateTime ahora = LocalDateTime.now();

        System.out.println("======================================================");
        System.out.println(" ESCENARIO 1: reserva -> confirmacion (Observer)");
        System.out.println("======================================================");
        HorarioTutoria horario1 = docente.publicarHorario(
                "H1", asignatura, ahora.plusDays(3), ahora.plusDays(3).plusHours(1), 1);
        Reserva reserva1 = servicioReservas.reservarTutoria(estudiante, horario1);
        servicioReservas.confirmarReserva(reserva1.getId());
        System.out.println("Estado final: " + reserva1);

        System.out.println();
        System.out.println("======================================================");
        System.out.println(" ESCENARIO 2: cancelacion con >24h de anticipacion (Strategy)");
        System.out.println("======================================================");
        HorarioTutoria horario2 = docente.publicarHorario(
                "H2", asignatura, ahora.plusDays(5), ahora.plusDays(5).plusHours(1), 1);
        Reserva reserva2 = servicioReservas.reservarTutoria(estudiante, horario2);
        servicioReservas.cancelarReserva(reserva2.getId(), "El estudiante ya no requiere la tutoria");
        System.out.println("Estado final: " + reserva2);
        System.out.println("Motivo registrado: " + reserva2.getMotivoCancelacion());

        System.out.println();
        System.out.println("======================================================");
        System.out.println(" ESCENARIO 3: cancelacion faltando menos de 2h (Strategy)");
        System.out.println("======================================================");
        HorarioTutoria horario3 = docente.publicarHorario(
                "H3", asignatura, ahora.plusMinutes(30), ahora.plusMinutes(90), 1);
        Reserva reserva3 = servicioReservas.reservarTutoria(estudiante, horario3);
        servicioReservas.cancelarReserva(reserva3.getId(), "Surgio un imprevisto");
        System.out.println("Estado final: " + reserva3);
        System.out.println("Motivo registrado: " + reserva3.getMotivoCancelacion());
    }
}
