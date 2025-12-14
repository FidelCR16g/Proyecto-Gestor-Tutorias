package gestortutoriasfx.utilidades;

import gestortutoriasfx.modelo.pojo.Tutor;
import gestortutoriasfx.utilidad.TarjetaBase;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.function.Consumer;

public class TarjetaTutor extends TarjetaBase {

    private final Tutor tutor;

    public TarjetaTutor(Tutor tutor, Consumer<Tutor> accionAlSeleccionar) {
        super();
        this.tutor = tutor;
        inicializarContenido(accionAlSeleccionar);
    }

    private void inicializarContenido(Consumer<Tutor> accionClick) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setPrefHeight(100);
        this.setPrefWidth(280);

        Label lbNombre = new Label(tutor.getNombreCompleto());
        lbNombre.setFont(Font.font("System", FontWeight.BOLD, 14));
        lbNombre.setStyle("-fx-text-fill: #212121;");
        lbNombre.setWrapText(true);
        lbNombre.setAlignment(Pos.CENTER);
        lbNombre.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        HBox infoInferior = new HBox();
        infoInferior.setAlignment(Pos.CENTER);
        
        Label lbUsuario = new Label(tutor.getNumPersonal()); 
        lbUsuario.setStyle("-fx-text-fill: #757575; -fx-font-size: 12px;");
        
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label lbEspacios = new Label("Espacios: " + tutor.getEspaciosTutorados());
        lbEspacios.setStyle("-fx-text-fill: #757575; -fx-font-size: 12px;");

        infoInferior.getChildren().addAll(lbUsuario, spacer, lbEspacios);

        this.getChildren().addAll(lbNombre, infoInferior);

        this.setOnMouseClicked(e -> {
            if (accionClick != null) {
                accionClick.accept(tutor);
            }
            this.seleccionar();
        });
    }

    public Tutor getTutor() {
        return tutor;
    }
}