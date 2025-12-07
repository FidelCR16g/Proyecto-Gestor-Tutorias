package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.PeriodoEscolarImplementacion;
import gestortutoriasfx.dominio.ReporteTutoriaImplementacion;
import gestortutoriasfx.dominio.SesionTutoriaImplementacion;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.FechaTutoria;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import gestortutoriasfx.modelo.pojo.ReporteTutoria;
import gestortutoriasfx.utilidad.Utilidades;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

/**
 * Nombre de la Clase: FXMLGestionarReporteDeTutoriaController
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 05/12/2025
 *
 * Descripción:
 * Se encarga de controlar la gestion del reporte de la tutoria
 */

public class FXMLGestionarReporteDeTutoriaController implements Initializable {

    @FXML 
    private VBox vbListaReportes;
    @FXML 
    private Button btnGenerar;
    @FXML 
    private Button btnConsultar;
    @FXML 
    private Button btnEditar;
    @FXML 
    private Button btnExportar;

    private PeriodoEscolar periodoActual;
    private FechaTutoria sesionSeleccionada; 
    private ReporteTutoria reporteSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPeriodo();
        if (periodoActual != null) {
            cargarTarjetasDeSesiones();
        }
    }
    
    @FXML
    private void clicGenerarReporte(ActionEvent event) {
        irPantallaFormulario(null, false);
    }

    @FXML
    private void clicEditarReporte(ActionEvent event) {
        irPantallaFormulario(this.reporteSeleccionado, false);
    }

    @FXML
    private void clicConsultarReporte(ActionEvent event) {
        irPantallaFormulario(this.reporteSeleccionado, true);
    }
    
    @FXML
    private void clicExportarReporte(ActionEvent event) {
        if (reporteSeleccionado == null) {
            Utilidades.mostrarAlertaSimple("Atención", 
                    "Selecciona un reporte de la lista.", Alert.AlertType.WARNING);
            return;
        }

        if (periodoActual == null) {
            Utilidades.mostrarAlertaSimple("Error", 
                    "No se ha cargado el Periodo Escolar correctamente.", Alert.AlertType.WARNING);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte de Texto");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo de Texto (*.txt)", "*.txt"));
        fileChooser.setInitialFileName("Reporte_Sesion_" + reporteSeleccionado.getNumSesion() + ".txt");
        
        File archivo = fileChooser.showSaveDialog(btnExportar.getScene().getWindow());

        if (archivo != null) {
            boolean exito = ReporteTutoriaImplementacion.exportarReporteTextoPlano(
                this.reporteSeleccionado, 
                this.periodoActual,
                archivo
            );

            if (exito) {
                Utilidades.mostrarAlertaSimple("Éxito", 
                        "El archivo se generó correctamente.", Alert.AlertType.INFORMATION);
            } else {
                Utilidades.mostrarAlertaSimple("Error", 
                        "No se pudo escribir el archivo.", Alert.AlertType.WARNING);
            }
        }
    }
    
    private void irPantallaFormulario(ReporteTutoria reporteAEnviar, boolean esSoloLectura){
        try {
            FXMLLoader cargador = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLFormularioReporteTutoria.fxml");
            Parent vista = cargador.load();
            
            FXMLFormularioReporteTutoriaController controlador = cargador.getController();
            controlador.inicializarDatos(sesionSeleccionada, reporteAEnviar, esSoloLectura);
            
            Pane panel = (Pane) btnGenerar.getScene().lookup("#panelContenido");
            if (panel != null) {
                panel.getChildren().clear();
                panel.getChildren().add(vista);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", 
                    "No se pudo cargar el formulario.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicSalir(ActionEvent event) {
        if (Utilidades.mostrarAlertaVerificacion("Salir", "¿Desea cerrar esta sección?", "")) {
            try {
                Pane panel = (Pane) btnGenerar.getScene().lookup("#panelContenido");
                if (panel != null) panel.getChildren().clear();
            } catch (Exception e) {}
        }
    }

    private void cargarPeriodo() {
        HashMap<String, Object> respuesta = PeriodoEscolarImplementacion.obtenerPeriodoActual();
        if (!(boolean) respuesta.get("error")) {
            periodoActual = (PeriodoEscolar) respuesta.get("periodo");
        } else {
            Utilidades.mostrarAlertaSimple("Error", 
                    "No hay periodo activo.", Alert.AlertType.WARNING);
        }
    }

    private void cargarTarjetasDeSesiones() {
        HashMap<String, Object> respuestaFechas = 
                SesionTutoriaImplementacion.obtenerFechasPorPeriodo(periodoActual.getIdPeriodoEscolar());
        HashMap<String, Object> respuestaReportes = 
                ReporteTutoriaImplementacion.obtenerReportesPorPeriodo(Sesion.getIdTutor(), 
                        periodoActual.getIdPeriodoEscolar()
        );

        if (!(boolean) respuestaFechas.get("error") && !(boolean) respuestaReportes.get("error")) {
            ArrayList<FechaTutoria> fechas = (ArrayList<FechaTutoria>) respuestaFechas.get("fechas");
            ArrayList<ReporteTutoria> reportes = (ArrayList<ReporteTutoria>) respuestaReportes.get("reportes");
            vbListaReportes.getChildren().clear();

            for (FechaTutoria fechaTutoria : fechas) {
                ReporteTutoria reporteEncontrado = null;
                for (ReporteTutoria reporte : reportes) {
                    if (reporte.getNumSesion() == fechaTutoria.getNumSesion()) {
                        reporteEncontrado = reporte;
                        break;
                    }
                }
                HBox tarjeta = crearTarjeta(fechaTutoria, reporteEncontrado);
                vbListaReportes.getChildren().add(tarjeta);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error", ""
                    + "No se pudo cargar la información.", Alert.AlertType.WARNING);
        }
    }

    private HBox crearTarjeta(FechaTutoria fecha, ReporteTutoria reporte) {
        HBox tarjeta = new HBox();
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setPadding(new Insets(15));
        tarjeta.setSpacing(20);
        
        tarjeta.setStyle("-fx-background-color: #ffffff; -fx-border-color: #bdbdbd; -fx-border-width: 1; "
                + "-fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand;");

        Label lbTitulo = new Label("Sesión " + fecha.getNumSesion());
        lbTitulo.setFont(Font.font("System", FontWeight.BOLD, 16));
        lbTitulo.setStyle("-fx-text-fill: #37474f;");

        String textoEstatus = (reporte == null) ? "Pendiente" : reporte.getEstatus();
        Label lbEstatus = new Label(textoEstatus);
        
        if (reporte == null) {
            lbEstatus.setStyle("-fx-text-fill: #757575; -fx-font-style: italic;");
        } else if ("Enviado".equalsIgnoreCase(textoEstatus)) {
            lbEstatus.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
        } else {
            lbEstatus.setStyle("-fx-text-fill: #f57c00; -fx-font-weight: bold;");
        }

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lbFecha = new Label(fecha.getFechaInicio() + " - " + fecha.getFechaCierre());
        lbFecha.setStyle("-fx-text-fill: #78909c;");

        tarjeta.getChildren().addAll(lbTitulo, lbEstatus, spacer, lbFecha);

        tarjeta.setOnMouseClicked(e -> seleccionarTarjeta(tarjeta, fecha, reporte));
        
        return tarjeta;
    }

    private void seleccionarTarjeta(HBox tarjeta, FechaTutoria fecha, ReporteTutoria reporte) {
        this.sesionSeleccionada = fecha;
        this.reporteSeleccionado = reporte;

        for (javafx.scene.Node nodo : vbListaReportes.getChildren()) {
            nodo.setStyle("-fx-background-color: #ffffff; -fx-border-color: #bdbdbd; -fx-border-width: 1; "
                    + "-fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand;");
        }
        
        tarjeta.setStyle("-fx-background-color: #e3f2fd; -fx-border-color: #1565c0; -fx-border-width: 2; "
                + "-fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand;");

        if (reporte == null) {
            btnGenerar.setDisable(false);
            btnConsultar.setDisable(true);
            btnEditar.setDisable(true);
            btnExportar.setDisable(true);
        } else {
            btnGenerar.setDisable(true);
            btnConsultar.setDisable(false);
            btnExportar.setDisable(false);

            if ("Enviado".equalsIgnoreCase(reporte.getEstatus())) {
                btnEditar.setDisable(true);
            } else {
                btnEditar.setDisable(false);
            }
        }
    }
}