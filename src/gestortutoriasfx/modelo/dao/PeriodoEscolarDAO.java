package gestortutoriasfx.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Nombre de la Clase: PeriodoEscolarDAO
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 26/11/2025
 *
 * Descripción:
 * Clase encargada del acceso y ejecucion de las consultas dentro de la base de datos.
 */

public class PeriodoEscolarDAO {
    public static ResultSet obtenerPeriodoActual(Connection conexion) throws SQLException{
        if(conexion != null){
            String consulta = "SELECT * FROM periodoEscolar WHERE estado = 'Actual' LIMIT 1";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        throw new SQLException ("No hay conexion con la base de datos.");
    }
}
