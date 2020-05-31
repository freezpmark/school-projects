package ucastnici;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import display.GUI;
import parsers.Convertor;
import parsers.FullMessage;
import parsers.Packet;

public class Receiver
{
	static DatagramSocket socketReceive = null;
	static DatagramSocket socketSend = null;
	
	public Receiver()
	{	
		try {
			byte[] buffer = new byte[65507]; // just in case.. http://stackoverflow.com/questions/1098897/what-is-the-largest-safe-udp-packet-size-on-the-internet
			byte[] responseBuffer = new byte[1];
			int idMsg, packetNumber, dataLength, amount;
			long sentChecksum, reciChecksum;
			Checksum checksum;
			DatagramPacket datagramPacket;
			DatagramPacket sendDatagramPacket;
			InetAddress address;
			if (socketReceive == null) {
				socketReceive = new DatagramSocket(9002);
				socketReceive.setSoTimeout(60000);
			}
			if (socketSend == null)
				socketSend = new DatagramSocket();
			FullMessage msg = null;
			ByteArrayOutputStream mss = null;
			
			GUI.text.append("Caka sa na spravu...\n");
			
			while(true){
				datagramPacket = new DatagramPacket(buffer, buffer.length);
				
				socketReceive.receive(datagramPacket); // prijem a nasledne dekodovanie

				idMsg = Convertor.getIntegerOf1Hex(datagramPacket.getData()[8]);
				
				packetNumber = Convertor.getIntegerOf4Hex(
						datagramPacket.getData()[9],
						datagramPacket.getData()[10], 
						datagramPacket.getData()[11],
						datagramPacket.getData()[12]);
				
				dataLength = Convertor.getIntegerOf4Hex(
						datagramPacket.getData()[13],
						datagramPacket.getData()[14], 
						datagramPacket.getData()[15],
						datagramPacket.getData()[16]);
				
				amount = Convertor.getIntegerOf4Hex(
						datagramPacket.getData()[17],
						datagramPacket.getData()[18], 
						datagramPacket.getData()[19],
						datagramPacket.getData()[20]);
				
				address = datagramPacket.getAddress();
				
				if (dataLength == 0 && idMsg == 3) // keep-alive
					continue;
				
				else if (dataLength >= 0 && dataLength < 65507) {			
					//String value = new String(datagramPacket.getData(), Packet.HEADER_SIZE, dataLength);
					GUI.text.append("Velkost " + packetNumber + ". paketu: " + datagramPacket.getLength() + ", dlzka dat: " + dataLength + "\n");
											
					sentChecksum = Convertor.BytesToLong(datagramPacket.getData(), 0, 8);
					checksum = new CRC32();
					checksum.update(buffer, 8, 13 + dataLength);		// 1 + 4 + 4 + 4 + dlzka
					reciChecksum = checksum.getValue();
					if (reciChecksum == sentChecksum)
						responseBuffer[0] = 1;		// 1 = ACK
					else {
						GUI.text.append("Bolo to chybne poslane, posielam znovuvyziadanie ramca\n");
						responseBuffer[0] = 0;		// 0 = NACK
					}				
					
					if (msg == null && idMsg < 2) 		// spajam spravu dokopy
						msg = new FullMessage(amount);
					if(idMsg < 2) {
						String value = new String(datagramPacket.getData(), Packet.HEADER_SIZE, dataLength);
						msg.insertNewFragment(packetNumber, value);
					}

					else if(mss == null)				// spajam bajty pre subor
						mss = new ByteArrayOutputStream();
					if(idMsg == 2) {
						byte[] value = Arrays.copyOfRange(datagramPacket.getData(), Packet.HEADER_SIZE, dataLength+Packet.HEADER_SIZE);
						mss.write(value);
					}
					
				}
				else {
					System.out.println("Chybna dlzka: " + dataLength + "\n");
					responseBuffer[0] = 0;			// 0 = NACK
				}
				
				// poslanie ACK
				sendDatagramPacket = new DatagramPacket(responseBuffer, responseBuffer.length, address, 9003);	// poslanie ACK, v bufferu teda bude len hlavicka poslana
				socketSend.send(sendDatagramPacket);
				if(packetNumber == amount && responseBuffer[0] == 1 && idMsg < 2) {// koncim
					GUI.text.append("\n\nPrijata sprava: " + msg.getMessage() + "\n");
					msg = null;
				}
				if(packetNumber == amount && responseBuffer[0] == 1 && idMsg == 2) {
					byte[] bytes = mss.toByteArray();
					Path path = Paths.get("C:\\poslane\\posted");
					Files.write(path, bytes);
					mss = null;
				}
			}
			
		} catch (SocketTimeoutException e) {
			GUI.textFinal.append("Cas vyprsal. Ukoncujem prijimanie\n");
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			//socketReceive.close();
			//socketSend.close();
		}
	}
}