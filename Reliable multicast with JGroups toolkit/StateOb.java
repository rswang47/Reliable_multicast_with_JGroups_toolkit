import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;


public class StateOb implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1990870745153907167L;
	ArrayList<Account> accounts = new ArrayList<Account> ();
	HashMap<String, Stock> stocks = new HashMap<String, Stock> (100);
	int orderNum;
	
	public StateOb(ArrayList<Account> accounts, HashMap<String, Stock> stocks, int orderNum) {
		super();
		this.accounts.addAll(accounts);
		Set<Entry<String, Stock>> set = stocks.entrySet();
		for (Entry<String, Stock> s : set) {
			this.stocks.put(s.getKey(), s.getValue());
		}
		this.orderNum = orderNum;
	}
	
	public ArrayList<Account> getAccount() {
		return accounts;
	}
	
	public HashMap<String, Stock> getStock() {
		return stocks;
	}
	
	public int getOrderNum() {
		return orderNum;
	}
}
