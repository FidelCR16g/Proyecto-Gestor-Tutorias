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
    private int numSesion;
    private String descripcion;
    private String fecha; 
    private String fechaInicio;
    private String fechaCierre;

    public FechaTutoria() {
    }

    public int getIdFechaTutoria() { return idFechaTutoria; }
    public void setIdFechaTutoria(int idFechaTutoria) { this.idFechaTutoria = idFechaTutoria; }

    public int getIdPeriodoEscolar() { return idPeriodoEscolar; }
    public void setIdPeriodoEscolar(int idPeriodoEscolar) { this.idPeriodoEscolar = idPeriodoEscolar; }

    public int getNumSesion() { return numSesion; }
    public void setNumSesion(int numSesion) { this.numSesion = numSesion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) {
        this.fecha = fecha;
        // sincroniza compatibilidad
        this.fechaInicio = fecha;
        this.fechaCierre = fecha;
    }

    public String getFechaInicio() { return (fechaInicio != null) ? fechaInicio : fecha; }
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
        this.fecha = fechaInicio;
        this.fechaCierre = fechaInicio;
    }

    public String getFechaCierre() { return (fechaCierre != null) ? fechaCierre : fecha; }
    public void setFechaCierre(String fechaCierre) {
        this.fechaCierre = fechaCierre;
        this.fecha = (this.fecha != null) ? this.fecha : fechaCierre;
        this.fechaInicio = (this.fechaInicio != null) ? this.fechaInicio : fechaCierre;
    }

    @Override
    public String toString() {
        return numSesion + ". Sesión (" + getFechaInicio() + " - " + getFechaCierre() + ")";
    }
}
