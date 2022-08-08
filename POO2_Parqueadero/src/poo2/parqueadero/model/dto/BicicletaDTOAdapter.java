package poo2.parqueadero.model.dto;

import ufps.poo2.api.Bicicleta;

public class BicicletaDTOAdapter extends VehiculoDTO {

    private Bicicleta b;

    public BicicletaDTOAdapter() {
        b = new Bicicleta();
    }

    public void setCcPropietario(String ccPropietario) {
        b.setCcPropietario(ccPropietario);
    }

    public void setMarca(int marca) {
        b.setMarca(marca + "");
    }

    public void setTipo(String tipo) {
        b.setTipo(tipo);
    }

    public Bicicleta getBicicleta() {
        return b;
    }
}
