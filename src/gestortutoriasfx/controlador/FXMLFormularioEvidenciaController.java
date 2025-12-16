package gestortutoriasfx.controlador;

import gestortutoriasfx.dominio.EvidenciaImplementacion;
import gestortutoriasfx.modelo.Sesion;
import gestortutoriasfx.modelo.pojo.Evidencia;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
import gestortutoriasfx.utilidad.Utilidades;
import gestortutoriasfx.utilidades.ArrastrarSoltarUtilidad;
import gestortutoriasfx.utilidades.TarjetaArchivo;
import gestortutoriasfx.utilidades.ValidadorEvidencia;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class FXMLFormularioEvidenciaController implements Initializable {

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
    
    private void agregarEvidencia(Evidencia evidencia) {
        evidenciasPorGuardar.add(evidencia);
        
        TarjetaArchivo tarjeta = new TarjetaArchivo(evidencia, this::procesarEliminacion);
        vbListaArchivos.getChildren().add(tarjeta);
    
        validarBotonGuardar();
    }
    
    private double calcularTamanoKB(long bytes) {
        double kb = bytes / 1024.0;
        return Math.round(kb * 100.0) / 100.0;
    }
    
    private void cargarEvidenciasExistentes() {
        ArrayList<Evidencia> lista = obtenerEvidenciasDeBD();
        
        if (lista != null) {
            renderizarListaEvidencias(lista);
        }
    }
    
    private void configurarZonaDrop() {
        ArrastrarSoltarUtilidad.configurarZonaArrastrarSoltar(vbZonaDrop, this::procesarArchivos,
            this::abrirSelectorArchivos
        );
    }
    
    private Evidencia crearEvidenciaDesdeArchivo(File archivo) {
        byte[] contenido = leerBytesDelArchivo(archivo);
        if (contenido == null) return null;

        Evidencia evidencia = new Evidencia();
        evidencia.setNombreArchivo(archivo.getName());
        evidencia.setArchivo(contenido);
        evidencia.setEsNuevo(true);
        evidencia.setTamanoKB(calcularTamanoKB(archivo.length()));
        
        return evidencia;
    }
    
    private boolean esArchivoValido(File archivo) {
        String resultado = ValidadorEvidencia.validarArchivo(archivo);

        if (!resultado.equals("OK")) {
            Utilidades.mostrarAlertaSimple("Archivo no válido", 
                "El archivo " + archivo.getName() + " tiene errores:\n" + resultado, 
                Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
    
    private static boolean esCapacidadValida(int archivosActuales, int cantidadNuevos) {
        return (archivosActuales + cantidadNuevos) <= 5;
    }
    
    private void guardarCambios() {
        boolean exito = persistirCambiosEnBD();
        procesarResultadoGuardado(exito);
    }
    
    public void inicializarSesion(SesionTutoria sesion) {
        this.sesionActual = sesion;
        
        cbSesion.getItems().add("Tutoría No. " + sesion.getNumSesion() + " (" + sesion.getFecha() + ")");
        cbSesion.getSelectionModel().selectFirst();
        cbSesion.setDisable(true);
        
        cargarEvidenciasExistentes();
    }
    
    private byte[] leerBytesDelArchivo(File archivo) {
        try {
            return Files.readAllBytes(archivo.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error de Lectura", 
                "No se pudo procesar el archivo: " + archivo.getName(), Alert.AlertType.ERROR);
            return null;
        }
    }
    
    private void limpiarEstadoLocal() {
        evidenciasPorGuardar.clear();
        evidenciasPorEliminar.clear();
        btnGuardar.setDisable(true);
    }
    
    private ArrayList<Evidencia> obtenerEvidenciasDeBD() {
        HashMap<String, Object> respuesta = EvidenciaImplementacion.obtenerEvidenciasPorSesion(
            Sesion.getIdTutor(), sesionActual.getNumSesion());
        
        if (!(boolean) respuesta.get("error")) {
            return (ArrayList<Evidencia>) respuesta.get("evidencias");
        } else {
            Utilidades.mostrarAlertaSimple("Error", respuesta.get("mensaje").toString(), Alert.AlertType.WARNING);
            return null;
        }
    }
    
    private boolean persistirCambiosEnBD() {
        HashMap<String, Object> respuesta = EvidenciaImplementacion.guardarCambiosEvidencias(
            evidenciasPorGuardar, 
            evidenciasPorEliminar,
            Sesion.getIdTutor(),
            sesionActual.getNumSesion()
        );
        return !(boolean) respuesta.get("error");
    }
    
    private void procesarArchivos(List<File> archivos) {
        if (sesionActual == null) return;

        if (!validarCapacidadDisponible(archivos.size())) {
            return;
        }

        for (File archivo : archivos) {
            if (esArchivoValido(archivo)) {
                Evidencia nuevaEvidencia = crearEvidenciaDesdeArchivo(archivo);
                if (nuevaEvidencia != null) {
                    agregarEvidencia(nuevaEvidencia);
                }
            }
        }
        
    }
    
    private void procesarResultadoGuardado(boolean exito) {
        if (exito) {
            Utilidades.mostrarAlertaSimple("Éxito", "Cambios guardados correctamente.", Alert.AlertType.INFORMATION);
            limpiarEstadoLocal();
            regresarAListado();
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron aplicar los cambios.", Alert.AlertType.WARNING);
        }
    }

    private void procesarEliminacion(Evidencia evidencia) {
        vbListaArchivos.getChildren().removeIf(nodo -> 
            nodo instanceof TarjetaArchivo && ((TarjetaArchivo) nodo).getEvidencia().equals(evidencia)
        );

        if (evidencia.isEsNuevo()) {
            evidenciasPorGuardar.remove(evidencia);
        } else {
            evidenciasPorEliminar.add(evidencia);
        }

        validarBotonGuardar();
    }
    
    private void renderizarListaEvidencias(ArrayList<Evidencia> evidencias) {
        vbListaArchivos.getChildren().clear();
        for (Evidencia ev : evidencias) {
            TarjetaArchivo tarjeta = new TarjetaArchivo(ev, this::procesarEliminacion);
            vbListaArchivos.getChildren().add(tarjeta);
        }
    }
    
    private void regresarAListado() {
        try {
            FXMLLoader cargador = Utilidades.obtenerVista("/gestortutoriasfx/vista/FXMLGestionarEvidencias.fxml");
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
    
    private boolean validarCapacidadDisponible(int cantidadNuevos) {
        int archivosActuales = vbListaArchivos.getChildren().size();

        if (!esCapacidadValida(archivosActuales, cantidadNuevos)) {
            Utilidades.mostrarAlertaSimple("Límite excedido", 
                "Solo puede tener máximo 5 archivos por sesión.", 
                Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
}