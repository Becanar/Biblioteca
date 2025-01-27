package com.benat.cano.biblioteca.model;

import java.io.*;
import java.util.Properties;

/**
 * Clase abstracta para gestionar las propiedades de configuración.
 * Carga las propiedades desde un archivo externo (db.properties)
 * y proporciona acceso a sus valores a través de un método estático.
 */
public abstract class Propiedades {
	private static Properties props = new Properties(); ///< Propiedades cargadas desde el archivo.

	static {
		try (FileInputStream input = new FileInputStream("db.properties")) {
			props.load(input); // Carga las propiedades desde el archivo
		} catch (Exception e) {
			e.printStackTrace(); // Manejo de excepciones durante la carga
		}
	}

	/**
	 * Obtiene el valor asociado a la clave proporcionada desde el archivo de propiedades.
	 *
	 * @param clave La clave para la cual se desea obtener el valor.
	 * @return El valor asociado a la clave, o lanza una excepción si no se encuentra.
	 * @throws RuntimeException si la clave no existe en las propiedades.
	 */
	public static String getValor(String clave) {
		try (FileInputStream input = new FileInputStream("db.properties")) {
			props.load(input); // Carga las propiedades desde el archivo
		} catch (Exception e) {
			e.printStackTrace(); // Manejo de excepciones durante la carga
		}
		String valor = props.getProperty(clave); // Recupera el valor correspondiente a la clave
		if (valor != null) {
			return valor; // Retorna el valor si se encuentra
		}
		throw new RuntimeException("El fichero de configuracion no existe"); // Lanza excepción si no se encuentra
	}
	/**
	 * Actualiza la configuración del idioma en el archivo "db.properties".
	 * <p>
	 * Este método carga las propiedades existentes desde el archivo "db.properties",
	 * cambia el valor asociado a la clave "language" al código de idioma especificado,
	 * y luego guarda las propiedades modificadas de nuevo en el archivo.
	 * </p>
	 *
	 * @param clave  No se usa en este método, reservado para uso futuro o para otras configuraciones.
	 * @param cambio El nuevo código de idioma a establecer, como "en" para inglés o "es" para español.
	 *
	 * <p> Si el archivo "db.properties" no existe o ocurre un error durante la
	 * lectura/escritura, se captura una excepción IOException y se imprime su traza.
	 * </p>
	 */
	public static void setIdioma(String clave, String cambio) {
		Properties properties = new Properties();

		// Cargar las propiedades existentes
		try (FileInputStream fis = new FileInputStream("db.properties")) {
			properties.load(fis);  // Carga las propiedades desde el archivo

			// Actualizar la propiedad 'language'
			properties.setProperty("language", cambio);

			// Guardar las propiedades modificadas de vuelta al archivo
			try (FileOutputStream fos = new FileOutputStream("db.properties")) {
				properties.store(fos, "Actualización del idioma"); // Guarda con un comentario si lo deseas
			}
		} catch (IOException e) {
			e.printStackTrace(); // Manejo de excepciones
		}
    }
}
