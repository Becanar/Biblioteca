package com.benat.cano.biblioteca.model;

import java.time.LocalDateTime;

public class Prestamo {

    private int id_prestamo;
    private Alumno alumno;
    private Libro libro;
    private LocalDateTime fecha_prestamo;

    public Prestamo(int id_prestamo, Alumno alumno, Libro libro, LocalDateTime fecha_prestamo) {
        this.id_prestamo = id_prestamo;
        this.alumno = alumno;
        this.libro = libro;
        this.fecha_prestamo = fecha_prestamo;
    }

    public Prestamo() {}

    @Override
    public String toString() {
        return id_prestamo + " - " + alumno + " - " + libro + " - " + fecha_prestamo;
    }

    public int getId_prestamo() {
        return id_prestamo;
    }

    public void setId_prestamo(int id_prestamo) {
        this.id_prestamo = id_prestamo;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public LocalDateTime getFecha_prestamo() {
        return fecha_prestamo;
    }

    public void setFecha_prestamo(LocalDateTime fecha_prestamo) {
        this.fecha_prestamo = fecha_prestamo;
    }
}

