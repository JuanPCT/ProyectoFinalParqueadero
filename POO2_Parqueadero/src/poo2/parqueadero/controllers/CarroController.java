package poo2.parqueadero.controllers;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import poo2.parqueadero.model.dto.CarroDTO;
import poo2.parqueadero.model.dto.VehiculoDTO;
import poo2.parqueadero.model.fachada.Parqueadero;

public class CarroController implements Initializable {

    @FXML
    private TableView<CarroDTO> tablaCarro;
    @FXML
    private TableColumn<CarroDTO, String> colPlaca, colMarca, colModelo, colTiempo;
    @FXML
    private TextField txtPlaca, txtModelo, tiempoRegistrado;
    @FXML
    private ComboBox<String> cmbMarcas, cmbCilindrajes, cmbPuertas;

    private ObservableList<String> listMarcasCombo;
    private ObservableList<String> listCilindrajeCombo;
    private ObservableList<String> listPuertasCombo;

    private ObservableList<CarroDTO> dataTable;

    private Parqueadero parqueadero;

    public CarroController() throws RemoteException {

        dataTable = FXCollections.observableArrayList();
        parqueadero = Parqueadero.getInstance();
        listMarcasCombo = FXCollections.observableArrayList();
        listCilindrajeCombo = FXCollections.observableArrayList();
        listPuertasCombo = FXCollections.observableArrayList();
    }

    @FXML
    public void handleRegistrarEntrada(ActionEvent ev) throws RemoteException {
        String placa = txtPlaca.getText();
        String marca = cmbMarcas.getValue();
        String modelo = txtModelo.getText();
        String cilindraje = cmbCilindrajes.getValue();
        String puertas = cmbPuertas.getValue();

        parqueadero.registrarEntradaVehiculo(placa, marca, modelo, cilindraje,
                puertas, null, null, null, 'c');

        txtPlaca.clear();
        txtModelo.clear();
        cmbMarcas.getSelectionModel().clearSelection();
        cmbCilindrajes.getSelectionModel().clearSelection();
        cmbPuertas.getSelectionModel().clearSelection();

        listarCarrosEnTabla();
    }

    @FXML
    public void handleRegistrarSalida(ActionEvent ev) {
        CarroDTO carroSelected = tablaCarro.getSelectionModel().getSelectedItem();
        tablaCarro.getItems().removeAll(carroSelected);
        String exito = parqueadero.registrarSalidaVehiculo(carroSelected, null, null);

        if (exito != null) {
            tiempoRegistrado.setText(exito);
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("horaEntrada"));

        tablaCarro.setItems(dataTable);

        cmbMarcas.setItems(listMarcasCombo);
        cmbCilindrajes.setItems(listCilindrajeCombo);
        cmbPuertas.setItems(listPuertasCombo);

        ArrayList<String> listaMarcas = parqueadero.listarMarcas();

        listMarcasCombo.addAll(listaMarcas);
        listCilindrajeCombo.addAll("1000", "1200", "1500", "2000");
        listPuertasCombo.addAll("2", "3", "4", "5");

        listarCarrosEnTabla();

    }

    private void listarCarrosEnTabla() {

        dataTable.clear();

        ArrayList<VehiculoDTO> carros = parqueadero.listarVehiculos('c');
        try {
            if (carros != null) {
                for (VehiculoDTO dto : carros) {

                    CarroDTO cr = new CarroDTO();
                    cr.setPlaca(dto.getPlaca());
                    cr.setMarca(dto.getMarca());
                    cr.setModelo(dto.getModelo());
                    cr.setHoraEntrada(dto.getHoraEntrada());

                    dataTable.add(cr);
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR LISTAR CARROS CONTROLLER. " + e.getMessage());
        }
    }
}
