package poo2.parqueadero.model.dto;

public class BicicletaDTO extends VehiculoDTO {

    private String idbici;
    private String tipo;

    public String getIdbici() {
        return idbici;
    }

    public void setIdbici(String idbici) {
        this.idbici = idbici;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
