package poo2.parqueadero.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import poo2.parqueadero.model.dto.BicicletaDTO;
import poo2.parqueadero.model.dto.BicicletaDTOAdapter;
import poo2.parqueadero.model.dto.VehiculoDTO;

public class BicicletaDAO {

    private Connection conn;

    public BicicletaDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/poo2_local", "poo2", "poo2");
        } catch (SQLException e) {
            System.err.println("La conexión no se pudo establecer. " + e.getMessage());
        }

    }

    public BicicletaDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean agregar(BicicletaDTOAdapter dto) {
        boolean exito = false;

        try {

            String horaEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dto.getHoraEntrada());

            Statement statementOb = conn.createStatement();

            StringBuffer sb = new StringBuffer();
            sb.append("INSERT INTO BICICLETA (CCPROPIETARIO, MARCA, TIPO, HORAENTRADA) VALUES ");
            sb.append("('CC " + dto.getBicicleta().getCcPropietario() + "', ");
            sb.append(dto.getMarca() + ", ");
            sb.append("'" + dto.getBicicleta().getTipo() + "', ");
            sb.append("'" + horaEntrada + "')");

            statementOb.executeUpdate(sb.toString());

            StringBuffer sb2 = new StringBuffer();
            sb2.append("INSERT INTO HISTORIAL(TIPO, IDENTIFICADOR, MARCA, HORAINGRESO) ");
            sb2.append("VALUES ('" + 'o' + "', 'CC " + dto.getBicicleta().getCcPropietario()
                    + "', " + Integer.parseInt(dto.getMarca()) + ", '" + horaEntrada + "')");

            statementOb.executeUpdate(sb2.toString());

            exito = true;

        } catch (Exception e) {
            System.err.println("Ocurrió un error insertando el registro (agregarBiciDAO). "
                    + e.getMessage());
        } finally {
            // Close the connection            
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return exito;
    }

    public ArrayList<VehiculoDTO> listar() {
        ArrayList<VehiculoDTO> bicicletas = new ArrayList<>();
        Statement statementOb = null;

        try {
            statementOb = conn.createStatement();

            StringBuffer sb = new StringBuffer();
            sb.append("SELECT IDBICI, CCPROPIETARIO, MNAME, TIPO, HORAENTRADA ");
            sb.append("FROM BICICLETA B, MARCA M ");
            sb.append("WHERE M.MARCANO = B.MARCA ");
            sb.append("ORDER BY MNAME");

            ResultSet rs = statementOb.executeQuery(sb.toString());

            while (rs.next()) {
                BicicletaDTO dto = new BicicletaDTO();
                dto.setIdbici(rs.getString("IDBICI"));
                dto.setPlaca(rs.getString("CCPROPIETARIO"));
                dto.setMarca(rs.getString("MNAME"));
                dto.setTipo(rs.getString("TIPO"));
                dto.setHoraEntrada(rs.getTimestamp("HORAENTRADA"));

                bicicletas.add(dto);
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
        return bicicletas;
    }

    public String eliminar(BicicletaDTO bici, String tiempoParqueado, Timestamp tiempoSalida) {
        try {
            Statement statementOb = conn.createStatement();

            statementOb.executeLargeUpdate("DELETE FROM BICICLETA WHERE CCPROPIETARIO = '"
                    + bici.getPlaca() + "'");

            System.out.println("VEHICULOS ELIMINADOS = " + statementOb.getUpdateCount());

            Statement statementOb2 = conn.createStatement();

            String horaSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tiempoSalida);

            statementOb2.executeLargeUpdate("UPDATE HISTORIAL SET TIEMPO = '"
                    + tiempoParqueado + "', HORASALIDA = '" + horaSalida
                    + "' WHERE IDENTIFICADOR = '" + bici.getPlaca() + "' "
                    + "AND HORAINGRESO = '" + bici.getHoraEntrada() + "'");

        } catch (Exception e) {
            System.err.println("Se presentó un error eliminando (eliminarBiciDAO). " + e.getMessage());
        }
        return tiempoParqueado;
    }
}
