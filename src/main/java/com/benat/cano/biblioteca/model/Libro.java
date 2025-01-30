package com.benat.cano.biblioteca.model;

import java.sql.Blob;
import java.util.Objects;
/**
 * Representa un libro en la biblioteca.
 * La clase contiene información sobre un libro, como su código, título, autor,
 * editorial, estado, baja y la portada (en formato Blob).
 */
public class Libro {
    /**
     * El código único del libro.
     */
    private int codigo;
    /**
     * El título del libro.
     */
    private String titulo;
    /**
     * El autor del libro.
     */
    private String autor;
    /**
     * La editorial del libro.
     */
    private String editorial;
    /**
     * El estado del libro (por ejemplo, disponible, prestado, etc.).
     */
    private String estado;
    /**
     * El estado de baja del libro (0 si está activo, 1 si está dado de baja).
     */
    private int baja;
    /**
     * La portada del libro en formato Blob (para almacenar una imagen binaria).
     */
    private Blob portada;
    /**
     * Constructor que inicializa un libro con los valores proporcionados.
     *
     * @param codigo El código único del libro.
     * @param titulo El título del libro.
     * @param autor El autor del libro.
     * @param editorial La editorial del libro.
     * @param estado El estado del libro (por ejemplo, disponible, prestado).
     * @param baja El estado de baja del libro (0 si está activo, 1 si está dado de baja).
     * @param portada La portada del libro en formato Blob.
     */
    public Libro(int codigo, String titulo, String autor, String editorial, String estado, int baja, Blob portada) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.estado = estado;
        this.baja = baja;
        this.portada = portada;
    }
    /**
     * Constructor vacío para crear un objeto de tipo Libro.
     * Este constructor puede ser útil para crear instancias vacías y luego
     * establecer los valores con los métodos setters.
     */
    public Libro() {}
    /**
     * Devuelve una representación en forma de cadena del libro, que incluye solo el título.
     *
     * @return Una cadena que contiene el título del libro.
     */
    @Override
    public String toString() {
        return titulo;
    }
    /**
     * Obtiene el código único del libro.
     *
     * @return El código del libro.
     */
    public int getCodigo() {
        return codigo;
    }
    /**
     * Establece el código único del libro.
     *
     * @param codigo El código del libro.
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    /**
     * Obtiene el título del libro.
     *
     * @return El título del libro.
     */
    public String getTitulo() {
        return titulo;
    }
    /**
     * Establece el título del libro.
     *
     * @param titulo El título del libro.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    /**
     * Obtiene el autor del libro.
     *
     * @return El autor del libro.
     */
    public String getAutor() {
        return autor;
    }
    /**
     * Establece el autor del libro.
     *
     * @param autor El autor del libro.
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }
    /**
     * Obtiene la editorial del libro.
     *
     * @return La editorial del libro.
     */
    public String getEditorial() {
        return editorial;
    }
    /**
     * Establece la editorial del libro.
     *
     * @param editorial La editorial del libro.
     */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
    /**
     * Obtiene el estado del libro.
     *
     * @return El estado del libro (por ejemplo, disponible, prestado, etc.).
     */
    public String getEstado() {
        return estado;
    }
    /**
     * Establece el estado del libro.
     *
     * @param estado El estado del libro.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /**
     * Obtiene el estado de baja del libro.
     *
     * @return El estado de baja del libro (0 si está activo, 1 si está dado de baja).
     */
    public int getBaja() {
        return baja;
    }
    /**
     * Establece el estado de baja del libro.
     *
     * @param baja El estado de baja del libro.
     */
    public void setBaja(int baja) {
        this.baja = baja;
    }
    /**
     * Obtiene la portada del libro en formato Blob.
     *
     * @return La portada del libro en formato Blob.
     */
    public Blob getPortada() {
        return portada;
    }
    /**
     * Establece la portada del libro en formato Blob.
     *
     * @param portada La portada del libro.
     */
    public void setPortada(Blob portada) {
        this.portada = portada;
    }
    /**
     * Compara este libro con otro objeto para ver si son iguales.
     * Dos libros se consideran iguales si tienen el mismo código.
     *
     * @param o El objeto a comparar.
     * @return {@code true} si los libros son iguales, {@code false} si no lo son.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return codigo == libro.codigo;
    }
    /**
     * Genera un código hash basado en el código del libro.
     *
     * @return El código hash del libro.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(codigo);
    }
}
