package com.benat.cano.biblioteca.model;

import java.time.LocalDateTime;
/**
 * Representa un préstamo de un libro a un alumno en la biblioteca.
 * La clase contiene información sobre el préstamo, como el identificador del préstamo,
 * el alumno que realizó el préstamo, el libro prestado y la fecha del préstamo.
 */
public class Prestamo {
    /**
     * El identificador único del préstamo.
     */
    private int id_prestamo;
    /**
     * El alumno que realizó el préstamo.
     */
    private Alumno alumno;
    /**
     * El libro que fue prestado.
     */
    private Libro libro;
    /**
     * La fecha y hora en que se realizó el préstamo.
     */
    private LocalDateTime fecha_prestamo;
    /**
     * Constructor que inicializa un préstamo con los valores proporcionados.
     *
     * @param id_prestamo El identificador único del préstamo.
     * @param alumno El alumno que realiza el préstamo.
     * @param libro El libro que se va a prestar.
     * @param fecha_prestamo La fecha y hora del préstamo.
     */
    public Prestamo(int id_prestamo, Alumno alumno, Libro libro, LocalDateTime fecha_prestamo) {
        this.id_prestamo = id_prestamo;
        this.alumno = alumno;
        this.libro = libro;
        this.fecha_prestamo = fecha_prestamo;
    }
    /**
     * Constructor vacío para crear un objeto de tipo Prestamo.
     * Este constructor puede ser útil para crear instancias vacías y luego
     * establecer los valores con los métodos setters.
     */
    public Prestamo() {}
    /**
     * Devuelve una representación en forma de cadena del préstamo.
     *
     * @return Una cadena que contiene el id del préstamo, el alumno, el libro y la fecha del préstamo.
     */
    @Override
    public String toString() {
        return id_prestamo + " - " + alumno + " - " + libro + " - " + fecha_prestamo;
    }
    /**
     * Obtiene el identificador único del préstamo.
     *
     * @return El identificador del préstamo.
     */
    public int getId_prestamo() {
        return id_prestamo;
    }
    /**
     * Establece el identificador único del préstamo.
     *
     * @param id_prestamo El identificador del préstamo.
     */
    public void setId_prestamo(int id_prestamo) {
        this.id_prestamo = id_prestamo;
    }
    /**
     * Obtiene el alumno que realizó el préstamo.
     *
     * @return El alumno que realizó el préstamo.
     */
    public Alumno getAlumno() {
        return alumno;
    }
    /**
     * Establece el alumno que realizó el préstamo.
     *
     * @param alumno El alumno que realizó el préstamo.
     */
    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }
    /**
     * Obtiene el libro que fue prestado.
     *
     * @return El libro prestado.
     */
    public Libro getLibro() {
        return libro;
    }
    /**
     * Establece el libro que fue prestado.
     *
     * @param libro El libro prestado.
     */
    public void setLibro(Libro libro) {
        this.libro = libro;
    }
    /**
     * Obtiene la fecha y hora del préstamo.
     *
     * @return La fecha y hora en que se realizó el préstamo.
     */
    public LocalDateTime getFecha_prestamo() {
        return fecha_prestamo;
    }
    /**
     * Establece la fecha y hora del préstamo.
     *
     * @param fecha_prestamo La fecha y hora del préstamo.
     */
    public void setFecha_prestamo(LocalDateTime fecha_prestamo) {
        this.fecha_prestamo = fecha_prestamo;
    }
}

