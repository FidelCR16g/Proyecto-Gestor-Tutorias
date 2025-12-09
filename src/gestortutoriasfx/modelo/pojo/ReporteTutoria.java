package gestortutoriasfx.modelo.pojo;

/**
 * Nombre de la Clase: ReporteTutoria
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 05/11/2025
 *
 * Descripción:
 * Clase encargada de contener los datos del periodo escolar.
 */

public class ReporteTutoria {
    private int idReporteTutoria;
    private int idTutor;
    private int idPeriodoEscolar;
    private int idProgramaEducativo;
    private String estatus;
    
    private String nombreTutor;
    private String nombrePrograma;
    private String nombrePeriodoEscolar;
    
    private String fechaPrimeraTutoria;
    private String fechaUltimaTutoria;
    private int numSesion;
    private int numAlumnosAsistieron;
    private int numAlumnosRiesgo;
    private String comentarios;
    private String estadoLugar;
    private String fechaCreacionReporte;

    public ReporteTutoria() {
        this.estatus = "En Llenado"; 
        this.numAlumnosAsistieron = 0;
        this.numAlumnosRiesgo = 0;
    }
    
    public ReporteTutoria(int idReporteTutoria, int idTutor, int idPeriodoEscolar, int idProgramaEducativo, String estatus, String nombreTutor, String nombrePrograma, String nombrePeriodoEscolar, String fechaPrimeraTutoria, String fechaUltimaTutoria, int numSesion, int numAlumnosAsistieron, int numAlumnosRiesgo, String comentarios, String estadoLugar, String fechaCreacionReporte) {
        this.idReporteTutoria = idReporteTutoria;
        this.idTutor = idTutor;
        this.idPeriodoEscolar = idPeriodoEscolar;
        this.idProgramaEducativo = idProgramaEducativo;
        this.estatus = estatus;
        this.nombreTutor = nombreTutor;
        this.nombrePrograma = nombrePrograma;
        this.nombrePeriodoEscolar = nombrePeriodoEscolar;
        this.fechaPrimeraTutoria = fechaPrimeraTutoria;
        this.fechaUltimaTutoria = fechaUltimaTutoria;
        this.numSesion = numSesion;
        this.numAlumnosAsistieron = numAlumnosAsistieron;
        this.numAlumnosRiesgo = numAlumnosRiesgo;
        this.comentarios = comentarios;
        this.estadoLugar = estadoLugar;
        this.fechaCreacionReporte = fechaCreacionReporte;
    }

    public int getIdReporteTutoria() {
        return idReporteTutoria;
    }

    public void setIdReporteTutoria(int idReporteTutoria) {
        this.idReporteTutoria = idReporteTutoria;
    }

    public int getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(int idTutor) {
        this.idTutor = idTutor;
    }

    public int getIdPeriodoEscolar() {
        return idPeriodoEscolar;
    }

    public void setIdPeriodoEscolar(int idPeriodoEscolar) {
        this.idPeriodoEscolar = idPeriodoEscolar;
    }

    public int getIdProgramaEducativo() {
        return idProgramaEducativo;
    }

    public void setIdProgramaEducativo(int idProgramaEducativo) {
        this.idProgramaEducativo = idProgramaEducativo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getNombreTutor() {
        return nombreTutor;
    }

    public void setNombreTutor(String nombreTutor) {
        this.nombreTutor = nombreTutor;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public String getNombrePeriodoEscolar() {
        return nombrePeriodoEscolar;
    }

    public void setNombrePeriodoEscolar(String nombrePeriodoEscolar) {
        this.nombrePeriodoEscolar = nombrePeriodoEscolar;
    }

    public String getFechaPrimeraTutoria() {
        return fechaPrimeraTutoria;
    }

    public void setFechaPrimeraTutoria(String fechaPrimeraTutoria) {
        this.fechaPrimeraTutoria = fechaPrimeraTutoria;
    }

    public String getFechaUltimaTutoria() {
        return fechaUltimaTutoria;
    }

    public void setFechaUltimaTutoria(String fechaUltimaTutoria) {
        this.fechaUltimaTutoria = fechaUltimaTutoria;
    }

    public int getNumSesion() {
        return numSesion;
    }

    public void setNumSesion(int numSesion) {
        this.numSesion = numSesion;
    }

    public int getNumAlumnosAsistieron() {
        return numAlumnosAsistieron;
    }

    public void setNumAlumnosAsistieron(int numAlumnosAsistieron) {
        this.numAlumnosAsistieron = numAlumnosAsistieron;
    }

    public int getNumAlumnosRiesgo() {
        return numAlumnosRiesgo;
    }

    public void setNumAlumnosRiesgo(int numAlumnosRiesgo) {
        this.numAlumnosRiesgo = numAlumnosRiesgo;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getEstadoLugar() {
        return estadoLugar;
    }

    public void setEstadoLugar(String estadoLugar) {
        this.estadoLugar = estadoLugar;
    }

    public String getFechaCreacionReporte() {
        return fechaCreacionReporte;
    }

    public void setFechaCreacionReporte(String fechaCreacionReporte) {
        this.fechaCreacionReporte = fechaCreacionReporte;
    }
    
    
    
    @Override
    public String toString() {
        return "Reporte Sesión " + numSesion + " (" + estatus + ")";
    }
}