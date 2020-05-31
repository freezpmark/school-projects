package parsers;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Packet {
	
	public static final int HEADER_SIZE = 21;
	
	public static byte[] vytvorPacket(int packetNumber, int length, String string, int idMsg, int offset, int amount){	
		return Packet.vytvorPacket(packetNumber, length, string.getBytes(), idMsg, offset, amount);
	}
	
	public static byte[] vytvorPacket(int packetNumber, int length, byte[] bytes, int idMsg, int offset, int amount){
		byte[] data = new byte[length + HEADER_SIZE];					// 21 = 8 (CRC) + 1 + 4 + 4 + 4
		
		data[8] = Convertor.IntToBytes(idMsg)[3];						// ID spravy do data[8]
			
		byte[] cisloPacketuBytes = Convertor.IntToBytes(packetNumber);
		System.arraycopy(cisloPacketuBytes, 0, data, 9, 4);				// skopirujem cislo fragmentu do data[9] az data[12]
				
		byte[] dlzkaBytes = Convertor.IntToBytes(length);
		System.arraycopy(dlzkaBytes, 0, data, 13, 4);					// skopirujem dlzku do data[13] az data[16]
			
		byte[] pocetBytes = Convertor.IntToBytes(amount);
		System.arraycopy(pocetBytes, 0, data, 17, 4);					// skopirujem pocet fragmentov do data[17] az data[20]
				
		//Pridanie retazca, od 21 zacinaju data
		int stringLength = bytes.length;
		for(int i = HEADER_SIZE; i < length + HEADER_SIZE; i++) {
			data[i] = bytes[offset++];
						
			if(offset > stringLength)
				break;		
		}
		
		Checksum checksum;
		checksum = new CRC32();
		checksum.update(data,8,data.length-8);
		
		long suma = checksum.getValue();
					
		byte[] chucksumBytes = Convertor.LongToBytes(suma);
		System.arraycopy(chucksumBytes, 0, data, 0, 8);					// skopirujem chucksumBytes do data[0] az data[7]
		
		if (idMsg == 1) {	// posielam chybne
			int randomIndex = new Random().nextInt() % 8;	// nahodny byte medzi data[0] az data[7]
			
			if (data[randomIndex] < Byte.MAX_VALUE)
				data[randomIndex]++;
			else
				data[randomIndex]--;
		}
		
		return data;		
	}
}
