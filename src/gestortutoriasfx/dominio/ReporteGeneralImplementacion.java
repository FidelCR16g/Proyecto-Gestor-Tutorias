package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.dao.DocumentoReporteGeneralDAO;
import gestortutoriasfx.modelo.dao.ListaTutoresComentariosDAO;
import gestortutoriasfx.modelo.dao.ProblemasAcademicosReportadosGeneralesDAO;
import gestortutoriasfx.modelo.dao.ReporteGeneralDAO;
import gestortutoriasfx.modelo.pojo.DocumentoReporteGeneral;
import gestortutoriasfx.modelo.pojo.ProblemaAcademicoReportadoGeneral;
import gestortutoriasfx.modelo.pojo.ReporteGeneral;
import gestortutoriasfx.modelo.pojo.TutorComentarioGeneral;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;

public class ReporteGeneralImplementacion {

    public static HashMap<String, Object> buscarReportesGenerales(String busqueda) {
        HashMap<String, Object> resp = new LinkedHashMap<>();
        resp.put("error", true);

        int idCoord = Sesion.getIdCoordinador();

        Connection c = ConexionBD.abrirConexionBD();
        if (c != null) {
            try {
                ArrayList<ReporteGeneral> lista = ReporteGeneralDAO.listarPorCoordinador(c, idCoord, busqueda);
                resp.put("error", false);
                resp.put("reportes", lista);
            } catch (SQLException e) {
                resp.put("mensaje", "Error BD al buscar reportes generales: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(c);
            }
        } else {
            resp.put("mensaje", "Sin conexión con la base de datos.");
        }
        return resp;
    }

    public static HashMap<String, Object> obtenerReporteGeneralCompleto(int idReporteGeneral) {
        HashMap<String, Object> resp = new LinkedHashMap<>();
        resp.put("error", true);

        Connection c = ConexionBD.abrirConexionBD();
        if (c != null) {
            try {
                ReporteGeneral rg = ReporteGeneralDAO.obtenerPorId(c, idReporteGeneral);
                ArrayList<ProblemaAcademicoReportadoGeneral> problemas =
                        ProblemasAcademicosReportadosGeneralesDAO.obtenerPorReporte(c, idReporteGeneral);
                ArrayList<TutorComentarioGeneral> tutores =
                        ListaTutoresComentariosDAO.obtenerPorReporte(c, idReporteGeneral);

                // opcional: solo metadatos (sin blob)
                ArrayList<DocumentoReporteGeneral> docs =
                        DocumentoReporteGeneralDAO.listarMetadatosPorReporte(c, idReporteGeneral);

                resp.put("error", false);
                resp.put("reporte", rg);
                resp.put("problemas", problemas);
                resp.put("tutores", tutores);
                resp.put("documentos", docs);

            } catch (SQLException e) {
                resp.put("mensaje", "Error BD al cargar reporte completo: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(c);
            }
        } else {
            resp.put("mensaje", "Sin conexión con la base de datos.");
        }
        return resp;
    }

    public static HashMap<String, Object> guardarReporteGeneralCompleto(
            ReporteGeneral reporte,
            List<ProblemaAcademicoReportadoGeneral> problemas,
            List<TutorComentarioGeneral> tutores
    ) {
        HashMap<String, Object> resp = new LinkedHashMap<>();
        resp.put("error", true);

        Connection c = ConexionBD.abrirConexionBD();
        if (c != null) {
            try {
                c.setAutoCommit(false);

                // 🔹 asegura coordinador del login
                if (reporte.getIdCoordinador() == null || reporte.getIdCoordinador() <= 0) {
                    int idCoord = Sesion.getIdCoordinador();
                    if (idCoord > 0) reporte.setIdCoordinador(idCoord);
                }

                int idReporte = (reporte.getIdReporteGeneral() == null) ? 0 : reporte.getIdReporteGeneral();

                if (idReporte == 0) {
                    idReporte = ReporteGeneralDAO.insertarReporte(c, reporte);
                    if (idReporte == 0) throw new SQLException("No se generó el ID del Reporte General.");
                    reporte.setIdReporteGeneral(idReporte);
                } else {
                    int filas = ReporteGeneralDAO.actualizarReporte(c, reporte);
                    if (filas <= 0) throw new SQLException("No se actualizó el Reporte General (0 filas).");
                }

                // Reemplazar hijos (más simple y consistente)
                ProblemasAcademicosReportadosGeneralesDAO.eliminarPorReporte(c, idReporte);
                ListaTutoresComentariosDAO.eliminarPorReporte(c, idReporte);

                if (problemas != null && !problemas.isEmpty()) {
                    ProblemasAcademicosReportadosGeneralesDAO.insertarLote(c, problemas, idReporte);
                }

                if (tutores != null && !tutores.isEmpty()) {
                    ListaTutoresComentariosDAO.insertarLote(c, tutores, idReporte);
                }

                c.commit();
                resp.put("error", false);
                resp.put("mensaje", "Reporte general guardado correctamente.");
                resp.put("idReporteGeneral", idReporte);

            } catch (SQLException e) {
                resp.put("mensaje", "Error al guardar reporte general: " + e.getMessage());
                e.printStackTrace();
                try { c.rollback(); } catch (SQLException ex) { }
            } finally {
                try { c.setAutoCommit(true); } catch (SQLException ex) { }
                ConexionBD.cerrarConexion(c);
            }
        } else {
            resp.put("mensaje", "Sin conexión con la base de datos.");
        }
        return resp;
    }

    public static HashMap<String, Object> guardarDocumentoReporteGeneral(
            int idReporteGeneral,
            String nombreArchivo,
            String tipoArchivo,
            byte[] archivo
    ) {
        HashMap<String, Object> resp = new LinkedHashMap<>();
        resp.put("error", true);

        Connection c = ConexionBD.abrirConexionBD();
        if (c != null) {
            try {
                int filas = DocumentoReporteGeneralDAO.guardarDocumentoUpsert(c, idReporteGeneral, nombreArchivo, tipoArchivo, archivo);
                resp.put("error", false);
                resp.put("mensaje", (filas > 0) ? "Documento guardado correctamente." : "No se guardó el documento.");
            } catch (SQLException e) {
                resp.put("mensaje", "Error BD al guardar documento: " + e.getMessage());
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
