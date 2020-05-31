package interagujuceObjekty;

import objektyPreManipulaciu.Pokladna;

public class Pokladnik extends Zamestnanec {
	
	private int zaneprazdnenost;
	
	public Pokladnik(String meno, Pokladna pocitac) {
		super(meno);
		pocitac.prepniStav();
	}
	
	public int getZaneprazdnenost() {return zaneprazdnenost;}
	public void pridajZaneprazdnenost() {zaneprazdnenost++;}
	public void resetZaneprazdnenost() {zaneprazdnenost = 0;}
}
