package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.EvidenciaImplementacion;
import gestortutoriasfx.dominio.SesionTutoriaImplementacion;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.Evidencia;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
import gestortutoriasfx.utilidad.Utilidades;
import gestortutoriasfx.utilidades.TarjetaSesion;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Nombre de la Clase: FXMLListadoEvidenciasController
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 28/11/2025
 *
 * Descripción:
 * Se encarga de controlar los distintos elementos de los listados de evidencia
 */

public class FXMLGestionarEvidenciasController implements Initializable {
    @FXML 
    private VBox vbListaSesiones;
    @FXML 
    private Button btnSubir;
    @FXML 
    private Button btnEditar;

    private SesionTutoria sesionSeleccionada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarSesiones();
    }
    
    @FXML
    private void clicEditarEvidencia(ActionEvent event) {
        if (sesionSeleccionada != null) {
            irPantallaGestionarEvidencia(sesionSeleccionada);
        }
    }
    
    @FXML
    private void clicSalir(ActionEvent event) {
        if (Utilidades.mostrarAlertaVerificacion("Salir", "¿Desea salir de esta sección?",
                "¿Está seguro que desea salir de esta sección?")) {
            limpiarPanelCentral();
        }
    }
    
    @FXML
    private void clicSubirEvidencia(ActionEvent event) {
        if (sesionSeleccionada != null) {
            irPantallaGestionarEvidencia(sesionSeleccionada);
        }
    }
    
    private void actualizarSeleccionVisual(TarjetaSesion tarjetaSeleccionada) {
        for (Node nodo : vbListaSesiones.getChildren()) {
            if (nodo instanceof TarjetaSesion) {
                ((TarjetaSesion) nodo).deseleccionar();
            }
        }
        tarjetaSeleccionada.seleccionar();
    }

    private void cargarSesiones() {
        ArrayList<SesionTutoria> listaSesiones = obtenerListaSesionesDelTutor();
        
        if (listaSesiones != null) {
            mostrarSesionesEnInterfaz(listaSesiones);
        }
    }
    
    private void configurarBotonesAccion(SesionTutoria sesionTutoria) {
        boolean yaTieneArchivos = tieneEvidencias(sesionTutoria.getNumSesion());
    
        if (yaTieneArchivos) {
            btnSubir.setDisable(true);
            btnEditar.setDisable(false);
        } else {
            btnSubir.setDisable(false);
            btnEditar.setDisable(true);
    }
}
    
    private void manejarSeleccion(TarjetaSesion tarjetaClick) {
        actualizarSeleccionVisual(tarjetaClick);
        this.sesionSeleccionada = tarjetaClick.getSesion();
        configurarBotonesAccion(this.sesionSeleccionada);
    }
    
    private void mostrarSesionesEnInterfaz(ArrayList<SesionTutoria> listaSesiones) {
        vbListaSesiones.getChildren().clear();
        ArrayList<Integer> agregadas = new ArrayList<>();

        for (SesionTutoria sesion : listaSesiones) {
            if (!agregadas.contains(sesion.getNumSesion())) {
                TarjetaSesion tarjeta = new TarjetaSesion(sesion, "", "", null);
                tarjeta.setOnMouseClicked(e -> manejarSeleccion(tarjeta));
                vbListaSesiones.getChildren().add(tarjeta);
                agregadas.add(sesion.getNumSesion());
            }
        }
    }
    
    private void irPantallaGestionarEvidencia(SesionTutoria sesion) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLFormularioEvidencia.fxml");
            Parent vista = cargador.load();
            
            FXMLFormularioEvidenciaController controlador = cargador.getController();
            controlador.inicializarSesion(sesion);
            
            Pane panelPrincipal = (Pane) btnSubir.getScene().lookup("#panelContenido");
            
            if (panelPrincipal != null) {
                panelPrincipal.getChildren().clear();
                panelPrincipal.getChildren().add(vista);
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar la pantalla de gestión.", 
                    Alert.AlertType.WARNING);
        }
    }
    
    private void limpiarPanelCentral() {
        try {
            Pane panel = (Pane) btnSubir.getScene().lookup("#panelContenido");
            if(panel != null) panel.getChildren().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private ArrayList<Evidencia> obtenerEvidencias(int numSesion) {
        HashMap<String, Object> respuesta = EvidenciaImplementacion.obtenerEvidenciasPorSesion(
            Sesion.getIdTutor(), numSesion
        );
        
        if (!(boolean) respuesta.get("error")) {
            return (ArrayList<Evidencia>) respuesta.get("evidencias");
        } else {
            System.err.println("Error al obtener evidencias: " + respuesta.get("mensaje"));
            return null; 
        }
    }
    
    private ArrayList<SesionTutoria> obtenerListaSesionesDelTutor() {
        HashMap<String, Object> respuesta = SesionTutoriaImplementacion.obtenerSesionesTutor(Sesion.getIdTutor());
        
        if (!(boolean) respuesta.get("error")) {
            return (ArrayList<SesionTutoria>) respuesta.get("sesiones");
        } else {
            Utilidades.mostrarAlertaSimple("Error de Carga", 
                respuesta.get("mensaje").toString(), Alert.AlertType.WARNING);
            return null;
        }
    }
    
    private boolean tieneEvidencias(int numSesion) {
        ArrayList<Evidencia> lista = obtenerEvidencias(numSesion);
        return lista != null && !lista.isEmpty();
    }
}