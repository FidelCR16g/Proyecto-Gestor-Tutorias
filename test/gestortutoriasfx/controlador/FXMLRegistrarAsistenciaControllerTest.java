package gestortutoriasfx.controlador;

import gestortutoriasfx.modelo.pojo.SesionTutoria;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class FXMLRegistrarAsistenciaControllerTest {
    @Test
    public void testFlujoNormal_PrepararLista() throws Exception {
        FXMLRegistrarAsistenciaController controller = new FXMLRegistrarAsistenciaController();
        
        List<SesionTutoria> asistentes = new ArrayList<>();
        SesionTutoria s1 = new SesionTutoria(); 
        s1.setIdSesion(100);
        asistentes.add(s1);
        
        List<SesionTutoria> faltantes = new ArrayList<>();
        SesionTutoria s2 = new SesionTutoria();
        s2.setIdSesion(200);
        faltantes.add(s2);

        Method metodo = FXMLRegistrarAsistenciaController.class.getDeclaredMethod(
            "prepararListaParaGuardar", 
            List.class,
            List.class
        );
        metodo.setAccessible(true);

        ArrayList<SesionTutoria> resultado = (ArrayList<SesionTutoria>) metodo.invoke(controller, asistentes, faltantes);

        assertEquals("La lista final debe tener 2 elementos", 2, resultado.size());
        
        SesionTutoria resultado1 = resultado.stream().filter(s -> s.getIdSesion() == 100).findFirst().orElse(null);
        assertNotNull(resultado1);
        assertEquals("El estudiante de la lista 1 debe tener 'Asistio'", "Asistio", resultado1.getEstado());

        SesionTutoria resultado2 = resultado.stream().filter(s -> s.getIdSesion() == 200).findFirst().orElse(null);
        assertNotNull(resultado2);
        assertEquals("El estudiante de la lista 2 debe tener 'No Asistio'", "No Asistio", resultado2.getEstado());
    }

    @Test
    public void testFlujoAlterno_ListasVacias() throws Exception {
        FXMLRegistrarAsistenciaController controller = new FXMLRegistrarAsistenciaController();
        List<SesionTutoria> vacia1 = new ArrayList<>();
        List<SesionTutoria> vacia2 = new ArrayList<>();

        Method metodo = FXMLRegistrarAsistenciaController.class.getDeclaredMethod(
            "prepararListaParaGuardar", List.class, List.class
        );
        metodo.setAccessible(true);

        ArrayList<SesionTutoria> resultado = (ArrayList<SesionTutoria>) metodo.invoke(controller, vacia1, vacia2);

        assertNotNull("El resultado no debe ser nulo", resultado);
        assertTrue("La lista resultante debe estar vacía", resultado.isEmpty());
    }
    
    @Test
    public void testExcepcion_ElementoNuloEnLista() throws Exception {
        FXMLRegistrarAsistenciaController controller = new FXMLRegistrarAsistenciaController();

        List<SesionTutoria> listaCorrupta = new ArrayList<>();
        listaCorrupta.add(null); 

        Method metodo = FXMLRegistrarAsistenciaController.class.getDeclaredMethod(
            "prepararListaParaGuardar", List.class, List.class
        );
        metodo.setAccessible(true);

        try {
            metodo.invoke(controller, listaCorrupta, new ArrayList<SesionTutoria>());
            fail("La prueba debería haber fallado lanzando una excepción, pero no lo hizo.");
            
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (e.getCause() instanceof NullPointerException) {
                assertTrue(true);
            } else {
                fail("Se esperaba NullPointerException, pero ocurrió: " + e.getCause());
            }
        }
    }
}