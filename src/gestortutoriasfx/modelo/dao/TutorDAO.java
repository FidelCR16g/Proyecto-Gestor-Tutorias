/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestortutoriasfx.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author fave6
 */
public class TutorDAO {
    public static ResultSet obtenerIdTutor(Connection conexion, int idUsuario) throws SQLException{
        if(conexion != null){
            String consulta = "SELECT id_tutor FROM tutor WHERE id_usuario = ?";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idUsuario);
            return sentencia.executeQuery();
        }
        throw new SQLException ("No hay conexion con la base de datos.");
    }
}
