package edu.uees.tutorias.domain.repository;

import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.Reserva;

import java.util.List;
import java.util.Optional;

/**
 * Puerto (abstraccion) para la persistencia de reservas.
 *
 * <p>Vive dentro de {@code domain} porque describe una necesidad del
 * dominio ("guardar y recuperar reservas"), no una decision de
 * tecnologia. La capa de dominio y de servicio dependen de esta interfaz,
 * nunca de una implementacion concreta (Dependency Inversion Principle):
 * asi, cambiar de una lista en memoria a una base de datos relacional o a
 * un archivo no obliga a modificar {@code ServicioReservasImpl}, solo a
 * escribir una nueva implementacion de este puerto.</p>
 */
public interface RepositorioReservas {

    void guardar(Reserva reserva);

    Optional<Reserva> buscarPorId(String id);

    List<Reserva> listarPorEstudiante(Estudiante estudiante);

    List<Reserva> listarTodas();
}
