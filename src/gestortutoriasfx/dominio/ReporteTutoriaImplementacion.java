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

public class ReporteTutoriaImplementacion {

    public static HashMap<String, Object> guardarReporteCompleto(ReporteTutoria reporte, ArrayList<ProblematicaAcademica> listaProblematicas) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
    
        Connection conexion = ConexionBD.abrirConexionBD();
        if (conexion == null) {
            respuesta.put("mensaje", "No se pudo conectar a la base de datos.");
            return respuesta;
        }

        try {
            conexion.setAutoCommit(false);
            
            int idReporte;
            if (reporte.getIdReporteTutoria() > 0) {
                ReporteTutoriaDAO.actualizarReporte(conexion, reporte);
                idReporte = reporte.getIdReporteTutoria();
                ProblematicaAcademicaDAO.eliminarProblematicasPorReporte(conexion, idReporte);
            } else {
                idReporte = ReporteTutoriaDAO.registrarReporte(conexion, reporte);
                if (idReporte == 0) throw new SQLException("Error al obtener el ID del reporte generado.");
            }

            for (ProblematicaAcademica prob : listaProblematicas) {
                ProblematicaAcademicaDAO.registrarProblematica(conexion, prob, idReporte);
            }

            conexion.commit();
            respuesta.put("error", false);
            respuesta.put("mensaje", "El reporte y sus problemáticas se guardaron correctamente.");

        } catch (SQLException e) {
            try { conexion.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            respuesta.put("mensaje", "Error al procesar el reporte: " + e.getMessage());
        } finally {
            ConexionBD.cerrarConexion(conexion);
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

    public static HashMap<String, Object> buscarReportesParaRevision(String busqueda) {
        HashMap<String, Object> resp = new LinkedHashMap<>();
        resp.put("error", true);

        Connection c = ConexionBD.abrirConexionBD();
        if (c != null) {
            try {
                ArrayList<ReporteTutoria> lista = ReporteTutoriaDAO.obtenerReportesParaRevision(c, busqueda);
                resp.put("error", false);
                resp.put("reportes", lista);
            } catch (SQLException e) {
                resp.put("mensaje", "Error BD al buscar reportes: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(c);
            }
        } else {
            resp.put("mensaje", "Sin conexión con la base de datos.");
        }
        return resp;
    }

    public static HashMap<String, Object> marcarReporteComoRevisado(int idReporteTutoria) {
        HashMap<String, Object> resp = new LinkedHashMap<>();
        resp.put("error", true);

        Connection c = ConexionBD.abrirConexionBD();
        if (c != null) {
            try {
                int filas = ReporteTutoriaDAO.actualizarEstatus(c, idReporteTutoria, "Revisado");
                if (filas > 0) {
                    resp.put("error", false);
                    resp.put("mensaje", "Reporte marcado como Revisado.");
                } else {
                    resp.put("error", true);
                    resp.put("mensaje", "No se pudo marcar como Revisado (posiblemente ya estaba Revisado o no estaba Enviado).");
                }
            } catch (SQLException e) {
                resp.put("mensaje", "Error BD al actualizar estatus: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(c);
            }
        } else {
            resp.put("mensaje", "Sin conexión con la base de datos.");
        }
        return resp;
    }

    public static HashMap<String, Object> obtenerReportePorId(int idReporteTutoria) {
        HashMap<String, Object> resp = new LinkedHashMap<>();
        resp.put("error", true);

        Connection c = ConexionBD.abrirConexionBD();
        if (c != null) {
            try {
                ReporteTutoria r = ReporteTutoriaDAO.obtenerReportePorId(c, idReporteTutoria);
                
                if (r != null) {
                    ArrayList<ProblematicaAcademica> problematicas = 
                            ProblematicaAcademicaDAO.obtenerProblematicasPorReporte(c, idReporteTutoria);
                    resp.put("problematicas", problematicas);
                    
                    resp.put("error", false);
                    resp.put("reporte", r);
                } else {
                    resp.put("mensaje", "No se encontró el reporte.");
                }
            } catch (SQLException e) {
                resp.put("mensaje", "Error BD al cargar reporte: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(c);
            }
        } else {
            resp.put("mensaje", "Sin conexión con la base de datos.");
        }
        return resp;
    }
}