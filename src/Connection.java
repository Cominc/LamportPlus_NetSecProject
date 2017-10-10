import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


class Connection extends Thread {

	private final static String MEX_CLIENT_NOT_FOUND = "Unknown Client.";
	private final static String MEX_AUTH_FAIL = "Authentication fail.";
	private final static String MEX_AUTH_OK = "Authentication Success.";
	private final static String MEX_NEW_SETUP_NEEDED = "Maximum number of authentication reached, you need a new setup.";
	
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private HashMap<String, Entry> clients;

	public Connection(Socket client,HashMap<String, Entry> clients)
	{
		this.client = client;
		this.clients = clients;
		this.start();
	}

	public void run()
	{
		try
		{	
			// inizializza i buffer in entrata e uscita per stringhe
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

			// Scambio messaggi con il client
			String firstMexRecived = in.readLine();
			System.out.println(Settings.RECIVE_LABEL+firstMexRecived+Settings.NEW_LINE);
			
			Entry clientToServeData = clients.get(firstMexRecived);
			String firstMexSend;
			// Controllo se il client che richiede di autenticarsi è registrato
			if(clientToServeData!=null) {
				// Controllo n per evitare l'invio della password del client in chiaro
				if(clientToServeData.getN()<2) {
					System.out.println(Settings.SEND_LABEL+MEX_NEW_SETUP_NEEDED+Settings.NEW_LINE);
					out.println(MEX_NEW_SETUP_NEEDED);
				}else {
					// Invio la risposta al client
					//TODO creare stringhe per muta autenticazione
					
					
					firstMexSend = clientToServeData.getN()+Settings.SEPARATOR+clientToServeData.getSalt();
					System.out.println(Settings.SEND_LABEL+firstMexSend+Settings.NEW_LINE);
					out.println(firstMexSend);
					
					// Ricevo l'hash di risposta da parte del client
					String secondMexRecived = in.readLine();
					System.out.println(Settings.RECIVE_LABEL+secondMexRecived+Settings.NEW_LINE);
					
					if(clientToServeData.getHashN().equals(computeHash(secondMexRecived))) {
						// Autenticazione effettuata con successo, aggiorno i dati del client sul server
						clientToServeData.setN(clientToServeData.getN()-1);
						clientToServeData.setHashN(secondMexRecived);
						clients.replace(firstMexRecived, clientToServeData);
						
						// Comunico l'esito positivo al client
						System.out.println(Settings.SEND_LABEL+MEX_AUTH_OK+Settings.NEW_LINE);
						out.println(MEX_AUTH_OK);
					}else {
						// Comunico l'esito negativo al client
						System.out.println(Settings.SEND_LABEL+MEX_AUTH_FAIL+Settings.NEW_LINE);
						out.println(MEX_AUTH_FAIL);
					}
				}
			}else {
				// Comunico al client che non è conosciuto
				System.out.println(Settings.SEND_LABEL+MEX_CLIENT_NOT_FOUND+Settings.NEW_LINE);
				out.println(MEX_CLIENT_NOT_FOUND);
			}
			
			//TODO serve usare flush?
			//out.flush();
			
			// chiusura dei buffer e del socket
			in.close();
 			out.close();
 			
 			client.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private String computeHash(String strToHash)
	{
		String hash = "";
		try {
			MessageDigest md = MessageDigest.getInstance(Settings.HASH_ALG_CHOOSED);
			byte[] array = md.digest(Base64.getDecoder().decode(strToHash)); 
			hash = Base64.getEncoder().encodeToString(array);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hash;
	}
	
	//TODO codice da controllare
	private String generateHMac(String key, String data) {
		Mac hMac;
		String resBase64 = "";
		try {
			hMac = Mac.getInstance(Settings.HMAC_ALG_CHOOSED);
			//byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
			byte[] hmacKeyBytes = key.getBytes();
			SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, Settings.HMAC_ALG_CHOOSED);
			hMac.init(secretKey);
			//byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
			byte[] dataBytes = data.getBytes();
			byte[] res = hMac.doFinal(dataBytes);
			resBase64 = Base64.getEncoder().encodeToString(res);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resBase64;
	}
}