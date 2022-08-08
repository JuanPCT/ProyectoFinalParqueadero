package poo2.parqueadero.controllers;

/**
 * ツ Kenn Marcucci ツ
 */
public class ListaVehiculo {

    private String placa, marca, total, tiempo;

    public ListaVehiculo(String placa, String marca, String tiempo, String total) {
        this.placa = placa;
        this.marca = marca;
        this.tiempo = tiempo;
        this.total = total;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
