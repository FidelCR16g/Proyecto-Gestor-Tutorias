package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
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
    public static PeriodoEscolar obtenerPeriodoActual(Connection conexion) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        PeriodoEscolar periodo = null;
        
        String consulta = "SELECT * FROM periodoEscolar WHERE estado = 1 ORDER BY idPeriodoEscolar DESC LIMIT 1";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)){
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                periodo = new PeriodoEscolar();
                periodo.setIdPeriodoEscolar(resultado.getInt("idPeriodoEscolar"));
                periodo.setNombrePeriodoEscolar(resultado.getString("nombrePeriodoEscolar"));
                periodo.setFechaInicio(resultado.getString("fechaInicio"));
                periodo.setFechaFin(resultado.getString("fechaFin"));
                periodo.setEstado(resultado.getBoolean("estado"));
            }
            return periodo;
        }
    }
}