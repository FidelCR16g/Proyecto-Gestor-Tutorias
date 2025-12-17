package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.EstudianteImplementacion;
import gestortutoriasfx.modelo.pojo.Estudiante;
import gestortutoriasfx.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

public class FXMLGestionarTutoradoController implements Initializable {

    @FXML private TextField tfBuscar;

    @FXML private TableView<Estudiante> tvTutorados;
    @FXML private TableColumn<Estudiante, String> tcMatricula;
    @FXML private TableColumn<Estudiante, String> tcNombre;
    @FXML private TableColumn<Estudiante, String> tcPrograma;
    @FXML private TableColumn<Estudiante, String> tcEstado;

    @FXML private Label lbMensaje;

    private final ObservableList<Estudiante> listaTutorados = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (lbMensaje != null) lbMensaje.setText("");
        configurarTabla();
        cargarTodos();
    }

    private void configurarTabla() {
        if (tcMatricula != null) tcMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        if (tcNombre != null) tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        if (tcPrograma != null) tcPrograma.setCellValueFactory(new PropertyValueFactory<>("nombreProgramaEducativo"));

        if (tcEstado != null) {
            tcEstado.setCellValueFactory(cell -> {
                boolean activo = cell.getValue() != null && cell.getValue().isPerfilActivo();
                return new SimpleStringProperty(activo ? "Activo" : "Inactivo");
            });
        }

        if (tvTutorados != null) tvTutorados.setItems(listaTutorados);
    }

    private void cargarTodos() {
        HashMap<String, Object> resp = EstudianteImplementacion.obtenerTodosTutorados();

        if (resp != null && resp.get("error") instanceof Boolean && !(boolean) resp.get("error")) {
            ArrayList<Estudiante> estudiantes = (ArrayList<Estudiante>) resp.get("estudiantes");
            if (estudiantes == null) estudiantes = new ArrayList<>();

            listaTutorados.setAll(estudiantes);
            if (lbMensaje != null) lbMensaje.setText("Total tutorados: " + estudiantes.size());
        } else {
            String msg = (resp != null && resp.get("mensaje") != null)
                    ? resp.get("mensaje").toString()
                    : "No se pudieron cargar los tutorados.";
            if (lbMensaje != null) lbMensaje.setText(msg);
        }
    }

    @FXML
    private void clicBuscar(ActionEvent e) {
        String q = (tfBuscar != null && tfBuscar.getText() != null) ? tfBuscar.getText().trim().toLowerCase() : "";

        if (q.isEmpty()) {
            if (tvTutorados != null) tvTutorados.setItems(listaTutorados);
            if (lbMensaje != null) lbMensaje.setText("Total tutorados: " + listaTutorados.size());
            return;
        }

        ObservableList<Estudiante> filtrado = FXCollections.observableArrayList();
        for (Estudiante est : listaTutorados) {
            String texto = (safe(est.getMatricula()) + " " +
                            safe(est.getNombreCompleto()) + " " +
                            safe(est.getNombreProgramaEducativo())).toLowerCase();
            if (texto.contains(q)) filtrado.add(est);
        }

        if (tvTutorados != null) tvTutorados.setItems(filtrado);
        if (lbMensaje != null) lbMensaje.setText("Coincidencias: " + filtrado.size());
    }

    @FXML
    private void clicAgregar(ActionEvent e) {
        abrirFormularioEnPanel((Node) e.getSource(), false, null);
    }

    @FXML
    private void clicEditar(ActionEvent e) {
        Estudiante seleccionado = (tvTutorados != null) ? tvTutorados.getSelectionModel().getSelectedItem() : null;
        if (seleccionado == null) {
            Utilidades.mostrarAlertaSimple("Atención", "Selecciona un tutorado para editar.", Alert.AlertType.WARNING);
            return;
        }

        HashMap<String, Object> resp = EstudianteImplementacion.obtenerTutoradoPorMatricula(seleccionado.getMatricula());
        if (resp == null || (resp.get("error") instanceof Boolean && (boolean) resp.get("error"))) {
            String msg = (resp != null && resp.get("mensaje") != null) ? resp.get("mensaje").toString()
                    : "No se pudo recuperar la información del tutorado.";
            Utilidades.mostrarAlertaSimple("Error", msg, Alert.AlertType.ERROR);
            return;
        }

        Estudiante completo = (Estudiante) resp.get("estudiante");
        if (completo == null) {
            Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar el tutorado seleccionado.", Alert.AlertType.ERROR);
            return;
        }

        abrirFormularioEnPanel((Node) e.getSource(), true, completo);
    }

    @FXML
    private void clicEliminar(ActionEvent e) {
        Estudiante seleccionado = (tvTutorados != null) ? tvTutorados.getSelectionModel().getSelectedItem() : null;

        if (seleccionado == null) {
            Utilidades.mostrarAlertaSimple("Atención", "Selecciona un tutorado para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("Se eliminará de la base de datos");
        alert.setContentText("¿Eliminar a: " + seleccionado.getNombreCompleto()
                + " (" + seleccionado.getMatricula() + ")?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        HashMap<String, Object> resp = EstudianteImplementacion.eliminarTutorado(seleccionado.getMatricula());
        if (resp != null && resp.get("error") instanceof Boolean && !(boolean) resp.get("error")) {
            Utilidades.mostrarAlertaSimple("Éxito", "Tutorado eliminado correctamente.", Alert.AlertType.INFORMATION);
            cargarTodos();
        } else {
            String msg = (resp != null && resp.get("mensaje") != null) ? resp.get("mensaje").toString()
                    : "No se pudo eliminar el tutorado.";
            Utilidades.mostrarAlertaSimple("Error", msg, Alert.AlertType.ERROR);
        }
    }

    private void abrirFormularioEnPanel(Node origen, boolean edicion, Estudiante seleccionado) {
        StackPane panel = obtenerPanelContenido(origen);
        if (panel == null) return;

        try {
            FXMLLoader loader = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLFormularioTutorado.fxml");
            Parent vista = loader.load();

            FXMLFormularioTutoradoController ctrl = loader.getController();
            if (ctrl != null) {
                if (edicion) ctrl.cargarDatos(seleccionado);
                else ctrl.aplicarModoEdicion(false);
            }

            panel.getChildren().setAll(vista);

        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo abrir el formulario de tutorado.", Alert.AlertType.ERROR);
        }
    }

    private StackPane obtenerPanelContenido(Node origen) {
        try {
            Node n = origen.getScene().lookup("#panelContenido");
            if (n instanceof StackPane) return (StackPane) n;

            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No se encontró el panelContenido. Asegúrate de entrar desde Principal Administrador.",
                    Alert.AlertType.ERROR
            );
            return null;

        } catch (Exception ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "Nosos: No se pudo localizar el panelContenido en la escena.",
                    Alert.AlertType.ERROR
            );
            return null;
        }
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
