package gestortutoriasfx.modelo.pojo;

/**
 * Nombre de la Clase: PeriodoEscolar
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 26/11/2025
 *
 * Descripción:
 * Clase encargada de contener los datos del periodo escolar.
 */

public class PeriodoEscolar {
    private int idPeriodoEscolar;
    private String rangoPeriodo;
    private String fechaInicio;
    private String fechaFin;
    private boolean estado;
    private String nombrePeriodoEscolar;
    
    public PeriodoEscolar(){
    }

    public PeriodoEscolar(int idPeriodoEscolar, String rangoPeriodo, String fechaInicio, String fechaFin, boolean estado, String nombrePeriodoEscolar) {
        this.idPeriodoEscolar = idPeriodoEscolar;
        this.rangoPeriodo = rangoPeriodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.nombrePeriodoEscolar = nombrePeriodoEscolar;
    }

    public int getIdPeriodoEscolar() {
        return idPeriodoEscolar;
    }

    public void setIdPeriodoEscolar(int idPeriodoEscolar) {
        this.idPeriodoEscolar = idPeriodoEscolar;
    }

    public String getRangoPeriodo() {
        return rangoPeriodo;
    }

    public void setRangoPeriodo(String rangoPeriodo) {
        this.rangoPeriodo = rangoPeriodo;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getNombrePeriodoEscolar() {
        return nombrePeriodoEscolar;
    }

    public void setNombrePeriodoEscolar(String nombrePeriodoEscolar) {
        this.nombrePeriodoEscolar = nombrePeriodoEscolar;
    }
    
    
}