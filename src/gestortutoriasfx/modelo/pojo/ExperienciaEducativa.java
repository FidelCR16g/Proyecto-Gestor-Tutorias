package gestortutoriasfx.modelo.pojo;

import java.io.Serializable;
import java.util.Objects;

public class ExperienciaEducativa implements Serializable {

    private Integer idExperienciaEducativa;   
    private int nrc;                          
    private String idProfesor;                
    private int idProgramaEducativo;          
    private String nombreExperienciaEducativa;
    private Modalidad modalidad;              
    private int cupo;
    private int numCreditos;

    public enum Modalidad {
        Presencial,
        Virtual
    }

    public ExperienciaEducativa() {}

    public ExperienciaEducativa(Integer idExperienciaEducativa, int nrc, String idProfesor, int idProgramaEducativo,
                                String nombreExperienciaEducativa, Modalidad modalidad, int cupo, int numCreditos) {
        this.idExperienciaEducativa = idExperienciaEducativa;
        this.nrc = nrc;
        this.idProfesor = idProfesor;
        this.idProgramaEducativo = idProgramaEducativo;
        this.nombreExperienciaEducativa = nombreExperienciaEducativa;
        this.modalidad = modalidad;
        this.cupo = cupo;
        this.numCreditos = numCreditos;
    }

    public Integer getIdExperienciaEducativa() { return idExperienciaEducativa; }
    public void setIdExperienciaEducativa(Integer idExperienciaEducativa) { this.idExperienciaEducativa = idExperienciaEducativa; }

    public int getNrc() { return nrc; }
    public void setNrc(int nrc) { this.nrc = nrc; }

    public String getIdProfesor() { return idProfesor; }
    public void setIdProfesor(String idProfesor) { this.idProfesor = idProfesor; }

    public int getIdProgramaEducativo() { return idProgramaEducativo; }
    public void setIdProgramaEducativo(int idProgramaEducativo) { this.idProgramaEducativo = idProgramaEducativo; }

    public String getNombreExperienciaEducativa() { return nombreExperienciaEducativa; }
    public void setNombreExperienciaEducativa(String nombreExperienciaEducativa) { this.nombreExperienciaEducativa = nombreExperienciaEducativa; }

    public Modalidad getModalidad() { return modalidad; }
    public void setModalidad(Modalidad modalidad) { this.modalidad = modalidad; }

    public int getCupo() { return cupo; }
    public void setCupo(int cupo) { this.cupo = cupo; }

    public int getNumCreditos() { return numCreditos; }
    public void setNumCreditos(int numCreditos) { this.numCreditos = numCreditos; }

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
        return "ExperienciaEducativa{" +
                "idExperienciaEducativa=" + idExperienciaEducativa +
                ", nrc=" + nrc +
                ", idProfesor='" + idProfesor + '\'' +
                ", idProgramaEducativo=" + idProgramaEducativo +
                ", nombreExperienciaEducativa='" + nombreExperienciaEducativa + '\'' +
                ", modalidad=" + modalidad +
                ", cupo=" + cupo +
                ", numCreditos=" + numCreditos +
                '}';
    }
}
