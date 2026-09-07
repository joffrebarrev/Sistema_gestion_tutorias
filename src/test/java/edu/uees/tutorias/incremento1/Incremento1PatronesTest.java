package edu.uees.tutorias.incremento1;

import edu.uees.tutorias.domain.Asignatura;
import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.EstadoReserva;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioTutoria;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.observer.ObservadorReserva;
import edu.uees.tutorias.notification.CanalNotificacion;
import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorConsola;
import edu.uees.tutorias.notification.NotificadorEmail;
import edu.uees.tutorias.notification.NotificadorFactory;
import edu.uees.tutorias.notification.NotificadorSms;
import edu.uees.tutorias.strategy.PoliticaCancelacion;
import edu.uees.tutorias.strategy.PoliticaCancelacionAnticipada;
import edu.uees.tutorias.strategy.PoliticaCancelacionEstandar;
import edu.uees.tutorias.strategy.PoliticaCancelacionTardia;
import edu.uees.tutorias.strategy.SelectorPoliticaCancelacion;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas especificas del Incremento 1 (Ae3): verifican que los patrones
 * agregados/​revisados sobre el dominio de Ae1 funcionan de forma aislada,
 * ademas de las pruebas de {@code ServicioReservasImplTest} que ya cubren
 * el flujo completo integrado.
 */
class Incremento1PatronesTest {

    private Docente crearDocente() {
        return new Docente("D1", "Ing. Carla Zambrano", "carla.zambrano@uees.edu.ec", "Computacion");
    }

    private Estudiante crearEstudiante() {
        return new Estudiante("E1", "Joffre Barre", "joffre.barre@uees.edu.ec", "Telecomunicaciones");
    }

    // ---------- Observer ----------

    @Test
    void reservaNotificaATodosLosObservadoresRegistradosEnCadaCambioDeEstado() {
        Docente docente = crearDocente();
        Estudiante estudiante = crearEstudiante();
        Asignatura asignatura = new Asignatura("UCOM0310", "Diseno de Software");
        HorarioTutoria horario = docente.publicarHorario("H1", asignatura,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), 1);
        Reserva reserva = new Reserva("R1", estudiante, horario);

        List<EstadoReserva> estadosNotificados = new ArrayList<>();
        ObservadorReserva observadorEspia = (r, estadoAnterior) -> estadosNotificados.add(r.getEstado());

        reserva.agregarObservador(observadorEspia);
        reserva.notificarCreacion();
        reserva.confirmar();

        assertEquals(List.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA), estadosNotificados);
    }

    @Test
    void unObservadorNuevoNoRequiereModificarReservaNiElServicio() {
        // Evidencia de OCP: un observador adicional (por ejemplo un futuro
        // panel administrativo) solo implementa ObservadorReserva; no hace
        // falta tocar Reserva ni ServicioReservasImpl para sumarlo.
        List<String> eventosPanel = new ArrayList<>();
        ObservadorReserva panelAdministrativo = (r, estadoAnterior) ->
                eventosPanel.add("Reserva " + r.getId() + " ahora esta " + r.getEstado());

        Docente docente = crearDocente();
        Estudiante estudiante = crearEstudiante();
        Asignatura asignatura = new Asignatura("UCOM0310", "Diseno de Software");
        HorarioTutoria horario = docente.publicarHorario("H2", asignatura,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), 1);
        Reserva reserva = new Reserva("R2", estudiante, horario);
        reserva.agregarObservador(panelAdministrativo);
        reserva.notificarCreacion();

        assertEquals(1, eventosPanel.size());
        assertTrue(eventosPanel.get(0).contains("PENDIENTE"));
    }

    // ---------- Strategy ----------

    @Test
    void seleccionaPoliticaAnticipadaConMasDe24Horas() {
        Reserva reserva = crearReservaConHorarioEn(30);
        PoliticaCancelacion politica = SelectorPoliticaCancelacion.seleccionar(reserva, LocalDateTime.now());
        assertInstanceOf(PoliticaCancelacionAnticipada.class, politica);
    }

    @Test
    void seleccionaPoliticaTardiaConMenosDe2Horas() {
        Reserva reserva = crearReservaConHorarioEnMinutos(45);
        PoliticaCancelacion politica = SelectorPoliticaCancelacion.seleccionar(reserva, LocalDateTime.now());
        assertInstanceOf(PoliticaCancelacionTardia.class, politica);
    }

    @Test
    void seleccionaPoliticaEstandarEntre2Y24Horas() {
        Reserva reserva = crearReservaConHorarioEn(10);
        PoliticaCancelacion politica = SelectorPoliticaCancelacion.seleccionar(reserva, LocalDateTime.now());
        assertInstanceOf(PoliticaCancelacionEstandar.class, politica);
    }

    @Test
    void laPoliticaTardiaDejaConstanciaDePenalizacionEnElMotivo() {
        Reserva reserva = crearReservaConHorarioEnMinutos(30);
        String motivoFinal = new PoliticaCancelacionTardia().aplicar(reserva, "Imprevisto");
        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        assertTrue(motivoFinal.contains("penalizacion"));
    }

    private Reserva crearReservaConHorarioEn(int horas) {
        Docente docente = crearDocente();
        Estudiante estudiante = crearEstudiante();
        Asignatura asignatura = new Asignatura("UCOM0310", "Diseno de Software");
        HorarioTutoria horario = docente.publicarHorario("H-" + horas + "h", asignatura,
                LocalDateTime.now().plusHours(horas), LocalDateTime.now().plusHours(horas).plusMinutes(60), 1);
        return new Reserva("R-" + horas + "h", estudiante, horario);
    }

    private Reserva crearReservaConHorarioEnMinutos(int minutos) {
        Docente docente = crearDocente();
        Estudiante estudiante = crearEstudiante();
        Asignatura asignatura = new Asignatura("UCOM0310", "Diseno de Software");
        HorarioTutoria horario = docente.publicarHorario("H-" + minutos + "m", asignatura,
                LocalDateTime.now().plusMinutes(minutos), LocalDateTime.now().plusMinutes(minutos + 60), 1);
        return new Reserva("R-" + minutos + "m", estudiante, horario);
    }

    // ---------- Factory Method ----------

    @Test
    void laFactoryDevuelveLaImplementacionConcretaSegunElCanal() {
        Notificador email = NotificadorFactory.obtenerFactory(CanalNotificacion.EMAIL).crearNotificador();
        Notificador consola = NotificadorFactory.obtenerFactory(CanalNotificacion.CONSOLA).crearNotificador();
        Notificador sms = NotificadorFactory.obtenerFactory(CanalNotificacion.SMS).crearNotificador();

        assertInstanceOf(NotificadorEmail.class, email);
        assertInstanceOf(NotificadorConsola.class, consola);
        assertInstanceOf(NotificadorSms.class, sms);
    }
}
