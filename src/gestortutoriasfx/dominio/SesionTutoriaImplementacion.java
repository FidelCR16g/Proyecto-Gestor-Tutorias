package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.dao.EstudianteDAO;
import gestortutoriasfx.modelo.dao.SesionTutoriaDAO;
import gestortutoriasfx.modelo.pojo.Estudiante;
import gestortutoriasfx.modelo.pojo.FechaTutoria;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
import java.sql.Connection;
import java.sql.ResultSet;
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
    public static boolean cargarHorarioGenerado(List<SesionTutoria> horarios) {
        boolean exito = false;
        Connection conexion = ConexionBD.abrirConexionBD();
        
        if (conexion != null) {
            try {
                conexion.setAutoCommit(false);
                
                for (SesionTutoria sesion : horarios) {
                    SesionTutoriaDAO.registrarSesion(conexion, sesion);
                }
                
                conexion.commit();
                exito = true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                try { conexion.rollback(); } catch (SQLException e) { e.printStackTrace(); }
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        }
        return exito;
    }
    
     public static HashMap<String, Object> obtenerEstudiantesDelTutor(int idTutor) {
        return EstudianteImplementacion.obtenerTutoradosPorTutor(idTutor);
    }
    
    public static HashMap<String, Object> obtenerFechasPorPeriodo(int idPeriodo) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = null;

        try {
            conexion = ConexionBD.abrirConexionBD();
            ResultSet resultado = SesionTutoriaDAO.obtenerFechasPorPeriodo(conexion, idPeriodo);
            
            ArrayList<FechaTutoria> listaFechas = new ArrayList<>();
            
            while (resultado.next()) {
                FechaTutoria fecha = new FechaTutoria();
                fecha.setIdFechaTutoria(resultado.getInt("idFechaTutoria"));
                fecha.setIdPeriodo(resultado.getInt("idPeriodo"));
                fecha.setNumSesion(resultado.getInt("numSesion"));
                fecha.setFechaInicio(resultado.getString("fechaInicio"));
                fecha.setFechaCierre(resultado.getString("fechaCierre"));
                listaFechas.add(fecha);
            }
            
            respuesta.put("error", false);
            respuesta.put("fechas", listaFechas);

        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error al cargar las fechas de tutoría: " + e.getMessage());
            e.printStackTrace();
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
            int idTutor = Sesion.getIdTutor();
            
            ResultSet resultado = SesionTutoriaDAO.obtenerSesionesOcupadas(conexion, idTutor, idPeriodo);
            ArrayList<Integer> sesionesOcupadas = new ArrayList<>();
            
            while (resultado.next()) {
                sesionesOcupadas.add(resultado.getInt("numSesion"));
            }
            
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
}
