package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.PeriodoEscolarImplementacion;
import gestortutoriasfx.dominio.ProgramaEducativoImplementacion;
import gestortutoriasfx.dominio.ReporteGeneralImplementacion;
import gestortutoriasfx.dominio.ReporteTutoriaImplementacion;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import gestortutoriasfx.modelo.pojo.ProgramaEducativo;
import gestortutoriasfx.modelo.pojo.ProblemaAcademicoReportadoGeneral;
import gestortutoriasfx.modelo.pojo.ReporteGeneral;
import gestortutoriasfx.modelo.pojo.ReporteTutoria;
import gestortutoriasfx.modelo.pojo.TutorComentarioGeneral;
import gestortutoriasfx.utilidad.Utilidades;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
import javafx.scene.control.Alert;
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
import javafx.stage.FileChooser;
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

        // Guardar raíz del formulario (para volver aquí si abres "MostrarReporteTutoria")
        try {
            Node n = tvProblemas;
            while (n != null && n.getParent() != null) n = n.getParent();
            vistaFormulario = (Parent) n;
        } catch (Exception e) {
            vistaFormulario = null;
        }

        // Contexto
        if (lbContexto != null) {
            String nombre = (Sesion.getUsuarioSesion() != null && Sesion.getUsuarioSesion().getNombre() != null)
                    ? Sesion.getUsuarioSesion().getNombre()
                    : "";
            lbContexto.setText("Coordinador: " + nombre);
        }

        // Combos
        cbNumSesion.setItems(FXCollections.observableArrayList(1, 2, 3, 4));

        cargarProgramas();
        cargarPeriodoActual();

        // Tablas editables
        configurarTablaProblemas();
        configurarTablaTutores();

        tvProblemas.setItems(obsProblemas);
        tvTutores.setItems(obsTutores);

        // Totales: actualizar porcentaje al cambiar
        if (tfPorcentaje != null) tfPorcentaje.setEditable(false);

        if (tfTotalRegistrados != null) {
            tfTotalRegistrados.textProperty().addListener((o, a, b) -> actualizarPorcentaje());
        }
        if (tfTotalAsistieron != null) {
            tfTotalAsistieron.textProperty().addListener((o, a, b) -> actualizarPorcentaje());
        }

        // Lateral: reportes tutoría
        configurarTablaTutorias();
        tvTutorias.setItems(obsTutorias);
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
            aplicarModo();
            return;
        }

        if (base == null || base.getIdReporteGeneral() == null) {
            Utilidades.mostrarAlertaSimple("Error", "No se recibió un reporte válido.", Alert.AlertType.ERROR);
            this.reporteActual = new ReporteGeneral();
            this.reporteActual.setEstatus(ReporteGeneral.Estatus.Borrador);
            aplicarModo();
            return;
        }

        // Traer completo (reporte + tablas hijas)
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

        // Cargar campos base
        precargarCamposDesdeReporte();

        aplicarModo();
    }

    private void aplicarModo() {
        boolean soloLectura = (modo == Modo.VER);

        cbPrograma.setDisable(soloLectura);
        cbPeriodo.setDisable(true); // normalmente siempre solo periodo actual
        cbNumSesion.setDisable(soloLectura);
        dpFecha.setDisable(soloLectura);
        tfEstadoLugar.setDisable(soloLectura);
        taObjetivos.setDisable(soloLectura);

        tfTotalRegistrados.setDisable(soloLectura);
        tfTotalAsistieron.setDisable(soloLectura);

        tvProblemas.setEditable(!soloLectura);
        tvTutores.setEditable(!soloLectura);

        // Lateral siempre visible, no afecta.
    }

    private void precargarCamposDesdeReporte() {
        if (reporteActual == null) return;

        // programa: seleccionar por id si existe en combo
        if (reporteActual.getIdProgramaEducativo() != null && cbPrograma.getItems() != null) {
            for (ProgramaEducativo pe : cbPrograma.getItems()) {
                if (pe != null && pe.getIdProgramaEducativo() != null
                        && pe.getIdProgramaEducativo().equals(reporteActual.getIdProgramaEducativo())) {
                    cbPrograma.getSelectionModel().select(pe);
                    break;
                }
            }
        }

        // periodo actual ya viene seleccionado
        cbNumSesion.getSelectionModel().select(Integer.valueOf(reporteActual.getNumSesion()));

        if (reporteActual.getFecha() != null) dpFecha.setValue(reporteActual.getFecha());
        tfEstadoLugar.setText(reporteActual.getEstadoLugar() != null ? reporteActual.getEstadoLugar() : "");
        taObjetivos.setText(reporteActual.getObjetivos() != null ? reporteActual.getObjetivos() : "");

        tfTotalRegistrados.setText(String.valueOf(reporteActual.getTotalAlumnosRegistrados()));
        tfTotalAsistieron.setText(String.valueOf(reporteActual.getTotalAlumnosAsistieron()));
        tfPorcentaje.setText(reporteActual.getPorcentajeAsistencia() != null ? reporteActual.getPorcentajeAsistencia().toString() : "");
    }

    private void cargarProgramas() {
        HashMap<String, Object> resp = ProgramaEducativoImplementacion.obtenerProgramas();
        if (resp == null || (boolean) resp.getOrDefault("error", true)) return;

        @SuppressWarnings("unchecked")
        ArrayList<ProgramaEducativo> lista = (ArrayList<ProgramaEducativo>) resp.get("programas");
        if (lista == null) lista = new ArrayList<>();

        cbPrograma.setItems(FXCollections.observableArrayList(lista));

        // Selección automática: programa asignado al coordinador (si existe)
        int idCoord = Sesion.getIdCoordinador();
        for (ProgramaEducativo pe : lista) {
            if (pe != null && pe.getIdCoordinador() != null && pe.getIdCoordinador() == idCoord) {
                cbPrograma.getSelectionModel().select(pe);
                break;
            }
        }
    }

    private void cargarPeriodoActual() {
        HashMap<String, Object> resp = PeriodoEscolarImplementacion.obtenerPeriodoActual();
        if (resp == null || (boolean) resp.getOrDefault("error", true)) return;

        PeriodoEscolar p = (PeriodoEscolar) resp.get("periodo");
        if (p == null) return;

        cbPeriodo.setItems(FXCollections.observableArrayList(p));
        cbPeriodo.getSelectionModel().select(p);
        cbPeriodo.setDisable(true);
    }

    private void configurarTablaProblemas() {
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
        try {
            int registrados = parseIntSeguro(tfTotalRegistrados.getText());
            int asistieron = parseIntSeguro(tfTotalAsistieron.getText());

            if (registrados <= 0) {
                tfPorcentaje.setText("0.00");
                return;
            }
            double pct = (asistieron * 100.0) / registrados;
            tfPorcentaje.setText(String.format(java.util.Locale.US, "%.2f", pct));
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

    // ======= BOTONES =======

    @FXML
    private void clicAgregarProblema(ActionEvent e) {
        obsProblemas.add(new ProblemaAcademicoReportadoGeneral("", "", "", 0));
    }

    @FXML
    private void clicQuitarProblema(ActionEvent e) {
        ProblemaAcademicoReportadoGeneral sel = tvProblemas.getSelectionModel().getSelectedItem();
        if (sel != null) obsProblemas.remove(sel);
    }

    @FXML
    private void clicAgregarTutorComentario(ActionEvent e) {
        obsTutores.add(new TutorComentarioGeneral("", ""));
    }

    @FXML
    private void clicQuitarTutorComentario(ActionEvent e) {
        TutorComentarioGeneral sel = tvTutores.getSelectionModel().getSelectedItem();
        if (sel != null) obsTutores.remove(sel);
    }

    @FXML
    private void clicAutollenarTutores(ActionEvent e) {
        LinkedHashMap<String, TutorComentarioGeneral> existentes = new LinkedHashMap<>();
        for (TutorComentarioGeneral t : obsTutores) {
            if (t == null) continue;
            String key = (t.getNombreTutor() != null) ? t.getNombreTutor().trim() : "";
            if (!key.isEmpty()) existentes.put(key, t);
        }

        LinkedHashSet<String> nombres = new LinkedHashSet<>();
        for (ReporteTutoria rt : obsTutorias) {
            if (rt == null) continue;
            String n = (rt.getNombreTutor() != null) ? rt.getNombreTutor().trim() : "";
            if (!n.isEmpty()) nombres.add(n);
        }

        for (String n : nombres) {
            if (!existentes.containsKey(n)) {
                obsTutores.add(new TutorComentarioGeneral(n, ""));
            }
        }
    }

    @FXML
    private void clicCalcularDesdeTutorias(ActionEvent e) {
        int totalAsist = 0;
        for (ReporteTutoria rt : obsTutorias) {
            if (rt == null) continue;
            totalAsist += rt.getNumAlumnosAsistieron();
        }
        tfTotalAsistieron.setText(String.valueOf(totalAsist));
        actualizarPorcentaje();

        Utilidades.mostrarAlertaSimple(
                "Cálculo",
                "Se calculó 'Total alumnos que asistieron' sumando los reportes de tutoría visibles.\n" +
                "Si deseas un total de registrados real (tutorados asignados), ese dato debe capturarse o consultarse aparte.",
                Alert.AlertType.INFORMATION
        );
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
        ReporteTutoria sel = tvTutorias.getSelectionModel().getSelectedItem();
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

            Runnable volverAqui = () -> {
                if (panelContenido != null && vistaFormulario != null) {
                    panelContenido.getChildren().setAll(vistaFormulario);
                }
            };

            ctrl.inicializarReporte(detalle, volverAqui, panelContenido == null);

            if (panelContenido != null) {
                panelContenido.getChildren().setAll(vista);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo abrir FXMLMostrarReporteTutoria.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicGuardar(ActionEvent e) {
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
    }

    @FXML
    private void clicFinalizar(ActionEvent e) {
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
    }

    @FXML
    private void clicExportar(ActionEvent e) {
        if (reporteActual == null || reporteActual.getIdReporteGeneral() == null) {
            Utilidades.mostrarAlertaSimple("Atención", "Primero guarda el reporte para poder exportar.", Alert.AlertType.WARNING);
            return;
        }

        byte[] bytes = generarTXT();
        String nombre = "ReporteGeneral_" + reporteActual.getIdReporteGeneral() + ".txt";

        // Guardar en archivo local
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Guardar reporte");
            fc.setInitialFileName(nombre);
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texto (*.txt)", "*.txt"));

            File f = fc.showSaveDialog(((Node) e.getSource()).getScene().getWindow());
            if (f == null) return;

            Files.write(f.toPath(), bytes);

        } catch (Exception ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo guardar el archivo local.", Alert.AlertType.ERROR);
            return;
        }

        // Guardar en BD (documentoReporteGeneral)
        HashMap<String, Object> resp = ReporteGeneralImplementacion.guardarDocumentoReporteGeneral(
                reporteActual.getIdReporteGeneral(), nombre, "TXT", bytes
        );

        if (resp == null || (boolean) resp.getOrDefault("error", true)) {
            String msg = (resp != null) ? (String) resp.getOrDefault("mensaje", "Se guardó el archivo local, pero no en BD.") : "Se guardó el archivo local, pero no en BD.";
            Utilidades.mostrarAlertaSimple("Aviso", msg, Alert.AlertType.WARNING);
            return;
        }

        Utilidades.mostrarAlertaSimple("OK", "Exportado (archivo local + BD).", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void clicCancelar(ActionEvent e) {
        if (alRegresar != null) alRegresar.run();
    }

    private boolean armarDesdeFormulario(ReporteGeneral.Estatus estatus) {
        ProgramaEducativo pe = cbPrograma.getValue();
        PeriodoEscolar per = cbPeriodo.getValue();
        Integer ses = cbNumSesion.getValue();
        LocalDate fecha = dpFecha.getValue();

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
        reporteActual.setEstadoLugar(tfEstadoLugar.getText() != null ? tfEstadoLugar.getText().trim() : "");
        reporteActual.setObjetivos(taObjetivos.getText() != null ? taObjetivos.getText().trim() : "");

        reporteActual.setTotalAlumnosRegistrados(parseIntSeguro(tfTotalRegistrados.getText()));
        reporteActual.setTotalAlumnosAsistieron(parseIntSeguro(tfTotalAsistieron.getText()));

        reporteActual.setEstatus(estatus);

        return true;
    }

    private ArrayList<ProblemaAcademicoReportadoGeneral> filtrarProblemas() {
        ArrayList<ProblemaAcademicoReportadoGeneral> out = new ArrayList<>();
        for (ProblemaAcademicoReportadoGeneral p : obsProblemas) {
            if (p == null) continue;
            String ee = (p.getNombreExperienciaEducativa() != null) ? p.getNombreExperienciaEducativa().trim() : "";
            String prof = (p.getNombreProfesor() != null) ? p.getNombreProfesor().trim() : "";
            String prob = (p.getProblema() != null) ? p.getProblema().trim() : "";

            if (ee.isEmpty() && prof.isEmpty() && prob.isEmpty() && p.getNumEstudiantes() <= 0) continue;
            out.add(p);
        }
        return out;
    }

    private ArrayList<TutorComentarioGeneral> filtrarTutores() {
        ArrayList<TutorComentarioGeneral> out = new ArrayList<>();
        for (TutorComentarioGeneral t : obsTutores) {
            if (t == null) continue;
            String nom = (t.getNombreTutor() != null) ? t.getNombreTutor().trim() : "";
            String com = (t.getComentario() != null) ? t.getComentario().trim() : "";
            if (nom.isEmpty() && com.isEmpty()) continue;
            out.add(t);
        }
        return out;
    }

    private byte[] generarTXT() {
        StringBuilder sb = new StringBuilder();

        sb.append("REPORTE GENERAL DE TUTORÍAS\n");
        sb.append("====================================\n\n");

        sb.append("Programa: ").append(reporteActual.getNombreProgramaEducativo()).append("\n");
        sb.append("Periodo: ").append(reporteActual.getNombrePeriodoEscolar()).append("\n");
        sb.append("Sesión: ").append(reporteActual.getNumSesion()).append("\n");
        sb.append("Fecha: ").append(reporteActual.getFecha() != null ? reporteActual.getFecha().format(fmt) : "").append("\n");
        sb.append("Lugar/Estado: ").append(reporteActual.getEstadoLugar() != null ? reporteActual.getEstadoLugar() : "").append("\n");
        sb.append("Estatus: ").append(reporteActual.getEstatus() != null ? reporteActual.getEstatus().name() : "").append("\n\n");

        sb.append("Objetivos:\n").append(reporteActual.getObjetivos() != null ? reporteActual.getObjetivos() : "").append("\n\n");

        sb.append("Estadísticas globales:\n");
        sb.append(" - Registrados: ").append(reporteActual.getTotalAlumnosRegistrados()).append("\n");
        sb.append(" - Asistieron: ").append(reporteActual.getTotalAlumnosAsistieron()).append("\n");
        sb.append(" - % Asistencia: ").append(reporteActual.getPorcentajeAsistencia()).append("\n\n");

        sb.append("Problemas Académicos:\n");
        if (obsProblemas.isEmpty()) {
            sb.append(" (Sin registros)\n");
        } else {
            for (ProblemaAcademicoReportadoGeneral p : obsProblemas) {
                sb.append(" - EE: ").append(p.getNombreExperienciaEducativa()).append(" | Prof: ").append(p.getNombreProfesor()).append("\n");
                sb.append("   Problema: ").append(p.getProblema()).append(" | #Est: ").append(p.getNumEstudiantes()).append("\n");
            }
        }
        sb.append("\n");

        sb.append("Tutores y comentarios:\n");
        if (obsTutores.isEmpty()) {
            sb.append(" (Sin registros)\n");
        } else {
            for (TutorComentarioGeneral t : obsTutores) {
                sb.append(" - ").append(t.getNombreTutor()).append(": ").append(t.getComentario()).append("\n");
            }
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
