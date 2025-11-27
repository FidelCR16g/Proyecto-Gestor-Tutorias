package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.TutorImplementacion;
import gestortutoriasfx.dominio.UsuarioImplementacion;
import gestortutoriasfx.interfaz.IPrincipalControlador;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.Usuario;
import gestortutoriasfx.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
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
    private TextField tfPassword;
    @FXML
    private Label lbErrorPassword;
    @FXML
    private Label lbErrorUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicIniciarSesion(ActionEvent event) {
        if(validarCampos(tfUsuario.getText(), tfPassword.getText())){
            verificarCredenciales(tfUsuario.getText(), tfPassword.getText());
        }
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
    
    private void verificarCredenciales(String usuario, String password){
        HashMap<String, Object> respuesta = UsuarioImplementacion.obtenerUsuarios(usuario);
        boolean error = (boolean) respuesta.get("error");
        
        if(!error){
            Usuario usuarioSesion = (Usuario) respuesta.get("usuario");
            
            if(usuarioSesion.getPassword().equals(password)){
                Sesion.setUsuarioSesion(usuarioSesion);
                
                if(usuarioSesion.getIdRol() == 3){
                    cargarDatosTutor(usuarioSesion);
                }else{
                    Sesion.setIdTutor(0); 
                    irPantallaPrincipal(usuarioSesion);
                }
            }else{
                Utilidades.mostrarAlertaSimple("Credenciales incorrectas", "Contraseña incorrecta", Alert.AlertType.WARNING);
            }
        }else{
            Utilidades.mostrarAlertaSimple("Error", respuesta.get("mensaje").toString(), Alert.AlertType.WARNING);
        }
    }
    
    private void cargarDatosTutor(Usuario usuario) {
        HashMap<String, Object> respuesta = TutorImplementacion.obtenerIdTutor(usuario.getIdUsuario());
        boolean error = (boolean) respuesta.get("error");

        if (!error) {
            int idTutor = (int) respuesta.get("idTutor");
            Sesion.setIdTutor(idTutor);
            irPantallaPrincipal(usuario);
        } else {
            Utilidades.mostrarAlertaSimple("Error de Perfil", 
                    "El usuario tiene rol de Tutor pero no está registrado en la tabla de Tutores.", 
                    Alert.AlertType.ERROR);
        }
    }
    
    private String obtenerRutaFXMLPorRol(int idRol){
        switch(idRol){
            case 1:
                return "/gestortutoriasfx/vista/FXMLPrincipalAdministrador.fxml";
            case 2:
                return "/gestortutoriasfx/vista/FXMLPrincipalCoordinador.fxml";
            case 3:
                return "/gestortutoriasfx/vista/FXMLPrincipalTutor.fxml";
            default:
                return "";
        }
    }
    
    private void irPantallaPrincipal(Usuario usuario){
        try {
            String rutaFXML = obtenerRutaFXMLPorRol(usuario.getIdRol());
            
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
            escenario.setTitle("Principal");
            escenario.show();
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}