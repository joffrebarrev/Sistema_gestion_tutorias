package edu.uees.tutorias;

import edu.uees.tutorias.domain.Asignatura;
import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioTutoria;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorConsola;
import edu.uees.tutorias.persistence.RepositorioReservasEnMemoria;
import edu.uees.tutorias.domain.repository.RepositorioReservas;
import edu.uees.tutorias.service.ServicioReservas;
import edu.uees.tutorias.service.ServicioReservasImpl;

import java.time.LocalDateTime;

/**
 * Punto de entrada de demostracion. Ensambla las implementaciones
 * concretas (RepositorioReservasEnMemoria, NotificadorConsola) e
 * inyecta las abstracciones en {@link ServicioReservasImpl}: este es el
 * unico lugar del proyecto que conoce las clases concretas de
 * persistencia y notificacion.
 */
public class App {

    public static void main(String[] args) {
        RepositorioReservas repositorioReservas = new RepositorioReservasEnMemoria();
        Notificador notificador = new NotificadorConsola();
        ServicioReservas servicioReservas = new ServicioReservasImpl(repositorioReservas, notificador);

        Docente docente = new Docente("D1", "Ing. Carla Zambrano", "carla.zambrano@uees.edu.ec", "Computacion");
        Estudiante estudiante = new Estudiante("E1", "Joffre Barre", "joffre.barre@uees.edu.ec", "Telecomunicaciones");
        Asignatura asignatura = new Asignatura("UCOM0310", "Diseno de Software");

        HorarioTutoria horario = docente.publicarHorario(
                "H1", asignatura,
                LocalDateTime.of(2026, 8, 25, 15, 0),
                LocalDateTime.of(2026, 8, 25, 16, 0),
                1);

        System.out.println("--- Solicitud de tutoria ---");
        Reserva reserva = servicioReservas.reservarTutoria(estudiante, horario);

        System.out.println("--- Confirmacion ---");
        servicioReservas.confirmarReserva(reserva.getId());

        System.out.println("--- Estado final ---");
        System.out.println(reserva);
    }
}
