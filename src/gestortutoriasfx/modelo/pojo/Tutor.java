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
    private String noPersonal;
    private int idUsuario;

    public Tutor() {
    }
    
    public Tutor(int idTutor, String noPersonal, int idUsuario) {
        this.idTutor = idTutor;
        this.noPersonal = noPersonal;
        this.idUsuario = idUsuario;
    }

    public int getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(int idTutor) {
        this.idTutor = idTutor;
    }

    public String getNoPersonal() {
        return noPersonal;
    }

    public void setNoPersonal(String noPersonal) {
        this.noPersonal = noPersonal;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
