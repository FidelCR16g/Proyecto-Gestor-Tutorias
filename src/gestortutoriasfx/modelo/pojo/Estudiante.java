package gestortutoriasfx.modelo.pojo;

/**
 * Nombre de la Clase: Estudiante
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 25/11/2025
 *
 * Descripción:
 * Clase encargada de contener los datos del Estudiante.
 */

public class Estudiante {
    private int idEstudiante;
    private String matricula;
    private int idProgramaEducativo;
    private int idTutor;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String telefono;
    private byte[] foto;
    private String correoInstitucional;
    private int anioIngreso;
    private int semestre;
    private int creditosObtenidos;
    private boolean situacionRiesgo;
    private int cambioTutor;

    private boolean perfilActivo;
    
    private String nombreProgramaEducativo;

    public Estudiante() {
    }

    public Estudiante(int idEstudiante, String matricula, int idProgramaEducativo, int idTutor, String nombre, String apellidoPaterno, String apellidoMaterno, String telefono, byte[] foto, String correoInstitucional, int anioIngreso, int semestre, int creditosObtenidos, boolean situacionRiesgo, int cambioTutor, boolean perfilActivo, String nombreProgramaEducativo) {
        this.idEstudiante = idEstudiante;
        this.matricula = matricula;
        this.idProgramaEducativo = idProgramaEducativo;
        this.idTutor = idTutor;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
        this.foto = foto;
        this.correoInstitucional = correoInstitucional;
        this.anioIngreso = anioIngreso;
        this.semestre = semestre;
        this.creditosObtenidos = creditosObtenidos;
        this.situacionRiesgo = situacionRiesgo;
        this.cambioTutor = cambioTutor;
        this.perfilActivo = perfilActivo;
        this.nombreProgramaEducativo = nombreProgramaEducativo;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public boolean isPerfilActivo() {
        return perfilActivo;
    }

    public void setPerfilActivo(boolean perfilActivo) {
        this.perfilActivo = perfilActivo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getIdProgramaEducativo() {
        return idProgramaEducativo;
    }

    public void setIdProgramaEducativo(int idProgramaEducativo) {
        this.idProgramaEducativo = idProgramaEducativo;
    }

    public int getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(int idTutor) {
        this.idTutor = idTutor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public int getAnioIngreso() {
        return anioIngreso;
    }

    public void setAnioIngreso(int anioIngreso) {
        this.anioIngreso = anioIngreso;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public int getCreditosObtenidos() {
        return creditosObtenidos;
    }

    public void setCreditosObtenidos(int creditosObtenidos) {
        this.creditosObtenidos = creditosObtenidos;
    }

    public boolean isSituacionRiesgo() {
        return situacionRiesgo;
    }

    public void setSituacionRiesgo(boolean situacionRiesgo) {
        this.situacionRiesgo = situacionRiesgo;
    }

    public int getCambioTutor() {
        return cambioTutor;
    }

    public void setCambioTutor(int cambioTutor) {
        this.cambioTutor = cambioTutor;
    }

    public String getNombreProgramaEducativo() {
        return nombreProgramaEducativo;
    }

    public void setNombreProgramaEducativo(String nombreProgramaEducativo) {
        this.nombreProgramaEducativo = nombreProgramaEducativo;
    }
    
    public String getNombreCompleto() {
        return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
    }
    
    public void setNombreCompleto(String nombre, String apellidoPaterno, String apellidoMaterno) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " | " + getMatricula() + " | " + getNombreProgramaEducativo();
    }
}
