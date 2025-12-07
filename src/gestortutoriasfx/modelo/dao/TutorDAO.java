package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.Tutor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Nombre de la Clase: TutorDAO
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Univeresultadoidad Veracruzana
 * Curesultadoo: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 25/11/2025
 *
 * Descripción:
 * Clase encargada del acceso y ejecucion de las consultas dentro de la base de datos.
 */

public class TutorDAO {
    public static int obtenerIdTutor(Connection conexion, int idUsuario) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        int idTutor = 0;
        String consulta = "SELECT idTutor FROM rolTutor WHERE idUsuario = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idUsuario);
            
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    idTutor = resultado.getInt("idTutor");
                }
            }
        }
        
        return idTutor;
    }

    public static ArrayList<Tutor> obtenerTutoresDisponibles(Connection conexion) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        ArrayList<Tutor> tutores = new ArrayList<>();
        
        String consulta = "SELECT t.idTutor, t.numPersonal, t.espaciosTutorados, " +
                "p.nombre, p.apellidoPaterno, p.apellidoMaterno " +
                "FROM tutor t " +
                "INNER JOIN profesor p ON t.numPersonal = p.numPersonal";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery()) {
            
            while (resultado.next()) {
                Tutor tutor = new Tutor();
                tutor.setIdTutor(resultado.getInt("idTutor"));
                tutor.setNumPersonal(resultado.getString("numPersonal"));
                tutor.setEspaciosTutorados(resultado.getInt("espaciosTutorados"));
                String nombre = resultado.getString("nombre") + " " +
                        resultado.getString("apellidoPaterno") + " " +
                        resultado.getString("apellidoMaterno");
                tutor.setNombreCompleto(nombre);
                tutores.add(tutor);
            }
        }
        return tutores;
    }
}