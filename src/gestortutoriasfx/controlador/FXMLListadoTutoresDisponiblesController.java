package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.TutorImplementacion;
import gestortutoriasfx.modelo.pojo.Tutor;
import gestortutoriasfx.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Nombre de la Clase: FXMLListadoTutoresDisponiblesController
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 07/12/2025
 *
 * Descripción:
 * Se encarga de controlar el listado de tutores disponibles
 */

public class FXMLListadoTutoresDisponiblesController implements Initializable {

    @FXML private GridPane gpListaTutores;
    @FXML private Button btnSeleccionar;
    @FXML private Label lbMensajeSeleccion;

    private Tutor tutorSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTutores();
    }

    private void cargarTutores() {
        HashMap<String, Object> respuesta = TutorImplementacion.obtenerListaTutores();
        
        if (!(boolean) respuesta.get("error")) {
            ArrayList<Tutor> lista = (ArrayList<Tutor>) respuesta.get("tutores");
            
            gpListaTutores.getChildren().clear();
            
            int columna = 0;
            int fila = 0;

            for (Tutor t : lista) {
                VBox tarjeta = crearTarjetaTutor(t);
                gpListaTutores.add(tarjeta, columna, fila);
                
                columna++;
                if (columna == 3) {
                    columna = 0;
                    fila++;
                }
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }
    
    private VBox crearTarjetaTutor(Tutor tutor) {
        VBox tarjeta = new VBox();
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setSpacing(10);
        tarjeta.setPadding(new Insets(15));
        tarjeta.setPrefHeight(50);
        tarjeta.setPrefWidth(280);
        
        tarjeta.setStyle("-fx-background-color: #ffffff; -fx-border-color: #bdbdbd; -fx-border-width: 1; "
                + "-fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand;");
        
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

        tarjeta.getChildren().addAll(lbNombre, infoInferior);
        tarjeta.setOnMouseClicked(e -> seleccionarTarjeta(tarjeta, tutor));

        return tarjeta;
    }

    private void seleccionarTarjeta(VBox tarjeta, Tutor tutor) {
        this.tutorSeleccionado = tutor;
        
        for (javafx.scene.Node nodo : gpListaTutores.getChildren()) {
            if (nodo instanceof VBox) {
                nodo.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; "
                            + "-fx-border-color: #e0e0e0; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-cursor: hand;");
            }
        }
        
        tarjeta.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 10; -fx-border-radius: 10; "
                       + "-fx-border-color: #1565c0; -fx-border-width: 2; -fx-effect: dropshadow(three-pass-box, rgba(21,101,192,0.3), 8, 0, 0, 2); -fx-cursor: hand;");
        
        btnSeleccionar.setDisable(false);
        if (lbMensajeSeleccion != null) lbMensajeSeleccion.setVisible(false);
    }

    @FXML
    private void clicSeleccionar(ActionEvent event) {
        if (tutorSeleccionado != null) {
            irPantallaAsignacion(tutorSeleccionado);
        }
    }
    
    @FXML
    private void clicSalir(ActionEvent event) {
        try {
            Pane panel = (Pane) btnSeleccionar.getScene().lookup("#panelContenido");
            if (panel != null) panel.getChildren().clear();
        } catch (Exception e) {}
    }

    private void irPantallaAsignacion(Tutor tutor) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLAsignarTutorados.fxml");
            Parent root = cargador.load();
            
            FXMLAsignarTutoradosController controlador = cargador.getController();
            controlador.inicializarTutor(tutor);
            
            Pane panel = (Pane) btnSeleccionar.getScene().lookup("#panelContenido");
            if (panel != null) {
                panel.getChildren().clear();
                panel.getChildren().add(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar la pantalla de asignación.", Alert.AlertType.ERROR);
        }
    }
}