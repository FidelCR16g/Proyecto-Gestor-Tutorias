package gestortutoriasfx.utilidades;

import java.io.File;

public class ValidadorEvidencia {

    private static final long LIMITE_BYTES = ((long) 25 * 1024 * 1024);

    public static boolean esExtensionValida(String nombreArchivo) {
        if (nombreArchivo == null) return false;
        String nombre = nombreArchivo.toLowerCase();
        return nombre.endsWith(".pdf") || nombre.endsWith(".jpg") || 
               nombre.endsWith(".jpeg") || nombre.endsWith(".png");
    }

    public static boolean esPesoValido(long pesoBytes) {
        return pesoBytes <= LIMITE_BYTES;
    }
    
    public static String validarArchivo(File archivo) {
        if (!esExtensionValida(archivo.getName())) {
            return "Formato no válido. Solo PDF, JPG y PNG.";
        }
        if (!esPesoValido(archivo.length())) {
            return "El archivo excede el límite de 25MB.";
        }
        return "OK";
    }
}