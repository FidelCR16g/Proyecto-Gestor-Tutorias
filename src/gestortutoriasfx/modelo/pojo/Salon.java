package gestortutoriasfx.modelo.pojo;

/**
 * Nombre de la Clase: Salon
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

public class Salon {
    private int idSalon;
    private int idEdificio;
    private String nombreSalon;
    private int cupo;

    private String nombreEdificio;
    
    public Salon(){
    }

    public Salon(int idSalon, int idEdificio, String nombreSalon, int cupo, String nombreEdificio) {
        this.idSalon = idSalon;
        this.idEdificio = idEdificio;
        this.nombreSalon = nombreSalon;
        this.cupo = cupo;
        this.nombreEdificio = nombreEdificio;
    }

    public int getIdSalon() {
        return idSalon;
    }

    public void setIdSalon(int idSalon) {
        this.idSalon = idSalon;
    }

    public int getIdEdificio() {
        return idEdificio;
    }

    public void setIdEdificio(int idEdificio) {
        this.idEdificio = idEdificio;
    }

    public String getNombreSalon() {
        return nombreSalon;
    }

    public void setNombreSalon(String nombreSalon) {
        this.nombreSalon = nombreSalon;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public String getNombreEdificio() {
        return nombreEdificio;
    }

    public void setNombreEdificio(String nombreEdificio) {
        this.nombreEdificio = nombreEdificio;
    }

    @Override
    public String toString() {
        return "Salon: " + nombreSalon + " - " + "Edificio: " + nombreEdificio;
    }
}
