package interaction_objects;

import systems.Vytah;

public class Administrator extends Zamestnanec {
	
		public Administrator(int cash, String meno) {
			super(3, meno);
		}
		
		public static void oprava(Administrator admin) {
			Vytah.funkcnost = true;
		}
		
		public static void alarm(Zamestnanec admin) {
			System.out.println("!!!ALARM BOL ZAPNUTY!!!");
			System.out.println("!!!OBCHOD JE EVAKUOVANY!!!");
		}
}