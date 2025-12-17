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

    public Administrador() {
    }

    public Administrador(int idRolAdministrador, int idUsuario) {
        this.idRolAdministrador = idRolAdministrador;
        this.idUsuario = idUsuario;
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

    @Override
    public String toString() {
        return "Administrador{" +
                "idRolAdministrador=" + idRolAdministrador +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
