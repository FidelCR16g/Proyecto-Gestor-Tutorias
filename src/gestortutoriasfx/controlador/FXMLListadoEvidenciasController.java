package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.SesionTutoriaImplementacion;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Nombre de la Clase: FXMLListadoEvidenciasController
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 28/11/2025
 *
 * Descripción:
 * Se encarga de controlar los distintos elementos de los listados de evidencia
 */

public class FXMLListadoEvidenciasController implements Initializable {

    @FXML 
    private VBox vbListaSesiones;
    @FXML 
    private Button btnSubir;
    @FXML 
    private Button btnEditar;

    private SesionTutoria sesionSeleccionada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarSesiones();
    }
    
    @FXML
    private void clicEditarEvidencia(ActionEvent event) {
        if (sesionSeleccionada != null) {
            irPantallaGestionarEvidencia(sesionSeleccionada);
        }
    }
    
    @FXML
    private void clicSalir(ActionEvent event) {
        if (Utilidades.mostrarAlertaVerificacion("Salir", "¿Desea salir de esta seccion?",
                "Esta seguro que desea salir de esta seccion.")) {
            limpiarPanelCentral();
        }else{
            return;
        }
    }
    
    @FXML
    private void clicSubirEvidencia(ActionEvent event) {
        if (sesionSeleccionada != null) {
            irPantallaGestionarEvidencia(sesionSeleccionada);
        }
    }

    private void cargarSesiones() {
        HashMap<String, Object> respuesta = SesionTutoriaImplementacion.obtenerSesionesTutor(Sesion.getIdTutor());
        
        if (!(boolean) respuesta.get("error")) {
            ArrayList<SesionTutoria> listaSesiones = (ArrayList<SesionTutoria>) respuesta.get("sesiones");
            
            vbListaSesiones.getChildren().clear();
            
            ArrayList<Integer> agregadas = new ArrayList<>();
            
            for (SesionTutoria sesionTutoria : listaSesiones) {
                if (!agregadas.contains(sesionTutoria.getNumSesion())) {
                    HBox tarjeta = crearTarjetaSesion(sesionTutoria);
                    vbListaSesiones.getChildren().add(tarjeta);
                    agregadas.add(sesionTutoria.getNumSesion());
                }
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error", respuesta.get("mensaje").toString(), 
                    Alert.AlertType.WARNING);
        }
    }
    
    private HBox crearTarjetaSesion(SesionTutoria sesion) {
        HBox tarjeta = new HBox();
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setPadding(new Insets(15));
        tarjeta.setSpacing(20);
        tarjeta.setStyle("-fx-background-color: #ffffff; -fx-border-color: #bdbdbd; -fx-border-width: 1; "
                + "-fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand;");
        
        Label lbTitulo = new Label("Tutoría No. " + sesion.getNumSesion());
        lbTitulo.setFont(Font.font("System", FontWeight.BOLD, 16));
        lbTitulo.setStyle("-fx-text-fill: #37474f;");
        
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        String fechaTexto = (sesion.getFecha() != null) ? sesion.getFecha() : "Sin fecha";
        Label lbFecha = new Label("Fecha Tutoría: " + fechaTexto);
        lbFecha.setFont(Font.font("System", 14));
        lbFecha.setStyle("-fx-text-fill: #78909c;");
        
        tarjeta.getChildren().addAll(lbTitulo, spacer, lbFecha);
        
        tarjeta.setOnMouseClicked(event -> {
            seleccionarTarjeta(tarjeta, sesion);
        });

        return tarjeta;
    }
    
    private void irPantallaGestionarEvidencia(SesionTutoria sesion) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLGestionarEvidencia.fxml");
            Parent vista = cargador.load();
            
            FXMLGestionarEvidenciaController controlador = cargador.getController();
            controlador.inicializarSesion(sesionSeleccionada);
            
            Pane panelPrincipal = (Pane) btnSubir.getScene().lookup("#panelContenido");
            
            if (panelPrincipal != null) {
                panelPrincipal.getChildren().clear();
                panelPrincipal.getChildren().add(vista);
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar la pantalla de gestión.", 
                    Alert.AlertType.WARNING);
        }
    }
    
    private void limpiarPanelCentral() {
        try {
            javafx.scene.layout.Pane panel = (javafx.scene.layout.Pane) btnSubir.getScene().lookup("#panelContenido");
            if(panel != null) panel.getChildren().clear();
        } catch (Exception e) {}
    }
    
    private void seleccionarTarjeta(HBox tarjetaSeleccionada, SesionTutoria sesionDatos) {
        this.sesionSeleccionada = sesionDatos;
        
        boolean yaTieneArchivos = tieneEvidencias(sesionDatos.getNumSesion());
        
        if (yaTieneArchivos) {
            btnSubir.setDisable(true);
            btnEditar.setDisable(false);
        } else {
            btnSubir.setDisable(false);
            btnEditar.setDisable(true);
        }

        for (javafx.scene.Node nodo : vbListaSesiones.getChildren()) {
            if (nodo instanceof HBox) {
                nodo.setStyle("-fx-background-color: #ffffff; "
                        + "-fx-border-color: #bdbdbd; "
                        + "-fx-border-width: 1; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-radius: 5; "
                        + "-fx-cursor: hand;");
            }
        }
        tarjetaSeleccionada.setStyle("-fx-background-color: #e0e0e0; "
                + "-fx-border-color: #616161; "
                + "-fx-border-width: 2; "
                + "-fx-background-radius: 5; "
                + "-fx-border-radius: 5; "
                + "-fx-cursor: hand;");
    }
    
    private boolean tieneEvidencias(int numSesion) {
        HashMap<String, Object> respuesta = gestortutoriasfx.dominio.EvidenciaImplementacion.obtenerEvidenciasPorSesion(
            Sesion.getIdTutor(), 
            numSesion
        );

        if (!(boolean) respuesta.get("error")) {
            ArrayList<?> lista = (ArrayList<?>) respuesta.get("evidencias");
            return !lista.isEmpty();
        }
        return false;
    }
}