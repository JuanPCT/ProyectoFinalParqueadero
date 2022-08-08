package poo2.parqueadero.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import poo2.parqueadero.model.dto.CarroDTO;
import poo2.parqueadero.model.dto.VehiculoDTO;

public class ParqueadosDAO {

    private Connection conn;

    public ParqueadosDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/poo2_local", "poo2", "poo2");
        } catch (SQLException e) {
            System.err.println("La conexión no se pudo establecer. " + e.getMessage());
        }

    }

    public ParqueadosDAO(Connection conn) {
        this.conn = conn;
    }

    public ArrayList<VehiculoDTO> listarVehiculos() {
        ArrayList<VehiculoDTO> vehiculos = new ArrayList<>();
        Statement statementOb = null;

        try {
            statementOb = conn.createStatement();

            StringBuffer sb = new StringBuffer();
            sb.append("SELECT IDENTIFICADOR, MNAME, HORAINGRESO, HORASALIDA ");
            sb.append("FROM HISTORIAL H, MARCA M ");
            sb.append("WHERE M.MARCANO = H.MARCA ");
            sb.append("ORDER BY MNAME");

            ResultSet rs = statementOb.executeQuery(sb.toString());

            while (rs.next()) {
                CarroDTO dto = new CarroDTO();
                dto.setPlaca(rs.getString("IDENTIFICADOR"));
                dto.setMarca(rs.getString("MNAME"));
                dto.setHoraEntrada(rs.getTimestamp("HORAINGRESO"));
                dto.setHoraSalida(rs.getTimestamp("HORASALIDA"));

                vehiculos.add(dto);

            }

        } catch (Exception e) {
            System.err.println("Se presentó un error "
                    + "ejecutando la consulta. (listarCarroDAO) " + e.getMessage());
        } finally {
            // Close the connection            
            try {
                statementOb.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return vehiculos;
    }
}
