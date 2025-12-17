package gestortutoriasfx.modelo.pojo;

/**
 * Nombre de la Clase: Coordinador
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
 * Clase encargada de contener los datos del coordinador.
 */

public class Coordinador {
    private int idCoordinador;
    private int idProfesor;
    private int aniosCargo;

    private String numPersonal;
    private String nombreCompleto;
    private String email;

    public Coordinador() {
    }

    public Coordinador(int idCoordinador, int idProfesor, int aniosCargo) {
        this.idCoordinador = idCoordinador;
        this.idProfesor = idProfesor;
        this.aniosCargo = aniosCargo;
    }

    public int getIdCoordinador() {
        return idCoordinador;
    }

    public void setIdCoordinador(int idCoordinador) {
        this.idCoordinador = idCoordinador;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public int getAniosCargo() {
        return aniosCargo;
    }

    public void setAniosCargo(int aniosCargo) {
        this.aniosCargo = aniosCargo;
    }

    public String getNumPersonal() {
        return numPersonal;
    }

    public void setNumPersonal(String numPersonal) {
        this.numPersonal = numPersonal;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Coordinador{" +
                "idCoordinador=" + idCoordinador +
                ", idProfesor=" + idProfesor +
                ", aniosCargo=" + aniosCargo +
                ", numPersonal='" + numPersonal + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
