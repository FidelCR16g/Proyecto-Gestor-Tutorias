/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gestortutoriasfx.controlador;

import gestortutoriasfx.interfaz.IPrincipalControlador;
import gestortutoriasfx.modelo.pojo.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author fave6
 */
public class FXMLPrincipalAdministradorController implements Initializable, IPrincipalControlador{

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void inicializarInformacion(Usuario usuario) {
        System.out.println("Bienvenido Administrador: " + usuario.getUsuario());
    }
    
}
