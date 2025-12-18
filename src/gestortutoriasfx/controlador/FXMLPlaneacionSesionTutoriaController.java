package gestortutoriasfx.controlador;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.FechaTutoriaDAO;
import gestortutoriasfx.modelo.pojo.FechaTutoria;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import gestortutoriasfx.utilidad.Utilidades;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLPlaneacionSesionTutoriaController {

    @FXML private Label lbContexto;
    @FXML private Label lbSesion;
    @FXML private Label lbFecha;
    @FXML private Label lbMensaje;
    @FXML private TextArea taDescripcion;

    private PeriodoEscolar periodo;
    private int numSesion;

    private LocalDate fechaSeleccionada;
    private Integer idFechaTutoriaEdicion = null;
    private Runnable alGuardarCallback;

    private StackPane panelContenido;
    private Parent vistaSesion;
    private Runnable volverCallback;
    
    @FXML private javafx.scene.control.Button btnSeleccionarFecha;
    @FXML private javafx.scene.control.Button btnGuardar;
    @FXML private javafx.scene.control.Button btnCancelar;

    private boolean soloLectura = false;

    

    public void configurarNavegacionEmbebida(StackPane panelContenido, Parent vistaSesion, Runnable volverCallback) {
        this.panelContenido = panelContenido;
        this.vistaSesion = vistaSesion;
        this.volverCallback = volverCallback;
    }

    public void inicializar(PeriodoEscolar periodo, int numSesion, Runnable alGuardarCallback) {
        this.periodo = periodo;
        this.numSesion = numSesion;
        this.alGuardarCallback = alGuardarCallback;

        if (lbContexto != null) {
            String nombre = (periodo != null && periodo.getNombrePeriodoEscolar() != null)
                    ? periodo.getNombrePeriodoEscolar()
                    : "(pendiente)";
            lbContexto.setText("Periodo: " + nombre);
        }

        if (lbSesion != null) {
            lbSesion.setText("Tutoría " + numSesion);
        }
        
        cargarExistenteSiHay();
        aplicarModoSoloLectura();
    }

    private void cargarExistenteSiHay() {
        limpiarMensaje();
        if (periodo == null) {
            mostrarMensajeError("No se recibió el periodo escolar.");
            setFechaUI(null);
            return;
        }
        try (Connection conn = ConexionBD.abrirConexionBD()) {
            if (conn == null) {
                mostrarMensajeError("No se pudo conectar a la base de datos.");
                return;
            }

            FechaTutoria ft = FechaTutoriaDAO.obtenerPorPeriodoYNumSesion(conn, periodo.getIdPeriodoEscolar(), numSesion);

            if (ft != null) {
                idFechaTutoriaEdicion = ft.getIdFechaTutoria();
                taDescripcion.setText(ft.getDescripcion() != null ? ft.getDescripcion() : "");

                // ft.getFechaInicio viene del DAO mapear()
                fechaSeleccionada = parseFechaSeguro(ft.getFechaInicio());
                setFechaUI(fechaSeleccionada);
            } else {
                idFechaTutoriaEdicion = null;
                taDescripcion.setText("");
                fechaSeleccionada = null;
                setFechaUI(null);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            mostrarMensajeError("No se pudo cargar la tutoría: " + ex.getMessage());
        }
    }

    @FXML
    private void clicSeleccionarFecha() {
        if (soloLectura) return;

        limpiarMensaje();
        if (periodo == null) {
            mostrarMensajeError("No hay periodo seleccionado.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/gestortutoriasfx/vista/FXMLRegistrarFechaTutoria.fxml")
            );
            Parent vistaCal = loader.load();

            FXMLRegistrarFechaTutoriaController ctrlCal = loader.getController();

            // ✅ EMBEBIDO (sin Stage)
            if (panelContenido != null && vistaSesion != null) {
                Consumer<LocalDate> onConfirm = (LocalDate seleccion) -> {
                    if (seleccion != null) {
                        fechaSeleccionada = seleccion;
                        setFechaUI(fechaSeleccionada);
                    }
                    panelContenido.getChildren().setAll(vistaSesion);
                };
                Runnable onCancel = () -> panelContenido.getChildren().setAll(vistaSesion);

                ctrlCal.inicializarEmbebido(periodo.getIdPeriodoEscolar(), numSesion, fechaSeleccionada, onConfirm, onCancel);
                panelContenido.getChildren().setAll(vistaCal);
                return;
            }

            // Fallback (modal)
            ctrlCal.inicializar(periodo.getIdPeriodoEscolar(), numSesion, fechaSeleccionada);

            Stage stage = new Stage();
            stage.setTitle("Seleccionar fecha - Tutoría " + numSesion);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(vistaCal));
            stage.showAndWait();

            LocalDate seleccion = ctrlCal.getFechaSeleccionada();
            if (seleccion != null) {
                fechaSeleccionada = seleccion;
                setFechaUI(fechaSeleccionada);
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensajeError("No se pudo abrir el calendario.");
        }
    }

    @FXML
    private void clicGuardar() {
        if (soloLectura) return;
        
        limpiarMensaje();

        if (periodo == null) {
            mostrarMensajeError("No hay periodo seleccionado.");
            return;
        }

        String descripcion = (taDescripcion.getText() != null) ? taDescripcion.getText().trim() : "";
        if (descripcion.isEmpty()) {
            mostrarMensajeError("Escribe una descripción / actividad.");
            return;
        }

        if (fechaSeleccionada == null) {
            mostrarMensajeError("Selecciona una fecha.");
            return;
        }

        LocalDate inicio = parseFechaSeguro(periodo.getFechaInicio());
        LocalDate fin = parseFechaSeguro(periodo.getFechaFin());

        if (inicio != null && fechaSeleccionada.isBefore(inicio)) {
            mostrarMensajeError("La fecha está antes del inicio del periodo (" + inicio + ").");
            return;
        }
        if (fin != null && fechaSeleccionada.isAfter(fin)) {
            mostrarMensajeError("La fecha está después del fin del periodo (" + fin + ").");
            return;
        }

        try (Connection conn = ConexionBD.abrirConexionBD()) {
            if (conn == null) {
                mostrarMensajeError("No se pudo conectar a la base de datos.");
                return;
            }

            conn.setAutoCommit(false);

            int idPeriodo = periodo.getIdPeriodoEscolar();
            String fechaSQL = fechaSeleccionada.toString();

            if (idFechaTutoriaEdicion == null) {
                int total = contarFechasPorPeriodo(conn, idPeriodo);
                if (total >= 4) {
                    throw new SQLException("Ya existen 4 fechas de tutoría registradas para este periodo.");
                }
            }

            Integer idExistenteEnFecha = obtenerIdPorFecha(conn, idPeriodo, fechaSQL);
            if (idExistenteEnFecha != null
                    && (idFechaTutoriaEdicion == null || !idExistenteEnFecha.equals(idFechaTutoriaEdicion))) {
                throw new SQLException("Ya existe una tutoría registrada en la fecha " + fechaSQL + ".");
            }

            if (idFechaTutoriaEdicion == null) {
                String ins = "INSERT INTO fechaTutoria (idPeriodoEscolar, descripcion, numSesion, fecha) VALUES (?,?,?,?)";
                try (PreparedStatement ps = conn.prepareStatement(ins)) {
                    ps.setInt(1, idPeriodo);
                    ps.setString(2, descripcion);
                    ps.setInt(3, numSesion);
                    ps.setString(4, fechaSQL);
                    int filas = ps.executeUpdate();
                    if (filas <= 0) throw new SQLException("No se pudo insertar la fecha de tutoría.");
                }
            } else {
                String upd = "UPDATE fechaTutoria SET descripcion = ?, fecha = ? WHERE idFechaTutoria = ?";
                try (PreparedStatement ps = conn.prepareStatement(upd)) {
                    ps.setString(1, descripcion);
                    ps.setString(2, fechaSQL);
                    ps.setInt(3, idFechaTutoriaEdicion);
                    int filas = ps.executeUpdate();
                    if (filas <= 0) throw new SQLException("No se pudo actualizar la fecha de tutoría.");
                }
            }

            conn.commit();

            Utilidades.mostrarAlertaSimple(
                    "Éxito",
                    "Se guardó la tutoría " + numSesion + " en la fecha " + fechaSQL + ".",
                    Alert.AlertType.INFORMATION
            );

            if (alGuardarCallback != null) alGuardarCallback.run();
            cerrar();

        } catch (SQLException ex) {
            ex.printStackTrace();
            mostrarMensajeError(ex.getMessage() != null ? ex.getMessage() : "Error al guardar.");
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarMensajeError("Error inesperado: " + ex.getMessage());
        }
    }

    @FXML
    private void clicCancelar() {
        cerrar();
    }

    private int contarFechasPorPeriodo(Connection conn, int idPeriodo) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM fechaTutoria WHERE idPeriodoEscolar = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPeriodo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
                return 0;
            }
        }
    }

    private Integer obtenerIdPorFecha(Connection conn, int idPeriodo, String fechaISO) throws SQLException {
        String sql = "SELECT idFechaTutoria FROM fechaTutoria WHERE idPeriodoEscolar = ? AND fecha = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPeriodo);
            ps.setString(2, fechaISO);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("idFechaTutoria");
                return null;
            }
        }
    }

    private void setFechaUI(LocalDate fecha) {
        if (lbFecha != null) {
            lbFecha.setText(fecha != null ? fecha.toString() : "(sin selección)");
        }
    }

    private void cerrar() {
        // ✅ Embebido: regresar a planeación
        if (volverCallback != null) {
            try { volverCallback.run(); } catch (Exception ignore) { }
            return;
        }

        // Fallback: cerrar ventana
        try {
            Stage stage = (Stage) lbSesion.getScene().getWindow();
            stage.close();
        } catch (Exception ignore) { }
    }

    private void limpiarMensaje() {
        if (lbMensaje != null) lbMensaje.setText("");
    }

    private void mostrarMensajeError(String mensaje) {
        if (lbMensaje != null) lbMensaje.setText(mensaje != null ? mensaje : "Error");
        try {
            Utilidades.mostrarAlertaSimple("Error", mensaje, Alert.AlertType.ERROR);
        } catch (Exception ignore) { }
    }

    private LocalDate parseFechaSeguro(String fecha) {
        if (fecha == null) return null;
        try {
            return LocalDate.parse(fecha.trim());
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
    
    public void setSoloLectura(boolean soloLectura) {
            this.soloLectura = soloLectura;
            aplicarModoSoloLectura();
        }
        private void aplicarModoSoloLectura() {
        if (!soloLectura) return;

        if (taDescripcion != null) {
            taDescripcion.setEditable(false);
            taDescripcion.setMouseTransparent(false);
        }

        if (btnSeleccionarFecha != null) btnSeleccionarFecha.setDisable(true);
        if (btnGuardar != null) btnGuardar.setDisable(true);

        if (btnCancelar != null) btnCancelar.setText("Regresar");
    }


}
