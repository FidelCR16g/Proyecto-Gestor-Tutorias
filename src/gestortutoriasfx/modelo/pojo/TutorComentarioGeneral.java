package gestortutoriasfx.modelo.pojo;

import java.io.Serializable;
import java.util.Objects;

public class TutorComentarioGeneral implements Serializable {
    private int idListaTutoresComentarios; 
    private int idReporteGeneral;          
    private Integer idTutor;                  
    private String nombreTutor;               
    private String comentario;                

    public TutorComentarioGeneral() { }

    public TutorComentarioGeneral(int idReporteGeneral, Integer idTutor, String nombreTutor, String comentario) {
        this.idReporteGeneral = idReporteGeneral;
        this.idTutor = idTutor;
        this.nombreTutor = nombreTutor;
        this.comentario = comentario;
    }

    public TutorComentarioGeneral(int idListaTutoresComentarios, int idReporteGeneral, Integer idTutor, String nombreTutor, String comentario) {
        this.idListaTutoresComentarios = idListaTutoresComentarios;
        this.idReporteGeneral = idReporteGeneral;
        this.idTutor = idTutor;
        this.nombreTutor = nombreTutor;
        this.comentario = comentario;
    }

    public int getIdListaTutoresComentarios() {
        return idListaTutoresComentarios;
    }

    public void setIdListaTutoresComentarios(int idListaTutoresComentarios) {
        this.idListaTutoresComentarios = idListaTutoresComentarios;
    }

    public int getIdReporteGeneral() {
        return idReporteGeneral;
    }

    public void setIdReporteGeneral(int idReporteGeneral) {
        this.idReporteGeneral = idReporteGeneral;
    }

    public Integer getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(Integer idTutor) {
        this.idTutor = idTutor;
    }

    public String getNombreTutor() {
        return nombreTutor;
    }

    public void setNombreTutor(String nombreTutor) {
        this.nombreTutor = nombreTutor;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TutorComentarioGeneral)) return false;
        TutorComentarioGeneral that = (TutorComentarioGeneral) o;
        return Objects.equals(idListaTutoresComentarios, that.idListaTutoresComentarios);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idListaTutoresComentarios);
    }
    
    @Override
    public String toString() {
        return nombreTutor + ": " + comentario;
    }
}
