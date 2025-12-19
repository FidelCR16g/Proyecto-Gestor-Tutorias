package gestortutoriasfx.controlador;

import java.lang.reflect.Method;
import org.junit.Test;
import static org.junit.Assert.*;

public class FXMLFormularioReporteTutoriaControllerTest {
    @Test
    public void testFlujoNormal_DatosValidos() throws Exception {
        String ee = "Ingeniería de Software";
        String profesor = "Juan Pérez";
        String problematica = "Alto índice de reprobación";
        String numAlumnos = "5";
        Method metodo = obtenerMetodoValidacion();
        
        boolean resultado = (boolean) metodo.invoke(null, ee, profesor, problematica, numAlumnos);

        assertTrue("El sistema debe aceptar datos válidos", resultado);
    }
    
    @Test
    public void testFlujoAlterno_CamposVacios() throws Exception {
        String ee = "Redes";
        String profesor = ""; 
        String problematica = "Falta de equipo";
        String numAlumnos = "10";
        Method metodo = obtenerMetodoValidacion();
        
        boolean resultado = (boolean) metodo.invoke(null, ee, profesor, problematica, numAlumnos);
        
        assertFalse("El sistema debe rechazar campos vacíos", resultado);
    }
    
    @Test
    public void testExcepcion_FormatoNumericoIncorrecto() throws Exception {
        String numAlumnosInvalido = "Cinco";
        Method metodo = obtenerMetodoValidacion();
        boolean resultado = (boolean) metodo.invoke(null, "EE", "Prof", "Prob", numAlumnosInvalido);

        assertFalse("El sistema debe manejar el error de formato numérico devolviendo false", resultado);
    }

    private Method obtenerMetodoValidacion() throws NoSuchMethodException {
        Method metodo = FXMLFormularioReporteTutoriaController.class.getDeclaredMethod(
            "validarCamposLogica", String.class, String.class, String.class, String.class
        );
        metodo.setAccessible(true);
        return metodo;
    }
}