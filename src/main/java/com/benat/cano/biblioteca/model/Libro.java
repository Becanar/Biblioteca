package com.benat.cano.biblioteca.model;

import java.sql.Blob;
import java.util.Objects;

public class Libro {

    private int codigo;
    private String titulo;
    private String autor;
    private String editorial;
    private String estado;
    private int baja;
    private Blob portada;

    public Libro(int codigo, String titulo, String autor, String editorial, String estado, int baja, Blob portada) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.estado = estado;
        this.baja = baja;
        this.portada = portada;
    }

    public Libro() {}

    @Override
    public String toString() {
        return titulo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getBaja() {
        return baja;
    }

    public void setBaja(int baja) {
        this.baja = baja;
    }

    public Blob getPortada() {
        return portada;
    }

    public void setPortada(Blob portada) {
        this.portada = portada;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return codigo == libro.codigo;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigo);
    }
}
