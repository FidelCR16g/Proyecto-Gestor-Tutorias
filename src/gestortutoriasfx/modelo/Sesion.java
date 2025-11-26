/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestortutoriasfx.modelo;

import gestortutoriasfx.modelo.pojo.Usuario;

/**
 *
 * @author fave6
 */
public class Sesion {
    private static Usuario usuarioSesion;
    private static int idTutor;

    public static Usuario getUsuarioSesion() {
        return usuarioSesion;
    }

    public static void setUsuarioSesion(Usuario usuarioSesion) {
        Sesion.usuarioSesion = usuarioSesion;
    }

    public static int getIdTutor() {
        return idTutor;
    }

    public static void setIdTutor(int idTutor) {
        Sesion.idTutor = idTutor;
    }
}
