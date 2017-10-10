import java.util.Scanner;

public class Main {
	
	private static final String PASSWORD_ALICE = "alice_pwd";
	
	public static void main(String[] args) {
		Client alice = new Client("Alice",PASSWORD_ALICE);
		int n;
		Scanner reader = new Scanner(System.in);
		do{
			System.out.println("Enter a number: ");
			n = reader.nextInt();
			System.out.println(alice.computeHashN(n, "E1F53135E559C253"));
		}while(n!=0);
		reader.close();
	}

}
