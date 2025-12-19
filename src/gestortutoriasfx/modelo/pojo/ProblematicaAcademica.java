package gestortutoriasfx.modelo.pojo;


public class ProblematicaAcademica {
    private int idProblematicaAcademica;
    private int idReporteTutoria;
    private int idExperienciaEducativa;
    
    private String nombreExperienciaEducativa; 
    private String nombreProfesor;
    private String problema;
    private int numEstudiantes;
    
    public ProblematicaAcademica() {
    }
    
    public ProblematicaAcademica(int idProblematicaAcademica, int idReporteTutoria, int idExperienciaEducativa, 
                                 String nombreExperienciaEducativa, String nombreProfesor, 
                                 String problema, int numEstudiantes) {
        this.idProblematicaAcademica = idProblematicaAcademica;
        this.idReporteTutoria = idReporteTutoria;
        this.idExperienciaEducativa = idExperienciaEducativa;
        this.nombreExperienciaEducativa = nombreExperienciaEducativa;
        this.nombreProfesor = nombreProfesor;
        this.problema = problema;
        this.numEstudiantes = numEstudiantes;
    }

    public ProblematicaAcademica(String nombreExperienciaEducativa, String nombreProfesor, String problema, int numEstudiantes) {
        this.nombreExperienciaEducativa = nombreExperienciaEducativa;
        this.nombreProfesor = nombreProfesor;
        this.problema = problema;
        this.numEstudiantes = numEstudiantes;
        this.idExperienciaEducativa = 0;
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

    public int getIdExperienciaEducativa() {
        return idExperienciaEducativa;
    }

    public void setIdExperienciaEducativa(int idExperienciaEducativa) {
        this.idExperienciaEducativa = idExperienciaEducativa;
    }

    public String getNombreExperienciaEducativa() {
        return nombreExperienciaEducativa;
    }

    public void setNombreExperienciaEducativa(String nombreExperienciaEducativa) {
        this.nombreExperienciaEducativa = nombreExperienciaEducativa;
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
    
    @Override
    public String toString() {
        return nombreExperienciaEducativa + " - " + problema;
    }
}
