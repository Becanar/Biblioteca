package com.benat.cano.biblioteca.dao;

import com.benat.cano.biblioteca.db.ConectorDB;
import com.benat.cano.biblioteca.model.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoLibro {

    public static Libro getLibro(int codigo) {
        ConectorDB connection;
        Libro libro = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT codigo,titulo,autor,editorial,estado,baja,portada FROM Libro WHERE codigo = ?";
            PreparedStatement ps = connection.getConnection().prepareStatement(consulta);
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int codigo_db = rs.getInt("codigo");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                String estado = rs.getString("estado");
                int baja = rs.getInt("baja");
                Blob portada = rs.getBlob("portada");
                libro = new Libro(codigo_db, titulo, autor, editorial, estado, baja, portada);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return libro;
    }
    public static Libro getLibroPorTituloYAutor(String tituloLibro, String autorLibro) {
        ConectorDB connection;
        Libro libro = null;
        try {
            connection = new ConectorDB();
            // Modificamos la consulta SQL para que busque por título y autor
            String consulta = "SELECT codigo, titulo, autor, editorial, estado, baja, portada FROM Libro WHERE titulo = ? AND autor = ?";
            PreparedStatement ps = connection.getConnection().prepareStatement(consulta);
            ps.setString(1, tituloLibro);  // Asignamos el valor del título
            ps.setString(2, autorLibro);   // Asignamos el valor del autor
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int codigo_db = rs.getInt("codigo");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                String estado = rs.getString("estado");
                int baja = rs.getInt("baja");
                Blob portada = rs.getBlob("portada");
                libro = new Libro(codigo_db, titulo, autor, editorial, estado, baja, portada);
            }

            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return libro;
    }



    public static ObservableList<Libro> cargarListado() {
        ConectorDB connection;
        ObservableList<Libro> libros = FXCollections.observableArrayList();
        try{
            connection = new ConectorDB();
            String consulta = "SELECT codigo,titulo,autor,editorial,estado,baja,portada FROM Libro";
            PreparedStatement ps = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int codigo_db = rs.getInt("codigo");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                String estado = rs.getString("estado");
                int baja = rs.getInt("baja");
                Blob portada = rs.getBlob("portada");
                Libro libro = new Libro(codigo_db, titulo, autor, editorial, estado, baja, portada);
                libros.add(libro);
            }
            rs.close();
            connection.closeConexion();
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return libros;
    }

    /**
     * Convierte un archivo de tipo File a un objeto Blob para ser almacenado en la base de datos.
     *
     * @param file el archivo a convertir a Blob
     * @return el Blob generado a partir del archivo
     * @throws SQLException en caso de errores al trabajar con la base de datos
     * @throws IOException  en caso de errores al leer el archivo
     */
    public static Blob convertFileToBlob(File file) throws SQLException, IOException {
        ConectorDB connection = new ConectorDB();
        // Open a connection to the database
        try (Connection conn = connection.getConnection();
             FileInputStream inputStream = new FileInputStream(file)) {

            // Create Blob
            Blob blob = conn.createBlob();
            // Write the file's bytes to the Blob
            byte[] buffer = new byte[1024];
            int bytesRead;

            try (var outputStream = blob.setBinaryStream(1)) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return blob;
        }
    }

    public static boolean esEliminable(Libro libro) {
        ConectorDB connection;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT count(*) as cont FROM Prestamo WHERE codigo_libro = ?";
            PreparedStatement ps = connection.getConnection().prepareStatement(consulta);
            ps.setInt(1, libro.getCodigo());
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
            consulta = "SELECT count(*) as cont FROM Historico_prestamo WHERE codigo_libro = ?";
            ps = connection.getConnection().prepareStatement(consulta);
            ps.setInt(1, libro.getCodigo());
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

    public static boolean modificar(Libro libro) {
        ConectorDB connection;
        PreparedStatement ps;
        try {
            connection = new ConectorDB();
            String consulta = "UPDATE Libro SET titulo = ?,autor = ?,editorial = ?,estado = ?,baja = ?,portada = ? WHERE codigo = ?";
            ps = connection.getConnection().prepareStatement(consulta);
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getEditorial());
            ps.setString(4, libro.getEstado());
            ps.setInt(5, libro.getBaja());
            ps.setBlob(6, libro.getPortada());
            ps.setInt(7, libro.getCodigo());
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

    public static boolean darDeBaja(Libro libro) {
        ConectorDB connection;
        PreparedStatement ps;
        try {
            connection = new ConectorDB();
            String consulta = "UPDATE Libro SET baja = 1 WHERE codigo = ?";
            ps = connection.getConnection().prepareStatement(consulta);
            ps.setInt(1, libro.getCodigo());
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

    public  static int insertar(Libro libro) {
        ConectorDB connection;
        PreparedStatement ps;
        try {
            connection = new ConectorDB();
            String consulta = "INSERT INTO Libro (titulo,autor,editorial,estado,baja,portada) VALUES (?,?,?,?,?,?) ";
            ps = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getEditorial());
            ps.setString(4, libro.getEstado());
            ps.setInt(5, libro.getBaja());
            ps.setBlob(6, libro.getPortada());
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

    public static boolean eliminar(Libro libro) {
        ConectorDB connection;
        PreparedStatement ps;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM Libro WHERE codigo = ?";
            ps = connection.getConnection().prepareStatement(consulta);
            ps.setInt(1, libro.getCodigo());
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
