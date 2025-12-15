package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.PeriodoEscolarImplementacion;
import gestortutoriasfx.dominio.ReporteTutoriaImplementacion;
import gestortutoriasfx.dominio.SesionTutoriaImplementacion;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.Estudiante;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import gestortutoriasfx.modelo.pojo.ProblematicaAcademica;
import gestortutoriasfx.modelo.pojo.ReporteTutoria;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
import gestortutoriasfx.utilidad.Utilidades;
import java.io.IOException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Nombre de la Clase: FXMLFormularioReporteTutoriaController
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 05/11/2025
 *
 * Descripción:
 * Se encarga de controlar el formualario del reporte del tutoria
 */

public class FXMLFormularioReporteTutoriaController implements Initializable {
    @FXML 
    private Label lbPeriodo;
    @FXML 
    private Label lbProgramaEducativo;
    @FXML 
    private Label lbFechaTutoria;
    @FXML 
    private Label lbTotalAsistencias;
    @FXML 
    private Label lbTotalRiesgo;
    @FXML 
    private Label lbSesion;
    @FXML 
    private TableView tvProblematicas;
    @FXML 
    private TableColumn tcEE;
    @FXML 
    private TableColumn tcProfesor;
    @FXML 
    private TableColumn tcProblematica;
    @FXML 
    private TableColumn tcNumAlumnos;
    @FXML 
    private TextField tfEE;
    @FXML 
    private TextField tfProfesor;
    @FXML 
    private TextField tfProblematica;
    @FXML 
    private TextField tfNumAlumnos;
    @FXML 
    private TextArea taComentarios;
    @FXML 
    private Button btnGuardar;
    @FXML 
    private Button btnQuitarSeleccionada;

    private PeriodoEscolar periodoActual;
    private SesionTutoria sesionSeleccionada;
    private ReporteTutoria reporteEdicion;
    
    private final ObservableList<ProblematicaAcademica> listaProblematicas = FXCollections.observableArrayList();
    private int asistenciasCalculadas = 0;
    private int riesgoCalculado = 0;
    private int idProgramaEducativoDetectado = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatosIniciales();
    }
    
    @FXML
    private void clicAgregarProblematica(ActionEvent event) {
        if (!validarCampos()) return;
        
        try {
            int cantidad = Integer.parseInt(tfNumAlumnos.getText());
            ProblematicaAcademica problematicaAcademica = new ProblematicaAcademica(
                    tfEE.getText(), tfProfesor.getText(), tfProblematica.getText(), cantidad
            );
            listaProblematicas.add(problematicaAcademica);
            limpiarCamposProblematica();
        } catch (NumberFormatException e) {
            Utilidades.mostrarAlertaSimple("Error", "La cantidad debe ser numérica.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicQuitarProblematica(ActionEvent event) {
        ProblematicaAcademica seleccion = 
                (ProblematicaAcademica) tvProblematicas.getSelectionModel().getSelectedItem();
        
        if (seleccion != null) {
            listaProblematicas.remove(seleccion);
        }
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        procesarGuardado("En Llenado");
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        if (Utilidades.mostrarAlertaVerificacion("Salir", 
                "¿Desea salir?", "Los cambios no guardados se perderán.")) {
            regresarPantallaAnterior();
        }
    }
    
    private void asignarContexto(SesionTutoria sesion, ReporteTutoria reporte) {
        this.sesionSeleccionada = sesion;
        this.reporteEdicion = reporte;
    }
    
    private void asegurarCargaDePeriodo() {
        if (this.periodoActual == null) {
            cargarDatosIniciales();
        }
    }

    private void actualizarEncabezado() {
        lbSesion.setText("Sesión " + sesionSeleccionada.getNumSesion());
        
        String fecha = (sesionSeleccionada.getFecha() != null) ? sesionSeleccionada.getFecha() 
                : "Sin fecha registrada";
        lbFechaTutoria.setText(fecha);
    }
    
    private void bloquearEdicion(boolean bloquear) {
        btnGuardar.setDisable(bloquear);
        btnQuitarSeleccionada.setDisable(bloquear);
        taComentarios.setEditable(!bloquear);
        tfEE.setDisable(bloquear);
        tfProfesor.setDisable(bloquear);
        tfProblematica.setDisable(bloquear);
        tfNumAlumnos.setDisable(bloquear);
        tvProblematicas.setMouseTransparent(bloquear);
    }
    
    private void calcularEstadisticas(int numSesion) {
        int idTutor = Sesion.getIdTutor();

        this.asistenciasCalculadas = obtenerConteoAsistencias(idTutor, numSesion);
        this.riesgoCalculado = obtenerConteoRiesgo(idTutor);

        lbTotalAsistencias.setText(String.valueOf(asistenciasCalculadas));
        lbTotalRiesgo.setText(String.valueOf(riesgoCalculado));
    }

    private void cargarDatosIniciales() {
        cargarPeriodoEscolar();
        cargarProgramaEducativoTutor();
    }
    
    private void cargarDatosReporte() {
        limpiarCamposVisuales();
        
        calcularEstadisticas(sesionSeleccionada.getNumSesion());
        
        HashMap<String, Object> respuesta = ReporteTutoriaImplementacion.obtenerReporteActual(
                Sesion.getIdTutor(),
                periodoActual.getIdPeriodoEscolar(),
                sesionSeleccionada.getNumSesion()
        );

        if (!(boolean) respuesta.get("error")) {
            procesarDatosRecuperados(respuesta);
        }
    }
    
    private void cargarPeriodoEscolar() {
        HashMap<String, Object> respuestaPeriodo = PeriodoEscolarImplementacion.obtenerPeriodoActual();
        
        if ((boolean) respuestaPeriodo.get("error")) {
            Utilidades.mostrarAlertaSimple("Error", "No hay periodo activo.", Alert.AlertType.WARNING);
            return;
        }
        
        periodoActual = (PeriodoEscolar) respuestaPeriodo.get("periodo");
        lbPeriodo.setText(periodoActual.getNombrePeriodoEscolar());
    }

    private void cargarProgramaEducativoTutor() {
        HashMap<String, Object> respuestaEstudiante = SesionTutoriaImplementacion.obtenerEstudiantesDelTutor(Sesion.getIdTutor());
        
        if ((boolean) respuestaEstudiante.get("error")) {
            lbProgramaEducativo.setText("Sin datos");
            return;
        }
        
        ArrayList<Estudiante> lista = (ArrayList<Estudiante>) respuestaEstudiante.get("estudiantes");
        
        if (lista == null || lista.isEmpty()) {
            lbProgramaEducativo.setText("Sin estudiantes asignados");
            return;
        
        }
        
        Estudiante primerEstudiante = lista.get(0);
        lbProgramaEducativo.setText(primerEstudiante.getNombreProgramaEducativo());
        idProgramaEducativoDetectado = primerEstudiante.getIdProgramaEducativo();
    }

    private void configurarTabla() {
        tvProblematicas.setItems(listaProblematicas);
        tcEE.setCellValueFactory(new PropertyValueFactory("nombreNRC"));
        tcProfesor.setCellValueFactory(new PropertyValueFactory("nombreProfesor"));
        tcProblematica.setCellValueFactory(new PropertyValueFactory("problema"));
        tcNumAlumnos.setCellValueFactory(new PropertyValueFactory("numEstudiantes"));
        tvProblematicas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private ReporteTutoria construirObjetoReporte() {
        ReporteTutoria reporte = new ReporteTutoria();
        reporte.setIdTutor(Sesion.getIdTutor());
        reporte.setIdPeriodoEscolar(periodoActual.getIdPeriodoEscolar());
        reporte.setIdProgramaEducativo(idProgramaEducativoDetectado);
        reporte.setNumSesion(sesionSeleccionada.getNumSesion());
        
        String fechaReporte = determinarFechaParaReporte();
        reporte.setFechaPrimeraTutoria(fechaReporte);
        reporte.setFechaUltimaTutoria(fechaReporte);
        
        reporte.setNumAlumnosAsistieron(asistenciasCalculadas);
        reporte.setNumAlumnosRiesgo(riesgoCalculado);
        reporte.setComentarios(taComentarios.getText());
        
        reporte.setEstadoLugar("Sin observaciones"); 

        return reporte;
    }
    
    private HashMap<String, Object> ejecutarTransaccionGuardado(ReporteTutoria reporte) {
        ArrayList<ProblematicaAcademica> listaParaGuardar = new ArrayList<>(listaProblematicas);
        
        return ReporteTutoriaImplementacion.guardarReporteCompleto(
                reporte, 
                listaParaGuardar
        );
    }

    private String determinarFechaParaReporte() {
        if (sesionSeleccionada.getFecha() != null) {
            return sesionSeleccionada.getFecha();
        }
        
        return new java.util.Date().toString(); 
    }
    
    private void gestionarPermisosEdicion(boolean solicitudSoloLectura) {
        boolean estaEnviado = (reporteEdicion != null && "Enviado".equalsIgnoreCase(reporteEdicion.getEstatus()));
        boolean debeBloquear = solicitudSoloLectura || estaEnviado;

        bloquearEdicion(debeBloquear);

        if (estaEnviado) {
            Utilidades.mostrarAlertaSimple("Solo Lectura", 
                "El reporte ya fue enviado y no puede ser modificado.", 
                Alert.AlertType.INFORMATION);
        }
    }
    
    public void inicializarDatos(SesionTutoria sesion, ReporteTutoria reporteRecibido, boolean esSoloLectura) {
        asignarContexto(sesion, reporteRecibido);
        asegurarCargaDePeriodo();
        cargarDatosReporte();
        actualizarEncabezado();
        llenarCamposFormulario();
        gestionarPermisosEdicion(esSoloLectura);
    }
    
    private void limpiarCamposVisuales() {
        listaProblematicas.clear();
        taComentarios.clear();
    }
    
    private void limpiarCamposProblematica() {
        tfEE.clear();
        tfProfesor.clear();
        tfProblematica.clear();
        tfNumAlumnos.clear();
    }
    
    private void llenarCamposFormulario() {
        if (reporteEdicion != null) {
            taComentarios.setText(reporteEdicion.getComentarios());
        } else {
            taComentarios.clear();
        }
    }
    
    private void llenarFormulario(ReporteTutoria reporte, List<ProblematicaAcademica> problematicas) {
        taComentarios.setText(reporte.getComentarios());
        
        if (problematicas != null) {
            listaProblematicas.addAll(problematicas);
        }
    }
    
    private void manejarResultadoTransaccion(HashMap<String, Object> respuesta) {
        boolean huboError = (boolean) respuesta.get("error");
        String mensaje = (String) respuesta.get("mensaje");

        if (huboError) {
            Utilidades.mostrarAlertaSimple("Error al Guardar", mensaje, Alert.AlertType.WARNING);
        } else {
            Utilidades.mostrarAlertaSimple("Éxito", mensaje, Alert.AlertType.INFORMATION);
            regresarPantallaAnterior();
        }
    }
    
    private int obtenerConteoAsistencias(int idTutor, int numSesion) {
        HashMap<String, Object> respuestaListaAsistencia = SesionTutoriaImplementacion.obtenerListaAsistencia(idTutor, numSesion);
        
        if (!(boolean) respuestaListaAsistencia.get("error")) {
            ArrayList<SesionTutoria> lista = (ArrayList<SesionTutoria>) respuestaListaAsistencia.get("lista");
            if (lista != null) {
                return (int) lista.stream()
                        .filter(s -> "Asistio".equalsIgnoreCase(s.getEstado()))
                        .count();
            }
        }
        return 0;
    }
    
    private int obtenerConteoRiesgo(int idTutor) {
        HashMap<String, Object> respuestaEstudiantes = SesionTutoriaImplementacion.obtenerEstudiantesDelTutor(idTutor);
        
        if (!(boolean) respuestaEstudiantes.get("error")) {
            List<Estudiante> estudiantes = (List<Estudiante>) respuestaEstudiantes.get("estudiantes");
            if (estudiantes != null) {
                return (int) estudiantes.stream()
                        .filter(Estudiante::isSituacionRiesgo)
                        .count();
            }
        }
        return 0;
    }
    
    private ReporteTutoria obtenerReporteDesdeFormulario() {
        validarDatosRequeridos();
        return construirObjetoReporte();
    }
    
    private ReporteTutoria prepararReporteParaPersistencia(String estatus) {
        ReporteTutoria reporte = obtenerReporteDesdeFormulario(); 
        
        reporte.setEstatus(estatus);
        
        if (reporteEdicion != null) {
            reporte.setIdReporteTutoria(reporteEdicion.getIdReporteTutoria());
        }
        
        return reporte;
    }
    
    private void procesarDatosRecuperados(HashMap<String, Object> respuesta) {
        ReporteTutoria reporteRecuperado = (ReporteTutoria) respuesta.get("reporte");

        if (reporteRecuperado != null) {
            this.reporteEdicion = reporteRecuperado;
            
            List<ProblematicaAcademica> problematicas = 
                (List<ProblematicaAcademica>) respuesta.get("problematicas");

            llenarFormulario(reporteRecuperado, problematicas);
            
            verificarEstadoReporte(reporteRecuperado);
        }
    }
    
    private void procesarGuardado(String estatus) {
        try {
            ReporteTutoria reporteFinal = prepararReporteParaPersistencia(estatus);
            HashMap<String, Object> respuesta = ejecutarTransaccionGuardado(reporteFinal);
            manejarResultadoTransaccion(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error Crítico", "Ocurrió un error inesperado al guardar.", Alert.AlertType.ERROR);
        }
    }
    
    private void regresarPantallaAnterior() {
        try {
            FXMLLoader cargador = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLGestionarReporteDeTutoria.fxml");
            javafx.scene.Parent vistaListado = cargador.load();
            javafx.scene.layout.Pane panelPrincipal = (javafx.scene.layout.Pane) btnGuardar.getScene().lookup("#panelContenido");
            if (panelPrincipal != null) {
                panelPrincipal.getChildren().clear();
                panelPrincipal.getChildren().add(vistaListado);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de Navegación", "No se pudo regresar al listado.", Alert.AlertType.WARNING);
        }
    }
    
    private boolean validarCampos() {
        boolean valido = true;
        String mensajeError = "Se encontraron los siguientes errores: \n";
        
        if (tfEE.getText().isEmpty()) {
            valido = false; 
            mensajeError += ("Ingrese una Experiencia Educativa.\n");
        }
        
        if (tfProfesor.getText().isEmpty()) {
            valido = false;
            mensajeError += ("Ingrese un Profesor.\n");
        }
        if (tfProblematica.getText().isEmpty()) {
            valido = false;
            mensajeError += ("Ingrese la descripcion de la Problematica.\n");
        }
        if (tfNumAlumnos.getText().isEmpty()) {
            valido = false;
            mensajeError += ("Ingrese el numero de alumnos.\n");
        }
        
        if (!valido) Utilidades.mostrarAlertaSimple("Datos Inválidos", 
                mensajeError, Alert.AlertType.WARNING);
        return valido;
    }
    
    private void validarDatosRequeridos() {
        if (periodoActual == null || sesionSeleccionada == null) {
            Utilidades.mostrarAlertaSimple("Error", 
                "Faltan datos de Período o Sesión para generar el reporte.", 
                Alert.AlertType.WARNING);
            throw new IllegalStateException("Datos incompletos en el formulario.");
        }
    }
    
    private void verificarEstadoReporte(ReporteTutoria reporte) {
        if ("Enviado".equalsIgnoreCase(reporte.getEstatus())) {
            bloquearEdicion(true);
        }
    }
}