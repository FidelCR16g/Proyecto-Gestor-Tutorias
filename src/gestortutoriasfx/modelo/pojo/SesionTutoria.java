package gestortutoriasfx.modelo.pojo;

/**
 * Nombre de la Clase: SesionTutoria
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
 * Clase encargada de contener los datos de la sesion de tutoria.
 */

public class SesionTutoria {
    private int idSesion;
    private int idPeriodo;
    private int idTutor;
    private String matriculaEstudiante;
    private int numSesion;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String estado;
    
    private String nombreEstudiante; 
    private String periodo;
    
    private boolean asistencia;

    public SesionTutoria() {
    }

    public SesionTutoria(int idSesion, int idPeriodo, int idTutor, String matriculaEstudiante, int numSesion, String fecha, String horaInicio, String horaFin, String estado, String nombreEstudiante, String periodo, boolean asistencia) {
        this.idSesion = idSesion;
        this.idPeriodo = idPeriodo;
        this.idTutor = idTutor;
        this.matriculaEstudiante = matriculaEstudiante;
        this.numSesion = numSesion;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
        this.nombreEstudiante = nombreEstudiante;
        this.periodo = periodo;
        this.asistencia = asistencia;
    }

    public boolean isAsistencia() {
        return asistencia;
    }

    public void setAsistencia(boolean asistencia) {
        this.asistencia = asistencia;
    }

    public int getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(int idSesion) {
        this.idSesion = idSesion;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public int getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(int idTutor) {
        this.idTutor = idTutor;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getMatriculaEstudiante() {
        return matriculaEstudiante;
    }

    public void setMatriculaEstudiante(String matriculaEstudiante) {
        this.matriculaEstudiante = matriculaEstudiante;
    }

    public int getNumSesion() {
        return numSesion;
    }

    public void setNumSesion(int numSesion) {
        this.numSesion = numSesion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
    
    @Override
    public String toString() {
        return this.nombreEstudiante + " | Matricula: " + this.matriculaEstudiante;
    }
}