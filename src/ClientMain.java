import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class ClientMain {
	
	private final static String AS_ADDRESS = "localhost";
	private final static int PORT = 31;
	
	private final static String CLIENT_START = "Client ready.\n";
	
	public static void main(String[] args) {
		
		// Alice cerca di autenticarsi essendo registrata sull'AS
		Client client = new Client("Alice","alice_pwd","alice_shared_key");
		// Bob cerca di autenticarsi senza essere registrato sull'AS
		//Client client = new Client("Bob","bob_pwd");
		// Trudy cerca di autenticarsi come Alice ma senza conoscerne la password
		//Client client = new Client("Alice","trudy_pwd");
		try
		{
			//Creazione socket 
			InetAddress address = InetAddress.getByName(AS_ADDRESS);
			Socket clientSocket = new Socket(address, PORT);
			
			System.out.println(CLIENT_START);
			
			// creazione buffer di scrittura e lettura
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			// Invio messaggio con la propria identità
			out.println(client.getName());
			System.out.println(Settings.SEND_LABEL+client.getName()+Settings.NEW_LINE);
			
			//Ricezione risposta dal server
			String serverResponse = in.readLine();
			System.out.println(Settings.RECIVE_LABEL+serverResponse+Settings.NEW_LINE);
			
			//Estrazione dal messaggio ricevuto dei parametri n e salt (sono separati dal carattere separatore)
			if(serverResponse.contains(Settings.SEPARATOR)){
				String salt = serverResponse.substring(serverResponse.indexOf(Settings.SEPARATOR)+1);
				int n = Integer.parseInt(serverResponse.substring(0,serverResponse.indexOf(Settings.SEPARATOR)));

				String hash = client.computeHashN(n-1, salt);
				
				// Invio messaggio con la risposta al server
				out.println(hash);
				System.out.println(Settings.SEND_LABEL+hash+Settings.NEW_LINE);
				
				//Esito autenticazione
				serverResponse = in.readLine();
				System.out.println(Settings.RECIVE_LABEL+serverResponse+Settings.NEW_LINE);
			}
			
			// chiusura socket
			clientSocket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}