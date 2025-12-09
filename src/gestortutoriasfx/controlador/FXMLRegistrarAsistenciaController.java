package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.PeriodoEscolarImplementacion;
import gestortutoriasfx.dominio.SesionTutoriaImplementacion;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.FechaTutoria;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
import gestortutoriasfx.utilidad.Utilidades;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

/**
 * Nombre de la Clase: FXMLRegistrarAsistenciaController
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 27/11/2025
 *
 * Descripción:
 * Se encarga de controlar los distintos elementos de los listados de evidencia
 */

public class FXMLRegistrarAsistenciaController implements Initializable {
    @FXML 
    private ComboBox<String> cbSesion;
    @FXML 
    private ListView<SesionTutoria> lvFaltas;
    @FXML 
    private ListView<SesionTutoria> lvAsistencias;
    @FXML 
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private ObservableList<SesionTutoria> listaFaltas;
    private ObservableList<SesionTutoria> listaAsistencias;
    private ObservableList<String> sesionesDisponibles;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarListas();
        configurarListenerSesion();
        cargarSesionesDisponibles();
    }
    
    @FXML
    private void clicGuardar(ActionEvent event) {
        for(SesionTutoria sesionTutoria : listaAsistencias) sesionTutoria.setEstado("Asistio");
        for(SesionTutoria sesionTutoria : listaFaltas) sesionTutoria.setEstado("No Asistio");
        
        ArrayList<SesionTutoria> listaFinal = new ArrayList<>();
        listaFinal.addAll(listaAsistencias);
        listaFinal.addAll(listaFaltas);
        
        if (!listaFinal.isEmpty()) {
            registrarAsistencia(listaFinal);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        if(Utilidades.mostrarAlertaVerificacion("Salir", "¿Desea salir?", "Los cambios no guardados se perderán.")) {
            limpiarPanelCentral();
        }
    }
    
    private SesionTutoria buscarEnLista(List<SesionTutoria> lista, int id) {
        for (SesionTutoria sesionTutoria : lista) {
            if (sesionTutoria.getIdSesion() == id) return sesionTutoria;
        }
        return null;
    }
    
    private void cargarAlumnos(int numSesion) {
        HashMap<String, Object> respuesta = SesionTutoriaImplementacion.obtenerListaAsistencia(Sesion.getIdTutor(), numSesion);
        
        if (!(boolean) respuesta.get("error")) {
            ArrayList<SesionTutoria> listaBD = (ArrayList<SesionTutoria>) respuesta.get("lista");
            boolean yaFueRegistrada = false;
            
            listaFaltas.clear();
            listaAsistencias.clear();
            
            for (SesionTutoria sesion : listaBD) {
                if ("Asistio".equalsIgnoreCase(sesion.getEstado())) {
                    listaAsistencias.add(sesion);
                    yaFueRegistrada = true;
                } else if ("No Asistio".equalsIgnoreCase(sesion.getEstado())) {
                    listaFaltas.add(sesion);
                    yaFueRegistrada = true;
                }else {
                    listaFaltas.add(sesion);
                }
            }
            
            configurarModoEdicion(yaFueRegistrada);
        } else {
            Utilidades.mostrarAlertaSimple("Error", respuesta.get("mensaje").toString(), 
                    Alert.AlertType.WARNING);
        }
    }
    
    private void cargarSesionesDisponibles() {
        HashMap<String, Object> respuestaPeriodo = PeriodoEscolarImplementacion.obtenerPeriodoActual();
        
        if (!(boolean) respuestaPeriodo.get("error")) {
            PeriodoEscolar periodo = (PeriodoEscolar) respuestaPeriodo.get("periodo");
            HashMap<String, Object> respuestaFechas = SesionTutoriaImplementacion.obtenerFechasPorPeriodo(periodo.getIdPeriodoEscolar());
            HashMap<String, Object> respuestaOcupadas = SesionTutoriaImplementacion.obtenerSesionesOcupadas(periodo.getIdPeriodoEscolar());
            
            if (!(boolean) respuestaFechas.get("error") && !(boolean) respuestaOcupadas.get("error")) {
                ArrayList<FechaTutoria> todasLasFechas = (ArrayList<FechaTutoria>) respuestaFechas.get("fechas");
                ArrayList<Integer> sesionesAgendadas = (ArrayList<Integer>) respuestaOcupadas.get("ocupadas");
                ObservableList itemsCombo = FXCollections.observableArrayList();
                
                if (sesionesAgendadas.isEmpty()) {
                    Utilidades.mostrarAlertaSimple("Sin Agenda", "No tienes sesiones agendadas.", Alert.AlertType.WARNING);
                } else {
                    for (FechaTutoria ft : todasLasFechas) {
                        if (sesionesAgendadas.contains(ft.getNumSesion())) {
                            itemsCombo.add(ft);
                        }
                    }
                }
                cbSesion.setItems(itemsCombo);
            } else {
                Utilidades.mostrarAlertaSimple("Error", "No se pudieron cargar las sesiones.", Alert.AlertType.ERROR);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudo recuperar el periodo actual.", Alert.AlertType.ERROR);
        }
    }
    
    private void configurarModoEdicion(boolean yaFueRegistrada) {
        boolean permitirEdicion = !yaFueRegistrada;

        lvFaltas.setDisable(!permitirEdicion);
        lvAsistencias.setDisable(!permitirEdicion);
        btnGuardar.setDisable(!permitirEdicion);
        btnCancelar.setDisable(!permitirEdicion);

        if (yaFueRegistrada) {
            Utilidades.mostrarAlertaSimple("Solo Lectura", 
                    "La asistencia para esta sesión ya fue registrada anteriormente.", 
                    Alert.AlertType.INFORMATION);
        } else {
            btnGuardar.setText("Guardar Cambios");
        }
    }

    private void configurarListas() {
        listaFaltas = FXCollections.observableArrayList();
        listaAsistencias = FXCollections.observableArrayList();
        
        lvFaltas.setItems(listaFaltas);
        lvAsistencias.setItems(listaAsistencias);
        
        configurarArrastre(lvFaltas, lvAsistencias);
        configurarArrastre(lvAsistencias, lvFaltas);
    }
    
    private void configurarListenerSesion() {
        cbSesion.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    try {
                        FechaTutoria fechaSeleccionada = (FechaTutoria) newValue;
                        int numSesion = fechaSeleccionada.getNumSesion();
                        
                        cargarAlumnos(numSesion);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    
    private void configurarArrastre(ListView<SesionTutoria> origen, ListView<SesionTutoria> destino) {
        origen.setOnDragDetected(event -> {
            SesionTutoria item = origen.getSelectionModel().getSelectedItem();
            if (item != null) {
                Dragboard db = origen.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(item.getIdSesion())); 
                db.setContent(content);
                event.consume();
            }
        });
        destino.setOnDragOver(event -> {
            if (event.getGestureSource() != destino && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        destino.setOnDragDropped(event -> {
            boolean success = false;
            Dragboard db = event.getDragboard();
            
            if (db.hasString()) {
                int idSesionMover = Integer.parseInt(db.getString());
                SesionTutoria itemMover = buscarEnLista(origen.getItems(), idSesionMover);
                
                if (itemMover != null) {
                    origen.getItems().remove(itemMover);
                    destino.getItems().add(itemMover);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
    
    private void limpiarPanelCentral() {
        try {
            Pane panel = (Pane) btnGuardar.getScene().lookup("#panelContenido");
            if(panel != null) panel.getChildren().clear();
        } catch (Exception e) {}
    }
    
    private void registrarAsistencia(ArrayList<SesionTutoria> lista) {
        HashMap<String, Object> respuesta = SesionTutoriaImplementacion.guardarAsistencias(lista);
        
        if (!(boolean) respuesta.get("error")) {
            Utilidades.mostrarAlertaSimple("Éxito", "Listas de asistencia guardadas correctamente.",
                    Alert.AlertType.INFORMATION);
            limpiarPanelCentral();
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron guardar los cambios en la BD.", 
                    Alert.AlertType.WARNING);
        }
    }
}