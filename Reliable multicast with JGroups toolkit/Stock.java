import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Stock implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3558329310207489217L;
	String symbol;
	String name;
	int price;
	ArrayList<Order> buyList = new ArrayList<Order> (100);
	ArrayList<Order> sellList = new ArrayList<Order> (100);
	
	public Stock(String symbol, String name) {
		super();
		this.symbol = symbol;
		this.name = name;
		price = 100;
	}
	
	public void addList(Order order) {
		if (order.getType().equals("BUY")) {
			buyList.add(order);
			Collections.sort(buyList, new OrderComparator());
			Collections.reverse(buyList);
		}
		else if (order.getType().equals("SELL")) {
			sellList.add(order);
			Collections.sort(sellList, new OrderComparator());
		}
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public String getName() {
		return name;
	}
	
	public void setPrice(int p) {
		this.price = p;
	}
	
	public int getPrice() {
		return price;
	}
	
	public boolean orderCompare() {
		if (buyList.isEmpty() || sellList.isEmpty()) {
			return false;
		}
		
		else if (buyList.get(0).getPrice() > sellList.get(0).getPrice()) {
			return true;
		}
		
		else {
			return false;
		}
	}
	
	public Order fetchBuyOrder() {
		return buyList.get(0);
	}
	
	public ArrayList<Order> getBuyList() {
		return buyList;
	}
	
	public ArrayList<Order> getSellList() {
		return sellList;
	}
	
	public Order fetchSellOrder() {
		return sellList.get(0);
	}
	
	public void orderRemove() {
		buyList.remove(0);
		sellList.remove(0);
	}
}

class OrderComparator implements Comparator<Order>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2193908049373144076L;

	public int compare(Order o1, Order o2) {
		if (o1.getPrice() < o2.getPrice()) {
			return -1;
		}
		
		else if (o1.getPrice() > o2.getPrice()) {
			return 1;
		}
		
		else {
			return 0;
		}
	}
}
