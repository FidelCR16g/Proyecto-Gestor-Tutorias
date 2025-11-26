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
public class UsuarioDAO {
    public static ResultSet obtenerUsuario(Connection conexionBD, String usuario) throws SQLException{
        if(conexionBD != null){
            String consulta = "SELECT * FROM usuario WHERE nombreUsuario= ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, usuario);
            return sentencia.executeQuery();
        }
        throw new SQLException ("No hay conexion con la base de datos."); 
    }
}