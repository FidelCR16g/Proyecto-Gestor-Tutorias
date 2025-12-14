package gestortutoriasfx.modelo;

import gestortutoriasfx.modelo.pojo.Usuario;

/**
 * Nombre de la Clase: Sesion
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
 * Clase encargada de administrar a los usuarios que hayan hecho login en la
 * aplicacion, su funcion principal es distinguir a los tutores.
 */

public class Sesion {
    private static Usuario usuarioSesion;
    
    private static int idTutor; 
    private static int idCoordinador;
    private static int idSupervisor;
    
    private Sesion() {}

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
    
    public static int getIdCoordinador() {
        return idCoordinador;
    }

    public static void setIdCoordinador(int idCoordinador) {
        Sesion.idCoordinador = idCoordinador;
    }

    public static int getIdSupervisor() {
        return idSupervisor;
    }

    public static void setIdSupervisor(int idSupervisor) {
        Sesion.idSupervisor = idSupervisor;
    }
   
    public static void cerrarSesion() {
        usuarioSesion = null;
        idTutor = 0;
        idCoordinador = 0;
        idSupervisor = 0;
    }
}