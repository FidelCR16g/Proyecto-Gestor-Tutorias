package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.PeriodoEscolarImplementacion;
import gestortutoriasfx.dominio.ReporteTutoriaImplementacion;
import gestortutoriasfx.dominio.SesionTutoriaImplementacion;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.Estudiante;
import gestortutoriasfx.modelo.pojo.FechaTutoria;
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
    private FechaTutoria sesionSeleccionada;
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
        if (!sonCamposProblematicaValidos()) return;
        try {
            int cantidad = Integer.parseInt(tfNumAlumnos.getText());
            ProblematicaAcademica prob = new ProblematicaAcademica(
                    tfEE.getText(),
                    tfProfesor.getText(),
                    tfProblematica.getText(),
                    cantidad
            );
            listaProblematicas.add(prob);
            limpiarCamposProblematica();
        } catch (NumberFormatException e) {
            Utilidades.mostrarAlertaSimple("Error", "La cantidad debe ser numérica.", Alert.AlertType.WARNING);
        }
    }


    @FXML
    private void clicQuitarProblematica(ActionEvent event) {
        ProblematicaAcademica seleccion = (ProblematicaAcademica) tvProblematicas.getSelectionModel().getSelectedItem();
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
        if (Utilidades.mostrarAlertaVerificacion(
                "Salir",
                "¿Desea salir?",
                "Los cambios no guardados se perderán.")) {
            regresarPantallaAnterior();
        }
    }

    public void inicializarDatos(FechaTutoria sesion, ReporteTutoria reporteRecibido, boolean esSoloLectura) {
        this.sesionSeleccionada = sesion;
        this.reporteEdicion = reporteRecibido;

        lbSesion.setText("Sesión " + sesion.getNumSesion());
        cargarDatosReporte(sesion);

        if (reporteRecibido != null) {
            taComentarios.setText(reporteEdicion.getComentarios());
        }

        boolean estaEnviado = reporteEdicion != null && "Enviado".equalsIgnoreCase(reporteEdicion.getEstatus());
        
        if (esSoloLectura || estaEnviado) {
            bloquearEdicion(true);
            if (estaEnviado) {
                 Utilidades.mostrarAlertaSimple("Solo Lectura", "El reporte ya fue enviado.", Alert.AlertType.INFORMATION);
            }
        } else {
            bloquearEdicion(false);
        }
    }


    private void cargarDatosIniciales() {
        HashMap<String, Object> respuestaPeriodo = PeriodoEscolarImplementacion.obtenerPeriodoActual();

        if ((boolean) respuestaPeriodo.get("error")) {
            Utilidades.mostrarAlertaSimple("Error", "No hay periodo activo.", Alert.AlertType.WARNING);
            return;
        }

        periodoActual = (PeriodoEscolar) respuestaPeriodo.get("periodo");
        lbPeriodo.setText(periodoActual.getNombrePeriodoEscolar());
        cargarProgramaEducativoTutor();
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

    private void cargarDatosReporte(FechaTutoria sesion) {
        listaProblematicas.clear();
        taComentarios.clear();
        lbFechaTutoria.setText(sesion.getFechaInicio() + " al " + sesion.getFechaCierre());
        calcularEstadisticas(sesion.getNumSesion());
        
        HashMap<String, Object> respuestaReporte = ReporteTutoriaImplementacion.obtenerReporteActual(
                Sesion.getIdTutor(),
                periodoActual.getIdPeriodoEscolar(),
                sesion.getNumSesion()
        );

        if ((boolean) respuestaReporte.get("error")) return;

        ReporteTutoria reporte = (ReporteTutoria) respuestaReporte.get("reporte");

        if (reporte != null) {
            this.reporteEdicion = reporte;
            taComentarios.setText(reporte.getComentarios());

            List<ProblematicaAcademica> problematicas = (List<ProblematicaAcademica>) respuestaReporte.get("problematicas");

            if (problematicas != null) {
                listaProblematicas.addAll(problematicas);
            }

            if (reporte.getEstatus() == "Enviado") {
                bloquearEdicion(true);
            }
        }
    }


    private void calcularEstadisticas(int numSesion) {
        int idTutor = Sesion.getIdTutor();
        asistenciasCalculadas = 0;

        HashMap<String, Object> respuestaAsistencia = SesionTutoriaImplementacion.obtenerListaAsistencia(idTutor, numSesion);
        if (!(boolean) respuestaAsistencia.get("error")) {
            ArrayList<SesionTutoria> lista = (ArrayList<SesionTutoria>) respuestaAsistencia.get("lista");
            if (lista != null) {
                asistenciasCalculadas = (int) lista.stream()
                        .filter(s -> "Asistio".equalsIgnoreCase(s.getEstado()))
                        .count();
            }
        }

        lbTotalAsistencias.setText(String.valueOf(asistenciasCalculadas));
        riesgoCalculado = 0;

        HashMap<String, Object> respuestaEstudiante = SesionTutoriaImplementacion.obtenerEstudiantesDelTutor(idTutor);
        if (!(boolean) respuestaEstudiante.get("error")) {
            List<Estudiante> estudiantes = (List<Estudiante>) respuestaEstudiante.get("estudiantes");
            if (estudiantes != null) {
                riesgoCalculado = (int) estudiantes.stream()
                        .filter(Estudiante::isSituacionRiesgo)
                        .count();
            }
        }
        lbTotalRiesgo.setText(String.valueOf(riesgoCalculado));
    }

    private void configurarTabla() {
        tvProblematicas.setItems(listaProblematicas);
        tcEE.setCellValueFactory(new PropertyValueFactory("nombreNRC"));
        tcProfesor.setCellValueFactory(new PropertyValueFactory("nombreProfesor"));
        tcProblematica.setCellValueFactory(new PropertyValueFactory("problema"));
        tcNumAlumnos.setCellValueFactory(new PropertyValueFactory("numEstudiantes"));
        tvProblematicas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
    
    private void limpiarCamposProblematica() {
        tfEE.clear();
        tfProfesor.clear();
        tfProblematica.clear();
        tfNumAlumnos.clear();
    }

    private void procesarGuardado(String estatus) {
        ReporteTutoria reporte = obtenerReporteDesdeFormulario();
        reporte.setEstatus(estatus);
        if (reporteEdicion != null) {
            reporte.setIdReporteTutoria(reporteEdicion.getIdReporteTutoria());
        }
        HashMap<String, Object> respuestaGuardado = ReporteTutoriaImplementacion.guardarReporteCompleto(
                reporte,
                new ArrayList<>(listaProblematicas)
        );

        if ((boolean) respuestaGuardado.get("error")) {
            Utilidades.mostrarAlertaSimple("Error", respuestaGuardado.get("mensaje").toString(), 
                    Alert.AlertType.WARNING);
            return;
        }

        Utilidades.mostrarAlertaSimple("Éxito", (String) respuestaGuardado.get("mensaje"), Alert.AlertType.INFORMATION);
        regresarPantallaAnterior();
    }


    private ReporteTutoria obtenerReporteDesdeFormulario() {
        if (periodoActual == null || sesionSeleccionada == null) {
            Utilidades.mostrarAlertaSimple("Error", "Faltan datos de Período o Sesión.", Alert.AlertType.WARNING);
            throw new IllegalStateException("El período o la sesión no están cargados.");
        }
        ReporteTutoria reporte = new ReporteTutoria();
        reporte.setIdTutor(Sesion.getIdTutor());
        reporte.setIdPeriodoEscolar(periodoActual.getIdPeriodoEscolar());
        reporte.setIdProgramaEducativo(idProgramaEducativoDetectado);
        reporte.setNumSesion(sesionSeleccionada.getNumSesion());
        reporte.setFechaPrimeraTutoria(sesionSeleccionada.getFechaInicio());
        reporte.setFechaUltimaTutoria(sesionSeleccionada.getFechaCierre());
        reporte.setNumAlumnosAsistieron(asistenciasCalculadas);
        reporte.setNumAlumnosRiesgo(riesgoCalculado);
        reporte.setComentarios(taComentarios.getText());
        reporte.setEstadoLugar("Sin observaciones");
        return reporte;
    }

    private boolean sonCamposProblematicaValidos() {
        if(tfEE.getText().isEmpty()){
            return false;
        }
        if(tfProfesor.getText().isEmpty()){
            return false;
        }
        if(tfProblematica.getText().isEmpty()){
            return false;
        }
        if(tfNumAlumnos.getText().isEmpty()){
            return false;
        }
        return true;
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
            Utilidades.mostrarAlertaSimple("Error de Navegación", "No se pudo regresar al listado.", 
                    Alert.AlertType.WARNING);
        }
    }
}