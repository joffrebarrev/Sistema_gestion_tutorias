package edu.uees.tutorias.domain;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Representa a una persona que participa en el sistema de tutorias.
 *
 * <p>Es la generalizacion valida de {@link Estudiante} y {@link Docente}:
 * ambos "SON UN" Usuario (comparten identidad, nombre y correo), pero cada
 * uno tiene reglas y comportamiento propios. Por eso Usuario es abstracta:
 * no existen usuarios "genericos" en el dominio, solo estudiantes o
 * docentes.</p>
 *
 * <p>Encapsula la validacion del correo para que ninguna subclase (ni el
 * resto del sistema) pueda dejar un Usuario en un estado invalido.</p>
 */
public abstract class Usuario {

    private static final Pattern EMAIL_VALIDO =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,}$");

    private final String id;
    private final String nombre;
    private final String email;

    protected Usuario(String id, String nombre, String email) {
        this.id = Objects.requireNonNull(id, "id no puede ser nulo");
        this.nombre = validarNombre(nombre);
        this.email = validarEmail(email);
    }

    private String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario no puede estar vacio");
        }
        return nombre;
    }

    private String validarEmail(String email) {
        if (email == null || !EMAIL_VALIDO.matcher(email).matches()) {
            throw new IllegalArgumentException("Email invalido: " + email);
        }
        return email;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Describe el rol del usuario dentro del sistema. Cada subclase lo
     * define de forma polimorfica (ejemplo de polimorfismo pertinente,
     * usado por ejemplo por el Notificador al componer mensajes).
     */
    public abstract String getRol();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario usuario)) return false;
        return id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getRol() + "{id='" + id + "', nombre='" + nombre + "'}";
    }
}
