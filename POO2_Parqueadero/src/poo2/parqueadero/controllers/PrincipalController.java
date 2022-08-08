package poo2.parqueadero.controllers;

import java.rmi.RemoteException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import poo2.parqueadero.controllers.europa.EuropeanInterfaceAdapter;
import poo2.parqueadero.model.fachada.Parqueadero;

public class PrincipalController {

    @FXML
    private TextField txtAgregarMarca;

    private Parqueadero parqueadero;
    private ControllersPool pool;

    public PrincipalController() throws RemoteException {
        parqueadero = Parqueadero.getInstance();
        pool = ControllersPool.getInstance();
    }
    
    @FXML
    public void handleSuscripcionAgenteExterno(ActionEvent event) {
    	
    	try {
            	
            	
            	AnchorPane root = (AnchorPane) FXMLLoader
                        .load(getClass().getResource("/poo2/parqueadero/views/DatosAgenteView.fxml"));
            	           	
                Stage stage = new Stage();

                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.show();
                

        } catch (Exception e) {
            System.err.println("Ocurrio un error al suscribir al agente. " + e.getMessage());
        }
    	    	
    }

    @FXML
    public void handleAgregarMarcaBtn(ActionEvent event) {
        try {
            String marca = txtAgregarMarca.getText();

            boolean exito = parqueadero.agregarMarca(marca);
            if (exito) {
                txtAgregarMarca.clear();
            }

        } catch (Exception e) {
            System.err.println("Ocurrio un error al agregar una marca. " + e.getMessage());
        }
    }

    @FXML
    public void handleAgenteAmericano(ActionEvent ev) {
        try {
            AnchorPane root = (AnchorPane) FXMLLoader
                    .load(getClass().getResource("/poo2/parqueadero/views/ExternalAgent.fxml"));

            Stage stage = new Stage();

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAgenteEuropeo(ActionEvent ev) {
        try {
            AnchorPane root = (AnchorPane) FXMLLoader
                    .load(getClass().getResource("/poo2/parqueadero/views/EuropeanAgent.fxml"));

            Stage stage = new Stage();

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSuscripcionAgentes(ActionEvent ev) {

        CentralReporteController con1 = 
        		(CentralReporteController) pool.getController("CentralReporteController");
        EuropeanInterfaceAdapter adap = new EuropeanInterfaceAdapter();

        System.out.println("Suscripciones - Controller Americano : " + con1);
        System.out.println("Suscripciones - Controller Europeo : " + adap);

        parqueadero.addController(con1);
        parqueadero.addController(adap);
    }
}
