package gestortutoriasfx.utilidades; // Asegúrate que el paquete sea el correcto

import gestortutoriasfx.modelo.pojo.Tutor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.function.Consumer;

public class TarjetaTutor extends VBox { 

    private final Tutor tutor;
    
    private final String ESTILO_NORMAL = "-fx-background-color: #ffffff; "
                + "-fx-border-color: #bdbdbd; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5; "
                + "-fx-cursor: hand; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 1);";
    
    private final String ESTILO_SELECCIONADO = "-fx-background-color: #e3f2fd; "
                + "-fx-border-color: #1565c0; "
                + "-fx-border-width: 2; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5; "
                + "-fx-cursor: hand;";

    public TarjetaTutor(Tutor tutor, Consumer<Tutor> accionAlSeleccionar) {
        super();
        this.tutor = tutor;
        inicializarContenido(accionAlSeleccionar);
    }

    private void inicializarContenido(Consumer<Tutor> accionClick) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setPadding(new Insets(15));
        this.setPrefWidth(280);
        
        this.setMinHeight(80); 
        this.setPrefHeight(USE_COMPUTED_SIZE);

        this.setStyle(ESTILO_NORMAL);

        Label lbNombre = new Label(tutor.getNombreCompleto());
        lbNombre.setFont(Font.font("System", FontWeight.BOLD, 14));
        lbNombre.setStyle("-fx-text-fill: #212121;");
        lbNombre.setWrapText(true);
        lbNombre.setAlignment(Pos.CENTER);
        lbNombre.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        lbNombre.setMaxWidth(Double.MAX_VALUE); 

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
        });
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void seleccionar() {
        this.setStyle(ESTILO_SELECCIONADO);
    }

    public void deseleccionar() {
        this.setStyle(ESTILO_NORMAL);
    }
}