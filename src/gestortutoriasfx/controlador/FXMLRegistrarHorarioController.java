package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.PeriodoEscolarImplementacion;
import gestortutoriasfx.dominio.SalonImplementacion;
import gestortutoriasfx.dominio.SesionTutoriaImplementacion;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.Estudiante;
import gestortutoriasfx.modelo.pojo.FechaTutoria;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import gestortutoriasfx.modelo.pojo.Salon;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
import gestortutoriasfx.utilidad.Utilidades;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Nombre de la Clase: FXMLRegistrarHorarioController
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 26/11/2025
 *
 * Descripción:
 * Se encarga de controlar los distintos elementos del formulario para registrar el horario de la tutoria.
 */

public class FXMLRegistrarHorarioController implements Initializable {
    @FXML
    private Label lbError;
    @FXML
    private Label lbPeriodoActual;
    @FXML
    private TextField tfHoraInicio;
    @FXML
    private Button btnGenerar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ComboBox cbNumeroSesion;
    @FXML
    private ComboBox<String> cbAmPm;
    @FXML
    private ComboBox cbSalon;
    @FXML
    private ComboBox cbModalidad;
    
    private PeriodoEscolar periodoActual;
    private ArrayList<FechaTutoria> rangosFechas;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPeriodo();
        if (periodoActual != null) {
            cargarCatalogos();
            cargarSesionesDisponibles(); 
            configurarFormularioBase();
        }
    }
    
    @FXML
    private void clicCancelar(ActionEvent event) {
        boolean cancelar = Utilidades.mostrarAlertaVerificacion("Cancelar Registro de Horario", 
                "¿Estás seguro de que quieres cancelar el registro del horario?", 
                "Si cancelas, la información no se guardará.");
        if(cancelar){
            limpiarPanelCentral();
        }
    }

    @FXML
    private void clicGenerarHorarios(ActionEvent event) {
        if (validarCampos()) {
            generarHorario();
        }
    }
    
    private void bloquearFormulario(String mensaje) {
        lbError.setText(mensaje);
        lbError.setStyle("-fx-text-fill: orange;");
        btnGenerar.setDisable(true);
        btnCancelar.setDisable(true);
        cbNumeroSesion.setDisable(true);
        tfHoraInicio.setDisable(true);
        cbAmPm.setDisable(true);
        cbSalon.setDisable(true);
        cbModalidad.setDisable(true);
    }
    
    private void cargarCatalogos() {
        ObservableList<String> modalidades = FXCollections.observableArrayList("Presencial", "Virtual");
        cbModalidad.setItems(modalidades);

        HashMap<String, Object> respSalones = SalonImplementacion.obtenerTodosSalones();
        if (!(boolean) respSalones.get("error")) {
            ArrayList<Salon> lista = (ArrayList<Salon>) respSalones.get("salones");
            cbSalon.setItems(FXCollections.observableArrayList(lista));
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron cargar los salones.", Alert.AlertType.ERROR);
        }
    }
    
    private void cargarPeriodo() {
        HashMap<String, Object> respuesta = PeriodoEscolarImplementacion.obtenerPeriodoActual();
        if (!(boolean) respuesta.get("error")) {
            periodoActual = (PeriodoEscolar) respuesta.get("periodo");
            lbPeriodoActual.setText(periodoActual.getNombrePeriodoEscolar());
        } else {
            lbPeriodoActual.setText("Sin Periodo Activo");
            bloquearFormulario("No hay un periodo escolar activo configurado.");
        }
    }
    
    private void cargarSesionesDisponibles() {
        HashMap<String, Object> respFechas = SesionTutoriaImplementacion.obtenerFechasPorPeriodo(periodoActual.getIdPeriodoEscolar());
        HashMap<String, Object> respOcupadas = SesionTutoriaImplementacion.obtenerSesionesOcupadas(periodoActual.getIdPeriodoEscolar());

        if (!(boolean) respFechas.get("error") && !(boolean) respOcupadas.get("error")) {
            rangosFechas = (ArrayList<FechaTutoria>) respFechas.get("fechas");
            ArrayList<Integer> sesionesHechas = (ArrayList<Integer>) respOcupadas.get("ocupadas");
            ObservableList<FechaTutoria> opcionesCombo = FXCollections.observableArrayList();
            
            for (FechaTutoria fechaTutoria : rangosFechas) {
                LocalDate cierre = LocalDate.parse(fechaTutoria.getFechaCierre());
                if (!sesionesHechas.contains(fechaTutoria.getNumSesion()) && !cierre.isBefore(LocalDate.now())) {
                    opcionesCombo.add(fechaTutoria);
                }
            }

            cbNumeroSesion.setItems(opcionesCombo);

            if (opcionesCombo.isEmpty()) {
                bloquearFormulario("Ya has agendado todas las sesiones disponibles para este periodo.");
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error de Conexión", 
                    "No se pudieron cargar las sesiones disponibles.", Alert.AlertType.ERROR);
        }
    }
    
    private void configurarFormularioBase() {
        ObservableList<String> amPmList = FXCollections.observableArrayList("AM", "PM");
        cbAmPm.setItems(amPmList);
        cbAmPm.getSelectionModel().selectFirst();
        lbError.setText("");
    }
    
    private void generarHorario() {
        FechaTutoria fechaSeleccionada = (FechaTutoria) cbNumeroSesion.getValue();
        Salon salonSeleccionado = (Salon) cbSalon.getValue();
        String modalidadSeleccionada = (String) cbModalidad.getValue();
        int numSesion = fechaSeleccionada.getNumSesion();
        String fechaBaseBD = fechaSeleccionada.getFechaInicio();
        
        ArrayList<Estudiante> listaEstudiantes = obtenerEstudiantes();
        
        if (listaEstudiantes == null) return;

        ArrayList<SesionTutoria> horariosParaGuardar = new ArrayList<>();
        
        String horaString = tfHoraInicio.getText() + " " + cbAmPm.getValue();
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime horaActual = LocalTime.parse(horaString, formatoEntrada);
        
        for (Estudiante estudiante : listaEstudiantes) {
            SesionTutoria sesion = new SesionTutoria();
            sesion.setIdPeriodoEscolar(periodoActual.getIdPeriodoEscolar());
            sesion.setIdTutor(Sesion.getIdTutor());
            sesion.setMatriculaEstudiante(estudiante.getMatricula());
            sesion.setNumSesion(numSesion);
            sesion.setFecha(fechaBaseBD);
            sesion.setHoraInicio(horaActual.format(formatoSalida));
            LocalTime horaFin = horaActual.plusMinutes(20);
            sesion.setHoraFin(horaFin.format(formatoSalida));
            sesion.setEstado("Programada");
            sesion.setIdSalon(salonSeleccionado.getIdSalon());
            sesion.setModalidad(modalidadSeleccionada);
            horariosParaGuardar.add(sesion);
            horaActual = horaFin;
        }
        
        cargarHorario(horariosParaGuardar);
    }

    private void cargarHorario(List<SesionTutoria> lista) {
        HashMap<String, Object> respuesta = SesionTutoriaImplementacion.cargarHorarioGenerado((ArrayList<SesionTutoria>) lista);        
        if (!(boolean) respuesta.get("error")) {
            Utilidades.mostrarAlertaSimple("Éxito", "Se genero la asignacion de horarios correctamente.", 
                    Alert.AlertType.INFORMATION);
            limpiarPanelCentral();
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron guardar los horarios en la BD.", 
                    Alert.AlertType.WARNING);
        }
    }
    
    private void limpiarPanelCentral() {
        try {
            javafx.scene.layout.Pane panel = (javafx.scene.layout.Pane) btnGenerar.getScene().lookup("#panelContenido");
            if (panel != null) {
                panel.getChildren().clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private ArrayList<Estudiante> obtenerEstudiantes(){
        HashMap<String, Object> respuesta = SesionTutoriaImplementacion.obtenerEstudiantesDelTutor(Sesion.getIdTutor());
        
        if (!(boolean) respuesta.get("error")) {
            ArrayList<Estudiante> lista = (ArrayList<Estudiante>) respuesta.get("estudiantes");
            if(lista.isEmpty()) {
                Utilidades.mostrarAlertaSimple("Sin Alumnos", 
                        "No tiene estudiantes asignados.", Alert.AlertType.WARNING);
                return null;
            }
            lista.sort(Comparator.comparingInt(Estudiante::getSemestre).thenComparing(Estudiante::getApellidoPaterno));
            return lista;
        } else {
            Utilidades.mostrarAlertaSimple("Error", 
                    respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
            return null;
        }
    }
    
    private boolean validarCampos() {
        boolean valido = true;
        StringBuilder msj = new StringBuilder();
        
        if (cbNumeroSesion.getValue() == null) {
            valido = false; msj.append("• Seleccione una sesión.\n");
        }
        
        if (tfHoraInicio.getText().isEmpty()) {
            valido = false; msj.append("• Ingrese hora de inicio.\n");
        } else {
            if (!tfHoraInicio.getText().matches("^(0?[1-9]|1[0-2]):[0-5][0-9]$")) {
                valido = false; msj.append("• Formato de hora inválido (HH:MM).\n");
            }
        }
        if (cbSalon.getValue() == null) {
            valido = false; msj.append("• Seleccione un salón.\n");
        }
        if (cbModalidad.getValue() == null) {
            valido = false; msj.append("• Seleccione una modalidad.\n");
        }
        
        if (!valido) Utilidades.mostrarAlertaSimple("Datos Inválidos", msj.toString(), Alert.AlertType.WARNING);
        return valido;
    }
}