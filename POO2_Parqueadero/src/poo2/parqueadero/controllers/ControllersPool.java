package poo2.parqueadero.controllers;

import java.util.HashMap;

public class ControllersPool {

    private HashMap<String, Object> controllers;
    private static ControllersPool pool;

    private ControllersPool() {
        controllers = new HashMap<String, Object>();
    }

    public static ControllersPool getInstance() {
        if (pool == null) {
            pool = new ControllersPool();
        }
        return pool;
    }

    public void addController(String name, Object controller) {
        System.out.println(name + " - " + controller);

        controllers.put(name, controller);
    }

    public Object getController(String name) {
        return controllers.get(name);
    }
}
