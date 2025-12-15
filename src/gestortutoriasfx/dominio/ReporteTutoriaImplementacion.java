package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.ProblematicaAcademicaDAO;
import gestortutoriasfx.modelo.dao.ReporteTutoriaDAO;
import gestortutoriasfx.modelo.pojo.ProblematicaAcademica;
import gestortutoriasfx.modelo.pojo.ReporteTutoria;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ReporteTutoriaImplementacion {
    public static HashMap<String, Object> guardarReporteCompleto(ReporteTutoria reporte, List<ProblematicaAcademica> problematicas) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        
        Connection conexion = ConexionBD.abrirConexionBD();
        if (conexion != null) {
            try {
                conexion.setAutoCommit(false);
                
                int idReporte = reporte.getIdReporteTutoria();
                
                if (idReporte == 0) {
                    idReporte = ReporteTutoriaDAO.registrarReporte(conexion, reporte);
                    if (idReporte == 0) throw new SQLException("No se generó el ID del reporte.");
                    reporte.setIdReporteTutoria(idReporte);
                } else {
                    ReporteTutoriaDAO.actualizarReporte(conexion, reporte);
                }
                
                ProblematicaAcademicaDAO.eliminarProblematicasPorReporte(conexion, idReporte);
                
                if (problematicas != null) {
                    for (ProblematicaAcademica prob : problematicas) {
                        ProblematicaAcademicaDAO.registrarProblematica(conexion, prob, idReporte);
                    }
                }
                
                conexion.commit();
                respuesta.put("error", false);
                respuesta.put("mensaje", "Reporte guardado exitosamente.");
                
            } catch (SQLException e) {
                respuesta.put("mensaje", "Error al guardar: " + e.getMessage());
                e.printStackTrace();
                try { conexion.rollback(); } catch (SQLException ex) { }
            } finally {
                try { conexion.setAutoCommit(true); } catch (SQLException ex) { }
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "Sin conexión con la base de datos.");
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerReporteActual(int idTutor, int idPeriodo, int numSesion) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        
        Connection conexion = ConexionBD.abrirConexionBD();
        if (conexion != null) {
            try {
                ReporteTutoria reporte = ReporteTutoriaDAO.obtenerReporte(conexion, idTutor, idPeriodo, numSesion);
                
                if (reporte != null) {
                    ArrayList<ProblematicaAcademica> problematicas = 
                            ProblematicaAcademicaDAO.obtenerProblematicasPorReporte(conexion, reporte.getIdReporteTutoria());
                    respuesta.put("problematicas", problematicas);
                }
                
                respuesta.put("error", false);
                respuesta.put("reporte", reporte);

            } catch (SQLException e) {
                respuesta.put("mensaje", "Error BD al cargar reporte: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "Sin conexión con la base de datos.");
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerReportesPorPeriodo(int idTutor, int idPeriodo) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexion = ConexionBD.abrirConexionBD();
        if (conexion != null) {
            try {
                ArrayList<ReporteTutoria> lista = ReporteTutoriaDAO.obtenerReportesPorPeriodo(conexion, idTutor, idPeriodo);
                respuesta.put("error", false);
                respuesta.put("reportes", lista);
            } catch (SQLException e) {
                respuesta.put("mensaje", "Error BD: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "Sin conexión.");
        }
        return respuesta;
    }
}