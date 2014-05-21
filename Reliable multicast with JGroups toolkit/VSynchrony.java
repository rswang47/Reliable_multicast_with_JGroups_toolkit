import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.jgroups.*;
import org.jgroups.util.Util;


public class VSynchrony extends ReceiverAdapter{
	//final List<Account> state = new LinkedList<Account>();
	JChannel channel;
	String user_name = System.getProperty("user.name", "n/a");
	Socket s = null;
	ServerSocket ss = null;
	static int processID;
	static int numOfClient;
	static int SPN;
	static int orderNum = 0;
	private static ArrayList<Account> accounts = new ArrayList<Account> ();
	private static HashMap<String, Stock> stocks = new HashMap<String, Stock> (100);

	private void start() throws Exception {
		init();
		channel = new JChannel();
		channel.setReceiver(this);
		channel.connect("Risheng_Wang");
		channel.getState(null, 10000);
		eventloop();
		channel.close();
	}
	
	public void init() {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream("index.properties"));
		} catch (Exception e1) {
			System.err.println("Cannot find the file.");
		}
		Set<Entry<Object, Object>> set = p.entrySet();
		for (Entry<Object, Object> e : set) {
			String symbol = (String) e.getKey();
			String name = (String) e.getValue();
			Stock s = new Stock(symbol, name);
			stocks.put(symbol, s);
			System.out.println("Stock added: " + stocks.get(symbol).getName());
		}
	}
	
	
	public void viewAccepterd (View new_view) {
		System.out.println("** view: " + new_view);
	}
	
	public void receive (Message msg) {
		String request[] = ((String) msg.getObject()).split(" ");
		System.out.println("Order " + orderNum + " : " + request[0] + " " + request[1] + " " + request[2] + " " + request[3]
				+ " " + request[4]);
		orderNum++;
		Order order = new Order(request[0], request[1], request[2], Integer.valueOf(request[3]));
		Stock s = stocks.get(request[2]);
		s.addList(order);
		boolean tradeFlag = s.orderCompare();
		if (tradeFlag) {
			Order buy = s.fetchBuyOrder();
			Order sell = s.fetchSellOrder();
			int buyClientID = Integer.valueOf(buy.getClientID().substring(1));
			int sellClientID = Integer.valueOf(sell.getClientID().substring(1));
			Account buyClient = accounts.get(buyClientID-1);
			Account sellClient = accounts.get(sellClientID-1);
			buyClient.buyStock(s, buy, buy.getShare());
			sellClient.sellStock(s, sell, sell.getShare());
			s.setPrice(sell.getPrice());
			Iterator<Order> it1 = s.getBuyList().iterator();
			Iterator<Order> it2 = s.getSellList().iterator();
			String buyOrder = "Buys: ";
			String sellOrder = "Sells: ";
			String accInfo = " ";
			while (it1.hasNext()) {
				Order o = it1.next();
				buyOrder = buyOrder + "[" + o.getClientID() + " " + o.getType() + " " + o.getSymbol() + " "
				+ o.getPrice() + " " + o.getShare() + "] ";
			}
			while (it2.hasNext()) {
				Order o = it2.next();
				sellOrder = sellOrder + "[" + o.getClientID() + " " + o.getType() + " " + o.getSymbol() + " "
						+ o.getPrice() + " " + o.getShare() + "] ";
			}
			for (int i=0;i<accounts.size();i++) {
				int j = i+1;
				accInfo = accInfo + "C" + j + " " + accounts.get(i).getBalance() + " ";
			}
			System.out.println("Price: " + s.getPrice());
			System.out.println(buyOrder);
			System.out.println(sellOrder);
			System.out.println("[" + accInfo + "]");
			s.orderRemove();
		}
		
		else {
			if (s.getBuyList().isEmpty() && !s.getSellList().isEmpty()) {
				Iterator<Order> it = s.getSellList().iterator();
				String sellOrder = "Sells: ";
				String accInfo = " ";
				while (it.hasNext()) {
					Order o = it.next();
					sellOrder = sellOrder + "[" + o.getClientID() + " " + o.getType() + " " + o.getSymbol() + " "
							+ o.getPrice() + " " + o.getShare() + "] ";
				}
				for (int i=0;i<accounts.size();i++) {
					int j = i+1;
					accInfo = accInfo + "C" + j + " " + accounts.get(i).getBalance() + " ";
				}
				System.out.println("Price: " + s.getPrice());
				System.out.println("Buys: []");
				System.out.println(sellOrder);
				System.out.println("[" + accInfo + "]");
			}
			
			else if (s.getSellList().isEmpty() && !s.getBuyList().isEmpty()) {
				Iterator<Order> it = s.getBuyList().iterator();
				String buyOrder = "Buys: ";
				String accInfo = " ";
				while (it.hasNext()) {
					Order o = it.next();
					buyOrder = buyOrder + "[" + o.getClientID() + " " + o.getType() + " " + o.getSymbol() + " "
							+ o.getPrice() + " " + o.getShare() + "] ";
				}
				for (int i=0;i<accounts.size();i++) {
					int j = i+1;
					accInfo = accInfo + "C" + j + " " + accounts.get(i).getBalance() + " ";
				}
				System.out.println("Price: " + s.getPrice());
				System.out.println(buyOrder);
				System.out.println("Sells: []");
				System.out.println("[" + accInfo + "]");
			}
			
			else {
				Iterator<Order> it1 = s.getBuyList().iterator();
				Iterator<Order> it2 = s.getSellList().iterator();
				String buyOrder = "Buys: ";
				String sellOrder = "Sells: ";
				String accInfo = " ";
				while (it1.hasNext()) {
					Order o = it1.next();
					buyOrder = buyOrder + "[" + o.getClientID() + " " + o.getType() + " " + o.getSymbol() + " "
					+ o.getPrice() + " " + o.getShare() + "] ";
				}
				while (it2.hasNext()) {
					Order o = it2.next();
					sellOrder = sellOrder + "[" + o.getClientID() + " " + o.getType() + " " + o.getSymbol() + " "
							+ o.getPrice() + " " + o.getShare() + "] ";
				}
				for (int i=0;i<accounts.size();i++) {
					int j = i+1;
					accInfo = accInfo + "C" + j + " " + accounts.get(i).getBalance() + " ";
				}
				System.out.println("Price: " + s.getPrice());
				System.out.println(buyOrder);
				System.out.println(sellOrder);
				System.out.println("[" + accInfo + "]");
			}
		}
	//	synchronized(state) {
	//		state.addAll(accounts);
	//	}
	}
	
	public void getState (OutputStream output) throws Exception {
		synchronized(accounts) {
			synchronized(stocks) {
			Util.objectToStream(new StateOb(accounts, stocks, orderNum), new DataOutputStream(output));
			}
		}
	}
	
	public void setState (InputStream input) throws Exception {
		StateOb state = (StateOb)Util.objectFromStream(new DataInputStream(input));
		synchronized(accounts) {
			synchronized(stocks) {
				stocks.clear();
				Set<Entry<String, Stock>> set = state.getStock().entrySet();
				for (Entry<String, Stock> s : set) {
					stocks.put(s.getKey(), s.getValue());
				}
			}
			accounts.clear();
			accounts.addAll(state.getAccount());
			orderNum = state.getOrderNum();
		}
	}
	
	private void eventloop() throws IOException {
		ss = new ServerSocket(SPN);
		while(true) {
			try {
				s = ss.accept();
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				String line = (String) ois.readObject();
				System.out.print("> "); System.out.flush();
				if (line.startsWith("quit") || line.startsWith("exit"))
					break;
				Message msg = new Message(null, null, line);
				channel.send(msg);
				ois.close();
				s.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		processID = Integer.valueOf(args[0]);
		numOfClient = Integer.valueOf(args[1]);
		SPN = Integer.valueOf(args[2]);
		if (processID == 0) {
			for (int i = 0;i < numOfClient;i++) {
				int j = i+1;
				String clientID = "C" + j;
				Account account = new Account(clientID);
				accounts.add(i, account);
			}
		}
		VSynchrony vs = new VSynchrony();
		vs.start();//equals to 'new SimpleChat().start()'
	}
}
