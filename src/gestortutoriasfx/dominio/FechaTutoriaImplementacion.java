package gestortutoriasfx.dominio;


import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.FechaTutoriaDAO;
import gestortutoriasfx.modelo.pojo.FechaTutoria;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class FechaTutoriaImplementacion {
    public static HashMap<String, Object> obtenerFechasOcupadas(int idPeriodoEscolar) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);

        Connection conexion = ConexionBD.abrirConexionBD();
        if (conexion != null) {
            try {
                Set<LocalDate> fechasOcupadas = FechaTutoriaDAO.obtenerFechasOcupadas(conexion, idPeriodoEscolar);
                
                respuesta.put("error", false);
                respuesta.put("fechasOcupadas", fechasOcupadas);

            } catch (SQLException e) {
                respuesta.put("mensaje", "Error BD al cargar calendario: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "No hay conexión con la base de datos.");
        }
        return respuesta;
    }

    public static HashMap<String, Object> obtenerFechasPorPeriodo(int idPeriodoEscolar) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);

        Connection conexion = ConexionBD.abrirConexionBD();
        if (conexion != null) {
            try {
                ArrayList<FechaTutoria> lista = FechaTutoriaDAO.obtenerFechasPorPeriodo(conexion, idPeriodoEscolar);
                
                respuesta.put("error", false);
                respuesta.put("fechas", lista);

            } catch (SQLException e) {
                respuesta.put("mensaje", "Error BD al cargar sesiones: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "No hay conexión con la base de datos.");
        }
        return respuesta;
    }

    public static HashMap<String, Object> obtenerFechaPorSesion(int idPeriodoEscolar, int numSesion) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);

        Connection conexion = ConexionBD.abrirConexionBD();
        if (conexion != null) {
            try {
                FechaTutoria fecha = FechaTutoriaDAO.obtenerPorPeriodoYNumSesion(conexion, idPeriodoEscolar, numSesion);
                
                respuesta.put("error", false);
                respuesta.put("fechaTutoria", fecha); 

            } catch (SQLException e) {
                respuesta.put("mensaje", "Error BD al buscar sesión: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "No hay conexión con la base de datos.");
        }
        return respuesta;
    }
    
    public static FechaTutoria obtenerPorPeriodoYNumSesion(int idPeriodo, int numSesion) {
    FechaTutoria fecha = null;
    Connection conexion = ConexionBD.abrirConexionBD();
    
    if (conexion != null) {
        try {
            fecha = FechaTutoriaDAO.obtenerPorPeriodoYNumSesion(conexion, idPeriodo, numSesion);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al buscar fecha por periodo y sesión: " + e.getMessage());
        } finally {
            ConexionBD.cerrarConexion(conexion);
        }
    }
    return fecha;
}
    
    public static HashMap<String, Object> registrarFecha(FechaTutoria fecha) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexion = ConexionBD.abrirConexionBD();
        if (conexion != null) {
            try {
                FechaTutoriaDAO.registrarFechaTutoria(conexion, fecha);
                respuesta.put("error", false);
                respuesta.put("mensaje", "Fecha registrada correctamente.");
            } catch (SQLException e) {
                respuesta.put("mensaje", "Error al registrar: " + e.getMessage());
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "Sin conexión.");
        }
        return respuesta;
    }

    public static HashMap<String, Object> actualizarFecha(FechaTutoria fecha) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexion = ConexionBD.abrirConexionBD();
        if (conexion != null) {
            try {
                FechaTutoriaDAO.actualizarFechaTutoria(conexion, fecha);
                respuesta.put("error", false);
                respuesta.put("mensaje", "Fecha actualizada correctamente.");
            } catch (SQLException e) {
                respuesta.put("mensaje", "Error al actualizar: " + e.getMessage());
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "Sin conexión.");
        }
        return respuesta;
    }
}
