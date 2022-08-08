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
import poo2.parqueadero.model.dto.BicicletaDTO;
import poo2.parqueadero.model.dto.VehiculoDTO;
import poo2.parqueadero.model.fachada.Parqueadero;

public class BicicletaController implements Initializable {

    @FXML
    private TableView<BicicletaDTO> tablaBici;
    @FXML
    private TableColumn<BicicletaDTO, String> colCedula, colMarca, colTiempo;
    @FXML
    private TextField txtCedula, txtTiempo;
    @FXML
    private ComboBox<String> cmbMarcas, cmbTipo;

    private ObservableList<String> listMarcasCombo;
    private ObservableList<String> listTiposCombo;

    private ObservableList<BicicletaDTO> dataTable;

    private Parqueadero parqueadero;

    public BicicletaController() throws RemoteException {
        dataTable = FXCollections.observableArrayList();
        parqueadero = Parqueadero.getInstance();
        listMarcasCombo = FXCollections.observableArrayList();
        listTiposCombo = FXCollections.observableArrayList();
    }

    @FXML
    public void handleRegistrarEntrada(ActionEvent ev) throws RemoteException {
        String cedula = txtCedula.getText();
        String marca = cmbMarcas.getValue();
        String tipo = cmbTipo.getValue();

        parqueadero.registrarEntradaVehiculo(null, marca, null, null,
                null, null, tipo, cedula, 'o');

        txtCedula.clear();
        cmbMarcas.getSelectionModel().clearSelection();
        cmbTipo.getSelectionModel().clearSelection();

        listarBicicletasEnTabla();
    }

    @FXML
    public void handleRegistrarSalida(ActionEvent ev) {
        BicicletaDTO bikeSelected = tablaBici.getSelectionModel().getSelectedItem();
        tablaBici.getItems().removeAll(bikeSelected);
        String exito = parqueadero.registrarSalidaVehiculo(null, null, bikeSelected);

        if (exito != null) {
            txtTiempo.setText(exito);
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        colCedula.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("horaEntrada"));

        tablaBici.setItems(dataTable);

        cmbMarcas.setItems(listMarcasCombo);
        cmbTipo.setItems(listTiposCombo);

        ArrayList<String> listaMarcas = parqueadero.listarMarcas();

        listMarcasCombo.addAll(listaMarcas);
        listTiposCombo.addAll("MONTANERA", "RUTA");

        listarBicicletasEnTabla();
    }

    private void listarBicicletasEnTabla() {
        dataTable.clear();

        ArrayList<VehiculoDTO> bicis = parqueadero.listarVehiculos('o');
        try {
            if (bicis != null) {
                for (VehiculoDTO dto : bicis) {

                    BicicletaDTO bk = new BicicletaDTO();
                    bk.setPlaca(dto.getPlaca());
                    bk.setMarca(dto.getMarca());
                    bk.setHoraEntrada(dto.getHoraEntrada());

                    dataTable.add(bk);
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR LISTAR MOTOS CONTROLLER. " + e.getMessage());
        }
    }
}
