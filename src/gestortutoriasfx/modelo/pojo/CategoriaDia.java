package gestortutoriasfx.modelo.pojo;

import java.io.Serializable;
import java.util.Objects;

public class CategoriaDia implements Serializable {

    private Integer idCategoriaDia;   
    private int idDiaCalendario;      
    private Categoria categoria;      

    public enum Categoria {
        InicioClases,
        FinClases,
        InicioIntersemestral,
        FinalIntersemestral,
        InscripcionEnLinea,
        ConsultaHorarioPago,
        CursoInduccion,
        ExamenesOrdinarios,
        ExamenesExtraordinarios,
        ExamenesTitulo,
        Inhabil,
        Festivo,
        Vacaciones,
        FILU,
        InicioTutoria,
        FinalTutoria,
        Tutoria,
        InicioCambioTutorado,
        FinalCambioTutorado,
        InicioRegistroReporteTutoria,
        FinalRegistroReporteTutoria,
        InicioRegistroReporteGeneral,
        FinalRegistroReporteGeneral,
        FinalActaConsejoTecnico
    }

    public CategoriaDia() {}

    public CategoriaDia(Integer idCategoriaDia, int idDiaCalendario, Categoria categoria) {
        this.idCategoriaDia = idCategoriaDia;
        this.idDiaCalendario = idDiaCalendario;
        this.categoria = categoria;
    }

    public Integer getIdCategoriaDia() { return idCategoriaDia; }
    public void setIdCategoriaDia(Integer idCategoriaDia) { this.idCategoriaDia = idCategoriaDia; }

    public int getIdDiaCalendario() { return idDiaCalendario; }
    public void setIdDiaCalendario(int idDiaCalendario) { this.idDiaCalendario = idDiaCalendario; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoriaDia)) return false;
        CategoriaDia that = (CategoriaDia) o;
        return Objects.equals(idCategoriaDia, that.idCategoriaDia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCategoriaDia);
    }

    @Override
    public String toString() {
        return "CategoriaDia{" +
                "idCategoriaDia=" + idCategoriaDia +
                ", idDiaCalendario=" + idDiaCalendario +
                ", categoria=" + categoria +
                '}';
    }
}