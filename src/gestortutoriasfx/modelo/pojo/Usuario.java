package gestortutoriasfx.modelo.pojo;

import java.util.ArrayList;

/**
 * Nombre de la Clase: Usuario
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
 * Clase encargada de contener los datos del usuario.
 */

public class Usuario {
    private int idUsuario;
    private String noPersonal;
    private String password;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private String rol;
    
    private int idRolEspecifico;
    
    public Usuario() {
    }

    public Usuario(int idUsuario, String noPersonal, String password, String nombre, String apellidoPaterno, String apellidoMaterno, String email, String rol, int idRolEspecifico) {
        this.idUsuario = idUsuario;
        this.noPersonal = noPersonal;
        this.password = password;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.email = email;
        this.rol = rol;
        this.idRolEspecifico = idRolEspecifico;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNoPersonal() {
        return noPersonal;
    }

    public void setNoPersonal(String noPersonal) {
        this.noPersonal = noPersonal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getIdRolEspecifico() {
        return idRolEspecifico;
    }

    public void setIdRolEspecifico(int idRolEspecifico) {
        this.idRolEspecifico = idRolEspecifico;
    }

    
}