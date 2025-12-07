package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.ProblematicaAcademica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProblematicaAcademicaDAO {

    public static int registrarProblematica(Connection conexion, ProblematicaAcademica problematicaAcademica, int idReporte) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        int filas = 0;
        
        String consulta = "INSERT INTO problemasAcademicosReportadosTutoria " 
                 + "(idReporteTutoria, NRC, nombreNRC, nombreProfesor, problema, numEstudiantes) "
                 + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idReporte);
            int nrc = (problematicaAcademica.getNrc() > 0) ? problematicaAcademica.getNrc() : 10001;
            sentencia.setInt(2, nrc);
            sentencia.setString(3, problematicaAcademica.getNombreNRC());
            sentencia.setString(4, problematicaAcademica.getNombreProfesor());
            sentencia.setString(5, problematicaAcademica.getProblema());
            sentencia.setInt(6, problematicaAcademica.getNumEstudiantes());
            filas = sentencia.executeUpdate();
            }
        return filas;
    }

    public static int eliminarProblematicasPorReporte(Connection conexion, int idReporte) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        int filas = 0;
        
        String consulta = "DELETE FROM problemasAcademicosReportadosTutoria WHERE idReporteTutoria = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idReporte);
            filas = sentencia.executeUpdate();
        }
        return filas;
    }

    public static ArrayList<ProblematicaAcademica> obtenerProblematicasPorReporte(Connection conexion, int idReporte) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        ArrayList<ProblematicaAcademica> lista = new ArrayList<>();
        
        String consulta = "SELECT * FROM problemasAcademicosReportadosTutoria WHERE idReporteTutoria = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idReporte);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    ProblematicaAcademica problematicaAcademica = new ProblematicaAcademica(
                            resultado.getString("nombreNRC"),
                            resultado.getString("nombreProfesor"),
                            resultado.getString("problema"),
                            resultado.getInt("numEstudiantes")
                        );
                    problematicaAcademica.setIdProblematicaAcademica(resultado.getInt("idProblemasAcademicosReportadosTutoria"));
                    problematicaAcademica.setIdReporteTutoria(resultado.getInt("idReporteTutoria"));
                    problematicaAcademica.setNrc(resultado.getInt("NRC"));
                    lista.add(problematicaAcademica);
                }
            }
        }
        return lista;
    }
}