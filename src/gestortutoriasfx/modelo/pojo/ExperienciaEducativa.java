package gestortutoriasfx.modelo.pojo;

import java.io.Serializable;
import java.util.Objects;

public class ExperienciaEducativa implements Serializable {

    private int idExperienciaEducativa;   
    private int nrc;                          
    private String idProfesor;
    private int idProgramaEducativo;          
    private String nombreExperienciaEducativa;
    private Modalidad modalidad;              
    private int cupo;
    private int numCreditos;

    private String nombreProfesor;
    private String nombreProgramaEducativo;

    public enum Modalidad {
        Presencial,
        Virtual
    }

    public ExperienciaEducativa() {}

    public ExperienciaEducativa(int idExperienciaEducativa, int nrc, String idProfesor, int idProgramaEducativo,
                                String nombreExperienciaEducativa, Modalidad modalidad, int cupo, int numCreditos,
                                String nombreProfesor, String nombreProgramaEducativo) {
        this.idExperienciaEducativa = idExperienciaEducativa;
        this.nrc = nrc;
        this.idProfesor = idProfesor;
        this.idProgramaEducativo = idProgramaEducativo;
        this.nombreExperienciaEducativa = nombreExperienciaEducativa;
        this.modalidad = modalidad;
        this.cupo = cupo;
        this.numCreditos = numCreditos;
        this.nombreProfesor = nombreProfesor;
        this.nombreProgramaEducativo = nombreProgramaEducativo;
    }

    public int getIdExperienciaEducativa() {
        return idExperienciaEducativa;
    }

    public void setIdExperienciaEducativa(int idExperienciaEducativa) {
        this.idExperienciaEducativa = idExperienciaEducativa;
    }

    public int getNrc() {
        return nrc;
    }

    public void setNrc(int nrc) {
        this.nrc = nrc;
    }

    public String getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(String idProfesor) {
        this.idProfesor = idProfesor;
    }

    public int getIdProgramaEducativo() {
        return idProgramaEducativo;
    }

    public void setIdProgramaEducativo(int idProgramaEducativo) {
        this.idProgramaEducativo = idProgramaEducativo;
    }

    public String getNombreExperienciaEducativa() {
        return nombreExperienciaEducativa;
    }

    public void setNombreExperienciaEducativa(String nombreExperienciaEducativa) {
        this.nombreExperienciaEducativa = nombreExperienciaEducativa;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public int getNumCreditos() {
        return numCreditos;
    }

    public void setNumCreditos(int numCreditos) {
        this.numCreditos = numCreditos;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    public String getNombreProgramaEducativo() {
        return nombreProgramaEducativo;
    }

    public void setNombreProgramaEducativo(String nombreProgramaEducativo) {
        this.nombreProgramaEducativo = nombreProgramaEducativo;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExperienciaEducativa)) return false;
        ExperienciaEducativa that = (ExperienciaEducativa) o;
        return Objects.equals(idExperienciaEducativa, that.idExperienciaEducativa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idExperienciaEducativa);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nrc).append(" - ").append(nombreExperienciaEducativa);
        if (nombreProfesor != null && !nombreProfesor.isEmpty()) {
            sb.append(" (").append(nombreProfesor).append(")");
        }
        return sb.toString();
    }
}
