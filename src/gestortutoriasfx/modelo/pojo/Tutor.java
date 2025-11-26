/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestortutoriasfx.modelo.pojo;

/**
 *
 * @author fave6
 */
public class Tutor {
    private int idTutor;
    private String noPersonal;
    private int idUsuario;

    public Tutor() {
    }
    
    public Tutor(int idTutor, String noPersonal, int idUsuario) {
        this.idTutor = idTutor;
        this.noPersonal = noPersonal;
        this.idUsuario = idUsuario;
    }

    public int getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(int idTutor) {
        this.idTutor = idTutor;
    }

    public String getNoPersonal() {
        return noPersonal;
    }

    public void setNoPersonal(String noPersonal) {
        this.noPersonal = noPersonal;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
