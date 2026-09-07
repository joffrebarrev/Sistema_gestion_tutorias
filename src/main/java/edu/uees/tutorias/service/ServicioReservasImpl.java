package edu.uees.tutorias.service;

import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioTutoria;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.repository.RepositorioReservas;
import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.observer.NotificacionDocenteObserver;
import edu.uees.tutorias.notification.observer.NotificacionEstudianteObserver;
import edu.uees.tutorias.strategy.PoliticaCancelacion;
import edu.uees.tutorias.strategy.SelectorPoliticaCancelacion;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Orquesta los casos de uso de reserva: valida entradas, delega en el
 * propio dominio ({@link Reserva}, {@link HorarioTutoria}) las reglas de
 * negocio, persiste el resultado y ensambla quien reacciona a los cambios
 * de estado.
 *
 * <p>Esta clase existe para separar la <b>orquestacion</b> de un caso de
 * uso de las <b>reglas de negocio</b> (que viven en {@code Reserva} y
 * {@code HorarioTutoria}) y de los <b>detalles tecnicos</b> (persistencia,
 * notificacion y politica de cancelacion, inyectados o seleccionados como
 * abstracciones). Es la aplicacion directa de Single Responsibility
 * Principle: si cambia la forma de notificar o de persistir, esta clase no
 * cambia; si cambia una regla de cuando una reserva puede confirmarse, el
 * cambio ocurre en {@code Reserva}, no aqui.</p>
 *
 * <p><b>Incremento 1 (Ae3) - que cambio aqui:</b> antes, este servicio
 * llamaba a {@code notificador.notificar(...)} directamente y por
 * duplicado (estudiante y docente) en cuatro metodos distintos -codigo
 * repetido y acoplado a "quien debe enterarse". Ahora, al crear una
 * reserva, el servicio registra los observadores interesados
 * ({@link NotificacionEstudianteObserver}, {@link
 * NotificacionDocenteObserver}) y {@code Reserva} misma avisa en cada
 * cambio de estado (patron Observer). Ademas, {@code cancelarReserva}
 * delega en {@link SelectorPoliticaCancelacion} la decision de que
 * consecuencia aplica segun la anticipacion de la cancelacion (patron
 * Strategy), en lugar de un {@code if/else} embebido aqui.</p>
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

        // Se registran los interesados; Reserva no sabe quienes son ni
        // cuantos, solo que existe la abstraccion ObservadorReserva.
        reserva.agregarObservador(new NotificacionEstudianteObserver(notificador));
        reserva.agregarObservador(new NotificacionDocenteObserver(notificador));

        repositorioReservas.guardar(reserva);
        reserva.notificarCreacion();
        return reserva;
    }

    @Override
    public void confirmarReserva(String idReserva) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        // Reserva.confirmar() ya notifica a los observadores registrados.
        reserva.confirmar();
    }

    @Override
    public void cancelarReserva(String idReserva, String motivo) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        PoliticaCancelacion politica = SelectorPoliticaCancelacion.seleccionar(reserva);
        // La politica delega en Reserva.cancelar(...), que valida la
        // transicion y notifica a los observadores.
        politica.aplicar(reserva, motivo);
    }

    @Override
    public void reprogramarReserva(String idReserva, HorarioTutoria nuevoHorario) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        // Reserva.reprogramar() ya notifica a los observadores registrados.
        reserva.reprogramar(nuevoHorario);
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
