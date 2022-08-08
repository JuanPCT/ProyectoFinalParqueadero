package poo2.parqueadero.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import poo2.parqueadero.model.dto.MotoDTO;
import poo2.parqueadero.model.dto.VehiculoDTO;

public class MotoDAO {

    private Connection conn;

    public MotoDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/poo2_local", "poo2", "poo2");
        } catch (SQLException e) {
            System.err.println("La conexión no se pudo establecer. " + e.getMessage());
        }

    }

    public MotoDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean agregar(MotoDTO moto) {
        boolean exito = false;

        try {
            Statement statementOb = conn.createStatement();
            String horaEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(moto.getHoraEntrada());
            StringBuffer sb = new StringBuffer();

            sb.append("INSERT INTO MOTO(PLACA, MARCA, MODELO, CILINDRAJE, TIPO, HORAENTRADA) ");
            sb.append("VALUES ('" + moto.getPlaca() + "', " + moto.getMarca() + ", "
                    + moto.getModelo() + ", " + moto.getCilindraje() + ", '" + moto.getTipo()
                    + "', '" + horaEntrada + "')");

            statementOb.executeUpdate(sb.toString());

            StringBuffer sb2 = new StringBuffer();

            sb2.append("INSERT INTO HISTORIAL(TIPO, IDENTIFICADOR, MARCA, HORAINGRESO) ");
            sb2.append("VALUES ('" + 'm' + "', '" + moto.getPlaca()
                    + "', " + moto.getMarca() + ", '" + horaEntrada + "')");

            statementOb.executeUpdate(sb2.toString());

            exito = true;

        } catch (Exception e) {
            System.err.println("Ocurrió un error "
                    + "insertando el registro. (agregarMotoDAO)" + e.getMessage());
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
        ArrayList<VehiculoDTO> motos = new ArrayList<>();
        Statement statementOb = null;

        try {
            statementOb = conn.createStatement();

            StringBuffer sb = new StringBuffer();
            sb.append("SELECT PLACA, MNAME, MODELO, CILINDRAJE, TIPO, HORAENTRADA ");
            sb.append("FROM MOTO T, MARCA M ");
            sb.append("WHERE M.MARCANO = T.MARCA ");
            sb.append("ORDER BY MNAME");

            ResultSet rs = statementOb.executeQuery(sb.toString());

            while (rs.next()) {
                MotoDTO dto = new MotoDTO();
                dto.setPlaca(rs.getString("PLACA"));
                dto.setMarca(rs.getString("MNAME"));
                dto.setModelo(rs.getString("MODELO"));
                dto.setCilindraje(rs.getString("CILINDRAJE"));
                dto.setTipo(rs.getString("TIPO"));
                dto.setHoraEntrada(rs.getTimestamp("HORAENTRADA"));

                motos.add(dto);

            }

        } catch (Exception e) {
            System.err.println("Se presentó un error "
                    + "ejecutando la consulta. (listarMotoDAO) " + e.getMessage());
        } finally {
            // Close the connection            
            try {
                statementOb.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return motos;
    }

    public String eliminar(MotoDTO moto, String tiempoParqueado, Timestamp tiempoSalida) {
        try {
            Statement statementOb = conn.createStatement();
            statementOb.executeLargeUpdate("DELETE FROM MOTO WHERE PLACA = '"
                    + moto.getPlaca() + "'");

            System.out.println("VEHICULOS ELIMINADOS = " + statementOb.getUpdateCount());

            Statement statementOb2 = conn.createStatement();

            String horaSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tiempoSalida);

            statementOb2.executeLargeUpdate("UPDATE HISTORIAL SET TIEMPO = '"
                    + tiempoParqueado + "', HORASALIDA = '" + horaSalida
                    + "' WHERE IDENTIFICADOR = '" + moto.getPlaca() + "' "
                    + "AND HORAINGRESO = '" + moto.getHoraEntrada() + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tiempoParqueado;
    }
}
