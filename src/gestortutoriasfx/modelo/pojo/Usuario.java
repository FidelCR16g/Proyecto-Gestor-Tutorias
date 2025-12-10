package gestortutoriasfx.modelo.pojo;

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
    private String usuario;
    private String password;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private int idRol;
    
    public Usuario() {
    }

    public Usuario(String usuario, String password, String apellidoPaterno, String apellidoMaterno, String email, int idRol) {
        this.usuario = usuario;
        this.password = password;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.email = email;
        this.idRol = idRol;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
