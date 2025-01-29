package com.benat.cano.biblioteca.dao;


import com.benat.cano.biblioteca.db.ConectorDB;
import com.benat.cano.biblioteca.model.Alumno;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoAlumno {


    public static Alumno getAlumno(String dni) {
        ConectorDB connection;
        Alumno alumno = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT dni,nombre,apellido1,apellido2 FROM Alumno WHERE dni = ?";
            PreparedStatement ps = connection.getConnection().prepareStatement(consulta);
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String dni_db = rs.getString("dni");
                String nombre = rs.getString("nombre");
                String apellido1 = rs.getString("apellido1");
                String apellido2 = rs.getString("apellido2");
                alumno = new Alumno(dni_db, nombre, apellido1, apellido2);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return alumno;
    }

    public static ObservableList<Alumno> cargarListado() {
        ConectorDB connection;
        ObservableList<Alumno> alumnos = FXCollections.observableArrayList();
        try{
            connection = new ConectorDB();
            String consulta = "SELECT dni,nombre,apellido1,apellido2 FROM Alumno";
            PreparedStatement ps = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String dni = rs.getString("dni");
                String nombre = rs.getString("nombre");
                String apellido1 = rs.getString("apellido1");
                String apellido2 = rs.getString("apellido2");
                Alumno alumno = new Alumno(dni, nombre, apellido1, apellido2);
                alumnos.add(alumno);
            }
            rs.close();
            connection.closeConexion();
        }catch (SQLException | FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return alumnos;
    }

    public static boolean esEliminable(Alumno alumno) {
        ConectorDB connection;
        try {
            connection = new ConectorDB();

            String consulta = "SELECT count(*) as cont FROM Prestamo WHERE dni_alumno = ?";
            PreparedStatement ps = connection.getConnection().prepareStatement(consulta);
            ps.setString(1, alumno.getDni());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int cont = rs.getInt("cont");
                if (cont != 0) {
                    rs.close();
                    connection.closeConexion();
                    return false;
                }
            }
            rs.close();
            ps.close();

            consulta = "SELECT count(*) as cont FROM Historico_prestamo WHERE dni_alumno = ?";
            ps = connection.getConnection().prepareStatement(consulta);
            ps.setString(1, alumno.getDni());
            rs = ps.executeQuery();
            if (rs.next()) {
                int cont = rs.getInt("cont");
                rs.close();
                connection.closeConexion();
                return (cont == 0);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean modificar(Alumno alumno) {
            ConectorDB connection;
        PreparedStatement ps;
        try {
            connection = new ConectorDB();
            String consulta = "UPDATE Alumno SET nombre = ?,apellido1 = ?,apellido2 = ? WHERE dni = ?";
            ps = connection.getConnection().prepareStatement(consulta);
            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getApellido1());
            ps.setString(3, alumno.getApellido2());
            ps.setString(4, alumno.getDni());
            int filasAfectadas = ps.executeUpdate();
            System.out.println("Filas afectadas: " + filasAfectadas);

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

    public  static boolean insertar(Alumno alumno) {
        ConectorDB connection;
        PreparedStatement ps;
        try {
            connection = new ConectorDB();
            String consulta = "INSERT INTO Alumno (dni,nombre,apellido1,apellido2) VALUES (?,?,?,?) ";
            ps = connection.getConnection().prepareStatement(consulta);
            ps.setString(1, alumno.getDni());
            ps.setString(2, alumno.getNombre());
            ps.setString(3, alumno.getApellido1());
            ps.setString(4, alumno.getApellido2());
            int filasAfectadas = ps.executeUpdate();
            connection.closeConexion();
            return (filasAfectadas > 0);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean eliminar(Alumno alumno) {
        ConectorDB connection;
        PreparedStatement ps;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM Alumno WHERE dni = ?";
            ps = connection.getConnection().prepareStatement(consulta);
            ps.setString(1, alumno.getDni());
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            connection.closeConexion();
            return filasAfectadas > 0;
        } catch (SQLException | FileNotFoundException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
