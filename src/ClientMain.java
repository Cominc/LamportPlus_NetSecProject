import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Base64;


public class ClientMain {
	
	private final static String AS_ADDRESS = "localhost";
	private final static int PORT = 31;
	
	private final static String CLIENT_START = "Client ready\n";
	private final static String AS_AUTH_OK = "Server authentication success.\n";
	private final static String AS_AUTH_KO = "Server authentication fail.\n";
	private final static String ERR_NO_NUMBER = "Error: non-numeric parameter received when a number is required.\n";
	
	public static void main(String[] args) {
		
		// Alice cerca di autenticarsi essendo registrata sull'AS
		Client client = new Client("Alice","alice_pwd","alice_shared_key");
		// Bob cerca di autenticarsi senza essere registrato sull'AS
		//Client client = new Client("Bob","bob_pwd","bob_shared_key");
		// Carol cerca di autenticarsi essendo registrata sull'AS che non ha carol_shared_key
        //Client client = new Client("Carol","carol_pwd","carol_shared_key");
		// Trudy cerca di autenticarsi come Alice ma senza conoscerne la password
		//Client client = new Client("Alice","trudy_pwd","trudy_shared_key");
		try
		{
			//TODO gestire se si lancia prima il client del server (far terminare senza crash)
			//Creazione socket 
			InetAddress address = InetAddress.getByName(AS_ADDRESS);
			Socket clientSocket = new Socket(address, PORT);
			
			System.out.println(CLIENT_START);
			
			// creazione buffer di scrittura e lettura
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			// Invio messaggio con la propria identità
			out.println(client.getName());
			out.flush();
			System.out.println(Settings.SEND_LABEL+client.getName()+Settings.NEW_LINE);
			
			//Ricezione risposta dal server
			String serverResponse = in.readLine();
			System.out.println(Settings.RECIVE_LABEL+serverResponse+Settings.NEW_LINE);
			
			
			//Estrazione dal messaggio ricevuto dei parametri n,salt,timestamp,HMAC(timestamp) (sono separati dal carattere separatore)
			if((serverResponse.length() - serverResponse.replace(Settings.SEPARATOR, "").length())==3){
				//TODO rinominare oppure usare direttamente serverResponse
				String strTemp = serverResponse;
				String timeStamp = strTemp.substring(0,strTemp.indexOf(Settings.SEPARATOR));
				strTemp = strTemp.substring(strTemp.indexOf(Settings.SEPARATOR)+1,strTemp.length());
				String hmac = strTemp.substring(0,strTemp.indexOf(Settings.SEPARATOR));
				strTemp = strTemp.substring(strTemp.indexOf(Settings.SEPARATOR)+1,strTemp.length());
				
				if(checkTimestamp(timeStamp)&&client.computeHMAC(timeStamp).equals(hmac))
				{	
					System.out.println(AS_AUTH_OK);
					try {  
						int n = Integer.parseInt(strTemp.substring(0,strTemp.indexOf(Settings.SEPARATOR)));
						strTemp = strTemp.substring(strTemp.indexOf(Settings.SEPARATOR)+1,strTemp.length());
						String salt = new String(Base64.getDecoder().decode(strTemp.substring(strTemp.indexOf(Settings.SEPARATOR)+1,strTemp.length())));
						
						String hash = client.computeHashN(n-1, salt);
						
						// Invio messaggio con la risposta al server
						out.println(hash);
						out.flush();
						System.out.println(Settings.SEND_LABEL+hash+Settings.NEW_LINE);
						
						//Esito autenticazione
						serverResponse = in.readLine();
						System.out.println(Settings.RECIVE_LABEL+serverResponse+Settings.NEW_LINE);
					}  
					catch(NumberFormatException nfe)  {  
						System.out.println(ERR_NO_NUMBER); 
					}  
				}else {
					System.out.println(AS_AUTH_KO);
				}
			}
			
			// chiusura socket
			clientSocket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static boolean checkTimestamp(String timestamp) {
		long timestampNow = System.currentTimeMillis();
		long timestampRecived = Long.parseLong(timestamp);
		// (timestampNow-DELTA) <= timestampRecived <= (timestampNow+DELTA)
		if(timestampRecived >= (timestampNow-Settings.DELTA) && timestampRecived <= (timestampNow+Settings.DELTA))
			return true;
		else
			return false;
	}
}