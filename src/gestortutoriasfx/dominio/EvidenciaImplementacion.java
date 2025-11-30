package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.EvidenciaDAO;
import gestortutoriasfx.modelo.pojo.Evidencia;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Nombre de la Clase: EvidenciaImplementacion
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
 * Se encarga de administrar la informacion de los distintos metodos de recuperacion de informacion de la 
 * evidencia de la tutoria de la base de datos.
 */

public class EvidenciaImplementacion {
    
    public static HashMap<String, Object> guardarCambiosEvidencias(
            List<Evidencia> nuevos, 
            List<Evidencia> eliminados, 
            int idTutor, 
            int numSesion) {
        
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        
        Connection conexion = ConexionBD.abrirConexionBD();
        
        if (conexion != null) {
            try {
                conexion.setAutoCommit(false);
                if (eliminados != null && !eliminados.isEmpty()) {
                    for (Evidencia ev : eliminados) {
                        EvidenciaDAO.eliminarEvidencia(conexion, ev.getIdEvidencia());
                    }
                }
                if (nuevos != null && !nuevos.isEmpty()) {
                    int idSesionDestino = EvidenciaDAO.obtenerIdSesionRepresentativa(conexion, idTutor, numSesion);
                    
                    if (idSesionDestino > 0) {
                        for (Evidencia ev : nuevos) {
                            if (ev.isEsNuevo()) {
                                ev.setIdSesion(idSesionDestino);
                                EvidenciaDAO.guardarEvidencia(conexion, ev);
                            }
                        }
                    } else {
                        throw new SQLException("No se encontró una sesión válida (idSesion) para asociar los archivos.");
                    }
                }
                conexion.commit();
                respuesta.put("error", false);
                respuesta.put("mensaje", "Evidencias actualizadas correctamente.");
                
            } catch (SQLException e) {
                respuesta.put("mensaje", "Error en la transacción: " + e.getMessage());
                e.printStackTrace();
                try { conexion.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            } finally {
                try { conexion.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "No hay conexión con la base de datos.");
        }
        
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerEvidenciasPorSesion(int idTutor, int numSesion) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = null;
        try {
            conexion = ConexionBD.abrirConexionBD();
            if (conexion != null) {
                int idSesion = EvidenciaDAO.obtenerIdSesionRepresentativa(conexion, idTutor, numSesion);
                ArrayList<Evidencia> evidencias = new ArrayList<>();
                if (idSesion > 0) {
                    ResultSet resultado = EvidenciaDAO.obtenerEvidenciasPorIdSesion(conexion, idSesion);
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
                respuesta.put("error", false);
                respuesta.put("evidencias", evidencias);
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "No hay conexión con la base de datos.");
            }
        } catch (SQLException e) {
            respuesta.put("mensaje", "Error SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrarConexion(conexion);
        }
        return respuesta;
    }
}