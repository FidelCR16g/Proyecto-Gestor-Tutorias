package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.EstudianteDAO;
import gestortutoriasfx.modelo.pojo.Estudiante;

/**
 * Nombre de la Clase: EstudianteImplementacion
 *
 * Proyecto: Sistema de Gestudianteión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 26/11/2025
 *
 * Descripción:
 * Se encarga de administrar la informacion de los distintos metodos de recuperacion de informacion del 
 * estudianteudiante de la base de datos.
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class EstudianteImplementacion {

    public static boolean actualizarAsignaciones(ArrayList<Estudiante> nuevos, ArrayList<Estudiante> removidos, int idTutor) {
        boolean exito = false;
        Connection conexion = ConexionBD.abrirConexionBD();

        if (conexion != null) {
            try {
                conexion.setAutoCommit(false);

                if (nuevos != null) {
                    for (Estudiante estudiante : nuevos) {
                        EstudianteDAO.asignarTutor(conexion, estudiante.getMatricula(), idTutor);
                    }
                }

                if (removidos != null) {
                    for (Estudiante estudiante : removidos) {
                        EstudianteDAO.desasignarTutor(conexion, estudiante.getMatricula(), idTutor);
                    }
                }

                conexion.commit();
                exito = true;

            } catch (SQLException e) {
                e.printStackTrace();
                try { conexion.rollback(); } catch (SQLException ex) { }
            } finally {
                try { conexion.setAutoCommit(true); } catch (SQLException ex) { }
                ConexionBD.cerrarConexion(conexion);
            }
        }
        return exito;
    }

    public static HashMap<String, Object> actualizarFotoTutorado(String matricula, byte[] foto) {
        HashMap<String, Object> resp = new LinkedHashMap<>();
        Connection con = null;

        try {
            con = ConexionBD.abrirConexionBD();
            int filas = EstudianteDAO.actualizarFoto(con, matricula, foto);
            
            resp.put("error", false);
            resp.put("filas", filas);
        } catch (SQLException ex) {
            resp.put("error", true);
            resp.put("mensaje", "Error BD: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            ConexionBD.cerrarConexion(con);
        }

        return resp;
    }

    public static HashMap<String, Object> obtenerTutoradosPorTutor(int idTutor) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = null;

        try {
            conexion = ConexionBD.abrirConexionBD();
            ArrayList<Estudiante> estudiantes = EstudianteDAO.obtenerTutoradosPorTutor(conexion, idTutor);
            respuesta.put("error", false);
            respuesta.put("estudiantes", estudiantes);
        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrarConexion(conexion);
        }
        return respuesta;
    }

    public static HashMap<String, Object> obtenerEstudiantesSinTutor() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        
        Connection conexion = ConexionBD.abrirConexionBD();
        if (conexion != null) {
            try {
                ArrayList<Estudiante> estudiantes = EstudianteDAO.obtenerEstudiantesSinTutor(conexion);
                respuesta.put("error", false);
                respuesta.put("estudiantes", estudiantes);
            } catch (SQLException e) {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Error BD: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Sin conexión con la BD.");
        }
        return respuesta;
    }

    public static HashMap<String, Object> obtenerTodosTutorados() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = null;

        try {
            conexion = ConexionBD.abrirConexionBD();
            ArrayList<Estudiante> estudiantes = EstudianteDAO.obtenerTodos(conexion);
            respuesta.put("error", false);
            respuesta.put("estudiantes", estudiantes);
        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error BD: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrarConexion(conexion);
        }

        return respuesta;
    }

    public static HashMap<String, Object> eliminarTutorado(String matricula) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = null;

        try {
            conexion = ConexionBD.abrirConexionBD();
            conexion.setAutoCommit(false);

            int filas = EstudianteDAO.eliminarPorMatricula(conexion, matricula);

            conexion.commit();
            respuesta.put("error", false);
            respuesta.put("filas", filas);

        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error BD: " + e.getMessage());
            e.printStackTrace();
            try { if (conexion != null) conexion.rollback(); } catch (SQLException ex) {}
        } finally {
            try { if (conexion != null) conexion.setAutoCommit(true); } catch (SQLException ex) {}
            ConexionBD.cerrarConexion(conexion);
        }

        return respuesta;
    }

    public static HashMap<String, Object> actualizarTutorado(Estudiante e) {
        HashMap<String, Object> resp = new LinkedHashMap<>();
        Connection con = null;

        try {
            con = ConexionBD.abrirConexionBD();
            int filas = EstudianteDAO.actualizar(con, e);
            resp.put("error", false);
            resp.put("filas", filas);
        } catch (SQLException ex) {
            resp.put("error", true);
            resp.put("mensaje", "Error BD: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            ConexionBD.cerrarConexion(con);
        }
        return resp;
    }

    public static HashMap<String, Object> obtenerTutoradoPorMatricula(String matricula) {
        HashMap<String, Object> resp = new LinkedHashMap<>();
        Connection con = null;

        try {
            con = ConexionBD.abrirConexionBD();
            Estudiante est = EstudianteDAO.obtenerPorMatricula(con, matricula);

            if (est == null) {
                resp.put("error", true);
                resp.put("mensaje", "No existe estudiante con matrícula: " + matricula);
            } else {
                resp.put("error", false);
                resp.put("estudiante", est);
            }
        } catch (SQLException ex) {
            resp.put("error", true);
            resp.put("mensaje", "Error BD: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            ConexionBD.cerrarConexion(con);
        }

        return resp;
    }
    
    public static HashMap<String, Object> registrarTutorado(Estudiante e, int idTutor) {
        HashMap<String, Object> resp = new LinkedHashMap<>();
        Connection con = null;

        try {
            con = ConexionBD.abrirConexionBD();
            con.setAutoCommit(false);

            int idEstudiante = EstudianteDAO.insertar(con, e);
            e.setIdEstudiante(idEstudiante);
            
            EstudianteDAO.asignarTutor(con, e.getMatricula(), idTutor);

            con.commit();
            resp.put("error", false);
            resp.put("idEstudiante", idEstudiante);
            resp.put("mensaje", "Tutorado registrado y asignado correctamente.");

        } catch (SQLException ex) {
            resp.put("error", true);
            resp.put("mensaje", "Error al registrar: " + ex.getMessage());
            ex.printStackTrace();
            try { if (con != null) con.rollback(); } catch (SQLException ignore) {}

        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (SQLException ignore) {}
            ConexionBD.cerrarConexion(con);
        }
        return resp;
    }
}