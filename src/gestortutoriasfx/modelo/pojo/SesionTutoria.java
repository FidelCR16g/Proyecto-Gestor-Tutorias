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
    private Integer idTutor;
    private Integer idSalon;
    private Integer idEstudiante;
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

    public SesionTutoria() {
    }

    // Constructor “amplio” (si ya lo usabas)
    public SesionTutoria(int idSesion, int idPeriodoEscolar, Integer idTutor, Integer idSalon,
                         Integer idEstudiante, String matriculaEstudiante,
                         String nombreTutor, String nombreSalon, String nombreEstudiante,
                         int numSesion, String fecha, String horaInicio, String horaFin,
                         String estado, String modalidad, String periodo) {
        this.idSesion = idSesion;
        this.idPeriodoEscolar = idPeriodoEscolar;
        this.idTutor = idTutor;
        this.idSalon = idSalon;
        this.idEstudiante = idEstudiante;
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
        this.periodo = periodo;
    }

    public int getIdSesion() { return idSesion; }
    public void setIdSesion(int idSesion) { this.idSesion = idSesion; }

    public int getIdPeriodoEscolar() { return idPeriodoEscolar; }
    public void setIdPeriodoEscolar(int idPeriodoEscolar) { this.idPeriodoEscolar = idPeriodoEscolar; }

    public Integer getIdTutor() { return idTutor; }
    public void setIdTutor(Integer idTutor) { this.idTutor = idTutor; }

    public Integer getIdSalon() { return idSalon; }
    public void setIdSalon(Integer idSalon) { this.idSalon = idSalon; }

    public Integer getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(Integer idEstudiante) { this.idEstudiante = idEstudiante; }

    public String getMatriculaEstudiante() { return matriculaEstudiante; }
    public void setMatriculaEstudiante(String matriculaEstudiante) { this.matriculaEstudiante = matriculaEstudiante; }

    public String getNombreTutor() { return nombreTutor; }
    public void setNombreTutor(String nombreTutor) { this.nombreTutor = nombreTutor; }

    public String getNombreSalon() { return nombreSalon; }
    public void setNombreSalon(String nombreSalon) { this.nombreSalon = nombreSalon; }

    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }

    public int getNumSesion() { return numSesion; }
    public void setNumSesion(int numSesion) { this.numSesion = numSesion; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    @Override
    public String toString() {
        return this.nombreEstudiante + " | Matricula: " + this.matriculaEstudiante;
    }
}
