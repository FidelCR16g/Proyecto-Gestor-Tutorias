package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.ProgramaEducativoDAO;
import gestortutoriasfx.modelo.pojo.ProgramaEducativo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ProgramaEducativoImplementacion {
    public static HashMap<String, Object> obtenerProgramas() {
        HashMap<String, Object> resp = new LinkedHashMap<>();
        Connection con = null;

        try {
            con = ConexionBD.abrirConexionBD();
            ArrayList<ProgramaEducativo> lista = ProgramaEducativoDAO.obtenerProgramas(con);
            
            resp.put("error", false);
            resp.put("programas", lista);
            
        } catch (SQLException ex) {
            resp.put("error", true);
            resp.put("mensaje", "Error BD: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            ConexionBD.cerrarConexion(con);
        }

        return resp;
    }
}
