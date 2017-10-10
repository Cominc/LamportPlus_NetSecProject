
public class Entry {
	
	private int n;
	private String salt;
	private String hashN;
	private String sharedSecret;

	public Entry(int n, String salt, String hashN, String sharedSecret) {
		super();
		this.n = n;
		this.salt = salt;
		this.hashN = hashN;
		this.sharedSecret = sharedSecret;
	}
	
	public int getN() {
		return n;
	}
	
	public void setN(int n) {
		this.n = n;
	}
	
	public String getSalt() {
		return salt;
	}
	
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	public String getHashN() {
		return hashN;
	}
	
	public void setHashN(String hashN) {
		this.hashN = hashN;
	}

	public String getSharedSecret() {
		return sharedSecret;
	}

	public void setSharedSecret(String sharedSecret) {
		this.sharedSecret = sharedSecret;
	}
	
	

}