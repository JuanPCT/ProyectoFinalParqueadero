package agenteDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class AgenteDAO {

    private Connection conn;

    public AgenteDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/poo2_local", "poo2", "poo2");
        } catch (SQLException e) {
            System.err.println("La conexión no se pudo establecer. " + e.getMessage());
        }

    }

    public AgenteDAO(Connection conn) {
        this.conn = conn;
    }

    public ArrayList<String> consultarVehiculos() {
        ArrayList<String> vehiculos = new ArrayList<>();
        Statement statementOb = null;

        try {
            statementOb = conn.createStatement();

            StringBuffer sb = new StringBuffer();
            sb.append("SELECT MARCA, TIPO ");
            sb.append("FROM HISTORIAL H, MARCA M ");
            sb.append("WHERE M.MARCANO = H.MARCA ");
            sb.append("ORDER BY MNAME");

            System.out.println(sb.toString());
            ResultSet rs = statementOb.executeQuery(sb.toString());
            
            while (rs.next()) {
                String tipo = rs.getString("TIPO");
                if (tipo.equals("c")) {
                    tipo = "carro";
                } else if (tipo.equals("m")) {
                	tipo = "moto";
                } else if (tipo.equals("o")) {
                	tipo = "bicicleta";
                }
                
                vehiculos.add(tipo);

            }

        } catch (Exception e) {
            System.err.println("Se presentó un error ejecutando la consulta. " + e.getMessage());
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
