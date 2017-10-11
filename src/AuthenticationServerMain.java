import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class AuthenticationServerMain {
	
	private static final String AUTH_SERVER_START = "Authentication Server ready";
	private final static int PORT = 31;
	
	public static void main(String[] args)
	{
		HashMap<String, Entry> clients = new HashMap<>();
		Entry alice_data = new Entry(2, "E1F53135E559C253", "wR+EoB2aPC35/KQxJpN1rSh4nPE44Kzp5af6FjOgBvoNc+S3h7LYCphJvEJ689tCjr9PKPjDYmFzcp5WbKTeKQ==", "alice_shared_key");
		Entry carol_data = new Entry(3, "84B03D034B409D4E", "c18rAnlDfUuAicjqO1kHjAAUneAZslS9MDlQUtI7pq+XTMhWGSIsRy+EUwV2wYBoQMMM6GWd4ZWaOxLtJKg6fg==", "wrong_carol_shared_key");
		clients.put("Alice", alice_data);
		clients.put("Carol", carol_data);
		try
		{
			ServerSocket server = new ServerSocket(PORT);
			System.out.println(AUTH_SERVER_START);
			// ciclo infinito, in attesa di connessioni
			boolean ctrl = true;
			while(ctrl)
			{
				// chiamata bloccante, in attesa di una nuova connessione
				Socket client = server.accept();

				// la nuova richiesta viene gestita da un thread indipendente
				//TODO controllare come si accede a clients (deve essere accesso unico, qualunque modifica deve essere vista da tutti)
				new Connection(client,clients);
			}
			server.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}