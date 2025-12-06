package gestortutoriasfx.controlador;

import gestortutoriasfx.modelo.pojo.Usuario;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class FXMLSeleccionRolController implements Initializable {

    @FXML
    private VBox vbContenedorBotones;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void inicializar(Usuario usuario, List<String> roles, FXMLInicioSesionController loginController) {
        vbContenedorBotones.getChildren().clear();
        
        for (String rol : roles) {
            Button btnRol = crearBotonRol(rol);
            
            btnRol.setOnAction(event -> {
                loginController.ingresarAlSistema(usuario, rol);
                cerrarVentana();
            });
            
            vbContenedorBotones.getChildren().add(btnRol);
        }
    }
    
    private Button crearBotonRol(String nombreRol) {
        Button btn = new Button(nombreRol);
        btn.setPrefWidth(250);
        btn.setPrefHeight(45);
        btn.setFont(new Font("System Bold", 14));
        btn.setStyle("-fx-background-color: #0277bd; -fx-text-fill: white; -fx-cursor: hand; ");
        
        VBox.setMargin(btn, new Insets(5, 0, 5, 0));
        
        return btn;
    }
    
    private void cerrarVentana() {
        Stage escenario = (Stage) vbContenedorBotones.getScene().getWindow();
        escenario.close();
    }
}