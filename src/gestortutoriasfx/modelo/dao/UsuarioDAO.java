package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Nombre de la Clase: UsuarioDAO
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 25/11/2025
 *
 * Descripción:
 * Clase encargada del acceso y ejecucion de las consultas dentro de la base de datos.
 */

public class UsuarioDAO {
    public static Usuario iniciarSesion(Connection conexion, String noPersonal, String password) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE noPersonal = ? AND password = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, noPersonal);
            sentencia.setString(2, password);
            
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(resultado.getInt("idUsuario"));
                    usuario.setNoPersonal(resultado.getString("noPersonal"));
                    usuario.setPassword(resultado.getString("password"));
                    usuario.setNombre(resultado.getString("nombre"));
                    usuario.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                    usuario.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                    usuario.setEmail(resultado.getString("email"));
                    usuario.setRol(resultado.getString("rol"));
                }
            }   
        }
        
        return usuario;
    }
    
    public static ArrayList<String> obtenerRoles(Connection conexion, int idUsuario) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        ArrayList<String> roles = new ArrayList<>();
        String consulta = "{call obtenerRolesDeUsuario(?)}";
        try (java.sql.CallableStatement sentencia = conexion.prepareCall(consulta)) {
            sentencia.setInt(1, idUsuario);
            
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    roles.add(resultado.getString("nombreRol"));
                }
            }
        }
        
        return roles;
    }
}