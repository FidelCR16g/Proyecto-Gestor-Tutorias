package gestortutoriasfx.controlador;

import gestortutoriasfx.interfaz.IPrincipalControlador;
import gestortutoriasfx.modelo.pojo.Usuario;
import gestortutoriasfx.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Nombre de la Clase: FXMLPrincipalTutorController
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 25/11/2025
 *
 * Descripción:
 * Se encarga de controlar los distintos elementos de la pantalla principal del tutor
 */

public class FXMLPrincipalTutorController implements Initializable, IPrincipalControlador{

    @FXML
    private BorderPane borderPanePrincipal;
    @FXML
    private StackPane panelContenido;
    @FXML
    private Label lbBienvenida;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @Override
    public void inicializarInformacion(Usuario usuario) {
        if(usuario != null){
            lbBienvenida.setText("Bienvenido(a) " + usuario.getNombre());
        }
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        boolean deseaSalir = Utilidades.mostrarAlertaVerificacion("Cerrar Sesión", 
                "¿Está seguro de que desea salir?", 
                "Se regresará a la pantalla de inicio de sesión.");
        
        if (!deseaSalir) return;
        gestortutoriasfx.modelo.Sesion.cerrarSesion();

        cerrarVentanaActual(event);
        abrirVentanaLogin();
    }

    @FXML
    private void clicRegistrarHorarios(ActionEvent event) {
        irVista("/gestortutoriasfx/vista/FXMLRegistrarHorario.fxml");
    }

    @FXML
    private void clicRegistrarAsistencia(ActionEvent event) {
        irVista("/gestortutoriasfx/vista/FXMLRegistrarAsistencia.fxml");
    }

    @FXML
    private void clicGestionarEvidencia(ActionEvent event) {
        irVista("/gestortutoriasfx/vista/FXMLGestionarEvidencias.fxml");
    }
    
    @FXML
    private void clicLlenarReporte(ActionEvent event) {
        irVista("/gestortutoriasfx/vista/FXMLGestionarReporteDeTutoria.fxml");
    }
    
    private void abrirVentanaLogin() {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/gestortutoriasfx/vista/FXMLInicioSesion.fxml"));
            Parent root = cargador.load();
            
            Scene escena = new Scene(root);
            Stage escenarioLogin = new Stage();
            escenarioLogin.setScene(escena);
            escenarioLogin.setTitle("Inicio de Sesión");
            escenarioLogin.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar la ventana de inicio de sesión.", Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentanaActual(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stageActual = (Stage) source.getScene().getWindow();
        stageActual.close();
    }
    
    private void irVista(String ruta) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVista(ruta);
            Parent vista = cargador.load();
            panelContenido.getChildren().clear();
            panelContenido.getChildren().add(vista);
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de Navegación", 
                    "No se pudo cargar la vista: " + ruta, 
                    Alert.AlertType.WARNING);
        }
    }
}
