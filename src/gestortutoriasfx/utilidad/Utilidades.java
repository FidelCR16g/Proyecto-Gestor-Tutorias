/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestortutoriasfx.utilidad;

import gestortutoriasfx.GestorTutoriasFX;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author fave6
 */
public class Utilidades {
    //Alertas que solo muestran algo
    public static void mostrarAlertaSimple(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
    
    public static boolean mostrarAlertaConfirmacion(String titulo, String contenido){
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        Optional<ButtonType> botonSeleccionado = alerta.showAndWait();
        return (botonSeleccionado.get() == ButtonType.OK);
    }
    
    //Metodo para colocar la url de los FXML de manera mas sencilla
    public static FXMLLoader obtenerVista(String url){
        return new FXMLLoader(GestorTutoriasFX.class.getResource(url));
    }
}
