package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.SesionTutoriaDAO;
import gestortutoriasfx.modelo.pojo.FechaTutoria;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Nombre de la Clase: SesionTutoriaImplementacion
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
 * Se encarga de administrar la informacion de los distintos metodos de recuperacion de informacion de la 
 * sesion de tutoria de la base de datos.
 */

public class SesionTutoriaImplementacion {
    public static HashMap<String, Object> cargarHorarioGenerado(ArrayList<SesionTutoria> horarios) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexion = ConexionBD.abrirConexionBD();
        
        if (conexion != null) {
            try {
                conexion.setAutoCommit(false);
                for (SesionTutoria sesionTutoria : horarios) {
                    SesionTutoriaDAO.registrarSesion(conexion, sesionTutoria);
                }
                conexion.commit();
                respuesta.put("error", false);
                respuesta.put("mensaje", "Se registraron " + horarios.size() + " horarios correctamente.");
                
            } catch (SQLException ex) {
                respuesta.put("mensaje", "Error al guardar en BD: " + ex.getMessage());
                ex.printStackTrace();
                try { conexion.rollback(); } catch (SQLException e) { e.printStackTrace(); }
            } finally {
                try { conexion.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "No hay conexión con la base de datos.");
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> guardarAsistencias(List<SesionTutoria> lista) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexion = ConexionBD.abrirConexionBD();
        
        if (conexion != null) {
            try {
                conexion.setAutoCommit(false);
                
                for(SesionTutoria sesion : lista){
                    boolean asistio = sesion.isAsistencia();
                    SesionTutoriaDAO.actualizarEstadoAsistencia(conexion, sesion.getIdSesion(), asistio);
                }
                
                conexion.commit();
                respuesta.put("error", false);
                respuesta.put("mensaje", "Se ha registrado la asistencia correctamente.");
                
            } catch (SQLException ex) {
                respuesta.put("mensaje", "Error al guardar en BD: " + ex.getMessage());
                ex.printStackTrace();
                try { conexion.rollback(); } catch (SQLException e) { e.printStackTrace(); }
            } finally {
                try { conexion.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "No hay conexión con la base de datos.");
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerEstudiantesDelTutor(int idTutor) {
        return EstudianteImplementacion.obtenerTutoradosPorTutor(idTutor);
    }
    
    public static HashMap<String, Object> obtenerFechasPorPeriodo(int idPeriodo) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexion = ConexionBD.abrirConexionBD();

        try {
            ArrayList<FechaTutoria> listaFechas = SesionTutoriaDAO.obtenerFechasPorPeriodo(conexion, idPeriodo);
            respuesta.put("error", false);
            respuesta.put("fechas", listaFechas);

        } catch (SQLException e) {
            respuesta.put("mensaje", "Error al cargar las fechas de tutoría: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrarConexion(conexion);
        }
        
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerListaAsistencia(int idTutor, int numSesion) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexion = ConexionBD.abrirConexionBD();
        
        try {
            ArrayList<SesionTutoria> lista = SesionTutoriaDAO.obtenerAlumnosPorSesion(conexion, idTutor, numSesion);
            
            respuesta.put("error", false);
            respuesta.put("lista", lista);
            
        } catch (SQLException ex) {
            respuesta.put("mensaje", ex.getMessage());
        } finally {
            ConexionBD.cerrarConexion(conexion);
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerSesionesOcupadas(int idPeriodo) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = null;

        try {
            conexion = ConexionBD.abrirConexionBD();
            ArrayList<Integer> sesionesOcupadas = 
                    SesionTutoriaDAO.obtenerSesionesOcupadas(conexion, gestortutoriasfx.modelo.Sesion.getIdTutor(), idPeriodo);
            respuesta.put("error", false);
            respuesta.put("ocupadas", sesionesOcupadas);

        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error al verificar sesiones disponibles: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrarConexion(conexion);
        }
        
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerSesionesTutor(int idTutor) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = null;
        
        try {
            conexion = ConexionBD.abrirConexionBD();
            ArrayList<SesionTutoria> listaSesiones = SesionTutoriaDAO.obtenerSesionesAgrupadasPorTutor(conexion, idTutor);
            
            respuesta.put("error", false);
            respuesta.put("sesiones", listaSesiones);
            
        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error al cargar las sesiones: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrarConexion(conexion);
        }
        
        return respuesta;
    }
}