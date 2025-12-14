package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.EstudianteImplementacion;
import gestortutoriasfx.modelo.pojo.Estudiante;
import gestortutoriasfx.modelo.pojo.Tutor;
import gestortutoriasfx.utilidad.Utilidades;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

/**
 * Nombre de la Clase: FXMLAsignarTutoradosController
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 07/11/2025
 *
 * Descripción:
 * Se encarga de controlar la asignacion de tutorados
 */

public class FXMLAsignarTutoradosController implements Initializable {
    @FXML 
    private Label lbNombreTutor;
    @FXML 
    private Label lbConteo;
    @FXML 
    private ListView<Estudiante> lvDisponibles;
    @FXML 
    private ListView<Estudiante> lvAsignados;
    @FXML 
    private Button btnGuardar;

    private ObservableList<Estudiante> listaDisponibles;
    private ObservableList<Estudiante> listaAsignados;
    private Tutor tutorActual;
    
    private ArrayList<Estudiante> nuevosAsignados;
    private ArrayList<Estudiante> desasignados;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarListas();
        nuevosAsignados = new ArrayList<>();
        desasignados = new ArrayList<>();
    }
    
    @FXML
    private void clicGuardar(ActionEvent event) {
        if (listaAsignados.size() > tutorActual.getEspaciosTutorados()) {
            Utilidades.mostrarAlertaSimple("Cupo Excedido", 
                    "El tutor no puede tener más alumnos de los permitidos.", Alert.AlertType.WARNING);
            return;
        }
        if (nuevosAsignados.isEmpty() && desasignados.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Sin Cambios", 
                    "No ha realizado ninguna modificación.", Alert.AlertType.INFORMATION);
            return;
        }
        boolean exito = EstudianteImplementacion.actualizarAsignaciones(
                nuevosAsignados, 
                desasignados, 
                tutorActual.getIdTutor()
        );
        if (exito) {
            Utilidades.mostrarAlertaSimple("Éxito", 
                    "Asignaciones actualizadas correctamente.", Alert.AlertType.INFORMATION);
            regresarAlListado();
        } else {
            Utilidades.mostrarAlertaSimple("Error", 
                    "No se pudieron guardar los cambios en la BD.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        if (!nuevosAsignados.isEmpty() || !desasignados.isEmpty()) {
            if (!Utilidades.mostrarAlertaVerificacion("Salir", 
                    "¿Desea salir sin guardar?", "Se perderán los movimientos.")) {
                return;
            }
        }
        regresarAlListado();
    }
    
    private void actualizarConteo() {
        int total = listaAsignados.size();
        int max = tutorActual.getEspaciosTutorados();
        lbConteo.setText(total + " / " + max + " Asignados");
        if (total > max) {
            lbConteo.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            lbConteo.setStyle("-fx-text-fill: #78909c;");
        }
    }
    
    private Estudiante buscarEnLista(List<Estudiante> lista, String matricula) {
        for (Estudiante estudiante : lista) {
            if (estudiante.getMatricula().equals(matricula)) return estudiante;
        }
        return null;
    }
    
    private void cargarEstudiantes() {
        HashMap<String, Object> respuestaAsignados = EstudianteImplementacion.obtenerTutoradosPorTutor(tutorActual.getIdTutor());
        HashMap<String, Object> respuestaDisponibles = EstudianteImplementacion.obtenerEstudiantesSinTutor();
        if (!(boolean) respuestaAsignados.get("error") && !(boolean) respuestaDisponibles.get("error")) {
            ArrayList<Estudiante> asignadosBD = (ArrayList<Estudiante>) respuestaAsignados.get("estudiantes");
            ArrayList<Estudiante> disponiblesBD = (ArrayList<Estudiante>) respuestaDisponibles.get("estudiantes");
            
            listaAsignados.setAll(asignadosBD);
            listaDisponibles.setAll(disponiblesBD);
            
            actualizarConteo();
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron cargar las listas.", Alert.AlertType.ERROR);
        }
    }
    
    private void configurarArrastre(ListView<Estudiante> origen, ListView<Estudiante> destino, boolean esAsignacion) {
        configurarInicioArrastre(origen);
        configurarRecepcionArrastre(origen, destino, esAsignacion);
    }
    
    private void configurarInicioArrastre(ListView<Estudiante> origen) {
        origen.setOnDragDetected(event -> {
            Estudiante item = origen.getSelectionModel().getSelectedItem();
            
            if (item != null) {
                Dragboard db = origen.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(item.getMatricula());
                db.setContent(content);
                event.consume();
            }
        });
    }

    private void configurarListas() {
        listaDisponibles = FXCollections.observableArrayList();
        listaAsignados = FXCollections.observableArrayList();
        lvDisponibles.setItems(listaDisponibles);
        lvAsignados.setItems(listaAsignados);
        lvDisponibles.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lvAsignados.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        configurarArrastre(lvDisponibles, lvAsignados, true);
        configurarArrastre(lvAsignados, lvDisponibles, false);
    }
    
    private void configurarRecepcionArrastre(ListView<Estudiante> origen, ListView<Estudiante> destino, boolean esAsignacion) {
        destino.setOnDragOver(event -> {
            if (event.getGestureSource() != destino && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        
        destino.setOnDragDropped(event -> {
            boolean exito = procesarSoltado(event.getDragboard(), origen, destino, esAsignacion);
            event.setDropCompleted(exito);
            event.consume();
        });
    }
    
    public void inicializarTutor(Tutor tutor) {
        this.tutorActual = tutor;
        lbNombreTutor.setText(tutor.getNombreCompleto());
        cargarEstudiantes();
    }

    private void registrarCambio(Estudiante estudiante, boolean asignadoAhora) {
        if (asignadoAhora) {
            if (desasignados.contains(estudiante)) {
                desasignados.remove(estudiante); 
            } else {
                nuevosAsignados.add(estudiante); 
            }
        } else {
            if (nuevosAsignados.contains(estudiante)) {
                nuevosAsignados.remove(estudiante); 
            } else {
                desasignados.add(estudiante); 
            }
        }
    }
    
    private boolean procesarSoltado(Dragboard db, ListView<Estudiante> origen, ListView<Estudiante> destino, boolean esAsignacion) {
        if (!db.hasString()) return false;

        String matricula = db.getString();
        Estudiante estudianteMover = buscarEnLista(origen.getItems(), matricula);

        if (estudianteMover != null) {
            origen.getItems().remove(estudianteMover);
            destino.getItems().add(estudianteMover);
            registrarCambio(estudianteMover, esAsignacion);
            actualizarConteo();
            
            return true;
        }
        
        return false;
    }
    
    private void regresarAlListado() {
        try {
            FXMLLoader cargador = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLSeleccionTutor.fxml");
            Parent root = cargador.load();
            Pane panel = (Pane) btnGuardar.getScene().lookup("#panelContenido");
            if (panel != null) {
                panel.getChildren().clear();
                panel.getChildren().add(root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}