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
    private int idPeriodoEscolar;
    private int idTutor;
    private int idSalon;
    private String matriculaEstudiante;
    
    private String nombreTutor;
    private String nombreSalon;
    private String nombreEstudiante;
    
    private int numSesion;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String estado;
    private String modalidad;

    private String periodo;
    private boolean asistencia;

    public SesionTutoria() {
    }

    public SesionTutoria(int idSesion, int idPeriodoEscolar, int idTutor, int idSalon, String matriculaEstudiante, String nombreTutor, String nombreSalon, String nombreEstudiante, int numSesion, String fecha, String horaInicio, String horaFin, String estado, String modalidad, boolean asistencia) {
        this.idSesion = idSesion;
        this.idPeriodoEscolar = idPeriodoEscolar;
        this.idTutor = idTutor;
        this.idSalon = idSalon;
        this.matriculaEstudiante = matriculaEstudiante;
        this.nombreTutor = nombreTutor;
        this.nombreSalon = nombreSalon;
        this.nombreEstudiante = nombreEstudiante;
        this.numSesion = numSesion;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
        this.modalidad = modalidad;
        this.asistencia = asistencia;
    }

    public int getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(int idSesion) {
        this.idSesion = idSesion;
    }

    public int getIdPeriodoEscolar() {
        return idPeriodoEscolar;
    }

    public void setIdPeriodoEscolar(int idPeriodoEscolar) {
        this.idPeriodoEscolar = idPeriodoEscolar;
    }

    public int getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(int idTutor) {
        this.idTutor = idTutor;
    }

    public int getIdSalon() {
        return idSalon;
    }

    public void setIdSalon(int idSalon) {
        this.idSalon = idSalon;
    }

    public String getMatriculaEstudiante() {
        return matriculaEstudiante;
    }

    public void setMatriculaEstudiante(String matriculaEstudiante) {
        this.matriculaEstudiante = matriculaEstudiante;
    }

    public String getNombreTutor() {
        return nombreTutor;
    }

    public void setNombreTutor(String nombreTutor) {
        this.nombreTutor = nombreTutor;
    }

    public String getNombreSalon() {
        return nombreSalon;
    }

    public void setNombreSalon(String nombreSalon) {
        this.nombreSalon = nombreSalon;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
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
        this.asistencia = "Asistio".equalsIgnoreCase(estado); 
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public boolean isAsistencia() {
        return "Asistio".equalsIgnoreCase(this.estado);
    }

    public void setAsistencia(boolean asistencia) {
        this.asistencia = asistencia;
        this.estado = asistencia ? "Asistio" : "No Asistio";
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