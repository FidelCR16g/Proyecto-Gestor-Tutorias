package gestortutoriasfx.utilidades;

import gestortutoriasfx.modelo.pojo.Evidencia;
import gestortutoriasfx.utilidad.TarjetaBase;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TarjetaArchivo extends TarjetaBase {

    private Evidencia evidencia;

    public TarjetaArchivo(Evidencia evidencia, Consumer<Evidencia> accionEliminar) {
        super();
        this.evidencia = evidencia;
        inicializarContenido(accionEliminar);
    }

    private void inicializarContenido(Consumer<Evidencia> accionEliminar) {
        Label lbIcono = new Label("📄");
        lbIcono.setStyle("-fx-font-size: 20px;");

        Label lbNombre = new Label(evidencia.getNombreArchivo());
        lbNombre.setStyle("-fx-font-weight: bold; -fx-text-fill: #37474f;");

        String txtTamano = evidencia.getTamanoKB() > 0 ? evidencia.getTamanoKB() + " KB" : "Guardado";
        Label lbTamano = new Label(txtTamano);
        lbTamano.setStyle("-fx-text-fill: #90a4ae; -fx-font-size: 11px;");

        VBox info = new VBox(lbNombre, lbTamano);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnEliminar = new Button("✕");
        btnEliminar.setStyle("-fx-background-color: transparent; -fx-text-fill: #ef5350; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 14px;");
        
        btnEliminar.setOnAction(e -> accionEliminar.accept(this.evidencia));

        this.getChildren().addAll(lbIcono, info, spacer, btnEliminar);
    }
    
    public Evidencia getEvidencia() {
        return evidencia;
    }
}