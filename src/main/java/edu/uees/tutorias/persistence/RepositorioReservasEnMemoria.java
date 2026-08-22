package edu.uees.tutorias.persistence;

import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.repository.RepositorioReservas;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementacion concreta de {@link RepositorioReservas} basada en un mapa
 * en memoria. Sirve para desarrollo, pruebas y para la demostracion de
 * consola ({@code App}).
 *
 * <p>Es intercambiable: si el proyecto migrara a JPA/JDBC/MongoDB,
 * bastaria con escribir otra clase que implemente
 * {@code RepositorioReservas} (por ejemplo
 * {@code RepositorioReservasJpa}) e inyectarla en
 * {@code ServicioReservasImpl}, sin tocar la logica de negocio.</p>
 */
public class RepositorioReservasEnMemoria implements RepositorioReservas {

    private final Map<String, Reserva> reservas = new LinkedHashMap<>();

    @Override
    public void guardar(Reserva reserva) {
        reservas.put(reserva.getId(), reserva);
    }

    @Override
    public Optional<Reserva> buscarPorId(String id) {
        return Optional.ofNullable(reservas.get(id));
    }

    @Override
    public List<Reserva> listarPorEstudiante(Estudiante estudiante) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva reserva : reservas.values()) {
            if (reserva.getEstudiante().equals(estudiante)) {
                resultado.add(reserva);
            }
        }
        return resultado;
    }

    @Override
    public List<Reserva> listarTodas() {
        return new ArrayList<>(reservas.values());
    }
}
