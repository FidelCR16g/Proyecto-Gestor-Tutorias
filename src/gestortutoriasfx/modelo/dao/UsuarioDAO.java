package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public static Usuario obtenerUsuario(Connection conexion, String nombreUsuario) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        Usuario usuarioEncontrado = null;
        String consulta = "SELECT * FROM usuario WHERE nombreUsuario = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setString(1, nombreUsuario);
            
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    usuarioEncontrado = new Usuario();
                    usuarioEncontrado.setIdUsuario(resultado.getInt("idUsuario"));
                    usuarioEncontrado.setUsuario(resultado.getString("nombreUsuario")); 
                    usuarioEncontrado.setPassword(resultado.getString("password"));
                    usuarioEncontrado.setNombre(resultado.getString("nombre"));
                    usuarioEncontrado.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                    usuarioEncontrado.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                    usuarioEncontrado.setEmail(resultado.getString("email"));
                    usuarioEncontrado.setIdRol(resultado.getInt("idRol"));
                }
            }
        }
        return usuarioEncontrado;
    }
}