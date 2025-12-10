package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.dao.ProblematicaAcademicaDAO;
import gestortutoriasfx.modelo.dao.ReporteTutoriaDAO;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import gestortutoriasfx.modelo.pojo.ProblematicaAcademica;
import gestortutoriasfx.modelo.pojo.ReporteTutoria;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ReporteTutoriaImplementacion {
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
    
    public static boolean exportarReporteTextoPlano(ReporteTutoria reporteParametro, PeriodoEscolar periodo, File archivoDestino) {
    int idTutor = Sesion.getIdTutor(); 

    HashMap<String, Object> resp = obtenerReporteActual(idTutor, periodo.getIdPeriodoEscolar(), reporteParametro.getNumSesion());
    
    ArrayList<ProblematicaAcademica> lista = (boolean) resp.get("error") ? new ArrayList<>() : (ArrayList<ProblematicaAcademica>) resp.get("problematicas");
    
    ReporteTutoria reporteFresco = (ReporteTutoria) resp.get("reporte");
    
    ReporteTutoria reporteAUsar = (reporteFresco != null) ? reporteFresco : reporteParametro;

    try (FileWriter fw = new FileWriter(archivoDestino);
         BufferedWriter bw = new BufferedWriter(fw);
         PrintWriter escritor = new PrintWriter(bw)) {
        
        escritor.println("Fechas de Tutoría:      Del " + reporteAUsar.getFechaPrimeraTutoria() + " al " + reporteAUsar.getFechaUltimaTutoria());
        escritor.println("");
        escritor.println("----------------------------------------------------------------");
        
        escritor.println("RESUMEN DE ASISTENCIA");
        escritor.println("   > Alumnos Asistentes:  " + reporteAUsar.getNumAlumnosAsistieron()); 
        escritor.println("   > Alumnos en Riesgo:   " + reporteAUsar.getNumAlumnosRiesgo());

            escritor.println("PROBLEMÁTICAS ACADÉMICAS DETECTADAS");
            escritor.println("");
        
            String formatoTabla = "%-25s | %-25s | %-30s | %-10s%n";
        
            escritor.printf(formatoTabla, "EXPERIENCIA ED.", "PROFESOR", "PROBLEMA", "ALUMNOS");
            escritor.println("--------------------------+---------------------------+--------------------------------+------------");

            if (lista.isEmpty()) {
                escritor.println("                   NO SE REPORTAN PROBLEMÁTICAS                   ");
            } else {
                for (ProblematicaAcademica p : lista) {
                    escritor.printf(formatoTabla,
                            acortarTexto(p.getNombreNRC(), 25),
                            acortarTexto(p.getNombreProfesor(), 25),
                            acortarTexto(p.getProblema(), 30),
                            p.getNumEstudiantes()
                    );
                }
            }
            
            escritor.println("");
            escritor.println("----------------------------------------------------------------");

            escritor.println("COMENTARIOS GENERALES");
            escritor.println("");
            escritor.println(reporteAUsar.getComentarios());
            escritor.println("");
            escritor.println("");
            escritor.println("");
        
            escritor.println("__________________________             __________________________");
            escritor.println("     Nombre del Tutor                    Coordinador de Tutorías");
            escritor.println("");
            escritor.println("================================================================");

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static String acortarTexto(String texto, int largoMax) {
        if (texto == null) return "";
        if (texto.length() <= largoMax) return texto;
        return texto.substring(0, largoMax - 3) + "...";
    }
}