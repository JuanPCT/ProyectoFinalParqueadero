package poo2.parqueadero.controllers.europa;

import poo2.parqueadero.controllers.ControllerInterface;
import poo2.parqueadero.controllers.ControllersPool;

public class EuropeanInterfaceAdapter implements ControllerInterface {

    private ExternalAgentEuropeanController controller;

    public EuropeanInterfaceAdapter() {
        ControllersPool pool = ControllersPool.getInstance();

        controller = (ExternalAgentEuropeanController) pool.getController("ExternalAgentEuropeanController");
        System.out.println("ExternalAgentEuropeanController = " + controller);
    }

    @Override
    public void update(String msg) {
        if (msg.equals("carro")) {
            controller.informarEstado('c', 1);
        } else if (msg.equals("moto")) {
            controller.informarEstado('m', 1);
        } else if (msg.equals("bicicleta")) {
            controller.informarEstado('o', 1);
        }
    }
}
