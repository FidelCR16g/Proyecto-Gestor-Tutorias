package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.PeriodoEscolarImplementacion;
import gestortutoriasfx.dominio.ProgramaEducativoImplementacion;
import gestortutoriasfx.dominio.ReporteGeneralImplementacion;
import gestortutoriasfx.dominio.ReporteTutoriaImplementacion;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import gestortutoriasfx.modelo.pojo.ProblemaAcademicoReportadoGeneral;
import gestortutoriasfx.modelo.pojo.ProgramaEducativo;
import gestortutoriasfx.modelo.pojo.ReporteGeneral;
import gestortutoriasfx.modelo.pojo.ReporteTutoria;
import gestortutoriasfx.modelo.pojo.TutorComentarioGeneral;
import gestortutoriasfx.utilidad.Utilidades;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;

public class FXMLFormularioReporteGeneralController implements Initializable {

    public enum Modo { CREAR, EDITAR, VER }

    @FXML private Label lbContexto;

    @FXML private ComboBox<ProgramaEducativo> cbPrograma;
    @FXML private ComboBox<PeriodoEscolar> cbPeriodo;
    @FXML private ComboBox<Integer> cbNumSesion;
    @FXML private DatePicker dpFecha;
    @FXML private TextField tfEstadoLugar;

    @FXML private TextArea taObjetivos;

    @FXML private TextField tfTotalRegistrados;
    @FXML private TextField tfTotalAsistieron;
    @FXML private TextField tfPorcentaje;

    @FXML private TableView<ProblemaAcademicoReportadoGeneral> tvProblemas;
    @FXML private TableColumn<ProblemaAcademicoReportadoGeneral, String> colEE;
    @FXML private TableColumn<ProblemaAcademicoReportadoGeneral, String> colProfesor;
    @FXML private TableColumn<ProblemaAcademicoReportadoGeneral, String> colProblema;
    @FXML private TableColumn<ProblemaAcademicoReportadoGeneral, Integer> colCantidad;

    @FXML private TableView<TutorComentarioGeneral> tvTutores;
    @FXML private TableColumn<TutorComentarioGeneral, String> colTutor;
    @FXML private TableColumn<TutorComentarioGeneral, String> colComentario;

    @FXML private TabPane tpLateral;
    @FXML private TextField tfBuscarNumSesionTutoria;
    @FXML private TableView<ReporteTutoria> tvTutorias;
    @FXML private TableColumn<ReporteTutoria, String> colTutoriaTutor;
    @FXML private TableColumn<ReporteTutoria, String> colTutoriaFechas;
    @FXML private TableColumn<ReporteTutoria, String> colTutoriaAsistieron;
    @FXML private TableColumn<ReporteTutoria, String> colTutoriaRiesgo;
    @FXML private TableColumn<ReporteTutoria, String> colTutoriaEstatus;

    @FXML private Button btnGuardar;
    @FXML private Button btnFinalizar;

    private StackPane panelContenido;
    private Parent vistaFormulario;

    private Runnable alRegresar;
    private Modo modo = Modo.CREAR;

    private ReporteGeneral reporteActual;

    private final ObservableList<ProblemaAcademicoReportadoGeneral> obsProblemas = FXCollections.observableArrayList();
    private final ObservableList<TutorComentarioGeneral> obsTutores = FXCollections.observableArrayList();
    private final ObservableList<ReporteTutoria> obsTutorias = FXCollections.observableArrayList();
    private ArrayList<ReporteTutoria> baseTutorias = new ArrayList<>();

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Guardar raíz del formulario (para volver desde MostrarReporteTutoria)
        try {
            Node n = tvProblemas;
            while (n != null && n.getParent() != null) n = n.getParent();
            vistaFormulario = (Parent) n;
        } catch (Exception e) {
            vistaFormulario = null;
        }

        if (lbContexto != null) {
            String nombre = (Sesion.getUsuarioSesion() != null && Sesion.getUsuarioSesion().getNombre() != null)
                    ? Sesion.getUsuarioSesion().getNombre()
                    : "";
            lbContexto.setText("Coordinador: " + nombre);
        }

        if (cbNumSesion != null) {
            cbNumSesion.setItems(FXCollections.observableArrayList(1, 2, 3, 4));
        }

        cargarProgramas();
        cargarPeriodoActual();

        configurarTablaProblemas();
        configurarTablaTutores();

        if (tvProblemas != null) tvProblemas.setItems(obsProblemas);
        if (tvTutores != null) tvTutores.setItems(obsTutores);

        if (tfPorcentaje != null) tfPorcentaje.setEditable(false);

        if (tfTotalRegistrados != null) {
            tfTotalRegistrados.textProperty().addListener((o, a, b) -> actualizarPorcentaje());
        }
        if (tfTotalAsistieron != null) {
            tfTotalAsistieron.textProperty().addListener((o, a, b) -> actualizarPorcentaje());
        }

        configurarTablaTutorias();
        if (tvTutorias != null) tvTutorias.setItems(obsTutorias);
        cargarTutoriasParaLateral();
    }

    public void setPanelContenido(StackPane panelContenido) {
        this.panelContenido = panelContenido;
    }

    public void inicializarFormulario(Modo modo, ReporteGeneral base, Runnable alRegresar) {
        this.modo = (modo != null) ? modo : Modo.CREAR;
        this.alRegresar = alRegresar;

        if (this.modo == Modo.CREAR) {
            this.reporteActual = new ReporteGeneral();
            this.reporteActual.setEstatus(ReporteGeneral.Estatus.Borrador);

            obsProblemas.clear();
            obsTutores.clear();

            if (dpFecha != null) dpFecha.setValue(LocalDate.now());
            if (tfEstadoLugar != null) tfEstadoLugar.clear();
            if (taObjetivos != null) taObjetivos.clear();
            if (tfTotalRegistrados != null) tfTotalRegistrados.setText("0");
            if (tfTotalAsistieron != null) tfTotalAsistieron.setText("0");
            actualizarPorcentaje();

            aplicarModo();
            return;
        }

        if (base == null || base.getIdReporteGeneral() <= 0) {
            Utilidades.mostrarAlertaSimple("Error", "No se recibió un reporte válido.", Alert.AlertType.ERROR);
            this.reporteActual = new ReporteGeneral();
            this.reporteActual.setEstatus(ReporteGeneral.Estatus.Borrador);
            aplicarModo();
            return;
        }

        HashMap<String, Object> resp = ReporteGeneralImplementacion.obtenerReporteGeneralCompleto(base.getIdReporteGeneral());
        if (resp == null || (boolean) resp.getOrDefault("error", true)) {
            String msg = (resp != null) ? (String) resp.getOrDefault("mensaje", "No se pudo cargar el reporte.") : "No se pudo cargar el reporte.";
            Utilidades.mostrarAlertaSimple("Error", msg, Alert.AlertType.ERROR);
            return;
        }

        this.reporteActual = (ReporteGeneral) resp.get("reporte");
        if (this.reporteActual == null) this.reporteActual = base;

        @SuppressWarnings("unchecked")
        ArrayList<ProblemaAcademicoReportadoGeneral> problemas = (ArrayList<ProblemaAcademicoReportadoGeneral>) resp.get("problemas");
        @SuppressWarnings("unchecked")
        ArrayList<TutorComentarioGeneral> tutores = (ArrayList<TutorComentarioGeneral>) resp.get("tutores");

        obsProblemas.setAll(problemas != null ? problemas : new ArrayList<>());
        obsTutores.setAll(tutores != null ? tutores : new ArrayList<>());

        precargarCamposDesdeReporte();
        aplicarModo();
    }

    private void aplicarModo() {
        if (reporteActual != null && reporteActual.getEstatus() == ReporteGeneral.Estatus.Finalizado) {
            modo = Modo.VER;
        }
        boolean soloLectura = (modo == Modo.VER);

        if (cbPrograma != null) cbPrograma.setDisable(soloLectura);
        if (cbPeriodo != null) cbPeriodo.setDisable(true);
        if (cbNumSesion != null) cbNumSesion.setDisable(soloLectura);
        if (dpFecha != null) dpFecha.setDisable(soloLectura);
        if (tfEstadoLugar != null) tfEstadoLugar.setDisable(soloLectura);
        if (taObjetivos != null) taObjetivos.setDisable(soloLectura);

        if (tfTotalRegistrados != null) tfTotalRegistrados.setDisable(soloLectura);
        if (tfTotalAsistieron != null) tfTotalAsistieron.setDisable(soloLectura);

        if (tvProblemas != null) tvProblemas.setEditable(!soloLectura);
        if (tvTutores != null) tvTutores.setEditable(!soloLectura);

        if (btnGuardar != null) btnGuardar.setDisable(soloLectura);
        if (btnFinalizar != null) btnFinalizar.setDisable(soloLectura);
    }

    private void precargarCamposDesdeReporte() {
        if (reporteActual == null) return;

        if (reporteActual.getIdProgramaEducativo() > 0 && cbPrograma != null && cbPrograma.getItems() != null) {
            for (ProgramaEducativo pe : cbPrograma.getItems()) {
                if (pe != null && pe.getIdProgramaEducativo() == reporteActual.getIdProgramaEducativo()) {
                    cbPrograma.getSelectionModel().select(pe);
                    break;
                }
            }
        }

        if (cbNumSesion != null) cbNumSesion.getSelectionModel().select(Integer.valueOf(reporteActual.getNumSesion()));
        if (dpFecha != null && reporteActual.getFecha() != null) dpFecha.setValue(reporteActual.getFecha());
        if (tfEstadoLugar != null) tfEstadoLugar.setText(reporteActual.getEstadoLugar() != null ? reporteActual.getEstadoLugar() : "");
        if (taObjetivos != null) taObjetivos.setText(reporteActual.getObjetivos() != null ? reporteActual.getObjetivos() : "");

        if (tfTotalRegistrados != null) tfTotalRegistrados.setText(String.valueOf(reporteActual.getTotalAlumnosRegistrados()));
        if (tfTotalAsistieron != null) tfTotalAsistieron.setText(String.valueOf(reporteActual.getTotalAlumnosAsistieron()));

        BigDecimal pct = reporteActual.getPorcentajeAsistencia();
        if (tfPorcentaje != null) tfPorcentaje.setText(pct != null ? pct.toString() : "");
        actualizarPorcentaje();
    }

    private void cargarProgramas() {
        if (cbPrograma == null) return;

        HashMap<String, Object> resp = ProgramaEducativoImplementacion.obtenerProgramas();
        if (resp == null || (boolean) resp.getOrDefault("error", true)) return;

        @SuppressWarnings("unchecked")
        ArrayList<ProgramaEducativo> lista = (ArrayList<ProgramaEducativo>) resp.get("programas");
        if (lista == null) lista = new ArrayList<>();

        cbPrograma.setItems(FXCollections.observableArrayList(lista));

        int idCoord = Sesion.getIdCoordinador();
        for (ProgramaEducativo pe : lista) {
            if (pe != null && pe.getIdCoordinador() == idCoord) {
                cbPrograma.getSelectionModel().select(pe);
                break;
            }
        }
    }

    private void cargarPeriodoActual() {
        if (cbPeriodo == null) return;

        HashMap<String, Object> resp = PeriodoEscolarImplementacion.obtenerPeriodoActual();
        if (resp == null || (boolean) resp.getOrDefault("error", true)) return;

        PeriodoEscolar p = (PeriodoEscolar) resp.get("periodo");
        if (p == null) return;

        cbPeriodo.setItems(FXCollections.observableArrayList(p));
        cbPeriodo.getSelectionModel().select(p);
        cbPeriodo.setDisable(true);
    }

    private void configurarTablaProblemas() {
        if (tvProblemas == null) return;

        tvProblemas.setEditable(true);

        colEE.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null && d.getValue().getNombreExperienciaEducativa() != null
                ? d.getValue().getNombreExperienciaEducativa() : "")
        );
        colEE.setCellFactory(TextFieldTableCell.forTableColumn());
        colEE.setOnEditCommit(e -> {
            ProblemaAcademicoReportadoGeneral row = e.getRowValue();
            if (row != null) row.setNombreExperienciaEducativa(e.getNewValue());
        });

        colProfesor.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null && d.getValue().getNombreProfesor() != null
                ? d.getValue().getNombreProfesor() : "")
        );
        colProfesor.setCellFactory(TextFieldTableCell.forTableColumn());
        colProfesor.setOnEditCommit(e -> {
            ProblemaAcademicoReportadoGeneral row = e.getRowValue();
            if (row != null) row.setNombreProfesor(e.getNewValue());
        });

        colProblema.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null && d.getValue().getProblema() != null
                ? d.getValue().getProblema() : "")
        );
        colProblema.setCellFactory(TextFieldTableCell.forTableColumn());
        colProblema.setOnEditCommit(e -> {
            ProblemaAcademicoReportadoGeneral row = e.getRowValue();
            if (row != null) row.setProblema(e.getNewValue());
        });

        colCantidad.setCellValueFactory(d ->
            new ReadOnlyObjectWrapper<>(d.getValue() != null ? d.getValue().getNumEstudiantes() : 0)
        );
        colCantidad.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colCantidad.setOnEditCommit(e -> {
            ProblemaAcademicoReportadoGeneral row = e.getRowValue();
            if (row != null) row.setNumEstudiantes(e.getNewValue() != null ? e.getNewValue() : 0);
        });
    }

    private void configurarTablaTutores() {
        if (tvTutores == null) return;

        tvTutores.setEditable(true);

        colTutor.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null && d.getValue().getNombreTutor() != null
                ? d.getValue().getNombreTutor() : "")
        );
        colTutor.setCellFactory(TextFieldTableCell.forTableColumn());
        colTutor.setOnEditCommit(e -> {
            TutorComentarioGeneral row = e.getRowValue();
            if (row != null) row.setNombreTutor(e.getNewValue());
        });

        colComentario.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null && d.getValue().getComentario() != null
                ? d.getValue().getComentario() : "")
        );
        colComentario.setCellFactory(TextFieldTableCell.forTableColumn());
        colComentario.setOnEditCommit(e -> {
            TutorComentarioGeneral row = e.getRowValue();
            if (row != null) row.setComentario(e.getNewValue());
        });
    }

    private void configurarTablaTutorias() {
        if (tvTutorias == null) return;

        colTutoriaTutor.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null && d.getValue().getNombreTutor() != null
                ? d.getValue().getNombreTutor() : "")
        );

        colTutoriaFechas.setCellValueFactory(d -> {
            ReporteTutoria r = d.getValue();
            String fechas = "";
            if (r != null) {
                if (r.getFechaUltimaTutoria() != null && !r.getFechaUltimaTutoria().trim().isEmpty()) {
                    fechas = r.getFechaUltimaTutoria();
                } else if (r.getFechaCreacionReporte() != null) {
                    fechas = r.getFechaCreacionReporte();
                }
            }
            return new SimpleStringProperty(fechas);
        });

        colTutoriaAsistieron.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null ? String.valueOf(d.getValue().getNumAlumnosAsistieron()) : "0")
        );

        colTutoriaRiesgo.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null ? String.valueOf(d.getValue().getNumAlumnosRiesgo()) : "0")
        );

        colTutoriaEstatus.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue() != null && d.getValue().getEstatus() != null ? d.getValue().getEstatus() : "")
        );
    }

    private void cargarTutoriasParaLateral() {
        HashMap<String, Object> resp = ReporteTutoriaImplementacion.buscarReportesParaRevision("");
        if (resp == null || (boolean) resp.getOrDefault("error", true)) {
            obsTutorias.clear();
            baseTutorias = new ArrayList<>();
            return;
        }

        @SuppressWarnings("unchecked")
        ArrayList<ReporteTutoria> lista = (ArrayList<ReporteTutoria>) resp.get("reportes");
        if (lista == null) lista = new ArrayList<>();

        baseTutorias = lista;
        obsTutorias.setAll(lista);
    }

    private void actualizarPorcentaje() {
        if (tfPorcentaje == null) return;

        try {
            int registrados = parseIntSeguro(tfTotalRegistrados != null ? tfTotalRegistrados.getText() : null);
            int asistieron = parseIntSeguro(tfTotalAsistieron != null ? tfTotalAsistieron.getText() : null);

            if (registrados <= 0) {
                tfPorcentaje.setText("0.00");
                return;
            }

            BigDecimal pct = BigDecimal.valueOf(asistieron)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(registrados), 2, RoundingMode.HALF_UP);

            tfPorcentaje.setText(pct.toString());
        } catch (Exception e) {
            tfPorcentaje.setText("");
        }
    }

    private int parseIntSeguro(String s) {
        if (s == null) return 0;
        String t = s.trim();
        if (t.isEmpty()) return 0;
        return Integer.parseInt(t);
    }


    @FXML
    private void clicAgregarProblema(ActionEvent e) {
        if (modo == Modo.VER) return;

        ProblemaAcademicoReportadoGeneral p = new ProblemaAcademicoReportadoGeneral();
        p.setNombreExperienciaEducativa("");
        p.setNombreProfesor("");
        p.setProblema("");
        p.setNumEstudiantes(1); 

        obsProblemas.add(p);

        if (tvProblemas != null) {
            int idx = obsProblemas.size() - 1;
            tvProblemas.getSelectionModel().select(idx);
            tvProblemas.scrollTo(idx);
            tvProblemas.requestFocus();
            tvProblemas.edit(idx, colEE); 
        }
    }


    @FXML
    private void clicQuitarProblema(ActionEvent e) {
        if (modo == Modo.VER) return;
        ProblemaAcademicoReportadoGeneral sel = tvProblemas != null ? tvProblemas.getSelectionModel().getSelectedItem() : null;
        if (sel != null) obsProblemas.remove(sel);
    }

    @FXML
    private void clicAgregarTutorComentario(ActionEvent e) {
        if (modo == Modo.VER) return;
        TutorComentarioGeneral t = new TutorComentarioGeneral();
        t.setNombreTutor("");
        t.setComentario("");
        obsTutores.add(t);
        if (tvTutores != null) tvTutores.getSelectionModel().select(t);
    }

    @FXML
    private void clicQuitarTutorComentario(ActionEvent e) {
        if (modo == Modo.VER) return;
        TutorComentarioGeneral sel = tvTutores != null ? tvTutores.getSelectionModel().getSelectedItem() : null;
        if (sel != null) obsTutores.remove(sel);
    }

    @FXML
    private void clicBuscarReportesTutoriaPorSesion(ActionEvent e) {
        String t = (tfBuscarNumSesionTutoria != null) ? tfBuscarNumSesionTutoria.getText().trim() : "";
        if (t.isEmpty()) {
            obsTutorias.setAll(baseTutorias);
            return;
        }

        int ses;
        try {
            ses = Integer.parseInt(t);
        } catch (NumberFormatException ex) {
            Utilidades.mostrarAlertaSimple("Atención", "Ingresa una sesión válida (1 a 4).", Alert.AlertType.WARNING);
            return;
        }

        ArrayList<ReporteTutoria> filtrada = new ArrayList<>();
        for (ReporteTutoria rt : baseTutorias) {
            if (rt != null && rt.getNumSesion() == ses) filtrada.add(rt);
        }
        obsTutorias.setAll(filtrada);
    }

    @FXML
    private void clicLimpiarBusquedaReportesTutoria(ActionEvent e) {
        if (tfBuscarNumSesionTutoria != null) tfBuscarNumSesionTutoria.clear();
        obsTutorias.setAll(baseTutorias);
    }


        @FXML
    private void clicAbrirReporteTutoria(ActionEvent e) {
        ReporteTutoria sel = (tvTutorias != null) ? tvTutorias.getSelectionModel().getSelectedItem() : null;
        if (sel == null) {
            Utilidades.mostrarAlertaSimple("Atención", "Selecciona un reporte de tutoría.", Alert.AlertType.WARNING);
            return;
        }

        HashMap<String, Object> resp = ReporteTutoriaImplementacion.obtenerReportePorId(sel.getIdReporteTutoria());
        if (resp == null || (boolean) resp.getOrDefault("error", true)) {
            String msg = (resp != null) ? (String) resp.getOrDefault("mensaje", "No se pudo cargar el reporte.") : "No se pudo cargar el reporte.";
            Utilidades.mostrarAlertaSimple("Error", msg, Alert.AlertType.ERROR);
            return;
        }

        ReporteTutoria detalle = (ReporteTutoria) resp.get("reporte");
        if (detalle == null) {
            Utilidades.mostrarAlertaSimple("Error", "No se encontró el reporte.", Alert.AlertType.ERROR);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestortutoriasfx/vista/FXMLMostrarReporteTutoria.fxml"));
            Parent vista = loader.load();
            FXMLMostrarReporteTutoriaController ctrl = loader.getController();

            ctrl.inicializarReporte(detalle, null, true, false);

            Stage st = new Stage();
            st.setTitle("Reporte de Tutoría");
            st.setScene(new Scene(vista));

            // opcional pero recomendado: que sea modal respecto a la ventana actual
            Window owner = (tvTutorias != null && tvTutorias.getScene() != null) ? tvTutorias.getScene().getWindow() : null;
            if (owner != null) {
                st.initOwner(owner);
                st.initModality(Modality.WINDOW_MODAL);
            }

            st.show();

        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo abrir FXMLMostrarReporteTutoria.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicGuardar(ActionEvent e) {
        if (modo == Modo.VER) return;
        if (!validarFilasNoVaciasNiInvalidas()) return;
        if (!armarDesdeFormulario(ReporteGeneral.Estatus.Borrador)) return;

        ArrayList<ProblemaAcademicoReportadoGeneral> problemas = filtrarProblemas();
        ArrayList<TutorComentarioGeneral> tutores = filtrarTutores();

        HashMap<String, Object> resp = ReporteGeneralImplementacion.guardarReporteGeneralCompleto(reporteActual, problemas, tutores);
        if (resp == null || (boolean) resp.getOrDefault("error", true)) {
            String msg = (resp != null) ? (String) resp.getOrDefault("mensaje", "No se pudo guardar.") : "No se pudo guardar.";
            Utilidades.mostrarAlertaSimple("Error", msg, Alert.AlertType.ERROR);
            return;
        }

        Integer id = (Integer) resp.get("idReporteGeneral");
        if (id != null) reporteActual.setIdReporteGeneral(id);

        Utilidades.mostrarAlertaSimple("OK", "Reporte guardado correctamente.", Alert.AlertType.INFORMATION);
        cerrarYRegresar(e);
    }

    @FXML
    private void clicFinalizar(ActionEvent e) {
        if (modo == Modo.VER) return;
        if (!validarFilasNoVaciasNiInvalidas()) return;
        if (!armarDesdeFormulario(ReporteGeneral.Estatus.Finalizado)) return;

        ArrayList<ProblemaAcademicoReportadoGeneral> problemas = filtrarProblemas();
        ArrayList<TutorComentarioGeneral> tutores = filtrarTutores();

        HashMap<String, Object> resp = ReporteGeneralImplementacion.guardarReporteGeneralCompleto(reporteActual, problemas, tutores);
        if (resp == null || (boolean) resp.getOrDefault("error", true)) {
            String msg = (resp != null) ? (String) resp.getOrDefault("mensaje", "No se pudo finalizar.") : "No se pudo finalizar.";
            Utilidades.mostrarAlertaSimple("Error", msg, Alert.AlertType.ERROR);
            return;
        }

        Integer id = (Integer) resp.get("idReporteGeneral");
        if (id != null) reporteActual.setIdReporteGeneral(id);

        Utilidades.mostrarAlertaSimple("Listo", "Reporte finalizado.", Alert.AlertType.INFORMATION);
        this.modo = Modo.VER;
        aplicarModo();
        cerrarYRegresar(e);
    }

    @FXML
    private void clicCancelar(ActionEvent e) {
        if (alRegresar != null) alRegresar.run();
    }

    private boolean armarDesdeFormulario(ReporteGeneral.Estatus estatus) {
        ProgramaEducativo pe = cbPrograma != null ? cbPrograma.getValue() : null;
        PeriodoEscolar per = cbPeriodo != null ? cbPeriodo.getValue() : null;
        Integer ses = cbNumSesion != null ? cbNumSesion.getValue() : null;
        LocalDate fecha = dpFecha != null ? dpFecha.getValue() : null;

        if (pe == null) {
            Utilidades.mostrarAlertaSimple("Validación", "Selecciona un Programa Educativo.", Alert.AlertType.WARNING);
            return false;
        }
        if (per == null) {
            Utilidades.mostrarAlertaSimple("Validación", "No hay Periodo Escolar activo.", Alert.AlertType.WARNING);
            return false;
        }
        if (ses == null || ses < 1 || ses > 4) {
            Utilidades.mostrarAlertaSimple("Validación", "Selecciona No. de sesión (1–4).", Alert.AlertType.WARNING);
            return false;
        }
        if (fecha == null) {
            Utilidades.mostrarAlertaSimple("Validación", "Selecciona una fecha.", Alert.AlertType.WARNING);
            return false;
        }

        if (reporteActual == null) reporteActual = new ReporteGeneral();

        reporteActual.setIdCoordinador(Sesion.getIdCoordinador());
        reporteActual.setNombreCoordinador(Sesion.getUsuarioSesion() != null ? Sesion.getUsuarioSesion().getNombre() : "");

        reporteActual.setIdProgramaEducativo(pe.getIdProgramaEducativo());
        reporteActual.setNombreProgramaEducativo(pe.getNombre());

        reporteActual.setIdPeriodoEscolar(per.getIdPeriodoEscolar());
        reporteActual.setNombrePeriodoEscolar(per.getNombrePeriodoEscolar());

        reporteActual.setNumSesion(ses);
        reporteActual.setFecha(fecha);

        String lugar = tfEstadoLugar != null && tfEstadoLugar.getText() != null ? tfEstadoLugar.getText().trim() : "";
        reporteActual.setEstadoLugar(lugar);

        String obj = taObjetivos != null && taObjetivos.getText() != null ? taObjetivos.getText().trim() : "";
        reporteActual.setObjetivos(obj);

        int registrados = parseIntSeguro(tfTotalRegistrados != null ? tfTotalRegistrados.getText() : null);
        int asistieron = parseIntSeguro(tfTotalAsistieron != null ? tfTotalAsistieron.getText() : null);

        reporteActual.setTotalAlumnosRegistrados(registrados);
        reporteActual.setTotalAlumnosAsistieron(asistieron);

        BigDecimal pct = (registrados <= 0)
                ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.valueOf(asistieron).multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(registrados), 2, RoundingMode.HALF_UP);

        reporteActual.setPorcentajeAsistencia(pct);
        if (tfPorcentaje != null) tfPorcentaje.setText(pct.toString());

        reporteActual.setEstatus(estatus);
        return true;
    }

    private ArrayList<ProblemaAcademicoReportadoGeneral> filtrarProblemas() {
        ArrayList<ProblemaAcademicoReportadoGeneral> out = new ArrayList<>();
        for (ProblemaAcademicoReportadoGeneral p : obsProblemas) {
            if (p == null) continue;

            String ee = p.getNombreExperienciaEducativa() != null ? p.getNombreExperienciaEducativa().trim() : "";
            String prof = p.getNombreProfesor() != null ? p.getNombreProfesor().trim() : "";
            String prob = p.getProblema() != null ? p.getProblema().trim() : "";

            if (ee.isEmpty() && prof.isEmpty() && prob.isEmpty() && p.getNumEstudiantes() <= 0) continue;
            out.add(p);
        }
        return out;
    }

    private ArrayList<TutorComentarioGeneral> filtrarTutores() {
        ArrayList<TutorComentarioGeneral> out = new ArrayList<>();
        for (TutorComentarioGeneral t : obsTutores) {
            if (t == null) continue;

            String nom = t.getNombreTutor() != null ? t.getNombreTutor().trim() : "";
            String com = t.getComentario() != null ? t.getComentario().trim() : "";

            if (nom.isEmpty() && com.isEmpty()) continue;
            out.add(t);
        }
        return out;
    }
    
    private void cerrarYRegresar(ActionEvent e) {
        if (alRegresar != null) alRegresar.run();

        try {
            if (e != null && e.getSource() instanceof Node) {
                Window w = ((Node) e.getSource()).getScene().getWindow();
                if (w instanceof Stage) {
                    Stage st = (Stage) w;
                    if (panelContenido == null || st.getOwner() != null) {
                        st.close();
                    }
                }
            }
        } catch (Exception ex) {}
    }
    
    private boolean validarFilasNoVaciasNiInvalidas() {
        for (int i = 0; i < obsProblemas.size(); i++) {
            ProblemaAcademicoReportadoGeneral p = obsProblemas.get(i);
            if (p == null) {
                Utilidades.mostrarAlertaSimple("Validación",
                        "Problemas: existe una fila nula en la tabla.",
                        Alert.AlertType.WARNING);
                return false;
            }

            String ee = p.getNombreExperienciaEducativa() != null ? p.getNombreExperienciaEducativa().trim() : "";
            String prof = p.getNombreProfesor() != null ? p.getNombreProfesor().trim() : "";
            String prob = p.getProblema() != null ? p.getProblema().trim() : "";
            int cant = p.getNumEstudiantes();

            if (ee.isEmpty() || prof.isEmpty() || prob.isEmpty() || cant <= 0) {
                Utilidades.mostrarAlertaSimple(
                    "Validación",
                    "Problemas: no puedes dejar filas vacías.\n" +
                    "Revisa la fila " + (i + 1) + " (EE, Profesor, Problema y Cant. Alum > 0).",
                    Alert.AlertType.WARNING
                );
                if (tvProblemas != null) {
                    tvProblemas.getSelectionModel().select(i);
                    tvProblemas.scrollTo(i);
                }
                return false;
            }
        }

        for (int i = 0; i < obsTutores.size(); i++) {
            TutorComentarioGeneral t = obsTutores.get(i);
            if (t == null) {
                Utilidades.mostrarAlertaSimple("Validación",
                        "Tutores: existe una fila nula en la tabla.",
                        Alert.AlertType.WARNING);
                return false;
            }

            String nom = t.getNombreTutor() != null ? t.getNombreTutor().trim() : "";
            String com = t.getComentario() != null ? t.getComentario().trim() : "";

            if (nom.isEmpty() || com.isEmpty()) {
                Utilidades.mostrarAlertaSimple(
                    "Validación",
                    "Tutores: no puedes dejar filas vacías.\n" +
                    "Revisa la fila " + (i + 1) + " (Tutor y Comentario).",
                    Alert.AlertType.WARNING
                );
                if (tvTutores != null) {
                    tvTutores.getSelectionModel().select(i);
                    tvTutores.scrollTo(i);
                }
                return false;
            }
        }

        return true;
    }


    private boolean filaProblemaVacia(ProblemaAcademicoReportadoGeneral p) {
        if (p == null) return true;
        String ee = p.getNombreExperienciaEducativa() == null ? "" : p.getNombreExperienciaEducativa().trim();
        String pr = p.getNombreProfesor() == null ? "" : p.getNombreProfesor().trim();
        String pb = p.getProblema() == null ? "" : p.getProblema().trim();
        return ee.isEmpty() && pr.isEmpty() && pb.isEmpty() && p.getNumEstudiantes() <= 0;
    }

    private boolean filaTutorVacia(TutorComentarioGeneral t) {
        if (t == null) return true;
        String nom = t.getNombreTutor() == null ? "" : t.getNombreTutor().trim();
        String com = t.getComentario() == null ? "" : t.getComentario().trim();
        return nom.isEmpty() && com.isEmpty();
    }

}
