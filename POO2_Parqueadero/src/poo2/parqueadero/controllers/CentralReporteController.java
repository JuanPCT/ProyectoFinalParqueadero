package poo2.parqueadero.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CentralReporteController implements ControllerInterface {

    @FXML
    private TextField txtCarros;
    @FXML
    private TextField txtMotos;
    @FXML
    private TextField txtBicicletas;
    @FXML
    private TextField txtVehiculos;

    private ControllersPool pool;

    public CentralReporteController() {
        pool = ControllersPool.getInstance();
        pool.addController("CentralReporteController", this);
    }

    @Override
    public void update(String msg) {
        System.out.println("Imprime controlador suscriptor: " + msg);

        if (msg.equals("carro")) {
            String cantidad = txtCarros.getText();
            if (cantidad.isBlank()) {
                cantidad = "0";
            }

            int cant = Integer.parseInt(cantidad);

            txtCarros.setText("" + (cant + 1));
        } else if (msg.equals("moto")) {
            String cantidad = txtMotos.getText();
            if (cantidad.isBlank()) {
                cantidad = "0";
            }

            int cant = Integer.parseInt(cantidad);

            txtMotos.setText("" + (cant + 1));
        } else if (msg.equals("bicicleta")) {
            String cantidad = txtBicicletas.getText();
            if (cantidad.isBlank()) {
                cantidad = "0";
            }

            int cant = Integer.parseInt(cantidad);

            txtBicicletas.setText("" + (cant + 1));
        }

        if (!msg.isBlank()) {

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
        }
    }
}
