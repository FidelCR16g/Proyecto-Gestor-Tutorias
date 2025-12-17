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
    private Button btnGenerar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ComboBox cbNumeroSesion;
    @FXML
    private ComboBox<String> cbHora;
    @FXML
    private ComboBox<String> cbMinutos;
    @FXML
    private ComboBox cbSalon;
    @FXML
    private ComboBox cbModalidad;
    
    private PeriodoEscolar periodoActual;
    private ArrayList<FechaTutoria> rangosFechas;
    private static final DateTimeFormatter FORMATO_HORA_BD = DateTimeFormatter.ofPattern("HH:mm");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPeriodo();
        if (periodoActual != null) {
            cargarCatalogos();
            cargarSesionesDisponibles();
            configurarComboBoxesTiempo();
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
    
    private void actualizarComboSesiones(ObservableList<FechaTutoria> opciones) {
        cbNumeroSesion.setItems(opciones);
        
        if (opciones.isEmpty()) {
            bloquearFormulario("Ya has agendado todas las sesiones disponibles o vigentes para este periodo.");
        }
    }
    
    private void bloquearFormulario(String mensaje) {
        lbError.setText(mensaje);
        lbError.setStyle("-fx-text-fill: orange;");
        btnGenerar.setDisable(true);
        btnCancelar.setDisable(true);
        cbNumeroSesion.setDisable(true);
        cbHora.setDisable(true);
        cbMinutos.setDisable(true);
        cbSalon.setDisable(true);
        cbModalidad.setDisable(true);
    }
    
    private void configurarComboBoxesTiempo() {
        ObservableList<String> horas = FXCollections.observableArrayList();
        ObservableList<String> minutos = FXCollections.observableArrayList();
        
        for (int i = 7; i <= 18; i++) { 
            horas.add(String.format("%02d", i));
        }
        
        for (int i = 0; i < 60; i += 20) {
            minutos.add(String.format("%02d", i));
        }
        
        cbHora.setItems(horas);
        cbMinutos.setItems(minutos);
        
        cbHora.setValue("07");
        cbMinutos.setValue("00");
    }
    
    private ArrayList<SesionTutoria> calcularSecuenciaDeHorarios(
            ArrayList<Estudiante> estudiantes,
            FechaTutoria fechaConfig,
            Salon salon,
            String modalidad,
            LocalTime horaActual) {
        ArrayList<SesionTutoria> listaSesiones = new ArrayList<>();
        int duracionSesionMinutos = 20;
        for (Estudiante estudiante : estudiantes) {
            LocalTime horaFin = horaActual.plusMinutes(duracionSesionMinutos);

            SesionTutoria sesion = crearSesionIndividual(
                estudiante, fechaConfig, salon, modalidad, horaActual, horaFin
            );
            listaSesiones.add(sesion);
            horaActual = horaFin; 
        }
        return listaSesiones;
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

        if ((boolean) respFechas.get("error") || (boolean) respOcupadas.get("error")) {
            Utilidades.mostrarAlertaSimple("Error de Conexión", 
                    "No se pudieron cargar las sesiones disponibles.", Alert.AlertType.ERROR);
            return;
        }

        ArrayList<FechaTutoria> todasLasFechas = (ArrayList<FechaTutoria>) respFechas.get("fechas");
        ArrayList<Integer> sesionesOcupadas = (ArrayList<Integer>) respOcupadas.get("ocupadas");
        
        rangosFechas = todasLasFechas; 

        ObservableList<FechaTutoria> sesionesValidas = filtrarSesionesValidas(todasLasFechas, sesionesOcupadas);
        actualizarComboSesiones(sesionesValidas);
    }
    
    private SesionTutoria crearSesionIndividual(
            Estudiante estudiante,
            FechaTutoria fechaConfig,
            Salon salon,
            String modalidad,
            LocalTime inicio,
            LocalTime fin) {

        SesionTutoria sesion = new SesionTutoria();
        sesion.setIdPeriodoEscolar(periodoActual.getIdPeriodoEscolar());
        sesion.setIdTutor(Sesion.getIdTutor());
        sesion.setNumSesion(fechaConfig.getNumSesion());
        sesion.setFecha(fechaConfig.getFechaInicio());
        sesion.setIdSalon(salon.getIdSalon());
        sesion.setModalidad(modalidad);
        sesion.setEstado("Programada");
        sesion.setMatriculaEstudiante(estudiante.getMatricula());
        sesion.setHoraInicio(inicio.format(FORMATO_HORA_BD));
        sesion.setHoraFin(fin.format(FORMATO_HORA_BD));
        return sesion;
    }
    
    private boolean esFechaDisponible(FechaTutoria fecha, ArrayList<Integer> ocupadas, LocalDate hoy) {
        boolean yaFueAgendada = ocupadas.contains(fecha.getNumSesion());
        if (yaFueAgendada) return false;
        
        LocalDate fechaCierre = LocalDate.parse(fecha.getFechaCierre());
        boolean estaVencida = fechaCierre.isBefore(hoy);
        
        return !estaVencida;
    }
    
    private void generarHorario() {
        ArrayList<Estudiante> listaEstudiantes = obtenerEstudiantes();
        if (listaEstudiantes == null || listaEstudiantes.isEmpty()) return;

        FechaTutoria fechaConfig = (FechaTutoria) cbNumeroSesion.getValue();
        Salon salonSeleccionado = (Salon) cbSalon.getValue();
        String modalidad = (String) cbModalidad.getValue();
        LocalTime horaInicio = obtenerHoraInicioSeleccionada();

        ArrayList<SesionTutoria> horariosGenerados = calcularSecuenciaDeHorarios(
            listaEstudiantes, 
            fechaConfig, 
            salonSeleccionado, 
            modalidad, 
            horaInicio
        );

        registrarHorario(horariosGenerados);
    }
    
    private boolean guardarHorarios(List<SesionTutoria> lista) {
        HashMap<String, Object> respuesta = SesionTutoriaImplementacion.registrarSesion((ArrayList<SesionTutoria>) lista);        
        return !(boolean) respuesta.get("error");
    }
    
    private ObservableList<FechaTutoria> filtrarSesionesValidas(
            ArrayList<FechaTutoria> fechas, 
            ArrayList<Integer> ocupadas) {
        
        ObservableList<FechaTutoria> listaFiltrada = FXCollections.observableArrayList();
        LocalDate fechaHoy = LocalDate.now();

        for (FechaTutoria fecha : fechas) {
            if (esFechaDisponible(fecha, ocupadas, fechaHoy)) {
                listaFiltrada.add(fecha);
            }
        }
        return listaFiltrada;
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
        ArrayList<Estudiante> estudiantes = recuperarEstudiantes();
        
        if (estudiantes != null && !estudiantes.isEmpty()) {
            ordenarEstudiantes(estudiantes);
            return estudiantes;
        }
        
        if (estudiantes != null && estudiantes.isEmpty()) {
             Utilidades.mostrarAlertaSimple("Sin Alumnos", 
                "No tiene estudiantes asignados para generar horarios.", Alert.AlertType.WARNING);
        }
        
        return null;
    }
    
    private LocalTime obtenerHoraInicioSeleccionada() {
        String horaString = cbHora.getValue() + ":" + cbMinutos.getValue(); 
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("H:mm");
        return LocalTime.parse(horaString, formatoEntrada);
    }
    
    private void ordenarEstudiantes(ArrayList<Estudiante> lista) {
        lista.sort(Comparator.comparingInt(Estudiante::getSemestre)
             .thenComparing(Estudiante::getApellidoPaterno));
    }
    
    private void procesarResultadoRegistro(boolean exito) {
        if (exito) {
            Utilidades.mostrarAlertaSimple("Éxito", 
                "Se generó la asignación de horarios correctamente.", Alert.AlertType.INFORMATION);
            limpiarPanelCentral();
        } else {
            Utilidades.mostrarAlertaSimple("Error", 
                "No se pudieron guardar los horarios en la BD.", Alert.AlertType.WARNING);
        }
    }
    
    private ArrayList<Estudiante> recuperarEstudiantes() {
        HashMap<String, Object> respuesta = SesionTutoriaImplementacion.obtenerEstudiantesDelTutor(Sesion.getIdTutor());
        
        if (!(boolean) respuesta.get("error")) {
            return (ArrayList<Estudiante>) respuesta.get("estudiantes");
        } else {
            Utilidades.mostrarAlertaSimple("Error", 
                respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
            return null;
        }
    }
    
    private void registrarHorario(List<SesionTutoria> lista) {
        boolean exito = guardarHorarios(lista);
        procesarResultadoRegistro(exito);
    }
    
    private boolean validarCampos() {
        boolean valido = true;
        String mensajeError = "Se encontraron los siguientes errores: \n";
        
        if (cbNumeroSesion.getValue() == null) {
            valido = false; 
            mensajeError += "Seleccione una sesión.\n";
        }
        
        if (cbHora.getValue() == null) {
            valido = false; 
            mensajeError += "Ingrese hora de inicio.\n";
        }
        if (cbMinutos.getValue() == null) {
            valido = false; 
            mensajeError += "Ingrese minutos de inicio.\n";
        }
        if (cbSalon.getValue() == null) {
            valido = false; 
            mensajeError += ("Seleccione un salón.\n");
        }
        if (cbModalidad.getValue() == null) {
            valido = false; 
            mensajeError += ("Seleccione una modalidad.\n");
        }
        
        if (!valido) Utilidades.mostrarAlertaSimple("Datos Inválidos", 
                mensajeError, Alert.AlertType.WARNING);
        return valido;
    }
}