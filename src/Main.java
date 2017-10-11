import java.util.Scanner;

public class Main {
	
	
	public static void main(String[] args) {
		//String salt = "E1F53135E559C253";
		//Client client = new Client("Alice","alice_pwd","alice_shared_key");
		String salt = "84B03D034B409D4E";
		Client client = new Client("Carol","carol_pwd","carol_shared_key");
		int n;
		Scanner reader = new Scanner(System.in);
		do{
			System.out.println("Enter a number: ");
			n = reader.nextInt();
			System.out.println(client.computeHashN(n, salt));
			System.out.println(client.computeHMAC(System.currentTimeMillis()+""));
		}while(n!=0);
		reader.close();
	}

}
