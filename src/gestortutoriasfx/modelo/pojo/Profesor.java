package gestortutoriasfx.modelo.pojo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Profesor implements Serializable {

    private int idProfesor;          
    private String numPersonal;          
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private LocalDate fechaNacimiento;
    private LocalDate fechaContratacion;
    private String telefono;
    private String email;
    private String gradoEstudios;
    
    private int antiguedadAnios;     
    private Boolean perfilActivo;        

    public Profesor() {
        this.perfilActivo = true;
    }

    public Profesor(int idProfesor, String numPersonal, String nombre, String apellidoPaterno,
                    String apellidoMaterno, LocalDate fechaNacimiento, LocalDate fechaContratacion,
                    String telefono, String email, String gradoEstudios, int antiguedadAnios,
                    Boolean perfilActivo) {
        this.idProfesor = idProfesor;
        this.numPersonal = numPersonal;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaContratacion = fechaContratacion;
        this.telefono = telefono;
        this.email = email;
        this.gradoEstudios = gradoEstudios;
        this.antiguedadAnios = antiguedadAnios;
        this.perfilActivo = perfilActivo;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getNumPersonal() {
        return numPersonal;
    }

    public void setNumPersonal(String numPersonal) {
        this.numPersonal = numPersonal;
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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGradoEstudios() {
        return gradoEstudios;
    }

    public void setGradoEstudios(String gradoEstudios) {
        this.gradoEstudios = gradoEstudios;
    }

    public int getAntiguedadAnios() {
        return antiguedadAnios;
    }

    public void setAntiguedadAnios(int antiguedadAnios) {
        this.antiguedadAnios = antiguedadAnios;
    }

    public Boolean getPerfilActivo() {
        return perfilActivo;
    }

    public void setPerfilActivo(Boolean perfilActivo) {
        this.perfilActivo = perfilActivo;
    }
    
    public String getNombreCompleto() {
        return (nombre + " " + apellidoPaterno + " " + apellidoMaterno).trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profesor)) return false;
        Profesor profesor = (Profesor) o;
        return Objects.equals(idProfesor, profesor.idProfesor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProfesor);
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (" + numPersonal + ")";
    }
}