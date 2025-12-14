package gestortutoriasfx.utilidades;

import gestortutoriasfx.modelo.pojo.ReporteTutoria;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
import java.util.function.BiConsumer;

public class TarjetaReporte extends TarjetaSesion {

    public TarjetaReporte(SesionTutoria sesion, ReporteTutoria reporte, BiConsumer<TarjetaSesion, ReporteTutoria> accionClick) {
        super(sesion, determinarTexto(reporte), determinarEstilo(reporte), null);
        
        this.setOnMouseClicked(e -> accionClick.accept(this, reporte));
    }

    private static String determinarTexto(ReporteTutoria reporte) {
        if (reporte == null) return "Pendiente";
        if ("Enviado".equalsIgnoreCase(reporte.getEstatus())) return "Enviado";
        return reporte.getEstatus();
    }

    private static String determinarEstilo(ReporteTutoria reporte) {
        if (reporte == null) return "-fx-text-fill: #757575; -fx-font-style: italic;";
        if ("Enviado".equalsIgnoreCase(reporte.getEstatus())) return "-fx-text-fill: #2e7d32; -fx-font-weight: bold;";
        return "-fx-text-fill: #f57c00; -fx-font-weight: bold;";
    }
}