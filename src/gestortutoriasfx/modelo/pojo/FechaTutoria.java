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
    private int idPeriodo;
    private int numSesion;
    private String fechaInicio;
    private String fechaCierre;

    public FechaTutoria() {
    }

    public FechaTutoria(int idFechaTutoria, int idPeriodo, int numSesion, String fechaInicio, String fechaCierre) {
        this.idFechaTutoria = idFechaTutoria;
        this.idPeriodo = idPeriodo;
        this.numSesion = numSesion;
        this.fechaInicio = fechaInicio;
        this.fechaCierre = fechaCierre;
    }

    public int getIdFechaTutoria() {
        return idFechaTutoria;
    }

    public void setIdFechaTutoria(int idFechaTutoria) {
        this.idFechaTutoria = idFechaTutoria;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public int getNumSesion() {
        return numSesion;
    }

    public void setNumSesion(int numSesion) {
        this.numSesion = numSesion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(String fechaCierre) {
        this.fechaCierre = fechaCierre;
    }
    
    @Override
    public String toString() {
        return numSesion + ". Sesión (" + fechaInicio + " al " + fechaCierre + ")";
    }
}
