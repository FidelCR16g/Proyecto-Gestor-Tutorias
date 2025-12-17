package gestortutoriasfx.modelo.pojo;

/**
 *
 * @author fave6
 */
public class ProblematicaAcademica {

    // =======================
    // COMPATIBILIDAD (Reporte de Tutoría)
    // Tabla: problemasAcademicosReportadosTutoria
    // =======================
    private int idProblematicaAcademica;   // idProblemasAcademicosReportadosTutoria en BD
    private int idReporteTutoria;          // FK a reporteTutoria
    private int nrc;                       // NRC
    private String nombreNRC;              // nombreNRC (Experiencia Educativa)
    private String nombreProfesor;         // nombreProfesor
    private String problema;               // problema
    private int numEstudiantes;            // numEstudiantes

    // =======================
    // MODELO NUEVO (si lo usas después)
    // Tabla: problematica (detectada en sesión)
    // =======================
    private int idProblematica;                  // idProblematica en BD
    private int idSesion;                        // NOT NULL en BD
    private Integer idExperienciaEducativa;      // NULL en BD
    private Integer idEstudiante;                // NULL en BD
    private String nombreEstudiante;             // llenado por triggers / JOIN
    private String nombreExperienciaEducativa;   // llenado por triggers / JOIN
    private String titulo;                       // VARCHAR(100) NOT NULL
    private String descripcion;                  // TEXT NOT NULL
    private String estado;                       // ENUM('Abierta','Resuelta')

    public ProblematicaAcademica() {
    }

    // ✅ Constructor que usa tu Controller:
    // new ProblematicaAcademica(tfEE, tfProfesor, tfProblematica, cantidad)
    public ProblematicaAcademica(String nombreNRC, String nombreProfesor, String problema, int numEstudiantes) {
        this.nombreNRC = nombreNRC;
        this.nombreProfesor = nombreProfesor;
        this.problema = problema;
        this.numEstudiantes = numEstudiantes;
    }

    // ✅ Constructor “nuevo” (si luego lo ocupas)
    public ProblematicaAcademica(int idProblematica, int idSesion, Integer idExperienciaEducativa, Integer idEstudiante,
                                 String nombreProfesor, String nombreEstudiante, String nombreExperienciaEducativa,
                                 String titulo, String descripcion, String estado) {
        this.idProblematica = idProblematica;
        this.idSesion = idSesion;
        this.idExperienciaEducativa = idExperienciaEducativa;
        this.idEstudiante = idEstudiante;
        this.nombreProfesor = nombreProfesor;
        this.nombreEstudiante = nombreEstudiante;
        this.nombreExperienciaEducativa = nombreExperienciaEducativa;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    // =======================
    // GETTERS/SETTERS (Compat)
    // =======================
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

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    // =======================
    // GETTERS/SETTERS (Nuevo)
    // =======================
    public int getIdProblematica() {
        return idProblematica;
    }

    public void setIdProblematica(int idProblematica) {
        this.idProblematica = idProblematica;
    }

    public int getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(int idSesion) {
        this.idSesion = idSesion;
    }

    public Integer getIdExperienciaEducativa() {
        return idExperienciaEducativa;
    }

    public void setIdExperienciaEducativa(Integer idExperienciaEducativa) {
        this.idExperienciaEducativa = idExperienciaEducativa;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getNombreExperienciaEducativa() {
        return nombreExperienciaEducativa;
    }

    public void setNombreExperienciaEducativa(String nombreExperienciaEducativa) {
        this.nombreExperienciaEducativa = nombreExperienciaEducativa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        // Para la tabla de problemáticas del reporte, lo útil es esto:
        if (problema != null && !problema.trim().isEmpty()) {
            return nombreNRC + " | " + nombreProfesor + " | " + problema + " (" + numEstudiantes + ")";
        }
        // Para la tabla nueva (sesión)
        return (titulo != null ? titulo : "Problemática") + " (" + (estado != null ? estado : "") + ")";
    }
}
