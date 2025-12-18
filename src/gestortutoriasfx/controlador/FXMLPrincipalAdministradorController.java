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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class FXMLPrincipalAdministradorController implements Initializable, IPrincipalControlador {

    @FXML private BorderPane borderPanePrincipal;
    @FXML private StackPane panelContenido;
    @FXML private Label lbBienvenida;

    private Usuario usuarioSesion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (panelContenido != null) {
            panelContenido.setId("panelContenido");
        }
    }

    @Override
    public void inicializarInformacion(Usuario usuario) {
        this.usuarioSesion = usuario;

        if (lbBienvenida != null) {
            String nombre = (usuario != null && usuario.getNombre() != null) ? usuario.getNombre() : "";
            lbBienvenida.setText("Bienvenido(a) Administrador: " + nombre);
        }
    }

    @FXML
    private void clicGestionarTutorado(ActionEvent event) {
        cargarEnPanel("/gestortutoriasfx/vista/FXMLGestionarTutorado.fxml");
    }

    @FXML
    private void clicGestionarUsuarios(ActionEvent event) {
        Utilidades.mostrarAlertaSimple(
                "En construcción",
                "La sección 'Gestionar Usuarios' aún no está implementada.",
                Alert.AlertType.INFORMATION
        );
    }

    @FXML
    private void clicGestionarCalendario(ActionEvent event) {
        Utilidades.mostrarAlertaSimple(
                "En construcción",
                "La sección 'Gestionar Calendario' aún no está implementada.",
                Alert.AlertType.INFORMATION
        );
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        Utilidades.confirmarCerrarSesionYRedirigirALogin((Node) event.getSource());
    }


    private void cargarEnPanel(String ruta) {
        try {
            if (panelContenido == null) {
                Utilidades.mostrarAlertaSimple(
                        "Error",
                        "No existe panelContenido en el FXMLPrincipalAdministrador.fxml.",
                        Alert.AlertType.ERROR
                );
                return;
            }

            if (getClass().getResource(ruta) == null) {
                Utilidades.mostrarAlertaSimple(
                        "No encontrado",
                        "No existe el FXML: " + ruta,
                        Alert.AlertType.WARNING
                );
                return;
            }

            FXMLLoader loader = Utilidades.obtenerVista(ruta);
            Parent vista = loader.load();
            panelContenido.getChildren().setAll(vista);

        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No se pudo cargar la vista: " + ruta,
                    Alert.AlertType.ERROR
            );
        }
    }
}
