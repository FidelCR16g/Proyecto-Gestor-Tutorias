package gestortutoriasfx.controlador;

import gestortutoriasfx.modelo.pojo.Estudiante;
import gestortutoriasfx.modelo.pojo.FechaTutoria;
import gestortutoriasfx.modelo.pojo.Salon;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalTime;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class FXMLRegistrarHorarioControllerTest {
    
    @Test
    public void testFlujoNormal_CalculoSecuencia() throws Exception {
        FXMLRegistrarHorarioController controller = new FXMLRegistrarHorarioController();
        injectarPeriodoDummy(controller);

        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        estudiantes.add(new Estudiante());
        estudiantes.add(new Estudiante());
        estudiantes.add(new Estudiante());

        LocalTime horaInicio = LocalTime.of(9, 0);

        Method metodo = obtenerMetodoCalculo();
        ArrayList<SesionTutoria> resultado = (ArrayList<SesionTutoria>) metodo.invoke(
            controller, estudiantes, new FechaTutoria(), new Salon(), "Presencial", horaInicio
        );

        assertEquals("Deben generarse 3 sesiones", 3, resultado.size());
        assertEquals("09:00", resultado.get(0).getHoraInicio());
        assertEquals("09:20", resultado.get(1).getHoraInicio());
        assertEquals("09:40", resultado.get(2).getHoraInicio());
    }


    @Test
    public void testFlujoAlterno_ModalidadVirtual() throws Exception {
        FXMLRegistrarHorarioController controller = new FXMLRegistrarHorarioController();
        injectarPeriodoDummy(controller);

        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        estudiantes.add(new Estudiante()); 

        Salon salonVirtual = new Salon();
        salonVirtual.setNombreSalon("Zoom Personal");
        String modalidad = "Virtual";

        Method metodo = obtenerMetodoCalculo();
        ArrayList<SesionTutoria> resultado = (ArrayList<SesionTutoria>) metodo.invoke(
            controller, estudiantes, new FechaTutoria(), salonVirtual, modalidad, LocalTime.of(16, 30)
        );

        SesionTutoria sesion = resultado.get(0);
        assertEquals("La modalidad debe ser Virtual", "Virtual", sesion.getModalidad());
        assertEquals("16:30", sesion.getHoraInicio());
    }


    @Test
    public void testExcepcion_ListaVacia() throws Exception {
        FXMLRegistrarHorarioController controller = new FXMLRegistrarHorarioController();
        injectarPeriodoDummy(controller);
        
        ArrayList<Estudiante> listaVacia = new ArrayList<>();

        Method metodo = obtenerMetodoCalculo();
        ArrayList<SesionTutoria> resultado = (ArrayList<SesionTutoria>) metodo.invoke(
            controller, listaVacia, new FechaTutoria(), new Salon(), "Presencial", LocalTime.of(10, 0)
        );

        assertNotNull("El resultado no debe ser nulo", resultado);
        assertTrue("La lista resultante debe estar vacía", resultado.isEmpty());
    }


    private Method obtenerMetodoCalculo() throws NoSuchMethodException {
        Method metodo = FXMLRegistrarHorarioController.class.getDeclaredMethod(
            "calcularSecuenciaDeHorarios", 
            ArrayList.class, FechaTutoria.class, Salon.class, String.class, LocalTime.class
        );
        metodo.setAccessible(true);
        return metodo;
    }

    private void injectarPeriodoDummy(Object controller) throws Exception {
        Field campo = FXMLRegistrarHorarioController.class.getDeclaredField("periodoActual");
        campo.setAccessible(true);
        gestortutoriasfx.modelo.pojo.PeriodoEscolar periodo = new gestortutoriasfx.modelo.pojo.PeriodoEscolar();
        periodo.setIdPeriodoEscolar(1);
        campo.set(controller, periodo);
    }
}