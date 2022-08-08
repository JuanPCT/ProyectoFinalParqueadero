package poo2.parqueadero.model.fachada;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.ArrayList;

import interfaz.AgenteInterfaz;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import poo2.parqueadero.controllers.ControllerInterface;
import poo2.parqueadero.controllers.DatosClienteController;
import poo2.parqueadero.model.dao.CarroDAO;
import poo2.parqueadero.model.dao.MarcaDAO;
import poo2.parqueadero.model.dao.MotoDAO;
import poo2.parqueadero.model.dao.BicicletaDAO;
import poo2.parqueadero.model.dao.ParqueadosDAO;
import poo2.parqueadero.model.dto.BicicletaDTO;
import poo2.parqueadero.model.dto.BicicletaDTOAdapter;
import poo2.parqueadero.model.dto.CarroDTO;
import poo2.parqueadero.model.dto.MarcaDTO;
import poo2.parqueadero.model.dto.MotoDTO;
import poo2.parqueadero.model.dto.VehiculoDTO;
import poo2.parqueadero.model.factory.BicicletaAdapterVehiculoFactory;
import poo2.parqueadero.model.factory.CarroVehiculoFactory;
import poo2.parqueadero.model.factory.MotoVehiculoFactory;
import poo2.parqueadero.model.factory.VehiculoFactory;

public class Parqueadero extends UnicastRemoteObject implements AgenteInterfaz{

	private AgenteInterfaz servidorObj;
	private int puerto = 3232;	
	private static final long serialVersionUID = 1L;
	
    private ArrayList<ControllerInterface> suscriptores;
    private VehiculoFactory vF;

    private static Parqueadero instance;
    
    private TextField notificacion;
    
    private DatosClienteController controller;

    private Parqueadero() throws RemoteException{
        suscriptores = new ArrayList<ControllerInterface>();
        this.notificacion=new TextField();
        this.arrancarServidor();
    }
    
    public void setController(DatosClienteController controller) {
    	this.controller = controller;
    }
    
    public void establecerParametros(TextField notificacion) {
		this.notificacion = notificacion;
	}
    

    public static Parqueadero getInstance() throws RemoteException{
        if (instance == null) {
            instance = new Parqueadero();
        }

        return instance;
    }

    public ArrayList<String> listarMarcas() {
        MarcaDAO dao = new MarcaDAO();
        ArrayList<MarcaDTO> lista = dao.listar();

        ArrayList<String> listaNombres = null;
        if (lista != null && lista.size() > 0) {
            listaNombres = new ArrayList<String>();
            for (MarcaDTO dto : lista) {
                listaNombres.add(dto.getNombre());
            }
        }

        return listaNombres;
    }

    public boolean agregarMarca(String marca) {

        MarcaDAO mDao = new MarcaDAO();
        boolean exito = mDao.agregar(marca);

        return exito;
    }

    public String registrarSalidaVehiculo(CarroDTO carro, MotoDTO moto, BicicletaDTO bike) {

        String exito = null;

        if (carro != null) {
            Timestamp horaEntrada = carro.getHoraEntrada();
            Timestamp horaSalida = new Timestamp(System.currentTimeMillis());
            String tiempoParqueado = getTiempoParqueado(horaEntrada);
            CarroDAO cDao = new CarroDAO();
            exito = cDao.eliminar(carro, tiempoParqueado, horaSalida);

        } else if (moto != null) {
            Timestamp horaEntrada = moto.getHoraEntrada();
            Timestamp horaSalida = new Timestamp(System.currentTimeMillis());
            String tiempoParqueado = getTiempoParqueado(horaEntrada);
            MotoDAO mDao = new MotoDAO();
            exito = mDao.eliminar(moto, tiempoParqueado, horaSalida);

        } else if (bike != null) {
            Timestamp horaEntrada = bike.getHoraEntrada();
            Timestamp horaSalida = new Timestamp(System.currentTimeMillis());
            String tiempoParqueado = getTiempoParqueado(horaEntrada);
            BicicletaDAO bDao = new BicicletaDAO();
            exito = bDao.eliminar(bike, tiempoParqueado, horaSalida);
        }
        return exito;
    }

    public String getTiempoParqueado(Timestamp horaEntrada) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        int restaHora = Math.abs(horaEntrada.getHours() - (currentTime.getHours()));
        int restaMinutos = Math.abs(horaEntrada.getMinutes() - currentTime.getMinutes());
        int restaSegundos = Math.abs(horaEntrada.getSeconds() - currentTime.getSeconds());
        String tiempoParqueado = restaHora + ":" + restaMinutos + ":" + restaSegundos;

        return tiempoParqueado;
    }

    public int obtenerIdMarca(String nombreMarca) {
        int idMarca = 0;

        MarcaDAO dao = new MarcaDAO();
        idMarca = dao.obtenerId(nombreMarca);

        return idMarca;
    }

    public void registrarEntradaVehiculo(String placa, String marca,
            String modelo, String cilindraje, String puertas, String tipo,
            String tipoBicicleta, String propietario, char tipoVehiculo) throws RemoteException {

        vF = obtenerFactory(tipoVehiculo);

        Timestamp entrada = new Timestamp(System.currentTimeMillis());

        int marcaID = obtenerIdMarca(marca);

        if (tipoVehiculo == 'c') {

            registrarCarro(placa, marcaID + "", modelo, cilindraje, puertas, entrada);
            notifyControllers("carro");

        } else if (tipoVehiculo == 'm') {

            registrarMoto(placa, marcaID + "", modelo, cilindraje, tipo, entrada);
            notifyControllers("moto");

        } else if (tipoVehiculo == 'o') {

            registrarBicicleta(propietario, marcaID + "", tipoBicicleta, entrada);
            notifyControllers("bicicleta");
        }
    }

    public int hallarPrecio(int horas, int minutos) {
        int precio = 0;

        if (horas != 0) {
            precio += (horas * 3000);
        }

        if (minutos != 0) {
            precio += (minutos * 150);
        }
        return precio;
    }

    private boolean registrarCarro(String placa, String marca, String modelo,
            String cilindraje, String puertas, Timestamp entrada) {

        CarroDTO dto = (CarroDTO) vF.crearVehiculo();

        dto.setPlaca(placa);
        dto.setMarca(marca);
        dto.setModelo(modelo);
        dto.setCilindraje(cilindraje);
        dto.setPuertas(puertas);
        dto.setHoraEntrada(entrada);

        CarroDAO dao = new CarroDAO();
        dao.agregar(dto);

        return false;
    }

    private boolean registrarMoto(String placa, String marca, String modelo,
            String cilindraje, String tipo, Timestamp entrada) {

        MotoDTO dto = (MotoDTO) vF.crearVehiculo();
        dto.setPlaca(placa);
        dto.setMarca(marca);
        dto.setModelo(modelo);
        dto.setCilindraje(cilindraje);
        dto.setTipo(tipo);
        dto.setHoraEntrada(entrada);

        MotoDAO dao = new MotoDAO();
        dao.agregar(dto);

        return false;
    }

    private boolean registrarBicicleta(String cCPropietario, String marca,
            String tipo, Timestamp entrada) {

        BicicletaDTOAdapter dto = (BicicletaDTOAdapter) vF.crearVehiculo();
        dto.setCcPropietario(cCPropietario);
        dto.setMarca(marca);
        dto.setTipo(tipo);
        dto.setHoraEntrada(entrada);

        BicicletaDAO dao = new BicicletaDAO();
        dao.agregar(dto);

        return false;
    }

    private VehiculoFactory obtenerFactory(char tipo) {
        VehiculoFactory vF = null;

        if (tipo == 'c') {
            vF = new CarroVehiculoFactory();
        } else if (tipo == 'm') {
            vF = new MotoVehiculoFactory();
        } else if (tipo == 'o') {
            vF = new BicicletaAdapterVehiculoFactory();
        }
        return vF;
    }

    public ArrayList<VehiculoDTO> listarVehiculos(char tipo) {
        ArrayList<VehiculoDTO> lista = null;

        if (tipo == 'c') {
            lista = (new CarroDAO()).listar();
        } else if (tipo == 'm') {
            lista = (new MotoDAO()).listar();
        } else if (tipo == 'o') {
            lista = (new BicicletaDAO()).listar();
        } else if (tipo == 't') {
            lista = (new ParqueadosDAO()).listarVehiculos();
        }
        return lista;
    }

    public void addController(ControllerInterface conIn) {
        suscriptores.add(conIn);
        System.out.println("Suscripción realizada = " + conIn.getClass().getName());
    }

    public void removeController(ControllerInterface conIn) {
        suscriptores.remove(conIn);
    }

    public void notifyControllers(String msg) throws RemoteException {

        System.out.println("Notificando... ");
        
        	prepararNotificacion(msg);
        
        for (ControllerInterface subs : suscriptores) {
            subs.update(msg);
        }
    }

    public void arrancarServidor() {
		try {
			System.setProperty("java.rmi.server.hostname","localhost");
			String dirIP = (InetAddress.getLocalHost()).toString();	
			System.out.println("Escuchando en "+dirIP+" : "+puerto);
			
			Registry registry = LocateRegistry.createRegistry(puerto);
			
			registry.bind("servidorparqueadero", (AgenteInterfaz) this);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
    
    
	public String establecerConexion(String ip, int puerto) throws RemoteException {
		String serverAddress = ip;
		int serverPort = puerto;
		
		try {
			
			Registry reg = LocateRegistry.getRegistry(serverAddress, serverPort);
			servidorObj = (AgenteInterfaz) reg.lookup("servidoragente");
			System.out.println("Conectado con Agente Externo");
			
		}catch(Exception e) {			
			e.printStackTrace();
		}
		return null;
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
		controller.setTxtInfo(msg);
		return null;
	}
}
