package gestortutoriasfx.modelo.pojo;

import java.io.Serializable;
import java.util.Objects;

public class TutorComentarioGeneral implements Serializable {

    private Integer idListaTutoresComentarios; 
    private Integer idReporteGeneral;          
    private Integer idTutor;                  
    private String nombreTutor;               
    private String comentario;                

    public TutorComentarioGeneral() { }

    public TutorComentarioGeneral(String nombreTutor, String comentario) {
        this.nombreTutor = nombreTutor;
        this.comentario = comentario;
    }

    public Integer getIdListaTutoresComentarios() { return idListaTutoresComentarios; }
    public void setIdListaTutoresComentarios(Integer idListaTutoresComentarios) { this.idListaTutoresComentarios = idListaTutoresComentarios; }

    public Integer getIdReporteGeneral() { return idReporteGeneral; }
    public void setIdReporteGeneral(Integer idReporteGeneral) { this.idReporteGeneral = idReporteGeneral; }

    public Integer getIdTutor() { return idTutor; }
    public void setIdTutor(Integer idTutor) { this.idTutor = idTutor; }

    public String getNombreTutor() { return nombreTutor; }
    public void setNombreTutor(String nombreTutor) { this.nombreTutor = nombreTutor; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

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
}
