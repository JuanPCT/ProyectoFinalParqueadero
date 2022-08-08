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
import poo2.parqueadero.model.dto.MotoDTO;
import poo2.parqueadero.model.dto.VehiculoDTO;
import poo2.parqueadero.model.fachada.Parqueadero;

public class MotoController implements Initializable {

    @FXML
    private TableView<MotoDTO> tablaMoto;
    @FXML
    private TableColumn<MotoDTO, String> colPlaca, colMarca, colModelo, colTiempo;
    @FXML
    private TextField txtPlaca, txtModelo, tiempoRegistrado;
    @FXML
    private ComboBox<String> cmbMarcas, cmbCilindrajes, cmbTipos;

    private ObservableList<String> listMarcasCombo;
    private ObservableList<String> listCilindrajeCombo;
    private ObservableList<String> listTiposCombo;

    private ObservableList<MotoDTO> dataTable;

    private Parqueadero parqueadero;

    public MotoController() throws RemoteException {

        dataTable = FXCollections.observableArrayList();
        parqueadero = Parqueadero.getInstance();
        listMarcasCombo = FXCollections.observableArrayList();
        listCilindrajeCombo = FXCollections.observableArrayList();
        listTiposCombo = FXCollections.observableArrayList();
    }

    @FXML
    public void handleRegistrarEntrada(ActionEvent ev) throws RemoteException {
        String placa = txtPlaca.getText();
        String marca = cmbMarcas.getValue();
        String modelo = txtModelo.getText();
        String cilindraje = cmbCilindrajes.getValue();
        String tipo = cmbTipos.getValue();

        parqueadero.registrarEntradaVehiculo(placa, marca, modelo, cilindraje,
                null, tipo, null, null, 'm');

        txtPlaca.clear();
        txtModelo.clear();
        cmbMarcas.getSelectionModel().clearSelection();
        cmbCilindrajes.getSelectionModel().clearSelection();
        cmbTipos.getSelectionModel().clearSelection();

        listarMotosEnTabla();
    }

    @FXML
    public void handleRegistrarSalida(ActionEvent ev) {
        MotoDTO motoSelected = tablaMoto.getSelectionModel().getSelectedItem();
        tablaMoto.getItems().removeAll(motoSelected);
        String exito = parqueadero.registrarSalidaVehiculo(null, motoSelected, null);

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

        tablaMoto.setItems(dataTable);

        cmbMarcas.setItems(listMarcasCombo);
        cmbCilindrajes.setItems(listCilindrajeCombo);
        cmbTipos.setItems(listTiposCombo);

        ArrayList<String> listaMarcas = parqueadero.listarMarcas();

        listMarcasCombo.addAll(listaMarcas);
        listCilindrajeCombo.addAll("200", "300", "400");
        listTiposCombo.addAll("URBANA", "CARRETERA", "CALLEJERA");

        listarMotosEnTabla();

    }

    private void listarMotosEnTabla() {

        dataTable.clear();

        ArrayList<VehiculoDTO> motos = parqueadero.listarVehiculos('m');
        try {
            if (motos != null) {
                for (VehiculoDTO dto : motos) {

                    MotoDTO mt = new MotoDTO();
                    mt.setPlaca(dto.getPlaca());
                    mt.setMarca(dto.getMarca());
                    mt.setModelo(dto.getModelo());
                    mt.setHoraEntrada(dto.getHoraEntrada());

                    dataTable.add(mt);
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR LISTAR MOTOS CONTROLLER. " + e.getMessage());
        }
    }
}
