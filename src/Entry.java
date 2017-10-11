
public class Entry {
	
	private int n;
	private String salt;
	private String hashN;
	private String sharedKey;

	public Entry(int n, String salt, String hashN, String sharedKey) {
		super();
		this.n = n;
		this.salt = salt;
		this.hashN = hashN;
		this.sharedKey = sharedKey;
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

	public String getSharedKey() {
		return sharedKey;
	}

	public void setSharedKey(String sharedKey) {
		this.sharedKey = sharedKey;
	}
	
	

}