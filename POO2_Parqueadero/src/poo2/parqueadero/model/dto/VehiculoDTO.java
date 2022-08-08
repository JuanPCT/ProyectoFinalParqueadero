package poo2.parqueadero.model.dto;

import java.sql.Timestamp;

public class VehiculoDTO {

    private String placa;
    private String marca;
    private String modelo;
    private String cilindraje;

    private Timestamp horaEntrada, horaSalida;

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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCilindraje() {
        return cilindraje;
    }

    public void setCilindraje(String cilindraje) {
        this.cilindraje = cilindraje;
    }

    public Timestamp getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Timestamp horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Timestamp getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Timestamp horaSalida) {
        this.horaSalida = horaSalida;
    }
}
