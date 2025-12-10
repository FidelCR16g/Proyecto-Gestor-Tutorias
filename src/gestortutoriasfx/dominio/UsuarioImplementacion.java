/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.UsuarioDAO;
import gestortutoriasfx.modelo.pojo.Usuario;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Nombre de la Clase: UsuarioImplementacion
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
 * Se encarga de administrar la informacion de los distintos metodos de recuperacion de informacion de los 
 * usuarios de la base de datos.
 */

public class UsuarioImplementacion {
    public static HashMap<String, Object> obtenerUsuarios(String nombreUsuario){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = null;
        try{
            conexion = ConexionBD.abrirConexionBD();
            ResultSet resultado = UsuarioDAO.obtenerUsuario(conexion, nombreUsuario);
            Usuario usuario = null;
            while (resultado.next()){
                usuario = new Usuario();
                usuario.setIdUsuario(resultado.getInt("idUsuario"));
                usuario.setUsuario(resultado.getString("nombreUsuario"));
                usuario.setPassword(resultado.getString("password"));
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                usuario.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                usuario.setEmail(resultado.getString("email"));
                usuario.setIdRol(resultado.getInt("idRol"));
            }
            if (usuario != null) {
                respuesta.put("error", false);
                respuesta.put("usuario", usuario);
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Usuario no encontrado.");
            }
        }catch (SQLException e){
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }finally{
            ConexionBD.cerrarConexion(conexion);
        }
        return respuesta;
    }
}