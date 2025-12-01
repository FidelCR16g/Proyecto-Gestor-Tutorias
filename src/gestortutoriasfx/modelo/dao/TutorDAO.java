package gestortutoriasfx.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Nombre de la Clase: TutorDAO
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 25/11/2025
 *
 * Descripción:
 * Clase encargada del acceso y ejecucion de las consultas dentro de la base de datos.
 */

public class TutorDAO {
    public static int obtenerIdTutor(Connection conexion, int idUsuario) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        int idTutor = 0;
        String consulta = "SELECT idTutor FROM tutor WHERE idUsuario = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idUsuario);
            
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    idTutor = resultado.getInt("idTutor");
                }
            }
        }
        
        return idTutor;
    }
}
