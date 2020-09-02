package evolucak;

import java.io.File;

public class Controller {
	private Garden zen;
	
	Controller() {
		File file 	= new File("testik1.txt");
		zen 		= new Garden(file);
		new EvoluteSearch(zen);
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Controller controller = new Controller();
	}
}
