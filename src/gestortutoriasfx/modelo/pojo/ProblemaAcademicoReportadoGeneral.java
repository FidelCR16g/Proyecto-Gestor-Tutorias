package gestortutoriasfx.modelo.pojo;

import java.io.Serializable;
import java.util.Objects;

public class ProblemaAcademicoReportadoGeneral implements Serializable {

    private Integer idProblemasAcademicosReportadosGenerales; 
    private Integer idReporteGeneral;                          
    private Integer idExperienciaEducativa;                    

    private String nombreExperienciaEducativa;                 
    private String nombreProfesor;                             
    private String problema;                                   
    private int numEstudiantes;                                

    public ProblemaAcademicoReportadoGeneral() { }

    public ProblemaAcademicoReportadoGeneral(String nombreExperienciaEducativa, String nombreProfesor, String problema, int numEstudiantes) {
        this.nombreExperienciaEducativa = nombreExperienciaEducativa;
        this.nombreProfesor = nombreProfesor;
        this.problema = problema;
        this.numEstudiantes = numEstudiantes;
    }

    public Integer getIdProblemasAcademicosReportadosGenerales() { return idProblemasAcademicosReportadosGenerales; }
    public void setIdProblemasAcademicosReportadosGenerales(Integer id) { this.idProblemasAcademicosReportadosGenerales = id; }

    public Integer getIdReporteGeneral() { return idReporteGeneral; }
    public void setIdReporteGeneral(Integer idReporteGeneral) { this.idReporteGeneral = idReporteGeneral; }

    public Integer getIdExperienciaEducativa() { return idExperienciaEducativa; }
    public void setIdExperienciaEducativa(Integer idExperienciaEducativa) { this.idExperienciaEducativa = idExperienciaEducativa; }

    public String getNombreExperienciaEducativa() { return nombreExperienciaEducativa; }
    public void setNombreExperienciaEducativa(String nombreExperienciaEducativa) { this.nombreExperienciaEducativa = nombreExperienciaEducativa; }

    public String getNombreProfesor() { return nombreProfesor; }
    public void setNombreProfesor(String nombreProfesor) { this.nombreProfesor = nombreProfesor; }

    public String getProblema() { return problema; }
    public void setProblema(String problema) { this.problema = problema; }

    public int getNumEstudiantes() { return numEstudiantes; }
    public void setNumEstudiantes(int numEstudiantes) { this.numEstudiantes = numEstudiantes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProblemaAcademicoReportadoGeneral)) return false;
        ProblemaAcademicoReportadoGeneral that = (ProblemaAcademicoReportadoGeneral) o;
        return Objects.equals(idProblemasAcademicosReportadosGenerales, that.idProblemasAcademicosReportadosGenerales);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProblemasAcademicosReportadosGenerales);
    }
}
