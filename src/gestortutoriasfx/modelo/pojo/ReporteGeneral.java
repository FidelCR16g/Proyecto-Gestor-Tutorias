package gestortutoriasfx.modelo.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class ReporteGeneral implements Serializable {
    private int idReporteGeneral;            
    private int idPeriodoEscolar;                
    private int idProgramaEducativo;         
    private int idCoordinador;               

    private String nombreProgramaEducativo;      
    private String nombreCoordinador;            
    private String nombrePeriodoEscolar;         

    private int numSesion;                       
    private LocalDate fecha;                     
    private String objetivos;                    

    private int totalAlumnosRegistrados;         
    private int totalAlumnosAsistieron;          

    private BigDecimal porcentajeAsistencia;

    private Estatus estatus;                     
    private LocalDateTime fechaGeneracion;       
    private String estadoLugar;                  

    public enum Estatus {
        Borrador,
        Finalizado,
        Enviado,
        Revisado
    }

    public ReporteGeneral() {
        this.estatus = Estatus.Borrador;
        this.totalAlumnosRegistrados = 0;
        this.totalAlumnosAsistieron = 0;
        this.fechaGeneracion = LocalDateTime.now();
    }

    public ReporteGeneral(int idReporteGeneral, int idPeriodoEscolar, int idProgramaEducativo, int idCoordinador,
                          String nombreProgramaEducativo, String nombreCoordinador, String nombrePeriodoEscolar,
                          int numSesion, LocalDate fecha, String objetivos,
                          int totalAlumnosRegistrados, int totalAlumnosAsistieron,
                          BigDecimal porcentajeAsistencia, Estatus estatus,
                          LocalDateTime fechaGeneracion, String estadoLugar) {
        this.idReporteGeneral = idReporteGeneral;
        this.idPeriodoEscolar = idPeriodoEscolar;
        this.idProgramaEducativo = idProgramaEducativo;
        this.idCoordinador = idCoordinador;
        this.nombreProgramaEducativo = nombreProgramaEducativo;
        this.nombreCoordinador = nombreCoordinador;
        this.nombrePeriodoEscolar = nombrePeriodoEscolar;
        this.numSesion = numSesion;
        this.fecha = fecha;
        this.objetivos = objetivos;
        this.totalAlumnosRegistrados = totalAlumnosRegistrados;
        this.totalAlumnosAsistieron = totalAlumnosAsistieron;
        this.porcentajeAsistencia = porcentajeAsistencia;
        this.estatus = estatus;
        this.fechaGeneracion = fechaGeneracion;
        this.estadoLugar = estadoLugar;
    }

    public int getIdReporteGeneral() {
        return idReporteGeneral;
    }

    public void setIdReporteGeneral(int idReporteGeneral) {
        this.idReporteGeneral = idReporteGeneral;
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

    public int getIdCoordinador() {
        return idCoordinador;
    }

    public void setIdCoordinador(int idCoordinador) {
        this.idCoordinador = idCoordinador;
    }

    public String getNombreProgramaEducativo() {
        return nombreProgramaEducativo;
    }

    public void setNombreProgramaEducativo(String nombreProgramaEducativo) {
        this.nombreProgramaEducativo = nombreProgramaEducativo;
    }

    public String getNombreCoordinador() {
        return nombreCoordinador;
    }

    public void setNombreCoordinador(String nombreCoordinador) {
        this.nombreCoordinador = nombreCoordinador;
    }

    public String getNombrePeriodoEscolar() {
        return nombrePeriodoEscolar;
    }

    public void setNombrePeriodoEscolar(String nombrePeriodoEscolar) {
        this.nombrePeriodoEscolar = nombrePeriodoEscolar;
    }

    public int getNumSesion() {
        return numSesion;
    }

    public void setNumSesion(int numSesion) {
        this.numSesion = numSesion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public int getTotalAlumnosRegistrados() {
        return totalAlumnosRegistrados;
    }

    public void setTotalAlumnosRegistrados(int totalAlumnosRegistrados) {
        this.totalAlumnosRegistrados = totalAlumnosRegistrados;
    }

    public int getTotalAlumnosAsistieron() {
        return totalAlumnosAsistieron;
    }

    public void setTotalAlumnosAsistieron(int totalAlumnosAsistieron) {
        this.totalAlumnosAsistieron = totalAlumnosAsistieron;
    }

    public BigDecimal getPorcentajeAsistencia() {
        if (porcentajeAsistencia != null) return porcentajeAsistencia;

        if (totalAlumnosRegistrados <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal asistieron = BigDecimal.valueOf(totalAlumnosAsistieron);
        BigDecimal registrados = BigDecimal.valueOf(totalAlumnosRegistrados);
        return asistieron
                .multiply(BigDecimal.valueOf(100))
                .divide(registrados, 2, RoundingMode.HALF_UP);
    }

    public void setPorcentajeAsistencia(BigDecimal porcentajeAsistencia) {
        this.porcentajeAsistencia = porcentajeAsistencia;
    }

    public Estatus getEstatus() {
        return estatus;
    }

    public void setEstatus(Estatus estatus) {
        this.estatus = estatus;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getEstadoLugar() {
        return estadoLugar;
    }

    public void setEstadoLugar(String estadoLugar) {
        this.estadoLugar = estadoLugar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReporteGeneral)) return false;
        ReporteGeneral that = (ReporteGeneral) o;
        return Objects.equals(idReporteGeneral, that.idReporteGeneral);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReporteGeneral);
    }

    @Override
    public String toString() {
        return "Reporte General Sesión " + numSesion + " - " + nombreProgramaEducativo + " (" + estatus + ")";
    }
}