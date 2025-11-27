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
    public static ResultSet obtenerIdTutor(Connection conexion, int idUsuario) throws SQLException{
        if(conexion != null){
            String consulta = "SELECT idTutor FROM tutor WHERE idUsuario = ?";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idUsuario);
            return sentencia.executeQuery();
        }
        throw new SQLException ("No hay conexion con la base de datos.");
    }
}
