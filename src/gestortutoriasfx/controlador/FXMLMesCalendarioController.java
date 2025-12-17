package gestortutoriasfx.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class FXMLMesCalendarioController {

    @FXML private VBox rootMes;
    @FXML private Label lbNombreMes;
    @FXML private GridPane gpMes;

    private static final String[] ENCABEZADOS = {"L", "M", "M", "J", "V", "S", "D"};

    public void construirMes(
            YearMonth yearMonth,
            Map<LocalDate, Set<String>> categoriasPorDia,
            Consumer<LocalDate> onDiaSeleccionado
    ) {
        String nombreMes = yearMonth.getMonth()
                .getDisplayName(TextStyle.FULL, new Locale("es"))
                .toUpperCase();

        lbNombreMes.setText(nombreMes + " " + yearMonth.getYear());

        gpMes.getChildren().clear();

        ponerEncabezados();

        LocalDate primerDia = yearMonth.atDay(1);
        int colInicio = primerDia.getDayOfWeek().getValue() - 1;

        int fila = 1;
        int col = colInicio;

        int totalDias = yearMonth.lengthOfMonth();

        for (int dia = 1; dia <= totalDias; dia++) {
            LocalDate fecha = yearMonth.atDay(dia);
            Button btn = crearBotonDia(dia, fecha);

            Set<String> categorias = categoriasPorDia.get(fecha);
            aplicarEstilo(btn, categorias);

            if (!btn.isDisable()) {
                btn.setOnAction(e -> onDiaSeleccionado.accept(fecha));
            }

            GridPane.setConstraints(btn, col, fila);
            gpMes.getChildren().add(btn);

            col++;
            if (col == 7) {
                col = 0;
                fila++;
            }
        }
    }

    private void ponerEncabezados() {
        for (int col = 0; col < 7; col++) {
            Label lb = new Label(ENCABEZADOS[col]);
            lb.setStyle("-fx-font-weight: bold; -fx-text-fill: #455A64;");
            GridPane.setConstraints(lb, col, 0);
            gpMes.getChildren().add(lb);
        }
    }

    private Button crearBotonDia(int numero, LocalDate fecha) {
        Button b = new Button(String.valueOf(numero));
        b.setMaxWidth(Double.MAX_VALUE);
        b.setPrefHeight(32);
        b.setStyle("-fx-background-radius: 8;");
        b.setUserData(fecha);
        return b;
    }

    private void aplicarEstilo(Button b, Set<String> categorias) {

        if (categorias == null || categorias.isEmpty()) {
            b.setStyle("-fx-background-color: #E8F5E9;");
            return;
        }

        if (categorias.contains("Tutoria")) {
            b.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white;");
            b.setDisable(true);
            return;
        }

        if (categorias.contains("Inhabil")
                || categorias.contains("Festivo")
                || categorias.contains("Vacaciones")) {
            b.setStyle("-fx-background-color: #BDBDBD;");
            b.setDisable(true);
        }
    }
}

