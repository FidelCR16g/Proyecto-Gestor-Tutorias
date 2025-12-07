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
    private String numPersonal;
    private int idUsuario;
    
    private int espaciosTutorados;

    public Tutor() {
    }

    public Tutor(int idTutor, String numPersonal, int idUsuario, int espaciosTutorados) {
        this.idTutor = idTutor;
        this.numPersonal = numPersonal;
        this.idUsuario = idUsuario;
        this.espaciosTutorados = espaciosTutorados;
    }

    public int getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(int idTutor) {
        this.idTutor = idTutor;
    }

    public String getNumPersonal() {
        return numPersonal;
    }

    public void setNumPersonal(String numPersonal) {
        this.numPersonal = numPersonal;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getEspaciosTutorados() {
        return espaciosTutorados;
    }

    public void setEspaciosTutorados(int espaciosTutorados) {
        this.espaciosTutorados = espaciosTutorados;
    }
}
