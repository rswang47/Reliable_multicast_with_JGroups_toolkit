import java.io.Serializable;
import java.util.HashMap;


public class Account implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8228365426120309097L;
	String clientID;
	int balance;
	
	private HashMap<Stock, Integer> stock = new HashMap<Stock, Integer> (100);
	
	public Account(String clientID) {
		super();
		this.clientID = clientID;
		balance = 10000;
	}
	
	public synchronized void buyStock(Stock s, Order o, int shares) {
		balance = balance - o.getPrice()*shares;
		Integer currentShare = stock.get(s);
		if (currentShare == null) {
			stock.put(s, shares);
		}
		
		else {
			stock.put(s, currentShare + shares);
		}
	}
	
	public synchronized void sellStock(Stock s, Order o, int shares) {
		balance = balance + o.getPrice()*shares;
		Integer currentShare = stock.get(s);
		if (currentShare == null) {
			stock.put(s, 0 - shares);
		}
		
		else {
			stock.put(s, currentShare - shares);
		}
	}
	
	public int getBalance() {
		return balance;
	}
}
