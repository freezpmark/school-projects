package display;
import ucastnici.Sender;

import java.io.IOException;
import java.net.InetAddress;

import parsers.Address;
import ucastnici.Receiver;

public class Threads extends Thread{
	public int idMsg;
	InetAddress address;
	int size;
	String string;
	Sender keepAliveThread = null;
	
	public void run(){
		switch (idMsg) {
			case 0:
			case 1:
				new Sender(address, size, string, idMsg);
				
				// stary keep-alive thread vypnem
				if (this.keepAliveThread != null)
					this.keepAliveThread.Stop();
				
				this.keepAliveThread = new Sender(address);  // keep-alive
				break;
				
			case 2:
				try {
					new Sender(address, size, string);
					
					// stary keep-alive thread vypnem
					if (this.keepAliveThread != null)
						this.keepAliveThread.Stop();
					
					this.keepAliveThread = new Sender(address);  // keep-alive
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
								
			case 3:
				new Receiver();
		}
	}
	
	private Threads(int n) {
		this.idMsg = n;
	}
		
	private Threads(String adresa, int velkost, String retazec, int id) {
		this.address = Address.vytvorAdresu(adresa);
		this.size = velkost;
		this.string = retazec;
		this.idMsg = id;
	}

	public static void StartSenderThread(String adresa, int velkost, String retazec, int id) {
		new Threads(adresa, velkost, retazec, id).start();
	}
	
	public static void StartReceiverThread() {
		new Threads(3).start();
	}
}
