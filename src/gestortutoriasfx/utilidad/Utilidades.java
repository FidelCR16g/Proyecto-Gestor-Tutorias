package gestortutoriasfx.utilidad;

import gestortutoriasfx.GestorTutoriasFX;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.Sesion;
import java.io.IOException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Nombre de la Clase: Utilidades
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
 * Clase encargada de ejecutar los metodos usualmente repetidos en el resto de clases.
 */

public class Utilidades {
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
    
    public static boolean mostrarAlertaVerificacion(String titulo, String encabezado, String contenido){
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);

        ButtonType botonAceptar = new ButtonType("Aceptar");
        ButtonType botonCancelar = new ButtonType("Cancelar");
        alerta.getButtonTypes().setAll(botonAceptar, botonCancelar);

        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.isPresent() && resultado.get() == botonAceptar;
    }
    
    public static FXMLLoader obtenerVista(String url){
        return new FXMLLoader(GestorTutoriasFX.class.getResource(url));
    }
    
    public static void cerrarSesionYRedirigirALogin(Node nodoActual) {
        Sesion.cerrarSesion();
        ConexionBD.establecerCredenciales("login");

        try {
            FXMLLoader loader = obtenerVista("/gestortutoriasfx/vista/FXMLInicioSesion.fxml");
            Parent root = loader.load();

            Stage stageActual = (Stage) nodoActual.getScene().getWindow();
            stageActual.setScene(new Scene(root));
            stageActual.setTitle("Inicio de Sesión");
            stageActual.centerOnScreen();
            stageActual.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            mostrarAlertaSimple(
                    "Error",
                    "No se pudo cargar la pantalla de inicio de sesión.",
                    Alert.AlertType.ERROR
            );
        }
    }

    public static void confirmarCerrarSesionYRedirigirALogin(Node nodoActual) {
        boolean deseaSalir = mostrarAlertaVerificacion(
                "Cerrar Sesión",
                "¿Está seguro de que desea salir?",
                "Se regresará a la pantalla de inicio de sesión."
        );
        if (!deseaSalir) return;
        cerrarSesionYRedirigirALogin(nodoActual);
    }
}


