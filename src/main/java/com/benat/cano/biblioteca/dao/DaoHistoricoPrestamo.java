package com.benat.cano.biblioteca.dao;


import com.benat.cano.biblioteca.db.ConectorDB;
import com.benat.cano.biblioteca.model.Alumno;
import com.benat.cano.biblioteca.model.HistoricoPrestamos;
import com.benat.cano.biblioteca.model.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
/**
 * Clase de acceso a los datos de los historiales de préstamos de libros en la biblioteca.
 * Proporciona métodos para consultar y agregar registros en la tabla Historico_prestamo.
 */
public class DaoHistoricoPrestamo {
    /**
     * Carga el listado completo de los préstamos históricos.
     *
     * @return una lista observable de objetos {@link HistoricoPrestamos} que representan los registros históricos de los préstamos.
     */
    public static ObservableList<HistoricoPrestamos> cargarListado() {
        ConectorDB connection;
        ObservableList<HistoricoPrestamos> prestamos = FXCollections.observableArrayList();
        try{
            connection = new ConectorDB();
            String consulta = "SELECT id_prestamo,dni_alumno,codigo_libro,fecha_prestamo,fecha_devolucion FROM Historico_prestamo";
            PreparedStatement ps = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id_prestamo_db = rs.getInt("id_prestamo");
                String dni_alumno = rs.getString("dni_alumno");
                Alumno alumno = DaoAlumno.getAlumno(dni_alumno);
                int codigo_libro = rs.getInt("codigo_libro");
                Libro libro = DaoLibro.getLibro(codigo_libro);
                LocalDateTime fecha_prestamo = rs.getTimestamp("fecha_prestamo").toLocalDateTime();
                LocalDateTime fecha_devolucion = rs.getTimestamp("fecha_devolucion").toLocalDateTime();
                HistoricoPrestamos prestamo = new HistoricoPrestamos(id_prestamo_db, alumno, libro, fecha_prestamo, fecha_devolucion);
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
     * Inserta un nuevo registro en el historial de préstamos.
     *
     * @param prestamo el objeto {@link HistoricoPrestamos} que se va a insertar en la base de datos.
     * @return {@code true} si la inserción fue exitosa, {@code false} en caso contrario.
     */
    public  static boolean insertar(HistoricoPrestamos prestamo) {
        ConectorDB connection;
        PreparedStatement ps;
        try {
            connection = new ConectorDB();
            String consulta = "INSERT INTO Historico_prestamo (id_prestamo,dni_alumno,codigo_libro,fecha_prestamo,fecha_devolucion) VALUES (?,?,?,?,?) ";
            ps = connection.getConnection().prepareStatement(consulta);
            ps.setInt(1, prestamo.getId_prestamo());
            ps.setString(2, prestamo.getAlumno().getDni());
            ps.setInt(3, prestamo.getLibro().getCodigo());
            ps.setTimestamp(4, Timestamp.valueOf(prestamo.getFecha_prestamo()));
            ps.setTimestamp(5, Timestamp.valueOf(prestamo.getFecha_devolucion()));
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            connection.closeConexion();
            return (filasAfectadas > 0);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
