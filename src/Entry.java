
public class Entry {
	
	private int n;
	private String salt;
	private String hash_n;

	public Entry(int n, String salt, String hash_n) {
		super();
		this.n = n;
		this.salt = salt;
		this.hash_n = hash_n;
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
	
	public String getHash_n() {
		return hash_n;
	}
	
	public void setHash_n(String hash_n) {
		this.hash_n = hash_n;
	}

}