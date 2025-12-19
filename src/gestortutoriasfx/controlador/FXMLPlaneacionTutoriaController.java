package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.FechaTutoriaImplementacion;
import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.dao.FechaTutoriaDAO;
import gestortutoriasfx.modelo.dao.PeriodoEscolarDAO;
import gestortutoriasfx.modelo.pojo.FechaTutoria;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import gestortutoriasfx.modelo.pojo.Usuario;
import gestortutoriasfx.utilidad.Utilidades;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLPlaneacionTutoriaController implements javafx.fxml.Initializable {

    @FXML private Label lbContexto;

    @FXML private Label lbFecha1, lbFecha2, lbFecha3, lbFecha4;
    @FXML private Label lbActividad1, lbActividad2, lbActividad3, lbActividad4;
    @FXML private Label lbEstado1, lbEstado2, lbEstado3, lbEstado4;

    @FXML private Button btnAccion1, btnAccion2, btnAccion3, btnAccion4;
    @FXML private Button btnVer1, btnVer2, btnVer3, btnVer4;

    private PeriodoEscolar periodoActual;

    private StackPane panelContenido;
    private Parent vistaPlaneacion;

    public void setPanelContenido(StackPane panelContenido) {
        this.panelContenido = panelContenido;
    }

    public void setVistaPlaneacion(Parent vistaPlaneacion) {
        this.vistaPlaneacion = vistaPlaneacion;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPeriodoYRefrescar();
    }

    private void cargarPeriodoYRefrescar() {
        Connection con = null;
        try {
            con = ConexionBD.abrirConexionBD();
            if (con == null) {
                Utilidades.mostrarAlertaSimple("Error", "No hay conexión con la base de datos.", Alert.AlertType.ERROR);
                return;
            }

            periodoActual = PeriodoEscolarDAO.obtenerPeriodoActual(con);

            String coordinador = obtenerNombreCoordinador();

            if (periodoActual == null) {
                if (lbContexto != null) {
                    lbContexto.setText("Periodo: (sin periodo activo) | Coordinador: " + coordinador);
                }
                ponerTodoSinPlanear();
                return;
            }

            if (lbContexto != null) {
                lbContexto.setText("Periodo: " + periodoActual.getNombrePeriodoEscolar()
                        + " | Coordinador: " + coordinador);
            }

            cargarSesiones();

        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar la planeación.", Alert.AlertType.ERROR);
        } finally {
            ConexionBD.cerrarConexion(con);
        }
    }

    private String obtenerNombreCoordinador() {
        try {
            Usuario u = Sesion.getUsuarioSesion();
            if (u == null) return "----";
            String nombre = (u.getNombre() != null) ? u.getNombre().trim() : "";
            return nombre.isEmpty() ? "----" : nombre;
        } catch (Exception e) {
            return "----";
        }
    }

    private void cargarSesiones() {
        ponerSesionSinPlanear(1);
        ponerSesionSinPlanear(2);
        ponerSesionSinPlanear(3);
        ponerSesionSinPlanear(4);

        if (periodoActual == null) return;

        HashMap<String, Object> respuesta = FechaTutoriaImplementacion.obtenerFechasPorPeriodo(periodoActual.getIdPeriodoEscolar());

        if (!(boolean) respuesta.get("error")) {
            @SuppressWarnings("unchecked")
            ArrayList<FechaTutoria> fechas = (ArrayList<FechaTutoria>) respuesta.get("fechas");

            for (FechaTutoria ft : fechas) {
                int s = ft.getNumSesion();
                String desc = ft.getDescripcion();
                
                String fecha = ft.getFecha(); 

                ponerSesionPlaneada(s, fecha, desc);
            }
        } else {
            String mensaje = (String) respuesta.get("mensaje");
            Utilidades.mostrarAlertaSimple("Error", mensaje, Alert.AlertType.ERROR);
        }
    }

    @FXML private void clicAccionSesion1(ActionEvent e){ abrirPlaneacionSesion(1, false); }
    @FXML private void clicAccionSesion2(ActionEvent e){ abrirPlaneacionSesion(2, false); }
    @FXML private void clicAccionSesion3(ActionEvent e){ abrirPlaneacionSesion(3, false); }
    @FXML private void clicAccionSesion4(ActionEvent e){ abrirPlaneacionSesion(4, false); }

    @FXML private void clicConsultarSesion1(ActionEvent e){ consultarSesion(1); }
    @FXML private void clicConsultarSesion2(ActionEvent e){ consultarSesion(2); }
    @FXML private void clicConsultarSesion3(ActionEvent e){ consultarSesion(3); }
    @FXML private void clicConsultarSesion4(ActionEvent e){ consultarSesion(4); }

    private void consultarSesion(int sesion) {
        Button btn = getBtnVer(sesion);
        if (btn == null || btn.isDisable()) return;
        abrirPlaneacionSesion(sesion, true);
    }

    private void abrirPlaneacionSesion(int sesion, boolean soloLectura) {
        if (periodoActual == null) {
            Utilidades.mostrarAlertaSimple("Error", "No hay periodo escolar activo.", Alert.AlertType.ERROR);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gestortutoriasfx/vista/FXMLPlaneacionSesionTutoria.fxml")
            );
            Parent root = loader.load();

            FXMLPlaneacionSesionTutoriaController ctrl = loader.getController();
            ctrl.inicializar(periodoActual, sesion, this::refrescarTarjetas);
            ctrl.setSoloLectura(soloLectura);

            if (panelContenido != null && vistaPlaneacion != null) {
                Runnable volver = () -> {
                    refrescarTarjetas();
                    panelContenido.getChildren().setAll(vistaPlaneacion);
                };
                ctrl.configurarNavegacionEmbebida(panelContenido, root, volver);
                panelContenido.getChildren().setAll(root);
                return;
            }

            Stage stage = new Stage();
            stage.setTitle((soloLectura ? "Consultar" : "Planeación") + " de la sesión " + sesion);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo abrir la planeación de la sesión.", Alert.AlertType.ERROR);
        }
    }

    private void refrescarTarjetas() {
        cargarPeriodoYRefrescar();
    }

    private void ponerTodoSinPlanear() {
        ponerSesionSinPlanear(1);
        ponerSesionSinPlanear(2);
        ponerSesionSinPlanear(3);
        ponerSesionSinPlanear(4);
    }

    private void ponerSesionSinPlanear(int sesion) {
        getLbFecha(sesion).setText("Fecha: --/--/----");
        getLbActividad(sesion).setText("Actividad: (sin planear)");
        getLbEstado(sesion).setText("Sin planear");
        getBtnVer(sesion).setDisable(true);
        getBtnAccion(sesion).setText("Planear");
    }

    private void ponerSesionPlaneada(int sesion, String fechaISO, String descripcion) {
        String fechaTxt = (fechaISO != null) ? fechaISO : "--/--/----";
        String descTxt = (descripcion != null && !descripcion.trim().isEmpty())
                ? descripcion.trim()
                : "(sin descripción)";

        getLbFecha(sesion).setText("Fecha: " + fechaTxt);
        getLbActividad(sesion).setText("Actividad: " + descTxt);
        getLbEstado(sesion).setText("Planeada");
        getBtnVer(sesion).setDisable(false);
        getBtnAccion(sesion).setText("Editar");
    }

    private Label getLbFecha(int sesion) {
        switch (sesion) {
            case 1: return lbFecha1;
            case 2: return lbFecha2;
            case 3: return lbFecha3;
            case 4: return lbFecha4;
            default: return lbFecha4;
        }
    }

    private Label getLbActividad(int sesion) {
        switch (sesion) {
            case 1: return lbActividad1;
            case 2: return lbActividad2;
            case 3: return lbActividad3;
            case 4: return lbActividad4;
            default: return lbActividad4;
        }
    }

    private Label getLbEstado(int sesion) {
        switch (sesion) {
            case 1: return lbEstado1;
            case 2: return lbEstado2;
            case 3: return lbEstado3;
            case 4: return lbEstado4;
            default: return lbEstado4;
        }
    }

    private Button getBtnAccion(int sesion) {
        switch (sesion) {
            case 1: return btnAccion1;
            case 2: return btnAccion2;
            case 3: return btnAccion3;
            case 4: return btnAccion4;
            default: return btnAccion4;
        }
    }

    private Button getBtnVer(int sesion) {
        switch (sesion) {
            case 1: return btnVer1;
            case 2: return btnVer2;
            case 3: return btnVer3;
            case 4: return btnVer4;
            default: return btnVer4;
        }
    }
}
