package gestortutoriasfx.utilidad;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public abstract class TarjetaBase extends HBox {

    public TarjetaBase() {
        configurarEstiloBase();
    }

    private void configurarEstiloBase() {
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(15);
        this.setPadding(new Insets(15));
        this.deseleccionar();
    }

    public void seleccionar() {
        this.setStyle("-fx-background-color: #e3f2fd; "
                + "-fx-border-color: #1565c0; "
                + "-fx-border-width: 2; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5; "
                + "-fx-cursor: hand;");
    }

    public void deseleccionar() {
        this.setStyle("-fx-background-color: #ffffff; "
                + "-fx-border-color: #bdbdbd; "
                + "-fx-border-width: 1; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5; "
                + "-fx-cursor: hand; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 1);");
    }
}