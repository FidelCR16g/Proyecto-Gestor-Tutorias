package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.ReporteTutoriaImplementacion;
import gestortutoriasfx.modelo.pojo.ReporteTutoria;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLRevisarReportesTutoriaController implements Initializable {

    @FXML private TextField tfBusqueda;

    @FXML private TableView<ReporteTutoria> tvReportes;
    @FXML private TableColumn<ReporteTutoria, String> tcNombreTutor;
    @FXML private TableColumn<ReporteTutoria, String> tcPrograma;
    @FXML private TableColumn<ReporteTutoria, String> tcSesion;
    @FXML private TableColumn<ReporteTutoria, String> tcFecha;
    @FXML private TableColumn<ReporteTutoria, String> tcEstado;

    @FXML private Label lbMensaje;

    private StackPane panelContenido;
    private Parent vistaListado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            Node n = tvReportes;
            while (n != null && n.getParent() != null) n = n.getParent();
            vistaListado = (Parent) n;
        } catch (Exception e) {
            vistaListado = null;
        }

        if (lbMensaje != null) lbMensaje.setText("");

        tcNombreTutor.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null && d.getValue().getNombreTutor() != null
                ? d.getValue().getNombreTutor() : "")
        );

        tcPrograma.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null && d.getValue().getNombreProgramaEducativo() != null
                ? d.getValue().getNombreProgramaEducativo() : "")
        );

        tcSesion.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null ? String.valueOf(d.getValue().getNumSesion()) : "")
        );

        tcFecha.setCellValueFactory(d -> {
            ReporteTutoria r = d.getValue();
            String fecha = "";
            if (r != null) {
                if (r.getFechaUltimaTutoria() != null && !r.getFechaUltimaTutoria().trim().isEmpty()) fecha = r.getFechaUltimaTutoria();
                else if (r.getFechaCreacionReporte() != null) fecha = r.getFechaCreacionReporte();
            }
            return new SimpleStringProperty(fecha);
        });

        tcEstado.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null && d.getValue().getEstatus() != null
                ? d.getValue().getEstatus() : "")
        );

        cargarTabla("");
    }

    public void setPanelContenido(StackPane panelContenido) {
        this.panelContenido = panelContenido;
    }

    private void cargarTabla(String busqueda) {
        if (lbMensaje != null) lbMensaje.setText("");

        HashMap<String, Object> resp = ReporteTutoriaImplementacion.buscarReportesParaRevision(busqueda);
        if (resp == null) {
            if (lbMensaje != null) lbMensaje.setText("No se recibió respuesta del servidor.");
            tvReportes.setItems(FXCollections.observableArrayList());
            return;
        }

        boolean error = (boolean) resp.getOrDefault("error", true);
        if (!error) {
            @SuppressWarnings("unchecked")
            ArrayList<ReporteTutoria> lista = (ArrayList<ReporteTutoria>) resp.get("reportes");
            if (lista == null) lista = new ArrayList<>();

            tvReportes.setItems(FXCollections.observableArrayList(lista));
            if (lbMensaje != null) lbMensaje.setText(lista.isEmpty() ? "No hay reportes para mostrar." : "");
        } else {
            if (lbMensaje != null) lbMensaje.setText((String) resp.getOrDefault("mensaje", "Error al cargar reportes."));
            tvReportes.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    private void clicBuscar(ActionEvent event) {
        String q = (tfBusqueda != null) ? tfBusqueda.getText().trim() : "";
        cargarTabla(q);
    }

    @FXML
    private void clicVerReporte(ActionEvent event) {
        if (lbMensaje != null) lbMensaje.setText("");

        ReporteTutoria seleccionado = tvReportes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            if (lbMensaje != null) lbMensaje.setText("Selecciona un reporte de la tabla.");
            return;
        }

        HashMap<String, Object> respDetalle =
            ReporteTutoriaImplementacion.obtenerReportePorId(seleccionado.getIdReporteTutoria());

        if (respDetalle == null || (boolean) respDetalle.getOrDefault("error", true)) {
            if (lbMensaje != null) lbMensaje.setText((String) (respDetalle != null
                ? respDetalle.getOrDefault("mensaje", "No se pudo cargar el reporte.")
                : "No se pudo cargar el reporte."));
            return;
        }

        ReporteTutoria detalle = (ReporteTutoria) respDetalle.get("reporte");
        if (detalle == null) {
            if (lbMensaje != null) lbMensaje.setText("No se encontró el reporte.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/gestortutoriasfx/vista/FXMLMostrarReporteTutoria.fxml"));
            Parent vista = loader.load();
            FXMLMostrarReporteTutoriaController ctrl = loader.getController();

            Runnable volver = () -> {
                cargarTabla(tfBusqueda != null ? tfBusqueda.getText().trim() : "");
                if (panelContenido != null && vistaListado != null) {
                    panelContenido.getChildren().setAll(vistaListado);
                }
            };

            ctrl.inicializarReporte(detalle, volver, panelContenido == null);

            if (panelContenido != null) {
                panelContenido.getChildren().setAll(vista);
                return;
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            try {
                Stage owner = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.initOwner(owner);
            } catch (Exception ignore) { }

            stage.setTitle("Mostrar Reporte de Tutoría");
            stage.setScene(new Scene(vista));
            stage.showAndWait();

            cargarTabla(tfBusqueda != null ? tfBusqueda.getText().trim() : "");

        } catch (IOException ex) {
            ex.printStackTrace();
            if (lbMensaje != null) lbMensaje.setText("No se pudo abrir la vista FXMLMostrarReporteTutoria.");
        }
    }
}
