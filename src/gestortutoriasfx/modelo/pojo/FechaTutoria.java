package gestortutoriasfx.modelo.pojo;

/**
 * Nombre de la Clase: FechaTutoria
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
 * Clase encargada de contener los datos de la fecha de tutoria.
 */

public class FechaTutoria {
    private int idFechaTutoria;
    private int idPeriodoEscolar;
    private String descripcion;
    private int numSesion;
    private String fecha;
    private String fechaInicio;
    private String fechaCierre;

    public FechaTutoria() {
    }

    public FechaTutoria(int idFechaTutoria, int idPeriodoEscolar, String descripcion, int numSesion, String fecha, String fechaInicio, String fechaCierre) {
        this.idFechaTutoria = idFechaTutoria;
        this.idPeriodoEscolar = idPeriodoEscolar;
        this.descripcion = descripcion;
        this.numSesion = numSesion;
        this.fecha = fecha;
        this.fechaInicio = fechaInicio;
        this.fechaCierre = fechaCierre;
    }

    public int getIdFechaTutoria() {
        return idFechaTutoria;
    }

    public void setIdFechaTutoria(int idFechaTutoria) {
        this.idFechaTutoria = idFechaTutoria;
    }

    public int getIdPeriodoEscolar() {
        return idPeriodoEscolar;
    }

    public void setIdPeriodoEscolar(int idPeriodo) {
        this.idPeriodoEscolar = idPeriodo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fecha) {
        this.fechaInicio = fecha;
    }
    
    public void setFechaInicio() {
        this.fechaInicio = this.fecha;
    }

    public String getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(String fecha) {
        this.fechaCierre = fecha;
    }
    
    public void setFechaCierre() {
        this.fechaCierre = this.fecha;
    }
    
    public String toString() {
        return numSesion + ". Sesión (" + getFecha() + ")";
    }
}