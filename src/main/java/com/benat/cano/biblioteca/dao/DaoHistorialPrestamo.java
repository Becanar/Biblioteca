package com.benat.cano.biblioteca.dao;


import com.benat.cano.biblioteca.db.ConectorDB;
import com.benat.cano.biblioteca.model.Alumno;
import com.benat.cano.biblioteca.model.HistorialPrestamos;
import com.benat.cano.biblioteca.model.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DaoHistorialPrestamo {

    public static HistorialPrestamos getHistorialPrestamo(String id_prestamo) {
        ConectorDB connection;
        HistorialPrestamos prestamo = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id_prestamo,dni_alumno,codigo_libro,fecha_prestamo,fecha_devolucion FROM Historico_prestamo WHERE id_prestamo = ?";
            PreparedStatement ps = connection.getConnection().prepareStatement(consulta);
            ps.setString(1, id_prestamo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id_prestamo_db = rs.getInt("id_prestamo");
                String dni_alumno = rs.getString("dni_alumno");
                Alumno alumno = DaoAlumno.getAlumno(dni_alumno);
                int codigo_libro = rs.getInt("codigo_libro");
                Libro libro = DaoLibro.getLibro(codigo_libro);
                LocalDateTime fecha_prestamo = rs.getTimestamp("fecha_prestamo").toLocalDateTime();
                LocalDateTime fecha_devolucion = rs.getTimestamp("fecha_devolucion").toLocalDateTime();
                prestamo = new HistorialPrestamos(id_prestamo_db, alumno, libro, fecha_prestamo, fecha_devolucion);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return prestamo;
    }

    public static ObservableList<HistorialPrestamos> historialDeAlumno(Alumno alumno) {
        ConectorDB connection;
        ObservableList<HistorialPrestamos> prestamos = FXCollections.observableArrayList();
        try{
            connection = new ConectorDB();
            String consulta = "SELECT id_prestamo,dni_alumno,codigo_libro,fecha_prestamo,fecha_devolucion FROM Historico_prestamo WHERE dni_alumno = ?";
            PreparedStatement ps = connection.getConnection().prepareStatement(consulta);
            ps.setString(1, alumno.getDni());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id_prestamo_db = rs.getInt("id_prestamo");
                String dni_alumno = rs.getString("dni_alumno");
                Alumno alumno_db = DaoAlumno.getAlumno(dni_alumno);
                int codigo_libro = rs.getInt("codigo_libro");
                Libro libro = DaoLibro.getLibro(codigo_libro);
                LocalDateTime fecha_prestamo = rs.getTimestamp("fecha_prestamo").toLocalDateTime();
                LocalDateTime fecha_devolucion = rs.getTimestamp("fecha_devolucion").toLocalDateTime();
                HistorialPrestamos prestamo = new HistorialPrestamos(id_prestamo_db, alumno_db, libro, fecha_prestamo, fecha_devolucion);
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

    public static ObservableList<HistorialPrestamos> cargarListado() {
        ConectorDB connection;
        ObservableList<HistorialPrestamos> prestamos = FXCollections.observableArrayList();
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
                HistorialPrestamos prestamo = new HistorialPrestamos(id_prestamo_db, alumno, libro, fecha_prestamo, fecha_devolucion);
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

    public static boolean modificar(HistorialPrestamos prestamo) {
        ConectorDB connection;
        PreparedStatement ps;
        try {
            connection = new ConectorDB();
            String consulta = "UPDATE Historico_prestamo SET dni_alumno = ?,codigo_libro = ?,fecha_prestamo = ?,fecha_devolucion = ? WHERE id_prestamo = ?";
            ps = connection.getConnection().prepareStatement(consulta);
            ps.setString(1, prestamo.getAlumno().getDni());
            ps.setInt(2, prestamo.getLibro().getCodigo());
            ps.setTimestamp(3, Timestamp.valueOf(prestamo.getFecha_prestamo()));
            ps.setTimestamp(4, Timestamp.valueOf(prestamo.getFecha_devolucion()));
            ps.setInt(5, prestamo.getId_prestamo());
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

    public  static boolean insertar(HistorialPrestamos prestamo) {
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

    public static boolean eliminar(HistorialPrestamos prestamo) {
        ConectorDB connection;
        PreparedStatement ps;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM Historico_prestamo WHERE id_prestamo = ?";
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
