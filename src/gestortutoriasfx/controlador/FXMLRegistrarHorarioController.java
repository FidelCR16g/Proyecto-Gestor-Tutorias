package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.PeriodoEscolarImplementacion;
import gestortutoriasfx.dominio.SesionTutoriaImplementacion;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.Estudiante;
import gestortutoriasfx.modelo.pojo.FechaTutoria;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
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
    private DatePicker dpFechaInicio;
    @FXML
    private TextField tfHoraInicio;
    @FXML
    private Label lbTotalEstudiantes;
    @FXML
    private Button btnGenerar;
    @FXML
    private ComboBox cbNumeroSesion;
    
    private PeriodoEscolar periodoActual;
    private ArrayList<FechaTutoria> rangosFechas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPeriodo();
        configurarFormulario();
    }
    
    @FXML
    private void clicCancelar(ActionEvent event) {
        boolean cancelar = Utilidades.mostrarAlertaVerificacion("Cancelar Registro de Horario", 
                "¿Estás seguro de que quieres cancelar el registro del horario?", 
                "Si cancelas, la información no se guardará.");
        if(cancelar){
            resetearFormulario();
        }
    }

    @FXML
    private void clicGenerarHorarios(ActionEvent event) {
        lbError.setText("");
        if (validarCampos()) {
            generarHorario();
        }
    }
    
    private void cargarPeriodo() {
        HashMap<String, Object> respuesta = PeriodoEscolarImplementacion.obtenerPeriodoActual();
        if (!(boolean) respuesta.get("error")) {
            periodoActual = (PeriodoEscolar) respuesta.get("periodo");
            lbPeriodoActual.setText(periodoActual.getNombre());
        } else {
            lbPeriodoActual.setText("Sin Periodo");
            btnGenerar.setDisable(true);
        }
    }
    
    private void configurarFormulario() {
        HashMap<String, Object> respFechas = SesionTutoriaImplementacion.obtenerFechasPorPeriodo(periodoActual.getIdPeriodo());
        HashMap<String, Object> respOcupadas = SesionTutoriaImplementacion.obtenerSesionesOcupadas(periodoActual.getIdPeriodo());
        
        if(!(boolean)respFechas.get("error") && !(boolean)respOcupadas.get("error")){
            rangosFechas = (ArrayList<FechaTutoria>) respFechas.get("fechas");
            ArrayList<Integer> sesionesYaHechas = (ArrayList<Integer>) respOcupadas.get("ocupadas");
            ObservableList<FechaTutoria> opcionesCombo = FXCollections.observableArrayList();
            
            for(FechaTutoria ft : rangosFechas){
                LocalDate cierreSesion = LocalDate.parse(ft.getFechaCierre());
                if(!sesionesYaHechas.contains(ft.getNumSesion()) && !cierreSesion.isBefore(LocalDate.now())){
                    opcionesCombo.add(ft);
                }
            }
            cbNumeroSesion.setItems(opcionesCombo);
            if(opcionesCombo.isEmpty()){
                lbError.setText("Ya has agendado todas las sesiones de este periodo.");
                btnGenerar.setDisable(true);
            }
        }
        
        cbNumeroSesion.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null){
                configurarDatePicker((FechaTutoria) newVal);
            }
        });
        
        dpFechaInicio.setDisable(true);
    }

    private void configurarDatePicker(FechaTutoria rango) {
        dpFechaInicio.setDisable(false);
        dpFechaInicio.setValue(null);
        
        final LocalDate minDateBD = LocalDate.parse(rango.getFechaInicio());
        final LocalDate maxDateBD = LocalDate.parse(rango.getFechaCierre());
        final LocalDate hoy = LocalDate.now();

        dpFechaInicio.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                boolean fueraDeRangoBD = date.isBefore(minDateBD) || date.isAfter(maxDateBD);
                boolean esPasado = date.isBefore(hoy);
                
                if (empty || fueraDeRangoBD || esPasado) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffebee;");
                }
            }
        });
    }
    
    private void generarHorario() {
        FechaTutoria seleccion = (FechaTutoria) cbNumeroSesion.getValue();
        
        ArrayList<Estudiante> listaEstudiantes = obtenerEstudiantes();
        List<SesionTutoria> horariosParaGuardar = new ArrayList<>();
        
        String seleccionCombo = cbNumeroSesion.getSelectionModel().getSelectedItem().toString();
        int numSesion = seleccion.getNumSesion();
        
        LocalTime horaActual = LocalTime.parse(tfHoraInicio.getText());
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm"); 
        
        for (Estudiante estudiante : listaEstudiantes) {
            SesionTutoria sesion = new SesionTutoria();
            
            if (periodoActual != null) {
                sesion.setIdPeriodo(periodoActual.getIdPeriodo());
            }
            sesion.setIdTutor(Sesion.getIdTutor());
            sesion.setMatriculaEstudiante(estudiante.getMatricula());
            sesion.setNumSesion(numSesion);
            sesion.setEstado("Programada");
            
            sesion.setFecha(dpFechaInicio.getValue().toString());
            sesion.setHoraInicio(horaActual.format(formatoHora));
            
            LocalTime horaFin = horaActual.plusMinutes(20);
            sesion.setHoraFin(horaFin.format(formatoHora));
            
            horariosParaGuardar.add(sesion);
            
            horaActual = horaFin;
        }
        
        cargarHorario(horariosParaGuardar);
    }

    private void cargarHorario(List<SesionTutoria> lista) {
        boolean exito = SesionTutoriaImplementacion.cargarHorarioGenerado(lista);
        
        if (exito) {
            Utilidades.mostrarAlertaSimple("Éxito", "Se genero la asignacion de horarios correctamente.", Alert.AlertType.INFORMATION);
            limpiarFormulario();
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron guardar los horarios en la BD.", Alert.AlertType.ERROR);
        }
    }
    
    private void limpiarFormulario() {
        btnGenerar.setDisable(true);
        lbError.setText("Horarios generados exitosamente.");
        lbError.setStyle("-fx-text-fill: green;");
    }
    
    private ArrayList<Estudiante> obtenerEstudiantes(){
        HashMap<String, Object> respuesta = SesionTutoriaImplementacion.obtenerEstudiantesDelTutor(Sesion.getIdTutor());
        
        if (respuesta.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Sin Estudiantes", "No tiene estudiantes asignados para generar horarios.", Alert.AlertType.WARNING);
            return null;
        }
        
        ArrayList estudiantes = (ArrayList<Estudiante>) respuesta.get("estudiantes");
        
        if (estudiantes == null || estudiantes.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Sin Estudiantes", 
                    "No tiene estudiantes asignados para generar horarios.", 
                    Alert.AlertType.WARNING);
            return null;
        }
        
        estudiantes.sort(Comparator.comparingInt(Estudiante::getSemestre).thenComparing(Estudiante::getApellidoPaterno));
        return estudiantes;
    }
    
    private void resetearFormulario() {
        cbNumeroSesion.getSelectionModel().clearSelection();
        dpFechaInicio.setValue(null);
        tfHoraInicio.setText("");
        lbError.setText("");
        btnGenerar.setDisable(false);
    }
    
    private boolean validarCampos() {
        boolean valido = true;
        String mensajeError = "Se encontraron los siguientes errores: \n";
        if(cbNumeroSesion.getSelectionModel().isEmpty()){
            valido = false;
            mensajeError += "Debe seleccionar un número de sesión.\n";
        }
        if(dpFechaInicio.getValue() == null){
            valido = false;
            mensajeError += "La fecha de inicio es requerida.\n";
        }else{
            if(dpFechaInicio.getValue().isBefore(LocalDate.now())){
                valido = false;
                mensajeError += "La fecha no puede ser anterior al día de hoy.\n";
            }
        }
        if (tfHoraInicio.getText() == null || tfHoraInicio.getText().isEmpty()) {
            valido = false;
            mensajeError += "La hora de inicio es requerida.\n";
        } else {
            try {
                LocalTime.parse(tfHoraInicio.getText());
            } catch (Exception e) {
                valido = false;
                mensajeError += "Formato de hora inválido. Use HH:mm (Ej: 10:00).\n";
            }
        }
        if (!valido) {
            Utilidades.mostrarAlertaSimple("Datos Inválidos", 
                    "Por favor corrija los siguientes errores:\n" + mensajeError.toString(), 
                    Alert.AlertType.WARNING);
        }
        return valido;
    }
}