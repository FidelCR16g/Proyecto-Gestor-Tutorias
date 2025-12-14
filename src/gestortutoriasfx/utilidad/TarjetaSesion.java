package gestortutoriasfx.utilidades;

import gestortutoriasfx.modelo.pojo.SesionTutoria;
import gestortutoriasfx.utilidad.TarjetaBase;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TarjetaSesion extends TarjetaBase {

    private SesionTutoria sesion;

    public TarjetaSesion(SesionTutoria sesion, String textoEstado, String estiloEstado, Runnable accionClick) {
        super();
        this.sesion = sesion;
        inicializarContenido(textoEstado, estiloEstado, accionClick);
    }

    private void inicializarContenido(String textoEstado, String estiloEstado, Runnable accionClick) {
        Label lbTitulo = new Label("Tutoría No. " + sesion.getNumSesion());
        lbTitulo.setFont(Font.font("System", FontWeight.BOLD, 16));
        lbTitulo.setStyle("-fx-text-fill: #37474f;");

        if (textoEstado != null) {
            Label lbEstatus = new Label(textoEstado);
            lbEstatus.setStyle(estiloEstado != null ? estiloEstado : "-fx-text-fill: #757575;");
            this.getChildren().addAll(lbTitulo, lbEstatus);
        } else {
            this.getChildren().add(lbTitulo);
        }

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String fechaTexto = (sesion.getFecha() != null) ? sesion.getFecha() : "Sin fecha";
        Label lbFecha = new Label("Fecha: " + fechaTexto);
        lbFecha.setFont(Font.font("System", 14));
        lbFecha.setStyle("-fx-text-fill: #78909c;");

        this.getChildren().addAll(spacer, lbFecha);
        
        this.setOnMouseClicked(e -> accionClick.run());
    }

    public SesionTutoria getSesion() {
        return sesion;
    }
}