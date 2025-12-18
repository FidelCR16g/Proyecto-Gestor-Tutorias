package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.ReporteGeneralImplementacion;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.ReporteGeneral;
import gestortutoriasfx.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class FXMLGestionarReporteGeneralController implements Initializable {

    @FXML private Label lbContexto;
    @FXML private TextField tfBusqueda;

    @FXML private TableView<ReporteGeneral> tvReportesGenerales;
    @FXML private TableColumn<ReporteGeneral, String> colPrograma;
    @FXML private TableColumn<ReporteGeneral, String> colPeriodo;
    @FXML private TableColumn<ReporteGeneral, String> colSesion;
    @FXML private TableColumn<ReporteGeneral, String> colFecha;
    @FXML private TableColumn<ReporteGeneral, String> colEstatus;

    @FXML private Button btnExportar;

    private StackPane panelContenido;
    private Parent vistaListado;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Node n = tvReportesGenerales;
            while (n != null && n.getParent() != null) n = n.getParent();
            vistaListado = (Parent) n;
        } catch (Exception e) {
            vistaListado = null;
        }

        if (lbContexto != null) {
            String nombre = (Sesion.getUsuarioSesion() != null && Sesion.getUsuarioSesion().getNombre() != null)
                    ? Sesion.getUsuarioSesion().getNombre()
                    : "";
            lbContexto.setText("Coordinador: " + nombre);
        }

        if (btnExportar != null) {
            btnExportar.setDisable(true);
            btnExportar.setVisible(false);
            btnExportar.setManaged(false);
        }

        colPrograma.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null && d.getValue().getNombreProgramaEducativo() != null
                ? d.getValue().getNombreProgramaEducativo() : "")
        );

        colPeriodo.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null && d.getValue().getNombrePeriodoEscolar() != null
                ? d.getValue().getNombrePeriodoEscolar() : "")
        );

        colSesion.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null ? String.valueOf(d.getValue().getNumSesion()) : "")
        );

        colFecha.setCellValueFactory(d -> {
            ReporteGeneral r = d.getValue();
            String f = "";
            if (r != null && r.getFecha() != null) f = r.getFecha().format(fmt);
            return new SimpleStringProperty(f);
        });

        colEstatus.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null && d.getValue().getEstatus() != null
                ? d.getValue().getEstatus().name() : "")
        );

        cargarTabla("");
    }

    public void setPanelContenido(StackPane panelContenido) {
        this.panelContenido = panelContenido;
    }

    private void cargarTabla(String busqueda) {
        HashMap<String, Object> resp = ReporteGeneralImplementacion.buscarReportesGenerales(busqueda);
        if (resp == null || (boolean) resp.getOrDefault("error", true)) {
            String msg = (resp != null) ? (String) resp.getOrDefault("mensaje", "Error al cargar reportes.") : "Error al cargar reportes.";
            Utilidades.mostrarAlertaSimple("Error", msg, Alert.AlertType.ERROR);
            tvReportesGenerales.setItems(FXCollections.observableArrayList());
            return;
        }

        @SuppressWarnings("unchecked")
        ArrayList<ReporteGeneral> lista = (ArrayList<ReporteGeneral>) resp.get("reportes");
        if (lista == null) lista = new ArrayList<>();

        tvReportesGenerales.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void clicBuscar(ActionEvent e) {
        String q = (tfBusqueda != null) ? tfBusqueda.getText().trim() : "";
        cargarTabla(q);
    }

    @FXML
    private void clicLimpiar(ActionEvent e) {
        if (tfBusqueda != null) tfBusqueda.clear();
        cargarTabla("");
    }

    @FXML
    private void clicCrear(ActionEvent e) {
        abrirFormulario(FXMLFormularioReporteGeneralController.Modo.CREAR, null);
    }

    @FXML
    private void clicEditar(ActionEvent e) {
        ReporteGeneral sel = tvReportesGenerales.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Utilidades.mostrarAlertaSimple("Atención", "Selecciona un reporte para editar.", Alert.AlertType.WARNING);
            return;
        }
        abrirFormulario(FXMLFormularioReporteGeneralController.Modo.EDITAR, sel);
    }
    
    @FXML
    private void clicExportar(ActionEvent e) {
        Utilidades.mostrarAlertaSimple(
                "Exportar",
                "La opción Exportar está deshabilitada temporalmente.",
                Alert.AlertType.INFORMATION
        );
    }

    private void abrirFormulario(FXMLFormularioReporteGeneralController.Modo modo, ReporteGeneral base) {
        if (panelContenido == null) {
            Utilidades.mostrarAlertaSimple("Error", "No se recibió panelContenido del principal.", Alert.AlertType.ERROR);
            return;
        }

        try {
            FXMLLoader loader = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLFormularioReporteGeneral.fxml");
            Parent vistaForm = loader.load();
            FXMLFormularioReporteGeneralController ctrl = loader.getController();

            ctrl.setPanelContenido(panelContenido);

            Runnable volver = () -> {
                cargarTabla(tfBusqueda != null ? tfBusqueda.getText().trim() : "");
                if (vistaListado != null) panelContenido.getChildren().setAll(vistaListado);
                else panelContenido.getChildren().setAll(vistaForm);
            };

            ctrl.inicializarFormulario(modo, base, volver);

            panelContenido.getChildren().setAll(vistaForm);

        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo abrir el formulario de Reporte General.", Alert.AlertType.ERROR);
        }
    }
}
