package gestortutoriasfx.modelo.pojo;

import java.io.Serializable;
import java.util.Objects;

public class ProgramaEducativo implements Serializable {

    private int idProgramaEducativo; 
    private int idFacultad;             
    private int idSupervisor;       
    private int idCoordinador;      
    private String nombre;              
    private int plan;                   
    private Area area;                  
    private Nivel nivel;                
    private Modalidad modalidad;        
    private int creditos;
    private int cupo;
    private int numPeriodos;
    
    private String nombreFacultad;
    private String nombreCoordinador;
    private String nombreSupervisor;

    public enum Area {
        Artes,
        Ciencias_Biologicas_y_Agropecuarias,
        Ciencias_de_Salud,
        Economico_Administrativa,
        Humanidades,
        Tecnica
    }

    public enum Nivel {
        Tecnico,
        TSU,
        Licenciatura,
        Especializaciones,
        Especialidades_medicas,
        Maestrias,
        Doctorados
    }

    public enum Modalidad {
        Escolarizado,
        Abierto,
        Virtual,
        Mixto
    }

    public ProgramaEducativo() {}

    public ProgramaEducativo(int idProgramaEducativo, int idFacultad, int idSupervisor, int idCoordinador, String nombre, int plan, Area area, Nivel nivel, Modalidad modalidad, int creditos, int cupo, int numPeriodos, String nombreFacultad, String nombreCoordinador, String nombreSupervisor) {
        this.idProgramaEducativo = idProgramaEducativo;
        this.idFacultad = idFacultad;
        this.idSupervisor = idSupervisor;
        this.idCoordinador = idCoordinador;
        this.nombre = nombre;
        this.plan = plan;
        this.area = area;
        this.nivel = nivel;
        this.modalidad = modalidad;
        this.creditos = creditos;
        this.cupo = cupo;
        this.numPeriodos = numPeriodos;
        this.nombreFacultad = nombreFacultad;
        this.nombreCoordinador = nombreCoordinador;
        this.nombreSupervisor = nombreSupervisor;
    }

    public int getIdProgramaEducativo() {
        return idProgramaEducativo;
    }

    public void setIdProgramaEducativo(int idProgramaEducativo) {
        this.idProgramaEducativo = idProgramaEducativo;
    }

    public int getIdFacultad() {
        return idFacultad;
    }

    public void setIdFacultad(int idFacultad) {
        this.idFacultad = idFacultad;
    }

    public int getIdSupervisor() {
        return idSupervisor;
    }

    public void setIdSupervisor(int idSupervisor) {
        this.idSupervisor = idSupervisor;
    }

    public int getIdCoordinador() {
        return idCoordinador;
    }

    public void setIdCoordinador(int idCoordinador) {
        this.idCoordinador = idCoordinador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public int getNumPeriodos() {
        return numPeriodos;
    }

    public void setNumPeriodos(int numPeriodos) {
        this.numPeriodos = numPeriodos;
    }

    public String getNombreFacultad() {
        return nombreFacultad;
    }

    public void setNombreFacultad(String nombreFacultad) {
        this.nombreFacultad = nombreFacultad;
    }

    public String getNombreCoordinador() {
        return nombreCoordinador;
    }

    public void setNombreCoordinador(String nombreCoordinador) {
        this.nombreCoordinador = nombreCoordinador;
    }

    public String getNombreSupervisor() {
        return nombreSupervisor;
    }

    public void setNombreSupervisor(String nombreSupervisor) {
        this.nombreSupervisor = nombreSupervisor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProgramaEducativo)) return false;
        ProgramaEducativo that = (ProgramaEducativo) o;
        return Objects.equals(idProgramaEducativo, that.idProgramaEducativo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProgramaEducativo);
    }

    @Override
    public String toString() {
        String programa = (nombre != null) ? nombre : "Programa";
        return programa + " (Plan " + plan + ")";
    }
}