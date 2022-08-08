package poo2.parqueadero.controllers;

import java.rmi.RemoteException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import poo2.parqueadero.model.fachada.Parqueadero;

public class DatosClienteController{
	
	@FXML
	private TextField txtIP;
	@FXML
	private TextField txtPuerto;	

	private TextField txtInfo;
	
private Parqueadero fachada;
	
	public DatosClienteController() {
		try {
			fachada = Parqueadero.getInstance();
			fachada.setController(this);
			this.txtInfo = new TextField();
			fachada.establecerParametros(txtInfo);			
		}catch(RemoteException re) {
			re.printStackTrace();
		}
	}
	
	public void setTxtInfo(String txt) {
		this.txtInfo.setText(txt);
		setPuerto();
	}
	
	public void setPuerto() {
		try {
			this.txtPuerto.appendText(txtInfo.getText());
		}catch(Exception re) {
			re.printStackTrace();
			
		}
		
	}
	
	@FXML
    public void establecerConexionBtn(ActionEvent ev) {		
		try {
			fachada.establecerConexion(txtIP.getText(), Integer.parseInt(txtPuerto.getText()));
			Node source = (Node) ev.getSource();
		    Stage stage = (Stage) source.getScene().getWindow();
		    stage.close();
		}catch(Exception re) {
			re.printStackTrace();
			
		}
	}

	
}
