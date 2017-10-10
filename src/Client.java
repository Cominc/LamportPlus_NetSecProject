import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class Client implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String password;
	
	public Client(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String computeHashN(int n, String salt) {
		MessageDigest md;
		String hashN ="";
		try {
			md = MessageDigest.getInstance(Settings.HASH_ALG_CHOOSED);
			byte[] array = (password+salt).getBytes();
			for(int i=0; i< n; i++)
				array = md.digest(array); 
			hashN = Base64.getEncoder().encodeToString(array);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashN;
	}
	
}
