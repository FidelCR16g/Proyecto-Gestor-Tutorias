package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.ProblematicaAcademicaImplementacion;
import gestortutoriasfx.dominio.ReporteTutoriaImplementacion;
import gestortutoriasfx.modelo.pojo.ProblematicaAcademica;
import gestortutoriasfx.modelo.pojo.ReporteTutoria;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class FXMLMostrarReporteTutoriaController implements Initializable {

    @FXML private Label lbPeriodo;
    @FXML private Label lbProgramaEducativo;
    @FXML private Label lbSesion;
    @FXML private Label lbFechaTutoria;

    @FXML private Label lbTotalAsistencias;
    @FXML private Label lbTotalRiesgo;
    
    @FXML private TableView<ProblematicaAcademica> tvProblematicas;
    @FXML private TableColumn<ProblematicaAcademica, String> tcEE;
    @FXML private TableColumn<ProblematicaAcademica, String> tcProfesor;
    @FXML private TableColumn<ProblematicaAcademica, String> tcProblematica;
    @FXML private TableColumn<ProblematicaAcademica, Integer> tcNumAlumnos;

    @FXML private TextArea taComentarios;
    @FXML private Label lbMensaje;
    @FXML private CheckBox chRevisado;
    @FXML private Label lbEstatus;

    private ReporteTutoria reporteActual;
    private Runnable onCerrar;
    private boolean cerrarVentanaAlSalir = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (lbMensaje != null) lbMensaje.setText("");

        if (tcEE != null) {
            tcEE.setCellValueFactory(cd -> {
                return new SimpleStringProperty(safe(
                        cd.getValue() != null ? cd.getValue().getNombreExperienciaEducativa() : null
                ));
            });
        }
        if (tcProfesor != null) {
            tcProfesor.setCellValueFactory(cd -> new SimpleStringProperty(safe(
                cd.getValue() != null ? cd.getValue().getNombreProfesor() : null
            )));
        }
        if (tcProblematica != null) {
            tcProblematica.setCellValueFactory(cd -> new SimpleStringProperty(safe(
                cd.getValue() != null ? cd.getValue().getProblema() : null
            )));
        }
        if (tcNumAlumnos != null) {
            tcNumAlumnos.setCellValueFactory(cd ->
                new SimpleIntegerProperty(cd.getValue() != null ? cd.getValue().getNumEstudiantes() : 0).asObject()
            );
        }
    }

    public void inicializarReporte(ReporteTutoria reporte, Runnable onCerrar, boolean cerrarVentanaAlSalir) {
        this.reporteActual = reporte;
        this.onCerrar = onCerrar;
        this.cerrarVentanaAlSalir = cerrarVentanaAlSalir;
        pintarReporte();
        cargarProblematicas();
    }

    private void pintarReporte() {
        if (lbMensaje != null) lbMensaje.setText("");

        if (reporteActual == null) {
            if (lbMensaje != null) lbMensaje.setText("Reporte no disponible.");
            return;
        }

        if (lbPeriodo != null) lbPeriodo.setText(safe(reporteActual.getNombrePeriodoEscolar()));
        if (lbProgramaEducativo != null) lbProgramaEducativo.setText(safe(reporteActual.getNombreProgramaEducativo()));
        if (lbSesion != null) lbSesion.setText(String.valueOf(reporteActual.getNumSesion()));

        String fecha = !safe(reporteActual.getFechaUltimaTutoria()).isEmpty()
            ? reporteActual.getFechaUltimaTutoria()
            : reporteActual.getFechaCreacionReporte();

        if (lbFechaTutoria != null) lbFechaTutoria.setText(safe(fecha));

        if (lbTotalAsistencias != null) lbTotalAsistencias.setText(String.valueOf(reporteActual.getNumAlumnosAsistieron()));
        if (lbTotalRiesgo != null) lbTotalRiesgo.setText(String.valueOf(reporteActual.getNumAlumnosRiesgo()));

        if (taComentarios != null) taComentarios.setText(safe(reporteActual.getComentarios()));

        String estatus = safe(reporteActual.getEstatus());
        if (lbEstatus != null) lbEstatus.setText(estatus);

        boolean yaRevisado = "Revisado".equalsIgnoreCase(estatus);
        if (chRevisado != null) {
            chRevisado.setSelected(yaRevisado);
        }
    }

    private void cargarProblematicas() {
        if (tvProblematicas == null || reporteActual == null) return;

        HashMap<String, Object> resp = ProblematicaAcademicaImplementacion.obtenerProblematicasPorReporte(
            reporteActual.getIdReporteTutoria()
        );

        if (resp == null || (boolean) resp.getOrDefault("error", true)) {
            if (lbMensaje != null) lbMensaje.setText((String) (resp != null
                ? resp.getOrDefault("mensaje", "No se pudieron cargar problemáticas.")
                : "No se pudieron cargar problemáticas."));
            tvProblematicas.getItems().clear();
            return;
        }

        @SuppressWarnings("unchecked")
        ArrayList<ProblematicaAcademica> lista = (ArrayList<ProblematicaAcademica>) resp.get("problematicas");
        tvProblematicas.getItems().setAll(lista != null ? lista : new ArrayList<>());
    }

    @FXML
    private void clicMarcarRevisado(ActionEvent event) {

        if (lbMensaje != null) lbMensaje.setText("");

        if (reporteActual == null) {
            if (lbMensaje != null) lbMensaje.setText("No hay reporte cargado.");
            if (chRevisado != null) chRevisado.setSelected(false);
            return;
        }

  
        if (chRevisado != null && !(event != null && event.getSource() instanceof CheckBox)) {
            chRevisado.setSelected(true);
        }

        if (chRevisado != null && event != null && event.getSource() instanceof CheckBox && !chRevisado.isSelected()) {
            chRevisado.setSelected("Revisado".equalsIgnoreCase(safe(reporteActual.getEstatus())));
            return;
        }

        if ("Revisado".equalsIgnoreCase(safe(reporteActual.getEstatus()))) {
            if (lbMensaje != null) lbMensaje.setText("Este reporte ya está marcado como Revisado.");
            if (chRevisado != null) chRevisado.setSelected(true);
            return;
        }

        HashMap<String, Object> resp = ReporteTutoriaImplementacion.marcarReporteComoRevisado(
            reporteActual.getIdReporteTutoria()
        );

        boolean error = (boolean) (resp != null ? resp.getOrDefault("error", true) : true);
        String msg = (String) (resp != null
            ? resp.getOrDefault("mensaje", error ? "No se pudo actualizar." : "Actualizado.")
            : "No se pudo actualizar.");

        Alert a = new Alert(error ? Alert.AlertType.WARNING : Alert.AlertType.INFORMATION);
        a.setTitle("Actualizar estado");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();

        if (error) {
            if (chRevisado != null) chRevisado.setSelected(false);
            return;
        }

        // Actualizar estado local + UI
        reporteActual.setEstatus("Revisado");
        pintarReporte();

        // ✅ Ahora sí: regresar y recargar tabla
        if (onCerrar != null) onCerrar.run();

        // Si fue ventana modal, cerrarla
        if (cerrarVentanaAlSalir && event != null && event.getSource() instanceof Node) {
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        if (onCerrar != null) onCerrar.run();

        if (!cerrarVentanaAlSalir) return;

        if (event != null && event.getSource() instanceof Node) {
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }

    private String safe(String s) {
        return (s == null) ? "" : s;
    }
}
