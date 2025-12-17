package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.UsuarioImplementacion;
import gestortutoriasfx.interfaz.IPrincipalControlador;
import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.Usuario;
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

public class FXMLPrincipalTutorController implements Initializable, IPrincipalControlador {

    @FXML
    private BorderPane borderPanePrincipal;
    @FXML
    private StackPane panelContenido;
    @FXML
    private Label lbBienvenida;

    @Override
    public void initialize(URL url, ResourceBundle rb) { }

    @Override
    public void inicializarInformacion(Usuario usuario) {
        if (usuario != null) {
            lbBienvenida.setText("Bienvenido(a) " + usuario.getNombre());
        }
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        boolean deseaSalir = Utilidades.mostrarAlertaVerificacion(
                "Cerrar Sesión",
                "¿Está seguro de que desea salir?",
                "Se regresará a la pantalla de selección de rol."
        );
        if (!deseaSalir) return;

        regresarASeleccionRol(event);
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

    private void regresarASeleccionRol(ActionEvent event) {
        try {
            Usuario usuario = Sesion.getUsuarioSesion();
            if (usuario == null) {
                irALogin(event);
                return;
            }

            ConexionBD.establecerCredenciales("login");

            HashMap<String, Object> respuesta = UsuarioImplementacion.obtenerRolesUsuario(usuario.getIdUsuario());
            if ((boolean) respuesta.get("error")) {
                Sesion.cerrarSesion();
                irALogin(event);
                return;
            }

            ArrayList<String> roles = (ArrayList<String>) respuesta.get("roles");
            if (roles == null || roles.isEmpty()) {
                Sesion.cerrarSesion();
                irALogin(event);
                return;
            }

            FXMLLoader loader = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLSeleccionRol.fxml");
            Parent root = loader.load();

            FXMLSeleccionRolController controller = loader.getController();
            controller.inicializar(usuario, roles);

            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActual.setScene(new Scene(root));
            stageActual.setTitle("Seleccionar Perfil");
            stageActual.show();

            Sesion.setIdTutor(0);
            usuario.setRol(null);

        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No se pudo cargar la pantalla de selección de rol.",
                    Alert.AlertType.ERROR
            );
        }
    }

    private void irALogin(ActionEvent event) {
        try {
            ConexionBD.establecerCredenciales("login");
            FXMLLoader loader = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLInicioSesion.fxml");
            Parent root = loader.load();

            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActual.setScene(new Scene(root));
            stageActual.setTitle("Inicio de Sesión");
            stageActual.show();

        } catch (IOException e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No se pudo cargar la pantalla de inicio de sesión.",
                    Alert.AlertType.ERROR
            );
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
            Utilidades.mostrarAlertaSimple(
                    "Error de Navegación",
                    "No se pudo cargar la vista: " + ruta,
                    Alert.AlertType.WARNING
            );
        }
    }
}