package interaction_objects;

import manipulate_objects.Pokladna;

public class Pokladnik extends Zamestnanec {
	int rychlost;
	
	public Pokladnik(int fast, String meno, Pokladna stroj) {
		super(3, meno);
		this.rychlost = fast;
		stroj.pouzivana = true;
	}
}
