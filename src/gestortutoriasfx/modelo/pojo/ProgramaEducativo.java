package gestortutoriasfx.modelo.pojo;

import java.io.Serializable;
import java.util.Objects;

public class ProgramaEducativo implements Serializable {

    private Integer idProgramaEducativo; 
    private int idFacultad;             
    private Integer idSupervisor;       
    private Integer idCoordinador;      
    private String nombre;              
    private int plan;                   
    private Area area;                  
    private Nivel nivel;                
    private Modalidad modalidad;        
    private int creditos;
    private int cupo;
    private int numPeriodos;

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

    public ProgramaEducativo(Integer idProgramaEducativo, int idFacultad, Integer idSupervisor, Integer idCoordinador,
                             String nombre, int plan, Area area, Nivel nivel, Modalidad modalidad,
                             int creditos, int cupo, int numPeriodos) {
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
    }

    public Integer getIdProgramaEducativo() { return idProgramaEducativo; }
    public void setIdProgramaEducativo(Integer idProgramaEducativo) { this.idProgramaEducativo = idProgramaEducativo; }

    public int getIdFacultad() { return idFacultad; }
    public void setIdFacultad(int idFacultad) { this.idFacultad = idFacultad; }

    public Integer getIdSupervisor() { return idSupervisor; }
    public void setIdSupervisor(Integer idSupervisor) { this.idSupervisor = idSupervisor; }

    public Integer getIdCoordinador() { return idCoordinador; }
    public void setIdCoordinador(Integer idCoordinador) { this.idCoordinador = idCoordinador; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getPlan() { return plan; }
    public void setPlan(int plan) { this.plan = plan; }

    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }

    public Nivel getNivel() { return nivel; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }

    public Modalidad getModalidad() { return modalidad; }
    public void setModalidad(Modalidad modalidad) { this.modalidad = modalidad; }

    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }

    public int getCupo() { return cupo; }
    public void setCupo(int cupo) { this.cupo = cupo; }

    public int getNumPeriodos() { return numPeriodos; }
    public void setNumPeriodos(int numPeriodos) { this.numPeriodos = numPeriodos; }

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
        String n = (nombre != null) ? nombre : "Programa";
        return n + " (Plan " + plan + ")";
    }
}
