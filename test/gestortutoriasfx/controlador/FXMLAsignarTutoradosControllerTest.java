package gestortutoriasfx.controlador;

import gestortutoriasfx.modelo.pojo.Estudiante;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class FXMLAsignarTutoradosControllerTest {
    @Test
    public void testFlujoNormal_AsignarEstudiante() throws Exception {
        FXMLAsignarTutoradosController controller = new FXMLAsignarTutoradosController();
        injectarListas(controller);
        Estudiante alumno = new Estudiante();
        alumno.setMatricula("S20001");
        Method metodo = obtenerMetodoRegistrarCambio();
        metodo.invoke(controller, alumno, true);

        ArrayList<Estudiante> nuevos = obtenerCampoLista(controller, "nuevosAsignados");
        
        assertTrue("El alumno debe estar en la lista de nuevos asignados", nuevos.contains(alumno));
    }

    @Test
    public void testFlujoAlterno_DeshacerAsignacion() throws Exception {
        FXMLAsignarTutoradosController controller = new FXMLAsignarTutoradosController();
        injectarListas(controller);
        Estudiante alumno = new Estudiante();
        alumno.setMatricula("S20002");
        Method metodo = obtenerMetodoRegistrarCambio();
        metodo.invoke(controller, alumno, true);
        metodo.invoke(controller, alumno, false);

        ArrayList<Estudiante> nuevos = obtenerCampoLista(controller, "nuevosAsignados");
        ArrayList<Estudiante> desasignados = obtenerCampoLista(controller, "desasignados");

        assertFalse("El alumno ya no debe estar en nuevos asignados", nuevos.contains(alumno));
        assertFalse("El alumno tampoco debe estar en desasignados (era nuevo)", desasignados.contains(alumno));
    }

    @Test
    public void testExcepcion_ListasNoInicializadas() throws Exception {
        FXMLAsignarTutoradosController controller = new FXMLAsignarTutoradosController();
        Estudiante alumno = new Estudiante();
        Method metodo = obtenerMetodoRegistrarCambio();

        try {
            metodo.invoke(controller, alumno, true);
            fail("Debería haber fallado porque las listas son null");
        } catch (java.lang.reflect.InvocationTargetException e) {
            // 3. Verificar que la causa fue NullPointerException
            if (e.getCause() instanceof NullPointerException) {
                assertTrue(true);
            } else {
                fail("Se esperaba NullPointerException, pero ocurrió: " + e.getCause());
            }
        }
    }
    
    private Method obtenerMetodoRegistrarCambio() throws NoSuchMethodException {
        Method metodo = FXMLAsignarTutoradosController.class.getDeclaredMethod(
            "registrarCambio", Estudiante.class, boolean.class
        );
        metodo.setAccessible(true);
        return metodo;
    }

    private void injectarListas(Object controller) throws Exception {
        setCampoPrivado(controller, "nuevosAsignados", new ArrayList<Estudiante>());
        setCampoPrivado(controller, "desasignados", new ArrayList<Estudiante>());
    }

    private void setCampoPrivado(Object objeto, String nombreCampo, Object valor) throws Exception {
        Field campo = FXMLAsignarTutoradosController.class.getDeclaredField(nombreCampo);
        campo.setAccessible(true);
        campo.set(objeto, valor);
    }

    private ArrayList<Estudiante> obtenerCampoLista(Object objeto, String nombreCampo) throws Exception {
        Field campo = FXMLAsignarTutoradosController.class.getDeclaredField(nombreCampo);
        campo.setAccessible(true);
        return (ArrayList<Estudiante>) campo.get(objeto);
    }
}