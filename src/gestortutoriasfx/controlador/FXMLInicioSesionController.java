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

/**
 * Nombre de la Clase: FXMLInicioSesionController
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
 * Se encarga de controlar los distintos elementos del inicio de sesion
 */
public class FXMLInicioSesionController implements Initializable{

    @FXML
    private TextField tfUsuario;
    @FXML
    private Label lbErrorPassword;
    @FXML
    private Label lbErrorUsuario;
    @FXML
    private PasswordField pfPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConexionBD.establecerCredenciales("login");
    }    

    @FXML
    private void clicIniciarSesion(ActionEvent event) {
        if(validarCampos(tfUsuario.getText(), pfPassword.getText())){
            verificarCredenciales(tfUsuario.getText(), pfPassword.getText());
        }
    }
    
    private void cargarDatosTutor(Usuario usuario) {
        HashMap<String, Object> respuesta = TutorImplementacion.obtenerIdTutor(usuario.getIdUsuario());
        boolean error = (boolean) respuesta.get("error");

        if (!error) {
            int idTutor = (int) respuesta.get("idTutor");
            Sesion.setIdTutor(idTutor);
            irPantallaPrincipal(usuario, "Tutor");
        } else {
            Utilidades.mostrarAlertaSimple("Error de Perfil", 
                    "El usuario tiene rol de Tutor pero no está registrado en la tabla de Tutores.", 
                    Alert.AlertType.ERROR);
        }
    }
    
    private void gestionarRedireccionPorRol(Usuario usuario, ArrayList<String> roles) {
        if (roles.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Acceso Denegado", 
                    "El usuario no tiene roles asignados para ingresar al sistema.", 
                    Alert.AlertType.WARNING);
            return;
        }

        if (roles.size() == 1) {
            ingresarAlSistema(usuario, roles.get(0));
        } else {
            irPantallaSeleccionRol(usuario, roles);
        }
    }
    
    public void ingresarAlSistema(Usuario usuario, String rolSeleccionado) {
        ConexionBD.establecerCredenciales(rolSeleccionado);
        
        usuario.setRol(rolSeleccionado);
        Sesion.setUsuarioSesion(usuario);
        
        if ("Tutor".equalsIgnoreCase(rolSeleccionado)) {
            cargarDatosTutor(usuario);
        } else if ("Coordinador".equalsIgnoreCase(rolSeleccionado)) {
            Sesion.setIdTutor(0);
            irPantallaPrincipal(usuario, rolSeleccionado);
        } else {
            irPantallaPrincipal(usuario, rolSeleccionado);
        }
    }
    
    private Usuario intentarLogin(String noPersonal, String password) {
        HashMap<String, Object> respuesta = UsuarioImplementacion.validarLogin(noPersonal, password);
        
        if ((boolean) respuesta.get("error")) {
            Utilidades.mostrarAlertaSimple("Credenciales Incorrectas", 
                    respuesta.get("mensaje").toString(), Alert.AlertType.WARNING);
            return null;
        }
        
        return (Usuario) respuesta.get("usuario");
    }
    
    private void irPantallaPrincipal(Usuario usuario, String rol){
        try {
            String rutaFXML = obtenerRutaFXMLPorRol(rol);
            
            if(rutaFXML.isEmpty()){
                Utilidades.mostrarAlertaSimple("Rol no reconocido", 
                        "No hay una pantalla principal asignada a ese rol: ", Alert.AlertType.WARNING);
            }
            
            FXMLLoader cargador = Utilidades.obtenerVista(rutaFXML);
            Parent vista = cargador.load();
            
            IPrincipalControlador controlador = cargador.getController();
            controlador.inicializarInformacion(usuario);
            
            Scene escena = new Scene(vista);
            Stage escenario = (Stage) tfUsuario.getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle("Sistema de Gestión de Tutorías - " + rol);
            escenario.show();
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private void irPantallaSeleccionRol(Usuario usuario, ArrayList<String> roles) {
        try {
            FXMLLoader loader = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLSeleccionRol.fxml");
            Parent root = loader.load();
            
            FXMLSeleccionRolController controller = loader.getController();
            controller.inicializar(usuario, roles, this); 
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Seleccionar Rol");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.initOwner(tfUsuario.getScene().getWindow());
            stage.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", 
                    "No se pudo abrir la ventana de selección de roles.", Alert.AlertType.WARNING);
        }
    }
    
    private String obtenerRutaFXMLPorRol(String idRol){
        switch(idRol){
            case "Administrador":
                return "/gestortutoriasfx/vista/FXMLPrincipalAdministrador.fxml";
            case "Coordinador":
                return "/gestortutoriasfx/vista/FXMLPrincipalCoordinador.fxml";
            case "Tutor":
                return "/gestortutoriasfx/vista/FXMLPrincipalTutor.fxml";
            default:
                return "";
        }
    }
    
    private ArrayList<String> recuperarRoles(Usuario usuario) {
        HashMap<String, Object> respuesta = UsuarioImplementacion.obtenerRolesUsuario(usuario.getIdUsuario());
        
        if ((boolean) respuesta.get("error")) {
            Utilidades.mostrarAlertaSimple("Error de Sistema", 
                    respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
            return null;
        }
        
        return (ArrayList<String>) respuesta.get("roles");
    }
    
    private boolean validarCampos(String usuario, String password) {
        lbErrorUsuario.setText("");
        lbErrorPassword.setText("");
        boolean camposValidos = true;
        if (usuario.isEmpty()) {
            lbErrorUsuario.setText("Usuario obligatorio.");
            camposValidos = false;
        }
        if (password.isEmpty()) {
            lbErrorPassword.setText("Contraseña obligatoria.");
            camposValidos = false;
        }
        return camposValidos;
    }
    
    private void verificarCredenciales(String noPersonal, String password) {
        Usuario usuario = intentarLogin(noPersonal, password);
        if (usuario == null) return;

        ArrayList<String> roles = recuperarRoles(usuario);
        if (roles == null) return;

        gestionarRedireccionPorRol(usuario, roles);
    }
}