package poo2.parqueadero.controllers.europa;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import poo2.parqueadero.controllers.ControllersPool;

public class ExternalAgentEuropeanController implements EuropeanInterface {

    @FXML
    private TextField txtCarros;
    @FXML
    private TextField txtMotos;
    @FXML
    private TextField txtBicicletas;
    @FXML
    private TextField txtVehiculos;

    private ControllersPool pool;

    public ExternalAgentEuropeanController() {
        pool = ControllersPool.getInstance();
        pool.addController("ExternalAgentEuropeanController", this);
    }

    @Override
    public void informarEstado(char tipoVehiculo, int conteo) {
        System.out.println("Imprime Controlador Suscriptor Europeo: " + tipoVehiculo);

        if (tipoVehiculo == 'c') {
            String cantidad = txtCarros.getText();
            if (cantidad.isBlank()) {
                cantidad = "0";
            }

            int cant = Integer.parseInt(cantidad);
            txtCarros.setText("" + (cant + conteo));
        } else if (tipoVehiculo == 'm') {
            String cantidad = txtMotos.getText();
            if (cantidad.isBlank()) {
                cantidad = "0";
            }

            int cant = Integer.parseInt(cantidad);
            txtMotos.setText("" + (cant + conteo));
        } else if (tipoVehiculo == 'o') {
            String cantidad = txtBicicletas.getText();
            if (cantidad.isBlank()) {
                cantidad = "0";
            }

            int cant = Integer.parseInt(cantidad);
            txtBicicletas.setText("" + (cant + conteo));

        }

        if (txtCarros.getText() != null
            && txtMotos.getText() != null
            && txtBicicletas.getText() != null) {

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
