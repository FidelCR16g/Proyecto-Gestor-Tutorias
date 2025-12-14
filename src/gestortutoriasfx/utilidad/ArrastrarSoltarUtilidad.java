package gestortutoriasfx.utilidades;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.Node;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class ArrastrarSoltarUtilidad {
    private static final String ESTILO_ZONA_BASE = "-fx-background-color: #e3f2fd; "
            + "-fx-border-color: #2196f3; -fx-border-width: 2; "
            + "-fx-border-style: dashed; -fx-background-radius: 10; "
            + "-fx-border-radius: 10; -fx-cursor: hand;";

    private static final String ESTILO_ZONA_HOVER = "-fx-background-color: #bbdefb; "
            + "-fx-border-color: #1565c0; -fx-border-width: 2; "
            + "-fx-border-style: dashed; -fx-background-radius: 10; "
            + "-fx-border-radius: 10;";

    public static void configurarZonaArrastrarSoltar(Node zonaVisual, Consumer<List<File>> accionAlSoltar, Runnable accionDobleClick) {
        zonaVisual.setStyle(ESTILO_ZONA_BASE);

        zonaVisual.setOnDragEntered(event -> {
            if (event.getDragboard().hasFiles()) {
                zonaVisual.setStyle(ESTILO_ZONA_HOVER);
            }
        });

        zonaVisual.setOnDragExited(event -> zonaVisual.setStyle(ESTILO_ZONA_BASE));

        zonaVisual.setOnDragOver(event -> {
            if (event.getGestureSource() != zonaVisual && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        zonaVisual.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean exito = false;
            
            if (db.hasFiles()) {
                accionAlSoltar.accept(db.getFiles());
                exito = true;
            }
            
            event.setDropCompleted(exito);
            event.consume();
            zonaVisual.setStyle(ESTILO_ZONA_BASE);
        });
        
        if (accionDobleClick != null) {
            zonaVisual.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    accionDobleClick.run();
                }
            });
        }
    }
}