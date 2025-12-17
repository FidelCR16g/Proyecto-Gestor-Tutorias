package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.TutorImplementacion;
import gestortutoriasfx.interfaz.IPrincipalControlador;
import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.Usuario;
import gestortutoriasfx.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Selector de Rol.
 * - Entrar como Tutor / Coordinador (o Supervisor si existiera)
 * - Cerrar sesión aquí -> Login (cierre completo)
 */
public class FXMLSeleccionRolController implements Initializable {

    @FXML
    private Label labelBienvenida;

    @FXML
    private Button btnTutor;

    @FXML
    private Button btnRolSuperior;

    @FXML
    private Label lbMensaje;

    private Usuario usuario;
    private List<String> roles;

    private String rolSuperiorSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void inicializar(Usuario usuario, List<String> roles) {
        this.usuario = usuario;
        this.roles = roles;

        String nombreCompleto = String.format("%s %s %s",
                nvl(usuario.getNombre()),
                nvl(usuario.getApellidoPaterno()),
                nvl(usuario.getApellidoMaterno())
        ).trim();
        labelBienvenida.setText("Bienvenido(a) " + nombreCompleto);

        boolean tieneTutor = contieneRol("Tutor");
        btnTutor.setDisable(!tieneTutor);
        btnTutor.setVisible(tieneTutor);
        btnTutor.setManaged(tieneTutor);

        if (contieneRol("Coordinador")) {
            rolSuperiorSeleccionado = "Coordinador";
        } else if (contieneRol("Supervisor")) {
            rolSuperiorSeleccionado = "Supervisor";
        } else {
            rolSuperiorSeleccionado = null;
        }

        if (rolSuperiorSeleccionado != null) {
            btnRolSuperior.setText("Ingresar como " + rolSuperiorSeleccionado);
            btnRolSuperior.setDisable(false);
            lbMensaje.setText("");
        } else {
            btnRolSuperior.setText("Ingresar como ...");
            btnRolSuperior.setDisable(true);
            lbMensaje.setText("No se detectó un rol superior (Coordinador/Supervisor) para su usuario.");
        }
    }

    @FXML
    private void btnIngresarTutor(ActionEvent event) {
        if (usuario == null) return;

        if (!contieneRol("Tutor")) {
            lbMensaje.setText("Su usuario no tiene rol de Tutor.");
            return;
        }

        ingresarAlSistema("Tutor");
    }

    @FXML
    private void btnIngresarRolSuperior(ActionEvent event) {
        if (usuario == null) return;

        if (rolSuperiorSeleccionado == null) {
            lbMensaje.setText("No hay un rol superior disponible para ingresar.");
            return;
        }

        ingresarAlSistema(rolSuperiorSeleccionado);
    }

    @FXML
    private void btnCerrarSesion(ActionEvent event) {
        Sesion.cerrarSesion();
        ConexionBD.establecerCredenciales("login");

        try {
            FXMLLoader loader = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLInicioSesion.fxml");
            Parent root = loader.load();

            Stage stageActual = (Stage) btnRolSuperior.getScene().getWindow();
            stageActual.setScene(new Scene(root));
            stageActual.setTitle("Inicio de Sesión");
            stageActual.show();

        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No se pudo cargar la pantalla de inicio de sesión.",
                    Alert.AlertType.ERROR
            );
        }
    }

    private void ingresarAlSistema(String rolSeleccionado) {
        ConexionBD.establecerCredenciales(rolSeleccionado);

        usuario.setRol(rolSeleccionado);
        Sesion.setUsuarioSesion(usuario);

        if ("Tutor".equalsIgnoreCase(rolSeleccionado)) {
            cargarDatosTutorYEntrar();
        } else {
            Sesion.setIdTutor(0);
            irPantallaPrincipal(rolSeleccionado);
        }
    }

    private void cargarDatosTutorYEntrar() {
        HashMap<String, Object> respuesta = TutorImplementacion.obtenerIdTutor(usuario.getIdUsuario());
        boolean error = (boolean) respuesta.get("error");

        if (!error) {
            int idTutor = (int) respuesta.get("idTutor");
            Sesion.setIdTutor(idTutor);
            irPantallaPrincipal("Tutor");
        } else {
            Utilidades.mostrarAlertaSimple(
                    "Error de Perfil",
                    "El usuario tiene rol de Tutor pero no está registrado en la tabla de Tutores.",
                    Alert.AlertType.ERROR
            );
        }
    }

    private void irPantallaPrincipal(String rol) {
        try {
            String rutaFXML = obtenerRutaFXMLPorRol(rol);
            if (rutaFXML == null || rutaFXML.isEmpty()) {
                Utilidades.mostrarAlertaSimple(
                        "Rol no reconocido",
                        "No hay una pantalla principal asignada a ese rol: " + rol,
                        Alert.AlertType.WARNING
                );
                return;
            }

            FXMLLoader cargador = Utilidades.obtenerVista(rutaFXML);
            Parent vista = cargador.load();

            IPrincipalControlador controlador = cargador.getController();
            controlador.inicializarInformacion(usuario);

            Stage stageActual = (Stage) btnRolSuperior.getScene().getWindow();
            stageActual.setScene(new Scene(vista));
            stageActual.setTitle("Sistema de Gestión de Tutorías - " + rol);
            stageActual.show();

        } catch (IOException e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No se pudo cargar la pantalla principal para el rol: " + rol,
                    Alert.AlertType.ERROR
            );
        }
    }

    private String obtenerRutaFXMLPorRol(String idRol) {
        switch (idRol) {
            case "Administrador":
                return "/gestortutoriasfx/vista/FXMLPrincipalAdministrador.fxml";
            case "Coordinador":
                return "/gestortutoriasfx/vista/FXMLPrincipalCoordinador.fxml";
            case "Supervisor":
                return "/gestortutoriasfx/vista/FXMLPrincipalSupervisor.fxml";
            case "Tutor":
                return "/gestortutoriasfx/vista/FXMLPrincipalTutor.fxml";
            default:
                return "";
        }
    }

    private boolean contieneRol(String rolBuscado) {
        if (roles == null || rolBuscado == null) return false;
        return roles.stream().anyMatch(r -> rolBuscado.equalsIgnoreCase(r));
    }

    private String nvl(String s) {
        return s == null ? "" : s;
    }
}
