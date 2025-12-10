package gestortutoriasfx.modelo.pojo;

/**
 *
 * @author fave6
 */
public class ProblematicaAcademica {
    private int idProblematicaAcademica;
    private int idReporteTutoria;
    private int nrc;
    
    private String nombreNRC;
    private String nombreProfesor;
    private String problema;
    private int numEstudiantes;

    public ProblematicaAcademica() {
    }

    public ProblematicaAcademica(String nombreNRC, String nombreProfesor, String problema, int numEstudiantes) {
        this.nombreNRC = nombreNRC;
        this.nombreProfesor = nombreProfesor;
        this.problema = problema;
        this.numEstudiantes = numEstudiantes;
        this.nrc = 0;
    }

    public int getIdProblematicaAcademica() {
        return idProblematicaAcademica;
    }

    public void setIdProblematicaAcademica(int idProblematicaAcademica) {
        this.idProblematicaAcademica = idProblematicaAcademica;
    }

    public int getIdReporteTutoria() {
        return idReporteTutoria;
    }

    public void setIdReporteTutoria(int idReporteTutoria) {
        this.idReporteTutoria = idReporteTutoria;
    }

    public int getNrc() {
        return nrc;
    }

    public void setNrc(int nrc) {
        this.nrc = nrc;
    }

    public String getNombreNRC() {
        return nombreNRC;
    }

    public void setNombreNRC(String nombreNRC) {
        this.nombreNRC = nombreNRC;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    public String getProblema() {
        return problema;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }

    public int getNumEstudiantes() {
        return numEstudiantes;
    }

    public void setNumEstudiantes(int numEstudiantes) {
        this.numEstudiantes = numEstudiantes;
    }
}
