package gestortutoriasfx.modelo.pojo;

/**
 * Nombre de la Clase: Administrador
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
 * Clase encargada de contener los datos del administrador.
 */

public class Administrador {
    private int idRolAdministrador;
    private int idUsuario;

    private String nombreCompleto;
    private String numPersonal;
    private String email;

    public Administrador() {
    }

    public Administrador(int idRolAdministrador, int idUsuario, String nombreCompleto, String numPersonal, String email) {
        this.idRolAdministrador = idRolAdministrador;
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.numPersonal = numPersonal;
        this.email = email;
    }

    public int getIdRolAdministrador() {
        return idRolAdministrador;
    }

    public void setIdRolAdministrador(int idRolAdministrador) {
        this.idRolAdministrador = idRolAdministrador;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNumPersonal() {
        return numPersonal;
    }

    public void setNumPersonal(String numPersonal) {
        this.numPersonal = numPersonal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        if (nombreCompleto != null) {
            return nombreCompleto + " (Admin)";
        }
        return "Admin ID: " + idRolAdministrador;
    }
}
