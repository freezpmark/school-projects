package parsers;

public class FullMessage {

	private String[] part;
	
	public FullMessage(int partNumber) {
		part = new String[partNumber];
	}
	
	public void insertNewFragment(int order, String text) {
		part[order-1] = text;
	}
	
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < part.length; i++)
			sb.append(part[i]);
		return sb.toString();
	}
}

