package interaction_objects;

public class Zamestnanec extends INTERACTION_OBJECT {
	public String Meno_Priezvisko;
	
	Zamestnanec(int poz, String Meno) {
		super(poz);
		this.Meno_Priezvisko = Meno;
	}
	
	public static void alarm(Zamestnanec zam) {
		System.out.println("!!!ALARM BOL ZAPNUTY!!!");
		System.out.println("!!!OBCHOD JE EVAKUOVANY!!!");
	}
}
