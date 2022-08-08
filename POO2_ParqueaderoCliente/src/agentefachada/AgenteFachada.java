package agentefachada;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import agenteDAO.AgenteDAO;
import agentecontroller.AgenteController;
import interfaz.AgenteInterfaz;
import javafx.scene.control.TextField;


public class AgenteFachada extends UnicastRemoteObject implements AgenteInterfaz{

	private AgenteInterfaz servidorObj;
	private int puerto = 3131;	
	private static final long serialVersionUID = 1L;
	private TextField notificacion;
	
	private AgenteController controller;
	
	private static AgenteFachada instance;
	
	private AgenteDAO dao;
	
	private AgenteFachada() throws RemoteException{
		this.puerto = 3131;
		arrancarServidor();
		notificacion=new TextField();
		dao = new AgenteDAO();
	}
	
	public static AgenteFachada getInstance() throws RemoteException{
        if (instance == null) {
            instance = new AgenteFachada();
        }

        return instance;
    }
	
	public void establecerParametros(TextField notificacion,AgenteController controller) {
		this.notificacion = notificacion;
		this.controller=controller;
	}
	
	public void consultarBaseDeDatos() {
		ArrayList<String> vehiculos=dao.consultarVehiculos();
		for(String v: vehiculos) {
			notificacion.appendText(v);
			controller.update();
		}
	}
	
    public void arrancarServidor() {
		try {
			System.setProperty("java.rmi.server.hostname","localhost");
			String dirIP = (InetAddress.getLocalHost()).toString();
			
			System.out.println("Escuchando en "+dirIP+" : "+puerto);
			
			Registry registry = LocateRegistry.createRegistry(puerto);
			
			registry.bind("servidoragente", (AgenteInterfaz) this);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	} 
    
    public void configurarConexion(String ip, int puerto) {
		
		String serverAddress = ip;
		int serverPort = puerto;
		
		try {
			
			Registry reg = LocateRegistry.getRegistry(serverAddress, serverPort);
			servidorObj = (AgenteInterfaz) reg.lookup("servidorparqueadero");
			servidorObj.enviarNotificacion(String.valueOf(this.puerto));
			System.out.println("Conectado con Parqueadero");
			
		}catch(Exception e) {			
			e.printStackTrace();
		}
	}
    
    public void prepararNotificacion(String msg) {
		try {
			servidorObj.enviarNotificacion(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String enviarNotificacion(String msg) throws RemoteException {
		notificacion.appendText(msg);
		controller.update();
		return null;
	}
}
