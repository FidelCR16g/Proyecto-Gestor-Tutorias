package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.TutorImplementacion;
import gestortutoriasfx.modelo.pojo.Tutor;
import gestortutoriasfx.utilidad.Utilidades;
import gestortutoriasfx.utilidades.TarjetaTutor;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

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

public class FXMLSeleccionTutorController implements Initializable {

    @FXML private GridPane gpListaTutores;
    @FXML private Button btnSeleccionar;
    @FXML private Label lbMensajeSeleccion;

    private Tutor tutorSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTutores();
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
    
    private void actualizarSeleccionVisual() {
        for (Node nodo : gpListaTutores.getChildren()) {
            if (nodo instanceof TarjetaTutor) {
                TarjetaTutor tarjeta = (TarjetaTutor) nodo;
                if (tarjeta.getTutor().equals(this.tutorSeleccionado)) {
                    tarjeta.seleccionar();
                } else {
                    tarjeta.deseleccionar();
                }
            }
        }
    }
    
    private void cargarTutores() {
        HashMap<String, Object> respuesta = TutorImplementacion.obtenerListaTutores();
        
        if (!(boolean) respuesta.get("error")) {
            ArrayList<Tutor> lista = (ArrayList<Tutor>) respuesta.get("tutores");
            renderizarTarjetas(lista);
        } else {
            Utilidades.mostrarAlertaSimple("Error", respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
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
    
    private void manejarSeleccionTutor(Tutor tutor) {
        this.tutorSeleccionado = tutor;
        
        btnSeleccionar.setDisable(false);
        if (lbMensajeSeleccion != null) lbMensajeSeleccion.setVisible(false);
        
        actualizarSeleccionVisual();
    }
    
    private void renderizarTarjetas(ArrayList<Tutor> listaTutores) {
        gpListaTutores.getChildren().clear();
        int columna = 0;
        int fila = 0;

        for (Tutor t : listaTutores) {
            TarjetaTutor tarjeta = new TarjetaTutor(t, this::manejarSeleccionTutor);
            
            gpListaTutores.add(tarjeta, columna, fila);
            
            columna++;
            if (columna == 3) {
                columna = 0;
                fila++;
            }
        }
    }
}