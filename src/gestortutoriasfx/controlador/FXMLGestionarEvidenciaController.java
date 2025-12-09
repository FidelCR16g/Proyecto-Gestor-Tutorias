package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.EvidenciaImplementacion;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.Evidencia;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
import gestortutoriasfx.utilidad.Utilidades;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * Nombre de la Clase: FXMLGestionarEvidenciaController
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 29/11/2025
 *
 * Descripción:
 * Se encarga de controlar los distintos elementos de los listados de evidencia
 */

public class FXMLGestionarEvidenciaController implements Initializable {

    @FXML 
    private ComboBox<String> cbSesion;
    @FXML 
    private VBox vbListaArchivos;
    @FXML 
    private VBox vbZonaDrop;
    @FXML 
    private Button btnGuardar;

    private ArrayList<Evidencia> evidenciasPorGuardar;
    private ArrayList<Evidencia> evidenciasPorEliminar;
    
    private SesionTutoria sesionActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        evidenciasPorGuardar = new ArrayList<>();
        evidenciasPorEliminar = new ArrayList<>();
        configurarZonaDrop();
    }
    
    public void inicializarSesion(SesionTutoria sesion) {
        this.sesionActual = sesion;
        
        cbSesion.getItems().add("Tutoría No. " + sesion.getNumSesion() + " (" + sesion.getFecha() + ")");
        cbSesion.getSelectionModel().selectFirst();
        cbSesion.setDisable(true);
        
        cargarEvidenciasExistentes();
    }
    
    @FXML
    private void clicGuardar(ActionEvent event) {
        guardarCambios();
    }
    
    @FXML
    private void clicCancelar(ActionEvent event) {
        boolean hayCambios = !evidenciasPorGuardar.isEmpty() || !evidenciasPorEliminar.isEmpty();
        
        if (hayCambios) {
            if (!Utilidades.mostrarAlertaVerificacion("Salir", "¿Desea salir sin guardar?", 
                    "Se perderán los cambios pendientes.")) {
                return; 
            }
        }
        regresarAListado();
    }
    
    private void abrirSelectorArchivos() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Evidencias");
        FileChooser.ExtensionFilter filtro = new FileChooser.ExtensionFilter(
            "Archivos permitidos (PDF, JPG, PNG)", "*.pdf", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(filtro);
        
        List<File> archivos = fileChooser.showOpenMultipleDialog(vbZonaDrop.getScene().getWindow());
        if (archivos != null) {
            procesarArchivos(archivos);
        }
    }

    private void cargarEvidenciasExistentes() {
        HashMap<String, Object> respuesta = EvidenciaImplementacion.obtenerEvidenciasPorSesion(
                Sesion.getIdTutor(), 
                sesionActual.getNumSesion()
        );

        if (!(boolean) respuesta.get("error")) {
            ArrayList<Evidencia> existentes = (ArrayList<Evidencia>) respuesta.get("evidencias");
            
            vbListaArchivos.getChildren().clear();
            
            for (Evidencia ev : existentes) {
                HBox tarjeta = crearTarjetaVisual(ev);
                vbListaArchivos.getChildren().add(tarjeta);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error", (String) respuesta.get("mensaje"), 
                    Alert.AlertType.WARNING);
        }
    }

    private void configurarZonaDrop() {
        String estiloBase = "-fx-background-color: #e3f2fd; -fx-border-color: #2196f3; -fx-border-width: 2; "
                + "-fx-border-style: dashed; -fx-background-radius: 10; -fx-border-radius: 10; -fx-cursor: hand;";
        String estiloHover = "-fx-background-color: #bbdefb; -fx-border-color: #1565c0; -fx-border-width: 2; "
                + "-fx-border-style: dashed; -fx-background-radius: 10; -fx-border-radius: 10;";

        vbZonaDrop.setStyle(estiloBase);

        vbZonaDrop.setOnDragEntered(event -> {
            if (event.getDragboard().hasFiles()) vbZonaDrop.setStyle(estiloHover);
        });

        vbZonaDrop.setOnDragExited(event -> vbZonaDrop.setStyle(estiloBase));

        vbZonaDrop.setOnDragOver(event -> {
            if (event.getGestureSource() != vbZonaDrop && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        vbZonaDrop.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                procesarArchivos(db.getFiles());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
            vbZonaDrop.setStyle(estiloBase);
        });

        vbZonaDrop.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) abrirSelectorArchivos();
        });
    }
    
    private HBox crearTarjetaVisual(Evidencia evidencia) {
        HBox tarjeta = new HBox();
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setSpacing(15);
        tarjeta.setPadding(new Insets(10));
        tarjeta.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cfd8dc; -fx-border-radius: 5; "
                + "-fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 1);");

        Label lbIcono = new Label("📄");
        lbIcono.setStyle("-fx-font-size: 20px;");

        VBox info = new VBox();
        Label lbNombre = new Label(evidencia.getNombreArchivo());
        lbNombre.setStyle("-fx-font-weight: bold; -fx-text-fill: #37474f;");
        Label lbTamano = new Label(evidencia.getTamanoKB() > 0 ? evidencia.getTamanoKB() + " KB" : "Guardado");
        lbTamano.setStyle("-fx-text-fill: #90a4ae; -fx-font-size: 11px;");
        info.getChildren().addAll(lbNombre, lbTamano);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnEliminar = new Button("✕");
        btnEliminar.setStyle("-fx-background-color: transparent; -fx-text-fill: #ef5350; -fx-font-weight: bold; "
                + "-fx-cursor: hand; -fx-font-size: 14px;");
        
        btnEliminar.setOnAction(e -> {
            vbListaArchivos.getChildren().remove(tarjeta);

            if (evidencia.isEsNuevo()) {
                evidenciasPorGuardar.remove(evidencia);
            } else {
                evidenciasPorEliminar.add(evidencia);
            }
            validarBotonGuardar();
        });

        tarjeta.getChildren().addAll(lbIcono, info, spacer, btnEliminar);
        return tarjeta;
    }
    
    private void guardarCambios() {
        HashMap<String, Object> respuesta = EvidenciaImplementacion.guardarCambiosEvidencias(
                evidenciasPorGuardar, 
                evidenciasPorEliminar, 
                Sesion.getIdTutor(), 
                sesionActual.getNumSesion()
        );
        
        if (!(boolean) respuesta.get("error")) {
            Utilidades.mostrarAlertaSimple("Éxito", respuesta.get("mensaje").toString(), 
                    Alert.AlertType.INFORMATION);
            evidenciasPorGuardar.clear();
            evidenciasPorEliminar.clear();
            btnGuardar.setDisable(true);
            regresarAListado();
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron aplicar los cambios.", 
                    Alert.AlertType.WARNING);
        }
    }

    private void procesarArchivos(List<File> archivos) {
        if (sesionActual == null) return;

        int totalVisual = vbListaArchivos.getChildren().size() + archivos.size();
        if (totalVisual > 5) {
            Utilidades.mostrarAlertaSimple("Límite excedido", "Solo puede tener máximo 5 archivos por sesión.", 
                    Alert.AlertType.WARNING);
            return;
        }

        for (File archivo : archivos) {
            String nombre = archivo.getName().toLowerCase();
            if (!nombre.endsWith(".pdf") && !nombre.endsWith(".jpg") && 
                !nombre.endsWith(".jpeg") && !nombre.endsWith(".png")) {
                Utilidades.mostrarAlertaSimple("Formato no válido", "Archivo: " + nombre + "\nSolo PDF, JPG y PNG.", 
                        Alert.AlertType.WARNING);
                continue;
            }

            if (archivo.length() > 5 * 1024 * 1024) {
                Utilidades.mostrarAlertaSimple("Archivo pesado", "El archivo " + nombre + " excede 5MB.", 
                        Alert.AlertType.WARNING);
                continue;
            }

            try {
                byte[] bytes = Files.readAllBytes(archivo.toPath());
                
                Evidencia evidencia = new Evidencia();
                evidencia.setNombreArchivo(archivo.getName());
                evidencia.setArchivo(bytes);
                evidencia.setEsNuevo(true);
                
                double kb = archivo.length() / 1024.0;
                evidencia.setTamanoKB(Math.round(kb * 100.0) / 100.0);

                evidenciasPorGuardar.add(evidencia);

                HBox tarjeta = crearTarjetaVisual(evidencia);
                vbListaArchivos.getChildren().add(tarjeta);
                
                validarBotonGuardar();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void regresarAListado() {
        try {
            FXMLLoader cargador = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLListadoEvidencias.fxml");
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
    
    private void validarBotonGuardar() {
        boolean hayCambios = !evidenciasPorGuardar.isEmpty() || !evidenciasPorEliminar.isEmpty();
        btnGuardar.setDisable(!hayCambios);
    }
}