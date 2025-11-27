package gestortutoriasfx.controlador;

import gestortutoriasfx.interfaz.IPrincipalControlador;
import gestortutoriasfx.modelo.pojo.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * Nombre de la Clase: FXMLPrincipalAdministradorController
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
 * Se encarga de controlar los distintos elementos de la pantalla principal del administrador
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
