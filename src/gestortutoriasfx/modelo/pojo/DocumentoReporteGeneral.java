package gestortutoriasfx.modelo.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class DocumentoReporteGeneral implements Serializable {

    private int idDocumentoGeneral;   
    private int idReporteGeneral;         
    private String nombreArchivo;         
    private String tipoArchivo;           
    private byte[] archivo;               
    private LocalDateTime fechaGeneracion;

    public DocumentoReporteGeneral() { }

    public DocumentoReporteGeneral(int idReporteGeneral, String nombreArchivo, String tipoArchivo, byte[] archivo) {
        this.idReporteGeneral = idReporteGeneral;
        this.nombreArchivo = nombreArchivo;
        this.tipoArchivo = tipoArchivo;
        this.archivo = archivo;
    }

    public Integer getIdDocumentoGeneral() { return idDocumentoGeneral; }
    public void setIdDocumentoGeneral(Integer idDocumentoGeneral) { this.idDocumentoGeneral = idDocumentoGeneral; }

    public int getIdReporteGeneral() { return idReporteGeneral; }
    public void setIdReporteGeneral(int idReporteGeneral) { this.idReporteGeneral = idReporteGeneral; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    public String getTipoArchivo() { return tipoArchivo; }
    public void setTipoArchivo(String tipoArchivo) { this.tipoArchivo = tipoArchivo; }

    public byte[] getArchivo() { return archivo; }
    public void setArchivo(byte[] archivo) { this.archivo = archivo; }

    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DocumentoReporteGeneral)) return false;
        DocumentoReporteGeneral that = (DocumentoReporteGeneral) o;
        return Objects.equals(idDocumentoGeneral, that.idDocumentoGeneral);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDocumentoGeneral);
    }
}
