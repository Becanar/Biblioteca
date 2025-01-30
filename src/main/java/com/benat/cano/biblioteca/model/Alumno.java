package com.benat.cano.biblioteca.model;

import java.util.Objects;
/**
 * Representa a un alumno de la biblioteca.
 * Esta clase contiene la información personal de un alumno, como su DNI, nombre y apellidos.
 */
public class Alumno {
    /**
     * El DNI del alumno.
     */
    private String dni;
    /**
     * El nombre del alumno.
     */
    private String nombre;
    /**
     * El primer apellido del alumno.
     */
    private String apellido1;
    /**
     * El segundo apellido del alumno.
     */
    private String apellido2;
    /**
     * Constructor que inicializa un alumno con los valores proporcionados.
     *
     * @param dni El DNI del alumno.
     * @param nombre El nombre del alumno.
     * @param apellido1 El primer apellido del alumno.
     * @param apellido2 El segundo apellido del alumno.
     */
    public Alumno(String dni, String nombre, String apellido1, String apellido2) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }
    /**
     * Constructor vacío para crear un objeto de tipo Alumno.
     * Este constructor puede ser útil para crear instancias vacías y luego
     * establecer los valores con los métodos setters.
     */
    public Alumno() {}
    /**
     * Devuelve el nombre del alumno.
     *
     * @return El nombre del alumno.
     */
    @Override
    public String toString() {
        return nombre;
    }
    /**
     * Obtiene el DNI del alumno.
     *
     * @return El DNI del alumno.
     */
    public String getDni() {
        return dni;
    }
    /**
     * Establece el DNI del alumno.
     *
     * @param dni El DNI del alumno.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }
    /**
     * Obtiene el nombre del alumno.
     *
     * @return El nombre del alumno.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * Establece el nombre del alumno.
     *
     * @param nombre El nombre del alumno.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * Obtiene el primer apellido del alumno.
     *
     * @return El primer apellido del alumno.
     */
    public String getApellido1() {
        return apellido1;
    }
    /**
     * Obtiene el primer apellido del alumno.
     *
     * @return El primer apellido del alumno.
     */
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }
    /**
     * Obtiene el segundo apellido del alumno.
     *
     * @return El segundo apellido del alumno.
     */
    public String getApellido2() {
        return apellido2;
    }
    /**
     * Establece el segundo apellido del alumno.
     *
     * @param apellido2 El segundo apellido del alumno.
     */
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }
    /**
     * Compara este alumno con otro objeto para ver si son iguales.
     * Dos alumnos se consideran iguales si tienen el mismo DNI.
     *
     * @param o El objeto a comparar.
     * @return {@code true} si los alumnos son iguales, {@code false} si no lo son.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Alumno alumno = (Alumno) o;
        return Objects.equals(dni, alumno.dni);
    }

    /**
     * Genera un código hash basado en el DNI del alumno.
     *
     * @return El código hash del alumno.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(dni);
    }
}
