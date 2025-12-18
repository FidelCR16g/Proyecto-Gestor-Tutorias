package gestortutoriasfx.modelo.pojo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class DiaCalendario implements Serializable {

    private int idDiaCalendario;  
    private LocalDate fecha;          
    private int idPeriodoEscolar;      

    public DiaCalendario() {}

    public DiaCalendario(Integer idDiaCalendario, LocalDate fecha, int idPeriodoEscolar) {
        this.idDiaCalendario = idDiaCalendario;
        this.fecha = fecha;
        this.idPeriodoEscolar = idPeriodoEscolar;
    }

    public Integer getIdDiaCalendario() { return idDiaCalendario; }
    public void setIdDiaCalendario(Integer idDiaCalendario) { this.idDiaCalendario = idDiaCalendario; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public int getIdPeriodoEscolar() { return idPeriodoEscolar; }
    public void setIdPeriodoEscolar(int idPeriodoEscolar) { this.idPeriodoEscolar = idPeriodoEscolar; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiaCalendario)) return false;
        DiaCalendario that = (DiaCalendario) o;
        return Objects.equals(idDiaCalendario, that.idDiaCalendario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDiaCalendario);
    }

    @Override
    public String toString() {
        return "DiaCalendario{" +
                "idDiaCalendario=" + idDiaCalendario +
                ", fecha=" + fecha +
                ", idPeriodoEscolar=" + idPeriodoEscolar +
                '}';
    }
}
