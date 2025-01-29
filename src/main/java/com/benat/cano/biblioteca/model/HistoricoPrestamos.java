package com.benat.cano.biblioteca.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class HistoricoPrestamos {

        private int id_prestamo;
        private Alumno alumno;
        private Libro libro;
        private LocalDateTime fecha_prestamo;
        private LocalDateTime fecha_devolucion;

        public HistoricoPrestamos(int id_prestamo, Alumno alumno, Libro libro, LocalDateTime fecha_prestamo, LocalDateTime fecha_devolucion) {
            this.id_prestamo = id_prestamo;
            this.alumno = alumno;
            this.libro = libro;
            this.fecha_prestamo = fecha_prestamo;
            this.fecha_devolucion = fecha_devolucion;
        }

        public HistoricoPrestamos() {}

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

        public LocalDateTime getFecha_devolucion() {
            return fecha_devolucion;
        }

        public void setFecha_devolucion(LocalDateTime fecha_devolucion) {
            this.fecha_devolucion = fecha_devolucion;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            HistoricoPrestamos p = (HistoricoPrestamos) o;
            return id_prestamo == p.id_prestamo;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id_prestamo);
        }
    }


