package agentecontroller;

import java.rmi.RemoteException;

import agentefachada.AgenteFachada;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AgenteController {

	@FXML
	private TextField txtIPAmigo;
	@FXML
	private TextField txtPuertoAmigo;
	@FXML
	private TextField txtCarros;
	@FXML
	private TextField txtMotos;
	@FXML
	private TextField txtBicicletas;
	@FXML
	private TextField txtVehiculos;
	@FXML
	private Button consultarBtn;

	private TextField txtInfo;
	
	private AgenteFachada fachada;
	
	public AgenteController() throws RemoteException {
			txtInfo = new TextField();		
			fachada = AgenteFachada.getInstance();
			fachada.establecerParametros(txtInfo,this);
	}
	
	public void update() {

        if (txtInfo.getText().equals("carro")) {
            String cantidad = txtCarros.getText();
            if (cantidad.isBlank()) {
                cantidad = "0";
            }

            int cant = Integer.parseInt(cantidad);

            txtCarros.setText("" + (cant + 1));
        } else if (txtInfo.getText().equals("moto")) {
            String cantidad = txtMotos.getText();
            if (cantidad.isBlank()) {
                cantidad = "0";
            }

            int cant = Integer.parseInt(cantidad);

            txtMotos.setText("" + (cant + 1));
        } else if (txtInfo.getText().equals("bicicleta")) {
            String cantidad = txtBicicletas.getText();
            if (cantidad.isBlank()) {
                cantidad = "0";
            }

            int cant = Integer.parseInt(cantidad);

            txtBicicletas.setText("" + (cant + 1));
        }      

        String car = txtCarros.getText();
        String mot = txtMotos.getText();
        String bic = txtBicicletas.getText();

        if (car.isBlank()) {
            car = "0";
        }

        if (mot.isBlank()) {
            mot = "0";
        }

        if (bic.isBlank()) {
            bic = "0";
        }

        int carros = Integer.parseInt(car);
        int motos = Integer.parseInt(mot);
        int bicicletas = Integer.parseInt(bic);
        
        txtVehiculos.setText(carros + motos + bicicletas + "");
        
        txtInfo.clear();
    }
	
	@FXML
    public void establecerConexionBtn(ActionEvent ev) {		
		try {
			
			fachada.configurarConexion(txtIPAmigo.getText(), Integer.parseInt(txtPuertoAmigo.getText()));
		}catch(Exception re) {
			txtInfo.appendText("No se pudo realizar la conexion");
			re.printStackTrace();
			
		}
	}
	
	@FXML
	public void handleConsultarBtn(ActionEvent ev) {
		fachada.consultarBaseDeDatos();
		consultarBtn.setDisable(false);
	}
}
