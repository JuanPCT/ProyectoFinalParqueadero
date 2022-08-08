package poo2.parqueadero.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import poo2.parqueadero.model.dto.CarroDTO;
import poo2.parqueadero.model.dto.VehiculoDTO;

public class CarroDAO {

    private Connection conn;

    public CarroDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/poo2_local", "poo2", "poo2");
        } catch (SQLException e) {
            System.err.println("La conexión no se pudo establecer. " + e.getMessage());
        }

    }

    public CarroDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean agregar(CarroDTO carro) {
        boolean exito = false;

        try {
            Statement statementOb = conn.createStatement();
            String horaEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(carro.getHoraEntrada());
            StringBuffer sb = new StringBuffer();

            sb.append("INSERT INTO CARRO(PLACA, MARCA, MODELO, CILINDRAJE, PUERTAS, HORAENTRADA) ");
            sb.append("VALUES ('" + carro.getPlaca() + "', " + carro.getMarca() + ", "
                    + carro.getModelo() + ", " + carro.getCilindraje() + ", " + carro.getPuertas()
                    + ", '" + horaEntrada + "')");

            statementOb.executeUpdate(sb.toString());

            StringBuffer sb2 = new StringBuffer();

            sb2.append("INSERT INTO HISTORIAL(TIPO, IDENTIFICADOR, MARCA, HORAINGRESO) ");
            sb2.append("VALUES ('" + 'c' + "', '" + carro.getPlaca()
                    + "', " + carro.getMarca() + ", '" + horaEntrada + "')");

            statementOb.executeUpdate(sb2.toString());

            exito = true;

        } catch (Exception e) {
            System.err.println("Ocurrió un error "
                    + "insertando el registro. (agregarCarroDAO)" + e.getMessage());
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
        ArrayList<VehiculoDTO> carros = new ArrayList<>();
        Statement statementOb = null;

        try {
            statementOb = conn.createStatement();

            StringBuffer sb = new StringBuffer();
            sb.append("SELECT PLACA, MARCA, MODELO, CILINDRAJE, PUERTAS, HORAENTRADA ");
            sb.append("FROM CARRO C, MARCA M ");
            sb.append("WHERE M.MARCANO = C.MARCA ");
            sb.append("ORDER BY MNAME");

            ResultSet rs = statementOb.executeQuery(sb.toString());

            while (rs.next()) {
                CarroDTO dto = new CarroDTO();
                dto.setPlaca(rs.getString("PLACA"));
                dto.setMarca(rs.getString("MARCA"));
                dto.setModelo(rs.getString("MODELO"));
                dto.setCilindraje(rs.getString("CILINDRAJE"));
                dto.setPuertas(rs.getString("PUERTAS"));
                dto.setHoraEntrada(rs.getTimestamp("HORAENTRADA"));

                carros.add(dto);

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
        return carros;
    }

    public String eliminar(CarroDTO carro, String tiempoParqueado, Timestamp tiempoSalida) {
        try {
            Statement statementOb = conn.createStatement();
            statementOb.executeLargeUpdate("DELETE FROM CARRO WHERE PLACA = '"
                    + carro.getPlaca() + "'");

            System.out.println("VEHICULOS ELIMINADOS = " + statementOb.getUpdateCount());

            Statement statementOb2 = conn.createStatement();

            String horaSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tiempoSalida);

            statementOb2.executeLargeUpdate("UPDATE HISTORIAL SET TIEMPO = '"
                    + tiempoParqueado + "', HORASALIDA = '" + horaSalida
                    + "' WHERE IDENTIFICADOR = '" + carro.getPlaca() + "' "
                    + "AND HORAINGRESO = '" + carro.getHoraEntrada() + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tiempoParqueado;
    }
}
