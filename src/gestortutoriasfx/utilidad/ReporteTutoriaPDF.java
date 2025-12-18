package gestortutoriasfx.utilidades;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import gestortutoriasfx.modelo.pojo.ProblematicaAcademica;
import gestortutoriasfx.modelo.pojo.ReporteTutoria;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReporteTutoriaPDF {

    private static final Font FUENTE_TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
    private static final Font FUENTE_NEGRITA = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
    private static final Font FUENTE_NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 10);
    private static final Font FUENTE_PIE = FontFactory.getFont(FontFactory.HELVETICA, 10);

    public static boolean generarReporte(
            ReporteTutoria reporte,
            PeriodoEscolar periodo,
            List<ProblematicaAcademica> listaProblemas,
            String nombreTutor,
            File archivoDestino) {

        Document documento = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(documento, new FileOutputStream(archivoDestino));
            documento.open();

            agregarEncabezado(documento, periodo, reporte);
            agregarEstadisticas(documento, reporte);
            agregarTablaProblemas(documento, listaProblemas);
            agregarComentarios(documento, reporte);
            agregarFirmas(documento, nombreTutor);

            documento.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void agregarEncabezado(Document doc, PeriodoEscolar periodo, ReporteTutoria reporte) throws DocumentException {
        PdfPTable tablaEncabezado = new PdfPTable(2);
        tablaEncabezado.setWidthPercentage(100);
        
        agregarCeldaSinBorde(tablaEncabezado, "Programa Educativo: Ingeniería de Software", true);
        agregarCeldaSinBorde(tablaEncabezado, "Fecha Primer día de tutoría: " + formatearFecha(periodo.getFechaInicio()), false);

        agregarCeldaSinBorde(tablaEncabezado, "Periodo: " + periodo.getNombrePeriodoEscolar(), true);
        agregarCeldaSinBorde(tablaEncabezado, "Fecha Último día de tutoría: " + formatearFecha(periodo.getFechaFin()), false);
        
        PdfPCell celdaSesion = new PdfPCell(new Phrase("No. de sesión: " + reporte.getNumSesion() + "° Tutoría", FUENTE_NEGRITA));
        celdaSesion.setColspan(2);
        celdaSesion.setBorder(0);
        celdaSesion.setPaddingTop(5);
        tablaEncabezado.addCell(celdaSesion);

        doc.add(tablaEncabezado);
        doc.add(Chunk.NEWLINE);
    }

    private static void agregarEstadisticas(Document doc, ReporteTutoria reporte) throws DocumentException {
        PdfPTable tablaStats = new PdfPTable(2);
        tablaStats.setWidthPercentage(100);
        agregarCeldaSinBorde(tablaStats, "Núm. Alumnos que asistieron: " + reporte.getNumAlumnosAsistieron(), true);
        agregarCeldaSinBorde(tablaStats, "Núm. Alumnos en riesgo: " + reporte.getNumAlumnosRiesgo(), false);
        doc.add(tablaStats);
        doc.add(Chunk.NEWLINE);
    }

    private static void agregarTablaProblemas(Document doc, List<ProblematicaAcademica> listaProblemas) throws DocumentException {
        Paragraph tituloProblemas = new Paragraph("Problemas académicos detectados:", FUENTE_NEGRITA);
        doc.add(tituloProblemas);
        doc.add(new Paragraph(" ")); 

        if (listaProblemas == null || listaProblemas.isEmpty()) {
            doc.add(new Paragraph("No se reportan problemas académicos.", FUENTE_NORMAL));
        } else {
            PdfPTable tablaProblemas = new PdfPTable(4);
            tablaProblemas.setWidthPercentage(100);
            tablaProblemas.setWidths(new float[]{30f, 25f, 35f, 10f});

            agregarCabeceraTabla(tablaProblemas, "Experiencia Educativa");
            agregarCabeceraTabla(tablaProblemas, "Profesor");
            agregarCabeceraTabla(tablaProblemas, "Problema");
            agregarCabeceraTabla(tablaProblemas, "Núm. Alum");

            for (ProblematicaAcademica problema : listaProblemas) {
                tablaProblemas.addCell(crearCeldaNormal(problema.getNombreExperienciaEducativa()));
                tablaProblemas.addCell(crearCeldaNormal(problema.getNombreProfesor()));
                tablaProblemas.addCell(crearCeldaNormal(problema.getProblema()));
                tablaProblemas.addCell(crearCeldaCentrada(String.valueOf(problema.getNumEstudiantes())));
            }
            doc.add(tablaProblemas);
        }
        doc.add(Chunk.NEWLINE);
    }

    private static void agregarComentarios(Document doc, ReporteTutoria reporte) throws DocumentException {
        Paragraph tituloComentarios = new Paragraph("Comentarios Generales:", FUENTE_NEGRITA);
        doc.add(tituloComentarios);
        String texto;
        
        if(reporte.getComentarios() == null || reporte.getComentarios().isEmpty()){
            texto = "Sin comentarios adicionales.";
        }else{
            texto = reporte.getComentarios();
        }
        
        Paragraph contenido = new Paragraph(texto, FUENTE_NORMAL);
        contenido.setSpacingBefore(5);
        doc.add(contenido);
        
        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);
    }

    private static void agregarFirmas(Document doc, String nombreTutor) throws DocumentException {
        Paragraph atentamente = new Paragraph("Atentamente", FUENTE_PIE);
        atentamente.setAlignment(Element.ALIGN_CENTER);
        doc.add(atentamente);

        String fechaHoy = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES")).format(new Date());
        Paragraph lugarFecha = new Paragraph("Xalapa, Ver., a " + fechaHoy, FUENTE_PIE);
        lugarFecha.setAlignment(Element.ALIGN_CENTER);
        doc.add(lugarFecha);

        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);

        Paragraph nombre = new Paragraph(nombreTutor, FUENTE_NEGRITA);
        nombre.setAlignment(Element.ALIGN_CENTER);
        doc.add(nombre);

        Paragraph firma = new Paragraph("Nombre y firma del Tutor", FUENTE_PIE);
        firma.setAlignment(Element.ALIGN_CENTER);
        doc.add(firma);
    }

    private static void agregarCeldaSinBorde(PdfPTable tabla, String texto, boolean esNegrita) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, esNegrita ? FUENTE_NEGRITA : FUENTE_NORMAL));
        celda.setBorder(0);
        celda.setPaddingBottom(5);
        tabla.addCell(celda);
    }

    private static void agregarCabeceraTabla(PdfPTable tabla, String titulo) {
        PdfPCell celda = new PdfPCell(new Phrase(titulo, FUENTE_NEGRITA));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celda.setPadding(5);
        tabla.addCell(celda);
    }

    private static PdfPCell crearCeldaNormal(String contenido) {
        PdfPCell celda = new PdfPCell(new Phrase(contenido, FUENTE_NORMAL));
        celda.setPadding(5);
        return celda;
    }
    
    private static PdfPCell crearCeldaCentrada(String contenido) {
        PdfPCell celda = new PdfPCell(new Phrase(contenido, FUENTE_NORMAL));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(5);
        return celda;
    }

    private static String formatearFecha(String fechaBD) {
        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = formatoEntrada.parse(fechaBD);
            SimpleDateFormat formatoSalida = new SimpleDateFormat("d-MMM-yy", new Locale("es", "ES"));
            return formatoSalida.format(fecha);
        } catch (Exception e) {
            return fechaBD;
        } 
    }
}