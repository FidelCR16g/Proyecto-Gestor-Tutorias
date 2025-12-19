package gestortutoriasfx.controlador;

import java.lang.reflect.Method;
import org.junit.Test;
import static org.junit.Assert.*;

public class FXMLFormularioEvidenciaControllerTest {
    @Test
    public void testFlujoNormal_CapacidadValida() throws Exception {
        int archivosActuales = 2;
        int cantidadNuevos = 2;
        Method metodo = obtenerMetodoCapacidad();

        boolean esValido = (boolean) metodo.invoke(null, archivosActuales, cantidadNuevos);

        assertTrue("La capacidad de 4 archivos debe ser válida", esValido);
    }

    @Test
    public void testFlujoAlterno_CapacidadEnLimite() throws Exception {
        int archivosActuales = 5;
        int cantidadNuevos = 0;
        Method metodo = obtenerMetodoCapacidad();

        boolean esValido = (boolean) metodo.invoke(null, archivosActuales, cantidadNuevos);

        assertTrue("La capacidad de 5 archivos debe ser válida", esValido);
    }

    @Test
    public void testExcepcion_CapacidadExcedida() throws Exception {
        int archivosActuales = 4;
        int cantidadNuevos = 2;
        Method metodo = obtenerMetodoCapacidad();

        boolean esValido = (boolean) metodo.invoke(null, archivosActuales, cantidadNuevos);

        assertFalse("La capacidad de 6 archivos debe ser inválida", esValido);
    }
    
    private Method obtenerMetodoCapacidad() throws NoSuchMethodException {
        Method metodo = FXMLFormularioEvidenciaController.class.getDeclaredMethod(
            "esCapacidadValida", 
            int.class, 
            int.class
        );
        metodo.setAccessible(true);
        return metodo;
    }
}