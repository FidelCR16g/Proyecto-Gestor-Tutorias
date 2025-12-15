package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.PeriodoEscolarImplementacion;
import gestortutoriasfx.dominio.ProblematicaAcademicaImplementacion;
import gestortutoriasfx.dominio.ReporteTutoriaImplementacion;
import gestortutoriasfx.dominio.SesionTutoriaImplementacion;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import gestortutoriasfx.modelo.pojo.ProblematicaAcademica;
import gestortutoriasfx.modelo.pojo.ReporteTutoria;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
import gestortutoriasfx.utilidad.Utilidades;
import gestortutoriasfx.utilidades.ReporteTutoriaPDF;
import gestortutoriasfx.utilidades.TarjetaReporte;
import gestortutoriasfx.utilidades.TarjetaSesion;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
    private ArrayList<SesionTutoria> listaSesionesRealizadas;
    private ArrayList<ReporteTutoria> listaReportesHechos;

    private SesionTutoria sesionSeleccionada; 
    private ReporteTutoria reporteSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPeriodo();
        cargarSesiones();
        cargarReportes();
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
            Utilidades.mostrarAlertaSimple("Atención", "Selecciona un reporte.", Alert.AlertType.WARNING);
            return;
        }
    
        File archivoDestino = solicitarUbicacionArchivo();
    
        if (archivoDestino != null) {
            boolean exito = ejecutarExportacion(archivoDestino);
        
            if (exito) {
                Utilidades.mostrarAlertaSimple("Éxito", "PDF generado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                Utilidades.mostrarAlertaSimple("Error", "No se pudo generar el PDF.", Alert.AlertType.ERROR);
            }
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
    
    private void actualizarSeleccionVisual(TarjetaSesion tarjetaSeleccionada) {
        for (Node nodo : vbListaReportes.getChildren()) {
            if (nodo instanceof TarjetaSesion) {
                ((TarjetaSesion) nodo).deseleccionar();
            }
        }
        tarjetaSeleccionada.seleccionar();
    }
    
    private ReporteTutoria buscarReporteDeSesion(int numSesion) {
        if (listaReportesHechos != null) {
            for (ReporteTutoria reporteTutoria : listaReportesHechos) {
                if (reporteTutoria.getNumSesion() == numSesion) {
                    return reporteTutoria;
                }
            }
        }
        return null;
    }
    
    private void cargarPeriodo() {
        HashMap<String, Object> respuestaPeriodo = PeriodoEscolarImplementacion.obtenerPeriodoActual();
        if (!(boolean) respuestaPeriodo.get("error")) {
            this.periodoActual = (PeriodoEscolar) respuestaPeriodo.get("periodo");
        } else {
            Utilidades.mostrarAlertaSimple("Error", 
                    "No hay periodo activo.", Alert.AlertType.WARNING);
            return;
        }
    }
    
    private void cargarReportes(){
        HashMap<String, Object> respuestaReportes = ReporteTutoriaImplementacion.obtenerReportesPorPeriodo(
                Sesion.getIdTutor(), periodoActual.getIdPeriodoEscolar());
        if (!(boolean) respuestaReportes.get("error")) {
            this.listaReportesHechos = (ArrayList<ReporteTutoria>) respuestaReportes.get("reportes");
        } else {
            this.listaReportesHechos = new ArrayList<>();
            Utilidades.mostrarAlertaSimple("Error", 
                    "No se pudieron cargar los reportes.", Alert.AlertType.WARNING);
        }
    }
    
    private void cargarSesiones(){
        HashMap<String, Object> respuestaSesiones = SesionTutoriaImplementacion.obtenerSesionesTutor(Sesion.getIdTutor());
        if (!(boolean) respuestaSesiones.get("error")) {
            this.listaSesionesRealizadas = (ArrayList<SesionTutoria>) respuestaSesiones.get("sesiones");
        } else {
            this.listaSesionesRealizadas = new ArrayList<>();
            Utilidades.mostrarAlertaSimple("Error", 
                    "No se pudieron cargar las sesiones.", Alert.AlertType.WARNING);
        }
    }
    
    private void cargarTarjetasDeSesiones() {
        vbListaReportes.getChildren().clear();
        HashSet<Integer> sesionesProcesadas = new HashSet<>();

        if (listaSesionesRealizadas == null) listaSesionesRealizadas = new ArrayList<>();

        for (SesionTutoria sesionTutoria : listaSesionesRealizadas) {
            if (sesionesProcesadas.contains(sesionTutoria.getNumSesion())) continue;
            ReporteTutoria reporte = buscarReporteDeSesion(sesionTutoria.getNumSesion());
            TarjetaReporte tarjeta = new TarjetaReporte(sesionTutoria, reporte, this::manejarSeleccion);

            vbListaReportes.getChildren().add(tarjeta);
            sesionesProcesadas.add(sesionTutoria.getNumSesion());
        }
    }

    private void configurarBotonesAccion(ReporteTutoria reporte) {
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
    
    private boolean ejecutarExportacion(File archivo) {
        HashMap<String, Object> respuestaBD = ProblematicaAcademicaImplementacion.obtenerProblematicasPorReporte(reporteSeleccionado.getIdReporteTutoria());

        List<ProblematicaAcademica> listaProblemasAcademicos = new ArrayList<>();
        if (!(boolean) respuestaBD.get("error")) {
            listaProblemasAcademicos = (List<ProblematicaAcademica>) respuestaBD.get("problematicas");
        }
        
        return ReporteTutoriaPDF.generarReporte(
            this.reporteSeleccionado, 
            this.periodoActual, 
            listaProblemasAcademicos, 
            sesionSeleccionada.getNombreTutor(),
            archivo
        );
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

    private void manejarSeleccion(TarjetaSesion tarjetaClick, ReporteTutoria reporteAsociado) {
        actualizarSeleccionVisual(tarjetaClick);
        this.sesionSeleccionada = tarjetaClick.getSesion();
        this.reporteSeleccionado = reporteAsociado;
        configurarBotonesAccion(reporteAsociado);
    }
    
    private File solicitarUbicacionArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte de Tutoría");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF (*.pdf)", "*.pdf"));
        fileChooser.setInitialFileName("Reporte_Sesion_" + reporteSeleccionado.getNumSesion() + ".pdf");
        
        return fileChooser.showSaveDialog(btnExportar.getScene().getWindow());
    }
}