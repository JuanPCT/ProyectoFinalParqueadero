package poo2.parqueadero.controllers;

import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import poo2.parqueadero.model.fachada.Parqueadero;
import poo2.parqueadero.model.dto.VehiculoDTO;

public class ListarVehiculosController implements Initializable {

    @FXML
    private TableView<ListaVehiculo> listaTable;
    @FXML
    private TableColumn<ListaVehiculo, String> colPlacaLis, colMarcaLis, colTiempoLis, colTotalLis;
    @FXML
    private TextField txtTotalGlobal;

    private ObservableList<ListaVehiculo> dataTable;

    private int precioTotal;

    private Parqueadero parqueadero;
    private ListaVehiculo lv;

    public ListarVehiculosController() throws RemoteException {
        parqueadero = Parqueadero.getInstance();

        dataTable = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        // Configuracion de las columnas del listaTable.
        colPlacaLis.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colMarcaLis.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colTiempoLis.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
        colTotalLis.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Asociación del ObservableList al listaTable.
        listaTable.setItems(dataTable);
    }

    @FXML
    public void handleCalcularBtn(ActionEvent event) {

        txtTotalGlobal.setText("$" + precioTotal);
    }

    @FXML
    public void handleListarVehiculos(ActionEvent event) {
        dataTable.clear();

        ArrayList<VehiculoDTO> vehiculos = parqueadero.listarVehiculos('t');
        precioTotal = 0;
        if (vehiculos != null) {
            for (VehiculoDTO dto : vehiculos) {
                Timestamp entrada = dto.getHoraEntrada();

                if (dto.getHoraSalida() != null) {
                    Timestamp salida = dto.getHoraSalida();

                    int restaHora = Math.abs(entrada.getHours() - salida.getHours());
                    int restaMinutos = Math.abs(entrada.getMinutes() - salida.getMinutes());
                    int restaSegundos = Math.abs(entrada.getSeconds() - salida.getSeconds());

                    int precio = parqueadero.hallarPrecio(restaHora, restaMinutos);

                    String tiempoParqueado = restaHora + ":" + restaMinutos + ":" + restaSegundos;

                    if (precio == 0) {
                        lv = new ListaVehiculo(dto.getPlaca(), dto.getMarca(), tiempoParqueado, "GRATIS: 0 HORAS, 0 MINUTOS");
                        dataTable.add(lv);
                    } else {
                        lv = new ListaVehiculo(dto.getPlaca(), dto.getMarca(), tiempoParqueado, "$" + precio);
                        dataTable.add(lv);
                        precioTotal += precio;
                    }
                } else {
                    lv = new ListaVehiculo(dto.getPlaca(), dto.getMarca(), "NO HA SALIDO", "NO HA SALIDO");
                    dataTable.add(lv);
                }
            }
        }
        listaTable.setItems(dataTable);
    }
}
