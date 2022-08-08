package interfaz;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AgenteInterfaz extends Remote{	
	
	public String enviarNotificacion(String msg) throws RemoteException;
}
