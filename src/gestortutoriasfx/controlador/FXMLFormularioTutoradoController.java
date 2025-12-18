package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.EstudianteImplementacion;
import gestortutoriasfx.dominio.ProgramaEducativoImplementacion;
import gestortutoriasfx.dominio.TutorImplementacion;
import gestortutoriasfx.modelo.pojo.Estudiante;
import gestortutoriasfx.modelo.pojo.ProgramaEducativo;
import gestortutoriasfx.modelo.pojo.Tutor;
import gestortutoriasfx.utilidad.Utilidades;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

public class FXMLFormularioTutoradoController implements Initializable {

    @FXML private Label lbTitulo;

    @FXML private TextField tfMatricula;
    @FXML private ComboBox<ProgramaEducativo> cbPrograma;
    @FXML private ComboBox<Tutor> cbTutor;
    @FXML private DatePicker dpIngreso;
    @FXML private ComboBox<Integer> cbSemestre;

    @FXML private TextField tfNombre;
    @FXML private TextField tfApellidoPaterno;
    @FXML private TextField tfApellidoMaterno;
    @FXML private TextField tfTelefono;
    @FXML private TextField tfCorreo;

    @FXML private TitledPane tpSoloEdicion;
    @FXML private TextField tfCreditos;
    @FXML private TextField tfCambioTutor;

    @FXML private ImageView ivFoto;
    @FXML private Button btnCargarFoto;
    @FXML private CheckBox cbPerfilActivo;
    @FXML private Label lbInfo;
    @FXML private Button btnGuardar;

    private byte[] fotoBytes;
    private boolean modoEdicion = false;

    private boolean fotoNueva = false;
    private Estudiante tutoradoEdicion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (dpIngreso != null && dpIngreso.getValue() == null) dpIngreso.setValue(LocalDate.now());
        setInfo("Selecciona Programa → se cargan Tutores y Semestres.");

        aplicarModoEdicion(false);

        if (tfCorreo != null) {
            tfCorreo.setDisable(true);
            tfCorreo.setEditable(false);
        }

        if (tfMatricula != null) {
            tfMatricula.textProperty().addListener((obs, oldV, nuevo) -> {
                if (modoEdicion) return;
                MatriculaInfo mi = normalizarMatricula(nuevo);
                tfCorreo.setText(mi != null ? mi.correoCanon : "");
            });
        }

        cargarProgramas();
        limpiarTutoresYSemestres();

        if (cbPrograma != null) {
            cbPrograma.valueProperty().addListener((obs, oldV, nuevo) -> {
                if (nuevo == null || nuevo.getIdProgramaEducativo() == 0) {
                    limpiarTutoresYSemestres();
                    return;
                }
                cargarTutoresPorPrograma(nuevo.getIdProgramaEducativo());
                cargarSemestresPorPrograma(nuevo.getNumPeriodos());
            });
        }
    }

    private void cargarProgramas() {
        if (cbPrograma == null) return;

        HashMap<String, Object> resp = ProgramaEducativoImplementacion.obtenerProgramas();
        if (resp != null && resp.get("error") instanceof Boolean && !(boolean) resp.get("error")) {
            @SuppressWarnings("unchecked")
            ArrayList<ProgramaEducativo> programas = (ArrayList<ProgramaEducativo>) resp.get("programas");
            cbPrograma.getItems().setAll(programas != null ? programas : new ArrayList<>());
            cbPrograma.getSelectionModel().clearSelection();
        } else {
            setInfo(resp != null && resp.get("mensaje") != null
                    ? resp.get("mensaje").toString()
                    : "No se pudieron cargar Programas Educativos.");
        }
    }

    private void cargarTutoresPorPrograma(int idProgramaEducativo) {
        if (cbTutor == null) return;

        cbTutor.getItems().clear();
        cbTutor.getSelectionModel().clearSelection();

        HashMap<String, Object> resp =
                TutorImplementacion.obtenerTutoresPorProgramaEducativo(idProgramaEducativo);

        if (resp != null && resp.get("error") instanceof Boolean && !(boolean) resp.get("error")) {
            @SuppressWarnings("unchecked")
            ArrayList<Tutor> tutores = (ArrayList<Tutor>) resp.get("tutores");
            cbTutor.getItems().setAll(tutores != null ? tutores : new ArrayList<>());
        } else {
            setInfo(resp != null && resp.get("mensaje") != null
                    ? resp.get("mensaje").toString()
                    : "No se pudieron cargar Tutores para ese programa.");
        }
    }


    private void cargarSemestresPorPrograma(int numPeriodos) {
        if (cbSemestre == null) return;

        cbSemestre.getItems().clear();
        int max = numPeriodos;
        if (max <= 0) max = 14;

        for (int i = 1; i <= max; i++) cbSemestre.getItems().add(i);
        cbSemestre.getSelectionModel().select(Integer.valueOf(1));
    }

    private void limpiarTutoresYSemestres() {
        if (cbTutor != null) {
            cbTutor.getItems().clear();
            cbTutor.getSelectionModel().clearSelection();
        }
        if (cbSemestre != null) {
            cbSemestre.getItems().clear();
        }
    }

    public void aplicarModoEdicion(boolean esEdicion) {
        this.modoEdicion = esEdicion;

        if (tpSoloEdicion != null) {
            tpSoloEdicion.setVisible(esEdicion);
            tpSoloEdicion.setManaged(esEdicion);
        }
        if (lbTitulo != null) lbTitulo.setText(esEdicion ? "Editar Tutorado" : "Registrar Tutorado");
        if (tfMatricula != null) tfMatricula.setDisable(esEdicion);
    }

    public void cargarDatos(Estudiante e) {
        if (e == null) return;

        this.tutoradoEdicion = e;
        aplicarModoEdicion(true);

        tfMatricula.setText(e.getMatricula());
        tfNombre.setText(e.getNombre());
        tfApellidoPaterno.setText(e.getApellidoPaterno());
        tfApellidoMaterno.setText(e.getApellidoMaterno());
        tfTelefono.setText(e.getTelefono());
        tfCorreo.setText(e.getCorreoInstitucional());

        if (dpIngreso != null) dpIngreso.setValue(LocalDate.of(e.getAnioIngreso(), 1, 1));
        if (cbPerfilActivo != null) cbPerfilActivo.setSelected(e.isPerfilActivo());

        if (tfCreditos != null) { tfCreditos.setText(String.valueOf(e.getCreditosObtenidos())); tfCreditos.setDisable(true); }
        if (tfCambioTutor != null) { tfCambioTutor.setText(String.valueOf(e.getCambioTutor())); tfCambioTutor.setDisable(true); }

        if (e.getFoto() != null && e.getFoto().length > 0 && ivFoto != null) {
            ivFoto.setImage(new Image(new ByteArrayInputStream(e.getFoto())));
            fotoBytes = e.getFoto();
        }
        fotoNueva = false;

        if (cbPrograma != null && e.getIdProgramaEducativo() > 0) {
            for (ProgramaEducativo pe : cbPrograma.getItems()) {
                if (pe.getIdProgramaEducativo() != 0 && pe.getIdProgramaEducativo() == e.getIdProgramaEducativo()) {
                    cbPrograma.getSelectionModel().select(pe);
                    cargarTutoresPorPrograma(pe.getIdProgramaEducativo());
                    cargarSemestresPorPrograma(pe.getNumPeriodos());
                    break;
                }
            }
        }

        if (cbSemestre != null) cbSemestre.getSelectionModel().select(Integer.valueOf(e.getSemestre()));

        if (cbTutor != null && e.getIdTutor() != 0) {
            for (Tutor t : cbTutor.getItems()) {
                if (t.getIdTutor() == e.getIdTutor()) {
                    cbTutor.getSelectionModel().select(t);
                    break;
                }
            }
        }
    }

    @FXML
    private void clicCargarFoto(ActionEvent e) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));

        File archivo = fc.showOpenDialog(null);
        if (archivo == null) return;

        try {
            fotoBytes = java.nio.file.Files.readAllBytes(archivo.toPath());
            fotoNueva = true;
            if (ivFoto != null) ivFoto.setImage(new Image(archivo.toURI().toString()));
            setInfo("Foto cargada: " + archivo.getName());
        } catch (IOException ex) {
            fotoBytes = null;
            fotoNueva = false;
            Utilidades.mostrarAlertaSimple("Error", "No se pudo leer la imagen: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        ProgramaEducativo programaSel = (cbPrograma != null) ? cbPrograma.getValue() : null;
        Tutor tutorSel = (cbTutor != null) ? cbTutor.getValue() : null;
        Integer semestreSel = (cbSemestre != null) ? cbSemestre.getValue() : null;

        if (programaSel == null) { setInfo("Selecciona un Programa Educativo."); return; }
        if (tutorSel == null) { setInfo("Selecciona un Tutor (filtrado por programa)."); return; }
        if (semestreSel == null) { setInfo("Selecciona un semestre válido."); return; }

        if (!validarCampos()) return;

        Estudiante est = new Estudiante();

        if (modoEdicion) {
            est.setIdEstudiante(tutoradoEdicion.getIdEstudiante());
            est.setMatricula(tutoradoEdicion.getMatricula());
            est.setCorreoInstitucional(tutoradoEdicion.getCorreoInstitucional());
        } else {
            MatriculaInfo mi = normalizarMatricula(txt(tfMatricula));
            if (mi == null) { setInfo("Matrícula inválida."); return; }
            est.setMatricula(mi.matriculaCanon);
            est.setCorreoInstitucional(mi.correoCanon);
            est.setFoto(fotoBytes != null ? fotoBytes : new byte[0]);
        }

        est.setNombre(txt(tfNombre));
        est.setApellidoPaterno(txt(tfApellidoPaterno));
        est.setApellidoMaterno(txt(tfApellidoMaterno));
        est.setTelefono(txt(tfTelefono));

        est.setAnioIngreso(dpIngreso != null && dpIngreso.getValue() != null
                ? dpIngreso.getValue().getYear()
                : LocalDate.now().getYear());

        est.setSemestre(semestreSel);
        est.setPerfilActivo(cbPerfilActivo != null && cbPerfilActivo.isSelected());

        est.setIdProgramaEducativo(programaSel.getIdProgramaEducativo());
        est.setIdTutor(tutorSel.getIdTutor());

        if (modoEdicion) {
            HashMap<String, Object> resp = EstudianteImplementacion.actualizarTutoradoCompleto(est, tutorSel.getIdTutor());

            if (resp != null && resp.get("error") instanceof Boolean && !(boolean) resp.get("error")) {
                if (fotoNueva && fotoBytes != null && fotoBytes.length > 0) {
                    EstudianteImplementacion.actualizarFotoTutorado(est.getMatricula(), fotoBytes);
                }
                Utilidades.mostrarAlertaSimple("Éxito", "Cambios guardados.", Alert.AlertType.INFORMATION);
                regresarAGestionarTutorados((Node) event.getSource());
            } else {
                setInfo(resp != null && resp.get("mensaje") != null
                        ? resp.get("mensaje").toString()
                        : "No se pudieron guardar los cambios.");
            }
        } else {
            HashMap<String, Object> resp = EstudianteImplementacion.registrarTutorado(est, tutorSel.getIdTutor());

            if (resp != null && resp.get("error") instanceof Boolean && !(boolean) resp.get("error")) {
                Utilidades.mostrarAlertaSimple("Éxito", "Tutorado registrado.", Alert.AlertType.INFORMATION);
                regresarAGestionarTutorados((Node) event.getSource());
            } else {
                String msg = (resp != null && resp.get("mensaje") != null)
                        ? resp.get("mensaje").toString()
                        : "No se pudo registrar el tutorado.";
                setInfo(msg);
            }
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        regresarAGestionarTutorados((Node) event.getSource());
    }

    private boolean validarCampos() {
        if (!modoEdicion) {
            MatriculaInfo mi = normalizarMatricula(txt(tfMatricula));
            if (mi == null) {
                Utilidades.mostrarAlertaSimple("Validación",
                        "Matrícula inválida. Debe ser zS######## (8 dígitos).",
                        Alert.AlertType.WARNING);
                tfMatricula.requestFocus();
                return false;
            }
            tfMatricula.setText(mi.matriculaCanon);
            tfCorreo.setText(mi.correoCanon);
        } else {
            if (tutoradoEdicion == null || tutoradoEdicion.getMatricula() == null) {
                Utilidades.mostrarAlertaSimple("Validación",
                        "No se encontró la matrícula del tutorado en edición.",
                        Alert.AlertType.WARNING);
                return false;
            }
            tfMatricula.setText(tutoradoEdicion.getMatricula());
            tfCorreo.setText(tutoradoEdicion.getCorreoInstitucional());
        }

        if (cbPrograma == null || cbPrograma.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Validación", "Selecciona un Programa Educativo.", Alert.AlertType.WARNING);
            if (cbPrograma != null) cbPrograma.requestFocus();
            return false;
        }
        if (cbTutor == null || cbTutor.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Validación", "Selecciona un Tutor.", Alert.AlertType.WARNING);
            if (cbTutor != null) cbTutor.requestFocus();
            return false;
        }
        if (cbSemestre == null || cbSemestre.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Validación", "Selecciona un semestre.", Alert.AlertType.WARNING);
            if (cbSemestre != null) cbSemestre.requestFocus();
            return false;
        }

        if (dpIngreso == null || dpIngreso.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Validación", "Selecciona la fecha de ingreso.", Alert.AlertType.WARNING);
            if (dpIngreso != null) dpIngreso.requestFocus();
            return false;
        }

        if (txt(tfNombre).isEmpty()) {
            Utilidades.mostrarAlertaSimple("Validación", "El nombre es obligatorio.", Alert.AlertType.WARNING);
            tfNombre.requestFocus();
            return false;
        }
        if (txt(tfApellidoPaterno).isEmpty()) {
            Utilidades.mostrarAlertaSimple("Validación", "El apellido paterno es obligatorio.", Alert.AlertType.WARNING);
            tfApellidoPaterno.requestFocus();
            return false;
        }
        if (txt(tfApellidoMaterno).isEmpty()) {
            Utilidades.mostrarAlertaSimple("Validación", "El apellido materno es obligatorio.", Alert.AlertType.WARNING);
            tfApellidoMaterno.requestFocus();
            return false;
        }

        String tel = txt(tfTelefono);
        if (tel.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Validación", "El teléfono es obligatorio.", Alert.AlertType.WARNING);
            tfTelefono.requestFocus();
            return false;
        }
        if (!tel.matches("^\\d{10}$")) {
            Utilidades.mostrarAlertaSimple("Validación", "El teléfono debe tener 10 dígitos.", Alert.AlertType.WARNING);
            tfTelefono.requestFocus();
            return false;
        }

        if (txt(tfCorreo).isEmpty()) {
            Utilidades.mostrarAlertaSimple("Validación", "El correo institucional es obligatorio.", Alert.AlertType.WARNING);
            if (tfMatricula != null) tfMatricula.requestFocus();
            return false;
        }

        return true;
    }

    private void regresarAGestionarTutorados(Node origen) {
        StackPane panel = null;
        try {
            Node n = origen.getScene().lookup("#panelContenido");
            if (n instanceof StackPane) panel = (StackPane) n;
        } catch (Exception ignored) {}

        if (panel == null) {
            Utilidades.mostrarAlertaSimple("Error", "No se encontró el panelContenido para regresar.", Alert.AlertType.ERROR);
            return;
        }

        try {
            FXMLLoader loader = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLGestionarTutorado.fxml");
            Parent vista = loader.load();
            panel.getChildren().setAll(vista);
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo regresar a Gestionar Tutorados.", Alert.AlertType.ERROR);
        }
    }

    private String txt(TextField tf) {
        return tf != null && tf.getText() != null ? tf.getText().trim() : "";
    }

    private void setInfo(String msg) {
        if (lbInfo != null) lbInfo.setText(msg);
    }

    private static class MatriculaInfo {
        final String digitos;
        final String matriculaCanon;
        final String correoCanon;

        MatriculaInfo(String digitos) {
            this.digitos = digitos;
            this.matriculaCanon = "zS" + digitos;
            this.correoCanon = "zs" + digitos + "@estudiantes.uv.mx";
        }
    }

    private MatriculaInfo normalizarMatricula(String input) {
        if (input == null) return null;
        String s = input.trim();
        String dig = s.replaceAll("\\D", "");
        if (dig.length() != 8) return null;
        return new MatriculaInfo(dig);
    }
}
