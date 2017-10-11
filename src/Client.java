import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class Client {

	private String name;
	private String password;
	private String sharedKey;
	
	public Client(String name, String password, String sharedKey) {
		this.name = name;
		this.password = password;
		this.sharedKey = sharedKey;
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
	
	public String getSharedKey() {
		return sharedKey;
	}

	public void setSharedKey(String sharedKey) {
		this.sharedKey = sharedKey;
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
	
	public String computeHMAC(String data) {
		Mac hMac;
		String resBase64 = "";
		try {
			hMac = Mac.getInstance(Settings.HMAC_ALG_CHOOSED);
			//byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
			byte[] hmacKeyBytes = sharedKey.getBytes();
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
