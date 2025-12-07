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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Nombre de la Clase: FXMLPrincipalCoordinadorController
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
 * Se encarga de controlar los distintos elementos de la pantalla principal del coordinador.
 */

public class FXMLPrincipalCoordinadorController implements Initializable, IPrincipalControlador{

    @FXML
    private BorderPane borderPanePrincipal;
    @FXML
    private StackPane panelContenido;
    @FXML
    private Label lbBienvenida;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void inicializarInformacion(Usuario usuario) {
        System.out.println("Bienvenido Coordinador: " + usuario.getNombre());
    }

    @FXML
    private void clicAsignarTutorado(ActionEvent event) {
        irVista("/gestortutoriasfx/vista/FXMLListadoTutoresDisponibles.fxml");
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        boolean cerrar = Utilidades.mostrarAlertaVerificacion("Cerrar Sesión", 
                "¿Está seguro de que desea salir?", 
                "Se regresará a la pantalla de inicio de sesión.");
        
        if(cerrar){
            try {
                Stage escenarioActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestortutoriasfx/vista/FXMLInicioSesion.fxml"));
                Parent root = loader.load();
                
                Scene escena = new Scene(root);
                Stage escenarioLogin = new Stage();
                escenarioLogin.setScene(escena);
                escenarioLogin.setTitle("Inicio de Sesión");
                escenarioLogin.show();
                
                escenarioActual.close();
                
                gestortutoriasfx.modelo.Sesion.cerrarSesion();
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
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