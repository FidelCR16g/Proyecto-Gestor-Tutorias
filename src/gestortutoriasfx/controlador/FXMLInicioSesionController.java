package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.TutorImplementacion;
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
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLInicioSesionController implements Initializable {

    @FXML private TextField tfUsuario;
    @FXML private PasswordField pfPassword;
    @FXML private Label lbErrorUsuario;
    @FXML private Label lbErrorPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConexionBD.establecerCredenciales("login");
    }

    @FXML
    private void clicIniciarSesion(ActionEvent event) {
        String usuario = (tfUsuario != null) ? tfUsuario.getText() : null;
        String password = (pfPassword != null) ? pfPassword.getText() : null;

        if (!validarCampos(usuario, password)) return;

        verificarCredenciales(usuario.trim(), password);
    }

    private void verificarCredenciales(String noPersonal, String password) {
        Usuario usuario = intentarLogin(noPersonal, password);
        if (usuario == null) return;

        ArrayList<String> roles = recuperarRoles(usuario);
        if (roles == null || roles.isEmpty()) {
            Utilidades.mostrarAlertaSimple(
                    "Acceso Denegado",
                    "El usuario no tiene roles asignados para ingresar al sistema.",
                    Alert.AlertType.WARNING
            );
            return;
        }

        // Normalizar roles (trim) y eliminar nulls
        ArrayList<String> rolesLimpios = new ArrayList<>();
        for (String r : roles) {
            if (r != null && !r.trim().isEmpty()) rolesLimpios.add(r.trim());
        }
        if (rolesLimpios.isEmpty()) {
            Utilidades.mostrarAlertaSimple(
                    "Acceso Denegado",
                    "El usuario no tiene roles válidos para ingresar al sistema.",
                    Alert.AlertType.WARNING
            );
            return;
        }

        // Administrador: entra directo SIEMPRE
        boolean esAdmin = rolesLimpios.stream().anyMatch(r -> "Administrador".equalsIgnoreCase(r));
        if (esAdmin) {
            ingresarAlSistema(usuario, "Administrador");
            return;
        }

        if (rolesLimpios.size() == 1) {
            ingresarAlSistema(usuario, rolesLimpios.get(0));
        } else {
            irPantallaSeleccionRol(usuario, rolesLimpios);
        }
    }

    private Usuario intentarLogin(String noPersonal, String password) {
        HashMap<String, Object> respuesta = UsuarioImplementacion.validarLogin(noPersonal, password);

        if (respuesta == null || Boolean.TRUE.equals(respuesta.get("error"))) {
            String msg = (respuesta != null && respuesta.get("mensaje") != null)
                    ? respuesta.get("mensaje").toString()
                    : "No fue posible validar las credenciales.";
            Utilidades.mostrarAlertaSimple(
                    "Credenciales Incorrectas",
                    msg,
                    Alert.AlertType.WARNING
            );
            return null;
        }

        Object u = respuesta.get("usuario");
        if (!(u instanceof Usuario)) {
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No se obtuvo el usuario desde la validación de login.",
                    Alert.AlertType.ERROR
            );
            return null;
        }

        return (Usuario) u;
    }

    private ArrayList<String> recuperarRoles(Usuario usuario) {
        HashMap<String, Object> respuesta = UsuarioImplementacion.obtenerRolesUsuario(usuario.getIdUsuario());

        if (respuesta == null || Boolean.TRUE.equals(respuesta.get("error"))) {
            String msg = (respuesta != null && respuesta.get("mensaje") != null)
                    ? respuesta.get("mensaje").toString()
                    : "No fue posible recuperar roles del usuario.";
            Utilidades.mostrarAlertaSimple(
                    "Error de Sistema",
                    msg,
                    Alert.AlertType.ERROR
            );
            return null;
        }

        @SuppressWarnings("unchecked")
        ArrayList<String> roles = (ArrayList<String>) respuesta.get("roles");
        return roles;
    }

    public void ingresarAlSistema(Usuario usuario, String rolSeleccionado) {
        if (usuario == null || rolSeleccionado == null) return;

        String rol = rolSeleccionado.trim();
        if (rol.isEmpty()) return;

        // Credenciales por rol (si tu ConexionBD usa switch, esto evita problemas de espacios)
        ConexionBD.establecerCredenciales(rol);

        usuario.setRol(rol);
        Sesion.setUsuarioSesion(usuario);

        if ("Tutor".equalsIgnoreCase(rol)) {
            cargarDatosTutor(usuario);
            return; // OJO: cargarDatosTutor ya redirige
        }

        Sesion.setIdTutor(0);
        irPantallaPrincipal(usuario, rol);
    }

    private void cargarDatosTutor(Usuario usuario) {
        HashMap<String, Object> respuesta = TutorImplementacion.obtenerIdTutor(usuario.getIdUsuario());
        if (respuesta == null) {
            Utilidades.mostrarAlertaSimple(
                    "Error de Perfil",
                    "No fue posible consultar el idTutor.",
                    Alert.AlertType.ERROR
            );
            return;
        }

        boolean error = Boolean.TRUE.equals(respuesta.get("error"));
        if (!error) {
            int idTutor = (int) respuesta.get("idTutor");
            Sesion.setIdTutor(idTutor);
            irPantallaPrincipal(usuario, "Tutor");
        } else {
            Utilidades.mostrarAlertaSimple(
                    "Error de Perfil",
                    "El usuario tiene rol de Tutor pero no está registrado en la tabla de Tutores.",
                    Alert.AlertType.ERROR
            );
        }
    }


    private void irPantallaPrincipal(Usuario usuario, String rol) {
        try {
            String rutaFXML = obtenerRutaFXMLPorRol(rol);

            if (rutaFXML == null) {
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
            if (controlador != null) {
                controlador.inicializarInformacion(usuario);
            }

            Stage escenario = (Stage) tfUsuario.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Sistema de Gestión de Tutorías - " + rol);
            escenario.show();

        } catch (IOException e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No se pudo cargar la pantalla principal para el rol: " + rol,
                    Alert.AlertType.ERROR
            );
        }
    }

    private void irPantallaSeleccionRol(Usuario usuario, ArrayList<String> roles) {
        try {
            FXMLLoader loader = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLSeleccionRol.fxml");
            Parent root = loader.load();

            FXMLSeleccionRolController controller = loader.getController();
            controller.inicializar(usuario, roles);

            Stage stageActual = (Stage) tfUsuario.getScene().getWindow();
            stageActual.setScene(new Scene(root));
            stageActual.setTitle("Seleccionar Perfil");
            stageActual.show();

        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No se pudo abrir la ventana de selección de roles.",
                    Alert.AlertType.WARNING
            );
        }
    }

    private String obtenerRutaFXMLPorRol(String idRol) {
        if (idRol == null) return "";

        String rol = idRol.trim().toLowerCase();

        switch (rol) {
            case "administrador":
                return "/gestortutoriasfx/vista/FXMLPrincipalAdministrador.fxml";
            case "coordinador":
                return "/gestortutoriasfx/vista/FXMLPrincipalCoordinador.fxml";
            case "supervisor":
                return "/gestortutoriasfx/vista/FXMLPrincipalSupervisor.fxml";
            case "tutor":
                return "/gestortutoriasfx/vista/FXMLPrincipalTutor.fxml";
            default:
                return "";
        }
    }

    private boolean validarCampos(String usuario, String password) {
        if (lbErrorUsuario != null) lbErrorUsuario.setText("");
        if (lbErrorPassword != null) lbErrorPassword.setText("");

        boolean ok = true;

        if (usuario == null || usuario.trim().isEmpty()) {
            if (lbErrorUsuario != null) lbErrorUsuario.setText("Usuario obligatorio.");
            ok = false;
        }

        if (password == null || password.trim().isEmpty()) {
            if (lbErrorPassword != null) lbErrorPassword.setText("Contraseña obligatoria.");
            ok = false;
        }

        return ok;
    }
}
