import java.io.Serializable;

public class Order implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2012240520508630249L;
	String symbol;
	String clientID;
	String type;
	int price;
	int share;
	
	public Order(String clientID, String type, String symbol, int price) {
		super();
		this.symbol = symbol;
		this.clientID = clientID;
		this.type = type;
		this.price = price;
		share = 10;
	}
	
	public int getPrice() {
		return price;
	}
	
	public String getType() {
		return type;
	}
	
	public String getClientID() {
		return clientID;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public int getShare() {
		return share;
	}
}