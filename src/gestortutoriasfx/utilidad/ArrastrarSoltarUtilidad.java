package gestortutoriasfx.utilidades;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
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
    
    public static <T> void configurarIntercambioListas(
            ListView<T> origen, 
            ListView<T> destino, 
            Function<T, String> extractorId, 
            Consumer<T> accionPostMovimiento) {

        origen.setOnDragDetected(event -> {
            T item = origen.getSelectionModel().getSelectedItem();
            if (item != null) {
                Dragboard db = origen.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(extractorId.apply(item)); 
                db.setContent(content);
                event.consume();
            }
        });

        destino.setOnDragOver(event -> {
            if (event.getGestureSource() != destino && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        destino.setOnDragDropped(event -> {
            boolean success = false;
            Dragboard db = event.getDragboard();

            if (db.hasString()) {
                String idRecibido = db.getString();
                
                T itemMover = null;
                for (T item : origen.getItems()) {
                    if (extractorId.apply(item).equals(idRecibido)) {
                        itemMover = item;
                        break;
                    }
                }

                if (itemMover != null) {
                    origen.getItems().remove(itemMover);
                    destino.getItems().add(itemMover);
                    
                    if (accionPostMovimiento != null) {
                        accionPostMovimiento.accept(itemMover);
                    }
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
}