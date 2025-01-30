package com.benat.cano.biblioteca.dao;

import com.benat.cano.biblioteca.db.ConectorDB;
import com.benat.cano.biblioteca.model.Alumno;
import com.benat.cano.biblioteca.model.Libro;
import com.benat.cano.biblioteca.model.Prestamo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
/**
 * Clase DAO (Data Access Object) encargada de gestionar las operaciones de acceso a datos
 * relacionadas con los préstamos de libros en la base de datos.
 * Permite cargar la lista de préstamos, insertar un nuevo préstamo y eliminar un préstamo.
 */
public class DaoPrestamo {
    /**
     * Carga la lista de préstamos desde la base de datos y la devuelve como una lista observable.
     *
     * @return Una lista observable de objetos {@link Prestamo} que representan los préstamos cargados desde la base de datos.
     */
    public static ObservableList<Prestamo> cargarListado() {
        ConectorDB connection;
        ObservableList<Prestamo> prestamos = FXCollections.observableArrayList();
        try{
            connection = new ConectorDB();
            String consulta = "SELECT id_prestamo,dni_alumno,codigo_libro,fecha_prestamo FROM Prestamo";
            PreparedStatement ps = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id_prestamo_db = rs.getInt("id_prestamo");
                String dni_alumno = rs.getString("dni_alumno");
                Alumno alumno = DaoAlumno.getAlumno(dni_alumno);
                int codigo_libro = rs.getInt("codigo_libro");
                Libro libro = DaoLibro.getLibro(codigo_libro);
                LocalDateTime fecha_prestamo = rs.getTimestamp("fecha_prestamo").toLocalDateTime();
                Prestamo prestamo = new Prestamo(id_prestamo_db, alumno, libro, fecha_prestamo);
                prestamos.add(prestamo);
            }
            rs.close();
            connection.closeConexion();
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return prestamos;
    }
    /**
     * Inserta un nuevo préstamo en la base de datos.
     *
     * @param prestamo El objeto {@link Prestamo} que contiene los detalles del préstamo a insertar.
     * @return El ID del préstamo insertado en la base de datos o -1 si la inserción falló.
     */
    public  static int insertar(Prestamo prestamo) {
        ConectorDB connection;
        PreparedStatement ps;
        try {
            connection = new ConectorDB();
            String consulta = "INSERT INTO Prestamo (dni_alumno,codigo_libro,fecha_prestamo) VALUES (?,?,?) ";
            ps = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, prestamo.getAlumno().getDni());
            ps.setInt(2, prestamo.getLibro().getCodigo());
            ps.setTimestamp(3, Timestamp.valueOf(prestamo.getFecha_prestamo()));
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    ps.close();
                    connection.closeConexion();
                    return id;
                }
            }
            ps.close();
            connection.closeConexion();
            return -1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Elimina un préstamo de la base de datos.
     *
     * @param prestamo El objeto {@link Prestamo} que representa el préstamo a eliminar.
     * @return {@code true} si la eliminación fue exitosa, {@code false} si no lo fue.
     */
    public static boolean eliminar(Prestamo prestamo) {
        ConectorDB connection;
        PreparedStatement ps;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM Prestamo WHERE id_prestamo = ?";
            ps = connection.getConnection().prepareStatement(consulta);
            ps.setInt(1, prestamo.getId_prestamo());
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            connection.closeConexion();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
