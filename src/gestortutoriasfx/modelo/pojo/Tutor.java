package gestortutoriasfx.modelo.pojo;

import java.io.Serializable;
import java.util.Objects;

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

public class Tutor extends Profesor implements Serializable {
    private int idTutor;
    private int espaciosTutorados;

    public Tutor() {
        super();
        this.espaciosTutorados = 20;
    }

    public Tutor(int idTutor, int espaciosTutorados, int idProfesor, String numPersonal, 
                 String nombre, String apellidoPaterno, String apellidoMaterno, String email) {
        
        super(); 
        this.setIdProfesor(idProfesor);
        this.setNumPersonal(numPersonal);
        this.setNombre(nombre);
        this.setApellidoPaterno(apellidoPaterno);
        this.setApellidoMaterno(apellidoMaterno);
        this.setEmail(email);
        
        this.idTutor = idTutor;
        this.espaciosTutorados = espaciosTutorados;
    }

    public int getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(int idTutor) {
        this.idTutor = idTutor;
    }

    public int getEspaciosTutorados() {
        return espaciosTutorados;
    }

    public void setEspaciosTutorados(int espaciosTutorados) {
        this.espaciosTutorados = espaciosTutorados;
    }
    
    public String getCorreoInstitucional() {
        return super.getEmail();
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        super.setEmail(correoInstitucional);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tutor)) return false;
        Tutor tutor = (Tutor) o;
        return idTutor == tutor.idTutor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTutor);
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (" + espaciosTutorados + " espacios)";
    }
}