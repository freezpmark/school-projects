package ucastnici;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;

import display.GUI;
import parsers.Packet;

public class Sender{
	private DatagramSocket socketSend = null;	
	private InetAddress address = null;
		
	public void Stop(){
		if (this.socketSend != null)		
			this.socketSend.close();
	}
	
	// odosle text
	public Sender(InetAddress adresa, int velkostVzorky, String retazec, int idMsg){
		this(adresa, velkostVzorky, retazec.getBytes(), idMsg);		
		
		GUI.textFinal.append("\nOdoslana sprava: " + retazec + "\n");
	}
	
	// odosle subor
	public Sender(InetAddress adresa, int velkostVzorky, String subor) throws IOException{
		this(adresa, velkostVzorky, Files.readAllBytes(Paths.get(subor)), 2);	
		
		GUI.textFinal.append("\nOdoslany subor: " + subor + "\n");	
	}

	private Sender(InetAddress adresa, int velkostVzorky, byte[] retazec, int idMsg){		
		this.address = adresa;
		
		int i, j;
		int offset = 0;
		int poradoveCislo = 1;
		int pocetPacketov = 1;
		int[] packety;
		byte respCode;
		
		byte[] ackBuffer = new byte[1];
		DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length);
			
		DatagramSocket ackSocket;
		try {
			ackSocket = new DatagramSocket(9003);			// ACK port
			ackSocket.setSoTimeout(2000);					// set acknowledgment delay tollerance (in milliseconds)
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		
		if(velkostVzorky > 65507)
			velkostVzorky = 65507;
		
		int dataSize = velkostVzorky-21;
		
		// vytvorenie pola paketov s hodnotou ich velkosti
		pocetPacketov = retazec.length / dataSize;
		if (retazec.length % dataSize > 0)
			pocetPacketov++;
	
		packety = new int[pocetPacketov];
	
		for (i = 0; i < pocetPacketov; i++)
			if((i + 1) == pocetPacketov)
				packety[i] = retazec.length % dataSize;
			else
				packety[i] = dataSize;	

		try {
			if (socketSend == null)
				socketSend = new DatagramSocket();
			byte[] data;
			DatagramPacket datagramPacket;
			
			for(j = 0; j < pocetPacketov; j++) {			
				do {		
					data = Packet.vytvorPacket(poradoveCislo, packety[j], retazec, idMsg, offset, pocetPacketov);
					GUI.textFinal.append("velkost " + (j+1) + ". packetu: " + packety[j] + "\n");
					datagramPacket = new DatagramPacket(data, data.length, adresa, 9002);
					socketSend.send(datagramPacket);
				
					ackSocket.receive(ackPacket); // wait for acknowledgment
					respCode = ackPacket.getData()[0];

					if (idMsg == 1) // prvykrat som poslal chybne
						idMsg = 0;	// od teraz uz posielam spravne			
				
				} while (respCode == 0);	// ked bol NACK, tak cyklus pokracuje, lebo chceme poslat spravny ramec
			
				poradoveCislo++;
				offset += packety[j];
			}	
			
		} catch (SocketTimeoutException e){
			GUI.textFinal.append("Cas vyprsal. Ukoncujem prijimanie\n");
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			socketSend.close();
			ackSocket.close();
		}
	}
	
	public Sender(InetAddress adresa){	
		this.address = adresa;
		
		try {
			if (socketSend == null)
				socketSend = new DatagramSocket();
			
			byte[] data = Packet.vytvorPacket(0, 0, new byte[0], 3, 0, 1);
			DatagramPacket datagramPacket = new DatagramPacket(data, data.length, adresa, 9002);
			
			while (true){			
				socketSend.send(datagramPacket);
				Thread.sleep(30000);
			}	
			
		} catch (SocketTimeoutException e){
			GUI.textFinal.append("Cas vyprsal. Ukoncujem prijimanie\n");
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (InterruptedException e){
			e.printStackTrace();
		} finally {
			socketSend.close();
		}
	}
}
