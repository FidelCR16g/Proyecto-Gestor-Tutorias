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

public class FXMLPrincipalCoordinadorController implements Initializable, IPrincipalControlador {

    @FXML private BorderPane borderPanePrincipal;
    @FXML private StackPane panelContenido;
    @FXML private Label lbBienvenida;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Sin vista por defecto para evitar errores por rutas inexistentes.
    }

    @Override
    public void inicializarInformacion(Usuario usuario) {
        if (lbBienvenida == null) return;

        if (usuario != null) {
            String nombre = (usuario.getNombre() != null) ? usuario.getNombre() : "";
            lbBienvenida.setText("Bienvenido(a) Coordinador: " + nombre);
        } else {
            lbBienvenida.setText("Bienvenido(a) Coordinador");
        }
    }

    // ====== onAction del FXML ======

    @FXML
    private void clicAsignarTutorado(ActionEvent event) {
        irVista("/gestortutoriasfx/vista/FXMLSeleccionTutor.fxml");
    }

    @FXML
    private void clicRevisarReportesTutoria(ActionEvent event) {
        String ruta = "/gestortutoriasfx/vista/FXMLRevisarReportesTutoria.fxml";
        if (!existeFXML(ruta)) {
            Utilidades.mostrarAlertaSimple(
                "Pendiente",
                "No existe la vista: " + ruta + "\nCrea el FXML o cambia la ruta.",
                Alert.AlertType.INFORMATION
            );
            return;
        }
        irVista(ruta);
    }

    @FXML
    private void clicPlanearTutoria(ActionEvent event) {
        irVista("/gestortutoriasfx/vista/FXMLPlaneacionTutoria.fxml");
    }

    @FXML
    private void clicGestionarReportesGenerales(ActionEvent event) {
        String ruta = "/gestortutoriasfx/vista/FXMLGestionarReporteGeneral.fxml";
        if (!existeFXML(ruta)) {
            Utilidades.mostrarAlertaSimple(
                "Pendiente",
                "No existe la vista: " + ruta + "\nCrea el FXML o cambia la ruta.",
                Alert.AlertType.INFORMATION
            );
            return;
        }
        irVista(ruta);
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        Utilidades.confirmarCerrarSesionYRedirigirALogin((Node) event.getSource());
    }


    // ====== Navegación ======

    private void irVista(String ruta) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVista(ruta);
            Parent vista = cargador.load();

            Object ctrl = cargador.getController();

            if (ctrl instanceof FXMLRevisarReportesTutoriaController) {
                ((FXMLRevisarReportesTutoriaController) ctrl).setPanelContenido(panelContenido);
            }

            if (ctrl instanceof FXMLPlaneacionTutoriaController) {
                FXMLPlaneacionTutoriaController c = (FXMLPlaneacionTutoriaController) ctrl;
                c.setPanelContenido(panelContenido);
                c.setVistaPlaneacion(vista);
            }

            // ✅ NUEVO: Gestionar Reporte General embebido
            if (ctrl instanceof FXMLGestionarReporteGeneralController) {
                ((FXMLGestionarReporteGeneralController) ctrl).setPanelContenido(panelContenido);
            }

            // ✅ Opcional: si algún día navegas directo al formulario
            if (ctrl instanceof FXMLFormularioReporteGeneralController) {
                ((FXMLFormularioReporteGeneralController) ctrl).setPanelContenido(panelContenido);
            }

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

    private void regresarASeleccionRol(ActionEvent event) {
        try {
            Usuario usuario = Sesion.getUsuarioSesion();
            if (usuario == null) {
                Sesion.cerrarSesion();
                irALogin(event);
                return;
            }

            ConexionBD.establecerCredenciales("login");

            HashMap<String, Object> respuesta = UsuarioImplementacion.obtenerRolesUsuario(usuario.getIdUsuario());
            if (respuesta == null || (boolean) respuesta.getOrDefault("error", true)) {
                Sesion.cerrarSesion();
                irALogin(event);
                return;
            }

            @SuppressWarnings("unchecked")
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

    private boolean existeFXML(String ruta) {
        try {
            return getClass().getResource(ruta) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
