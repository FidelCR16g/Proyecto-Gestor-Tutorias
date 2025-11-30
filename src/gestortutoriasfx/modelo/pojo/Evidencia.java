package gestortutoriasfx.modelo.pojo;

/**
 * Nombre de la Clase: Evidencia
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 28/11/2025
 *
 * Descripción:
 * Clase encargada de contener los datos de la evidencia de la tutoria.
 */

public class Evidencia {
    private int idEvidencia;
    private int idSesion;
    private String nombreArchivo;
    private byte[] archivo;
    
    private boolean esNuevo; 
    private double tamanoKB;

    public Evidencia(){
    }
    
    public Evidencia(int idEvidencia, int idSesion, String nombreArchivo, byte[] archivo, boolean esNuevo, double tamanoKB) {
        this.idEvidencia = idEvidencia;
        this.idSesion = idSesion;
        this.nombreArchivo = nombreArchivo;
        this.archivo = archivo;
        this.esNuevo = esNuevo;
        this.tamanoKB = tamanoKB;
    }

    public int getIdEvidencia() {
        return idEvidencia;
    }

    public void setIdEvidencia(int idEvidencia) {
        this.idEvidencia = idEvidencia;
    }

    public int getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(int idSesion) {
        this.idSesion = idSesion;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public boolean isEsNuevo() {
        return esNuevo;
    }

    public void setEsNuevo(boolean esNuevo) {
        this.esNuevo = esNuevo;
    }

    public double getTamanoKB() {
        return tamanoKB;
    }

    public void setTamanoKB(double tamanoKB) {
        this.tamanoKB = tamanoKB;
    }
    
    
}
