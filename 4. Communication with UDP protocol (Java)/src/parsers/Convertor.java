package parsers;

import java.nio.ByteBuffer;

public class Convertor {

	public static int getIntegerOf1Hex(byte b1){
		return (b1 & 0xFF);
	}
	
	public static int getIntegerOf2Hex(byte b1, byte b2){
		return (b1 & 0xFF)*256 + (b2 & 0xFF);
	}
	
	public static int getIntegerOf4Hex(byte b1, byte b2, byte b3, byte b4){
		return (b1 & 0xFF)*256*256*256 + (b2 & 0xFF)*256*256 + (b3 & 0xFF)*256 + (b4 & 0xFF);
	}
	
	public static byte[] IntToBytes(int cislo){
		return ByteBuffer.allocate(4)
				.putInt(cislo)
				.array();
	}
	
	public static int BytesToInt(byte[] bytes, int offset, int length){  // v podstate to iste ako getIntegerOf4Hex
	    ByteBuffer buffer = ByteBuffer.allocate(4);
	    buffer.put(bytes, offset, length);
	    buffer.flip();
	    return buffer.getInt();
	}
	
	public static byte[] LongToBytes(long num){
		return ByteBuffer.allocate(8)
				.putLong(num)
				.array();
	}
	
	public static long BytesToLong(byte[] bytes, int offset, int length){
	    ByteBuffer buffer = ByteBuffer.allocate(8);
	    buffer.put(bytes, offset, length);
	    buffer.flip();
	    return buffer.getLong();
	}
	
}
