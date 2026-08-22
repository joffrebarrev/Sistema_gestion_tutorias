package edu.uees.tutorias.service;

import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioTutoria;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.repository.RepositorioReservas;
import edu.uees.tutorias.notification.Notificador;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Orquesta los casos de uso de reserva: valida entradas, delega en el
 * propio dominio ({@link Reserva}, {@link HorarioTutoria}) las reglas de
 * negocio, persiste el resultado y notifica a los interesados.
 *
 * <p>Esta clase existe para separar la <b>orquestacion</b> de un caso de
 * uso de las <b>reglas de negocio</b> (que viven en {@code Reserva} y
 * {@code HorarioTutoria}) y de los <b>detalles tecnicos</b> (persistencia
 * y notificacion, inyectados como abstracciones). Es la aplicacion
 * directa de Single Responsibility Principle: si cambia la forma de
 * notificar o de persistir, esta clase no cambia; si cambia una regla de
 * cuando una reserva puede confirmarse, el cambio ocurre en
 * {@code Reserva}, no aqui.</p>
 *
 * <p>Las dependencias (RepositorioReservas, Notificador) se reciben por
 * constructor -no se instancian con {@code new} dentro de la clase-, lo
 * que aplica Dependency Inversion Principle: {@code ServicioReservasImpl}
 * depende de abstracciones, y quien ensambla la aplicacion (por ejemplo
 * {@code App}) decide que implementacion concreta usar.</p>
 */
public class ServicioReservasImpl implements ServicioReservas {

    private final RepositorioReservas repositorioReservas;
    private final Notificador notificador;

    public ServicioReservasImpl(RepositorioReservas repositorioReservas, Notificador notificador) {
        this.repositorioReservas = Objects.requireNonNull(repositorioReservas);
        this.notificador = Objects.requireNonNull(notificador);
    }

    @Override
    public Reserva reservarTutoria(Estudiante estudiante, HorarioTutoria horario) {
        Objects.requireNonNull(estudiante, "estudiante no puede ser nulo");
        Objects.requireNonNull(horario, "horario no puede ser nulo");

        // La regla "hay cupo" la protege el propio HorarioTutoria/Reserva;
        // aqui solo se orquesta el caso de uso.
        Reserva reserva = new Reserva(UUID.randomUUID().toString(), estudiante, horario);
        repositorioReservas.guardar(reserva);

        notificador.notificar(estudiante,
                "Tu solicitud de tutoria de " + horario.getAsignatura().getNombre()
                        + " con " + horario.getDocente().getNombre() + " quedo PENDIENTE de confirmacion.");
        notificador.notificar(horario.getDocente(),
                "Tienes una nueva solicitud de tutoria de " + estudiante.getNombre()
                        + " para " + horario.getAsignatura().getNombre() + ".");
        return reserva;
    }

    @Override
    public void confirmarReserva(String idReserva) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        reserva.confirmar();
        notificador.notificar(reserva.getEstudiante(),
                "Tu reserva de " + reserva.getHorario().getAsignatura().getNombre() + " fue CONFIRMADA.");
    }

    @Override
    public void cancelarReserva(String idReserva, String motivo) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        reserva.cancelar(motivo);
        notificador.notificar(reserva.getEstudiante(),
                "Tu reserva de " + reserva.getHorario().getAsignatura().getNombre()
                        + " fue CANCELADA. Motivo: " + motivo);
        notificador.notificar(reserva.getHorario().getDocente(),
                "La reserva de " + reserva.getEstudiante().getNombre() + " fue cancelada.");
    }

    @Override
    public void reprogramarReserva(String idReserva, HorarioTutoria nuevoHorario) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        reserva.reprogramar(nuevoHorario);
        notificador.notificar(reserva.getEstudiante(),
                "Tu reserva fue reprogramada a " + nuevoHorario + ". Debe confirmarse nuevamente.");
    }

    @Override
    public List<Reserva> obtenerReservasDe(Estudiante estudiante) {
        return repositorioReservas.listarPorEstudiante(estudiante);
    }

    private Reserva obtenerReservaOFallar(String idReserva) {
        return repositorioReservas.buscarPorId(idReserva)
                .orElseThrow(() -> new IllegalArgumentException("No existe una reserva con id " + idReserva));
    }
}
