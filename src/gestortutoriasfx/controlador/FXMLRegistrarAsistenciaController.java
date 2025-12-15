package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.PeriodoEscolarImplementacion;
import gestortutoriasfx.dominio.SesionTutoriaImplementacion;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.FechaTutoria;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
import gestortutoriasfx.utilidad.Utilidades;
import gestortutoriasfx.utilidades.ArrastrarSoltarUtilidad;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
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
    private ComboBox<FechaTutoria> cbSesion;
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
    private ObservableList<FechaTutoria> sesionesDisponibles;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarListasVisuales();
        configurarArrastreBidireccional();
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
    
    private void actualizarComboSesiones() {
        if (sesionesDisponibles.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Sin Agenda", "No tienes sesiones agendadas.", Alert.AlertType.WARNING);
        }
        cbSesion.setItems(sesionesDisponibles);
    }
    
    private boolean clasificarAlumnosEnListas(ArrayList<SesionTutoria> listaBD) {
        listaFaltas.clear();
        listaAsistencias.clear();
        boolean registroPrevio = false;

        for (SesionTutoria sesion : listaBD) {
            if ("Asistio".equalsIgnoreCase(sesion.getEstado())) {
                listaAsistencias.add(sesion);
                registroPrevio = true;
            } else if ("No Asistio".equalsIgnoreCase(sesion.getEstado())) {
                listaFaltas.add(sesion);
                registroPrevio = true;
            } else {
                listaFaltas.add(sesion);
            }
        }
        return registroPrevio;
    }
    
    private void cargarAlumnos(int numSesion) {
        ArrayList<SesionTutoria> listaBD = obtenerListaAsistenciaDeBD(numSesion);
        
        if (listaBD != null) {
            boolean yaFueRegistrada = clasificarAlumnosEnListas(listaBD);
            configurarModoEdicion(yaFueRegistrada);
        }
    }
    
    private void cargarSesionesDisponibles() {
        PeriodoEscolar periodo = obtenerPeriodoActual();
        if (periodo == null) return;

        ArrayList<FechaTutoria> todasLasFechas = obtenerTodasLasFechas(periodo.getIdPeriodoEscolar());
        ArrayList<Integer> ocupadas = obtenerSesionesOcupadas(periodo.getIdPeriodoEscolar());

        if (todasLasFechas != null && ocupadas != null) {
            obtenerSesionesUsuario(todasLasFechas, ocupadas);
            actualizarComboSesiones();
        }
    }
    
    private void configurarArrastreBidireccional() {
        // De Faltas a Asistencias
        ArrastrarSoltarUtilidad.configurarIntercambioListas(
            lvFaltas, 
            lvAsistencias, 
            sesion -> String.valueOf(sesion.getIdSesion()),
            null
        );

        // De Asistencias a Faltas
        ArrastrarSoltarUtilidad.configurarIntercambioListas(
            lvAsistencias, 
            lvFaltas, 
            sesion -> String.valueOf(sesion.getIdSesion()), 
            null
        );
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
    
    private void configurarListenerSesion() {
        cbSesion.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    FechaTutoria fechaSeleccionada = (FechaTutoria) newValue;
                    cargarAlumnos(fechaSeleccionada.getNumSesion());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private boolean guardarListaAsistencia(ArrayList<SesionTutoria> lista) {
        HashMap<String, Object> respuesta = SesionTutoriaImplementacion.guardarAsistencias(lista);
        return !(boolean) respuesta.get("error");
    }
    
    private void inicializarListasVisuales() {
        listaFaltas = FXCollections.observableArrayList();
        listaAsistencias = FXCollections.observableArrayList();
        
        lvFaltas.setItems(listaFaltas);
        lvAsistencias.setItems(listaAsistencias);
    }
    
    private void limpiarPanelCentral() {
        try {
            Pane panel = (Pane) btnGuardar.getScene().lookup("#panelContenido");
            if(panel != null) panel.getChildren().clear();
        } catch (Exception e) {}
    }
    
    private ArrayList<SesionTutoria> obtenerListaAsistenciaDeBD(int numSesion) {
        HashMap<String, Object> respuesta = SesionTutoriaImplementacion.obtenerListaAsistencia(Sesion.getIdTutor(), numSesion);
        
        if (!(boolean) respuesta.get("error")) {
            return (ArrayList<SesionTutoria>) respuesta.get("lista");
        } else {
            Utilidades.mostrarAlertaSimple("Error", respuesta.get("mensaje").toString(), Alert.AlertType.WARNING);
            return null;
        }
    }
    
    private PeriodoEscolar obtenerPeriodoActual() {
        HashMap<String, Object> respuesta = PeriodoEscolarImplementacion.obtenerPeriodoActual();
        
        if (!(boolean) respuesta.get("error")) {
            return (PeriodoEscolar) respuesta.get("periodo");
        }
        Utilidades.mostrarAlertaSimple("Error", 
                "No se pudo recuperar el periodo actual.", Alert.AlertType.ERROR);
        return null;
    }

    private ArrayList<Integer> obtenerSesionesOcupadas(int idPeriodo) {
        HashMap<String, Object> resp = SesionTutoriaImplementacion.obtenerSesionesOcupadas(idPeriodo);
        
        return (boolean) resp.get("error") ? null : (ArrayList<Integer>) resp.get("ocupadas");
    }
    
    private void obtenerSesionesUsuario(ArrayList<FechaTutoria> fechas, ArrayList<Integer> ocupadas) {
        sesionesDisponibles = FXCollections.observableArrayList();
        for (FechaTutoria fechaTutoria : fechas) {
            if (ocupadas.contains(fechaTutoria.getNumSesion())) {
                sesionesDisponibles.add(fechaTutoria);
            }
        }
    }
    
    private ArrayList<FechaTutoria> obtenerTodasLasFechas(int idPeriodo) {
        HashMap<String, Object> resp = SesionTutoriaImplementacion.obtenerFechasPorPeriodo(idPeriodo);
        
        return (boolean) resp.get("error") ? null : (ArrayList<FechaTutoria>) resp.get("fechas");
    }
    
    private void procesarResultadoGuardado(boolean exito) {
        if (exito) {
            Utilidades.mostrarAlertaSimple("Éxito", 
                "Listas de asistencia guardadas correctamente.", Alert.AlertType.INFORMATION);
            limpiarPanelCentral();
        } else {
            Utilidades.mostrarAlertaSimple("Error", 
                "No se pudieron guardar los cambios en la BD.", Alert.AlertType.WARNING);
        }
    }
    
    private void registrarAsistencia(ArrayList<SesionTutoria> lista) {
        boolean exito = guardarListaAsistencia(lista);
        procesarResultadoGuardado(exito);
    }
}