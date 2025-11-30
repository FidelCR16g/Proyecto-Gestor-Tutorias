package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.Evidencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Nombre de la Clase: EvidenciaDAO
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 28/11/2025
 *
 * Descripción:
 * Clase encargada del acceso y ejecucion de las consultas dentro de la base de datos.
 */

public class EvidenciaDAO {
    public static int eliminarEvidencia(Connection conexion, int idEvidencia) throws SQLException {
        if(conexion != null){
            String consulta = "DELETE FROM evidencia WHERE idEvidencia = ?";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idEvidencia);
            return sentencia.executeUpdate();
        }
        throw new SQLException ("No hay conexion con la base de datos.");
    }
    
    public static int guardarEvidencia(Connection conexion, Evidencia evidencia) throws SQLException {
        if(conexion != null){
            String consulta = "INSERT INTO evidencia (idSesion, nombreArchivo, archivo) VALUES (?, ?, ?)";
        
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, evidencia.getIdSesion());
            sentencia.setString(2, evidencia.getNombreArchivo());
            sentencia.setBytes(3, evidencia.getArchivo());
        
            return sentencia.executeUpdate();
        }
        throw new SQLException ("No hay conexion con la base de datos.");
    }
    
    public static ResultSet obtenerEvidenciasPorIdSesion(Connection conexion, int idSesion) throws SQLException {
        if(conexion != null){
            String consulta = "SELECT idEvidencia, idSesion, nombreArchivo, LENGTH(archivo) as pesoBytes " +
                     "FROM evidencia WHERE idSesion = ?";
        
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idSesion);
        
            return sentencia.executeQuery();
        }
        throw new SQLException ("No hay conexion con la base de datos.");
    }
    
    public static int obtenerIdSesionRepresentativa(Connection conexion, int idTutor, int numSesion) throws SQLException {
        if(conexion != null){
            String consulta = "SELECT idSesion FROM sesionTutoria " +
                     "WHERE idTutor = ? AND numSesion = ? " +
                     "ORDER BY idSesion DESC LIMIT 1";
            
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idTutor);
            sentencia.setInt(2, numSesion);
        
            ResultSet resultSet = sentencia.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("idSesion");
            }
            return 0;
        }
        throw new SQLException ("No hay conexion con la base de datos.");
    }   
}