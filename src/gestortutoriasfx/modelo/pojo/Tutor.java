package gestortutoriasfx.modelo.pojo;

/**
 * Nombre de la Clase: Tutor
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
 * Clase encargada de contener los datos del tutor.
 */

public class Tutor {
    private int idTutor;
    private int idProfesor;           
    private int espaciosTutorados;

    private String numPersonal;
    private String nombreCompleto;
    private String email;

    public Tutor() {
    }

    public Tutor(int idTutor, int idProfesor, int espaciosTutorados) {
        this.idTutor = idTutor;
        this.idProfesor = idProfesor;
        this.espaciosTutorados = espaciosTutorados;
    }

    public int getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(int idTutor) {
        this.idTutor = idTutor;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public int getEspaciosTutorados() {
        return espaciosTutorados;
    }

    public void setEspaciosTutorados(int espaciosTutorados) {
        this.espaciosTutorados = espaciosTutorados;
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
        return (nombreCompleto != null ? nombreCompleto : "Tutor") +
                " | Cupo: " + espaciosTutorados;
    }
}
