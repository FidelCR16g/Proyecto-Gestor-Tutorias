package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.Evidencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        int filasAfectadas = 0;
        
        String consulta = "DELETE FROM evidencia WHERE idEvidencia = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idEvidencia);
            filasAfectadas = sentencia.executeUpdate();
        }
        
        return filasAfectadas;
    }
    
    public static int guardarEvidencia(Connection conexion, Evidencia evidencia) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        int filasAfectadas = 0;
        
        String consulta = "INSERT INTO evidencia (idSesion, nombreArchivo, archivo) VALUES (?, ?, ?)";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, evidencia.getIdSesion());
            sentencia.setString(2, evidencia.getNombreArchivo());
            sentencia.setBytes(3, evidencia.getArchivo());
            filasAfectadas = sentencia.executeUpdate();
        }
        
        return filasAfectadas;
    }
    
    public static ArrayList<Evidencia> obtenerEvidenciasPorIdSesion(Connection conexion, int idSesion) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        ArrayList<Evidencia> evidencias = new ArrayList<>();
        
        String consulta = "SELECT idEvidencia, idSesion, nombreArchivo, LENGTH(archivo) as pesoBytes " +
                "FROM evidencia WHERE idSesion = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idSesion);
            
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    Evidencia evidencia = new Evidencia();
                    evidencia.setIdEvidencia(resultado.getInt("idEvidencia"));
                    evidencia.setIdSesion(resultado.getInt("idSesion"));
                    evidencia.setNombreArchivo(resultado.getString("nombreArchivo"));
                    long bytes = resultado.getLong("pesoBytes");
                    double kb = bytes / 1024.0;
                    evidencia.setTamanoKB(Math.round(kb * 100.0) / 100.0);
                    evidencia.setEsNuevo(false);
                    evidencias.add(evidencia);
                }
            }
            
        }
        
        return evidencias;
    }
    
    public static int obtenerIdSesionRepresentativa(Connection conexion, int idTutor, int numSesion) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        int idSesion = 0;
        
        String consulta = "SELECT idSesion FROM sesionTutoria " +
                "WHERE idTutor = ? AND numSesion = ? " +
                "ORDER BY idSesion DESC LIMIT 1";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            sentencia.setInt(2, numSesion);
            
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    idSesion = resultado.getInt("idSesion");
                }
            }
            
        }
        
        return idSesion;
    }
}