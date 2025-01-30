package com.benat.cano.biblioteca.model;

import java.time.LocalDateTime;
import java.util.Objects;
/**
 * Representa un historial de préstamos de libros en la biblioteca.
 * La clase contiene información sobre un préstamo histórico, incluyendo el libro,
 * el alumno que lo solicitó, la fecha de préstamo y la fecha de devolución.
 */
public class HistoricoPrestamos {
    /**
     * El identificador único del préstamo.
     */
        private int id_prestamo;
    /**
     * El alumno que realizó el préstamo.
     */
        private Alumno alumno;
    /**
     * El libro prestado.
     */
        private Libro libro;
    /**
     * La fecha en que se realizó el préstamo.
     */
        private LocalDateTime fecha_prestamo;
    /**
     * La fecha de devolución del libro.
     */
        private LocalDateTime fecha_devolucion;
    /**
     * Constructor que inicializa un historial de préstamo con los valores proporcionados.
     *
     * @param id_prestamo El identificador único del préstamo.
     * @param alumno El alumno que realizó el préstamo.
     * @param libro El libro prestado.
     * @param fecha_prestamo La fecha del préstamo.
     * @param fecha_devolucion La fecha de devolución del libro.
     */
        public HistoricoPrestamos(int id_prestamo, Alumno alumno, Libro libro, LocalDateTime fecha_prestamo, LocalDateTime fecha_devolucion) {
            this.id_prestamo = id_prestamo;
            this.alumno = alumno;
            this.libro = libro;
            this.fecha_prestamo = fecha_prestamo;
            this.fecha_devolucion = fecha_devolucion;
        }
    /**
     * Constructor vacío para crear un objeto de tipo HistoricoPrestamos.
     * Este constructor puede ser útil para crear instancias vacías y luego
     * establecer los valores con los métodos setters.
     */
        public HistoricoPrestamos() {}
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
     * @return El alumno del préstamo.
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
     * Obtiene el libro prestado.
     *
     * @return El libro prestado.
     */
        public Libro getLibro() {
            return libro;
        }
    /**
     * Establece el libro prestado.
     *
     * @param libro El libro prestado.
     */
        public void setLibro(Libro libro) {
            this.libro = libro;
        }
    /**
     * Obtiene la fecha del préstamo.
     *
     * @return La fecha en que se realizó el préstamo.
     */
        public LocalDateTime getFecha_prestamo() {
            return fecha_prestamo;
        }
    /**
     * Establece la fecha del préstamo.
     *
     * @param fecha_prestamo La fecha del préstamo.
     */
        public void setFecha_prestamo(LocalDateTime fecha_prestamo) {
            this.fecha_prestamo = fecha_prestamo;
        }
    /**
     * Obtiene la fecha de devolución del libro.
     *
     * @return La fecha de devolución del libro.
     */
        public LocalDateTime getFecha_devolucion() {
            return fecha_devolucion;
        }
    /**
     * Establece la fecha de devolución del libro.
     *
     * @param fecha_devolucion La fecha de devolución del libro.
     */
        public void setFecha_devolucion(LocalDateTime fecha_devolucion) {
            this.fecha_devolucion = fecha_devolucion;
        }
    /**
     * Compara este historial de préstamo con otro objeto para ver si son iguales.
     * Dos historiales de préstamo se consideran iguales si tienen el mismo identificador de préstamo.
     *
     * @param o El objeto a comparar.
     * @return {@code true} si los historiales de préstamo son iguales, {@code false} si no lo son.
     */
        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            HistoricoPrestamos p = (HistoricoPrestamos) o;
            return id_prestamo == p.id_prestamo;
        }
    /**
     * Genera un código hash basado en el identificador del préstamo.
     *
     * @return El código hash del historial de préstamo.
     */
        @Override
        public int hashCode() {
            return Objects.hashCode(id_prestamo);
        }
    }


