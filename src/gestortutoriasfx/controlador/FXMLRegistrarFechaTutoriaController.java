package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.FechaTutoriaImplementacion;
import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.CalendarioDAO;
import gestortutoriasfx.modelo.dao.FechaTutoriaDAO;
import gestortutoriasfx.modelo.dao.PeriodoEscolarDAO;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import gestortutoriasfx.utilidad.Utilidades;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class FXMLRegistrarFechaTutoriaController {

    @FXML private VBox vbMeses;
    @FXML private Label lbContexto;
    @FXML private Label lbMensaje;

    private LocalDate fechaSeleccionada;

    private boolean modoEmbebido = false;
    private Consumer<LocalDate> onConfirm;
    private Runnable onCancel;

    private int idPeriodoEscolar;            
    private Integer numSesion;               
    private LocalDate fechaActualDeLaSesion; 

    private boolean yaCargo = false;

    @FXML
    public void initialize() {
    }

    public void inicializar(int idPeriodoEscolar, Integer numSesion, LocalDate fechaActualDeLaSesion) {
        this.idPeriodoEscolar = idPeriodoEscolar;
        this.numSesion = numSesion;
        this.fechaActualDeLaSesion = fechaActualDeLaSesion;

        this.modoEmbebido = false;
        this.onConfirm = null;
        this.onCancel = null;

        cargarCalendarioSeguro();
    }

    public void inicializarEmbebido(
            int idPeriodoEscolar,
            Integer numSesion,
            LocalDate fechaActualDeLaSesion,
            Consumer<LocalDate> onConfirm,
            Runnable onCancel
    ) {
        this.idPeriodoEscolar = idPeriodoEscolar;
        this.numSesion = numSesion;
        this.fechaActualDeLaSesion = fechaActualDeLaSesion;

        this.modoEmbebido = true;
        this.onConfirm = onConfirm;
        this.onCancel = onCancel;

        cargarCalendarioSeguro();
    }

    public LocalDate getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    private void cargarCalendarioSeguro() {
        if (yaCargo) return;
        yaCargo = true;
        cargarCalendario();
    }

    private void cargarCalendario() {
        if (vbMeses == null) return;

        vbMeses.getChildren().clear();
        limpiarMensaje();

        Connection con = null;

        try {
            con = ConexionBD.abrirConexionBD();
            if (con == null) {
                mostrarMensajeError("No hay conexión con la base de datos.");
                return;
            }

            PeriodoEscolar periodo = PeriodoEscolarDAO.obtenerPeriodoActual(con);
            if (periodo == null) {
                mostrarMensajeError("No hay un periodo escolar activo.");
                return;
            }

            int idPeriodo = (idPeriodoEscolar > 0) ? idPeriodoEscolar : periodo.getIdPeriodoEscolar();
            LocalDate inicio = LocalDate.parse(periodo.getFechaInicio());

            if (lbContexto != null) {
                String ses = (numSesion != null) ? ("Tutoría " + numSesion + " | ") : "";
                lbContexto.setText(ses + "Periodo: " + periodo.getNombrePeriodoEscolar());
            }

            Map<LocalDate, Set<String>> categorias = CalendarioDAO.obtenerCategoriasPorPeriodo(con, idPeriodo);
            if (categorias == null) categorias = new HashMap<>();

            Set<LocalDate> ocupadas = (Set<LocalDate>) FechaTutoriaImplementacion.obtenerFechasOcupadas(idPeriodo);
            if (ocupadas == null) ocupadas = new HashSet<>();

            if (fechaActualDeLaSesion != null) {
                ocupadas.remove(fechaActualDeLaSesion);
                fechaSeleccionada = fechaActualDeLaSesion;
            }

            for (LocalDate f : ocupadas) {
                categorias.computeIfAbsent(f, k -> new HashSet<>()).add("Tutoria");
            }

            YearMonth ym = YearMonth.from(inicio);

            for (int i = 0; i < 6; i++) {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/gestortutoriasfx/vista/FXMLMesCalendario.fxml")
                );

                VBox mes = loader.load();
                FXMLMesCalendarioController ctrlMes = loader.getController();

                ctrlMes.construirMes(ym, categorias, this::diaSeleccionado);

                vbMeses.getChildren().add(mes);
                ym = ym.plusMonths(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensajeError("Ocurrió un error al cargar el calendario.");
        } finally {
            ConexionBD.cerrarConexion(con);
        }
    }

    private void diaSeleccionado(LocalDate fecha) {
        this.fechaSeleccionada = fecha;
        limpiarMensaje();
    }

    @FXML
    private void clicGuardar() {
        limpiarMensaje();

        if (fechaSeleccionada == null) {
            mostrarMensajeError("Selecciona una fecha antes de guardar.");
            return;
        }

        if (modoEmbebido) {
            if (onConfirm != null) onConfirm.accept(fechaSeleccionada);
            return;
        }

        cerrarVentana();
    }

    @FXML
    private void clicCancelar() {
        fechaSeleccionada = null;

        if (modoEmbebido) {
            if (onCancel != null) onCancel.run();
            return;
        }

        cerrarVentana();
    }

    private void cerrarVentana() {
        if (vbMeses != null && vbMeses.getScene() != null) {
            vbMeses.getScene().getWindow().hide();
        }
    }

    private void limpiarMensaje() {
        if (lbMensaje != null) lbMensaje.setText("");
    }

    private void mostrarMensajeError(String msg) {
        if (lbMensaje != null) {
            lbMensaje.setText(msg != null ? msg : "Error");
        } else {
            Utilidades.mostrarAlertaSimple("Error", msg, Alert.AlertType.ERROR);
        }
    }
}
