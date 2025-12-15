package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.EstudianteImplementacion;
import gestortutoriasfx.modelo.pojo.Estudiante;
import gestortutoriasfx.modelo.pojo.Tutor;
import gestortutoriasfx.utilidad.Utilidades;
import gestortutoriasfx.utilidades.ArrastrarSoltarUtilidad;
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
        inicializarListasVisuales();
        nuevosAsignados = new ArrayList<>();
        desasignados = new ArrayList<>();
    }
    
    @FXML
    private void clicGuardar(ActionEvent event) {
        if (!validarReglasDeNegocio()) return;
        
        boolean exito = guardarCambiosEnBD();

        procesarResultadoGuardado(exito);
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
        int maximo = tutorActual.getEspaciosTutorados();
        lbConteo.setText(total + " / " + maximo + " Asignados");
        if (total > maximo) {
            lbConteo.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            lbConteo.setStyle("-fx-text-fill: #78909c;");
        }
    }
    
    private void actualizarListasVisuales(ArrayList<Estudiante> asignados, ArrayList<Estudiante> disponibles) {
        listaAsignados.setAll(asignados);
        listaDisponibles.setAll(disponibles);
        actualizarConteo();
    }
    
    private Estudiante buscarEnLista(List<Estudiante> lista, String matricula) {
        for (Estudiante estudiante : lista) {
            if (estudiante.getMatricula().equals(matricula)) return estudiante;
        }
        return null;
    }
    
    private void cargarEstudiantes() {
        ArrayList<Estudiante> asignados = obtenerTutoradosActuales();
        ArrayList<Estudiante> disponibles = obtenerAlumnosSinTutor();

        if (asignados == null || disponibles == null) {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron cargar las listas.", Alert.AlertType.ERROR);
            return;
        }

        actualizarListasVisuales(asignados, disponibles);
    }
    
    private void configurarArrastreBidireccional(){
        //De disponibles a asignados
        ArrastrarSoltarUtilidad.configurarIntercambioListas(
            lvDisponibles, 
            lvAsignados, 
            Estudiante::getMatricula,
            estudiante -> {
                registrarCambio(estudiante, true); 
                actualizarConteo(); 
            }
        );

        //De asignados a disponibles
        ArrastrarSoltarUtilidad.configurarIntercambioListas(
            lvAsignados, 
            lvDisponibles, 
            Estudiante::getMatricula, 
            estudiante -> {
                registrarCambio(estudiante, false); 
                actualizarConteo(); 
            }
        );
    }

    private void inicializarListasVisuales() {
        listaDisponibles = FXCollections.observableArrayList();
        listaAsignados = FXCollections.observableArrayList();
        lvDisponibles.setItems(listaDisponibles);
        lvAsignados.setItems(listaAsignados);
        lvDisponibles.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lvAsignados.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    
    private boolean guardarCambiosEnBD() {
        return EstudianteImplementacion.actualizarAsignaciones(
            nuevosAsignados, 
            desasignados, 
            tutorActual.getIdTutor()
        );
    }
    
    public void inicializarTutor(Tutor tutor) {
        this.tutorActual = tutor;
        lbNombreTutor.setText(tutor.getNombreCompleto());
        cargarEstudiantes();
    }
    
    private ArrayList<Estudiante> obtenerAlumnosSinTutor() {
        HashMap<String, Object> respuesta = EstudianteImplementacion.obtenerEstudiantesSinTutor();
        if (!(boolean) respuesta.get("error")) {
            return (ArrayList<Estudiante>) respuesta.get("estudiantes");
        }
        return null;
    }
    
    private ArrayList<Estudiante> obtenerTutoradosActuales() {
        HashMap<String, Object> respuesta = EstudianteImplementacion.obtenerTutoradosPorTutor(tutorActual.getIdTutor());
        if (!(boolean) respuesta.get("error")) {
            return (ArrayList<Estudiante>) respuesta.get("estudiantes");
        }
        return null;
    }
    
    private void procesarResultadoGuardado(boolean exito) {
        if (exito) {
            Utilidades.mostrarAlertaSimple("Éxito", 
                "Asignaciones actualizadas correctamente.", Alert.AlertType.INFORMATION);
            regresarAlListado();
        } else {
            Utilidades.mostrarAlertaSimple("Error", 
                "No se pudieron guardar los cambios en la BD.", Alert.AlertType.ERROR);
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
    
    private boolean validarReglasDeNegocio() {
        if (listaAsignados.size() > tutorActual.getEspaciosTutorados()) {
            Utilidades.mostrarAlertaSimple("Cupo Excedido", 
                "El tutor no puede tener más alumnos de los permitidos.", Alert.AlertType.WARNING);
            return false;
        }
        
        if (nuevosAsignados.isEmpty() && desasignados.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Sin Cambios", 
                "No ha realizado ninguna modificación.", Alert.AlertType.INFORMATION);
            return false;
        }
        return true;
    }
}