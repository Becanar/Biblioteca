package com.benat.cano.biblioteca.dao;


import com.benat.cano.biblioteca.db.ConectorDB;
import com.benat.cano.biblioteca.model.Alumno;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Clase DAO (Data Access Object) para gestionar las operaciones con la entidad Alumno en la base de datos.
 * Contiene métodos para obtener, insertar, modificar, eliminar y consultar información sobre los alumnos.
 */
public class DaoAlumno {
    /**
     * Obtiene un alumno de la base de datos a partir de su DNI.
     *
     * @param dni el DNI del alumno a buscar
     * @return un objeto Alumno con la información del alumno encontrado, o null si no se encuentra
     */

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
    /**
     * Carga una lista de todos los alumnos registrados en la base de datos.
     *
     * @return una lista observable con los alumnos registrados
     */
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
    /**
     * Comprueba si un alumno es eliminable, lo cual depende de si ha realizado algún préstamo
     * o si tiene registros en el historial de préstamos.
     *
     * @param alumno el objeto Alumno a verificar
     * @return true si el alumno es eliminable (no tiene préstamos activos ni en el historial), false en caso contrario
     */
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
    /**
     * Modifica los datos de un alumno en la base de datos.
     *
     * @param alumno el objeto Alumno con los nuevos datos a guardar
     * @return true si la actualización fue exitosa, false en caso contrario
     */
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
    /**
     * Inserta un nuevo alumno en la base de datos.
     *
     * @param alumno el objeto Alumno a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
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
    /**
     * Elimina un alumno de la base de datos.
     *
     * @param alumno el objeto Alumno a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
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
