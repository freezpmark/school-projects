package interagujuceObjekty;

import velkoobchod.*;

public class Skladnik extends Zamestnanec {
	
	private int vycerpanost;
	
	public Skladnik(String meno) {
		super(meno);
	}
	
	public int getVycerpanost() {return vycerpanost;}
	public void resetVycerpanost() {vycerpanost = 0;}
	
	// Metoda na skladovanie popri priebehu spracovavania zakaznikov
	public void skladuj(Velkoobchod obchod) {
		if (obchod.predajna.getPecivo() <= 0) {
			obchod.sklad.pricitajPecivo(-10);
			obchod.predajna.pricitajPecivo(10);
			vycerpanost++;
		}
		if (obchod.predajna.getMliecneVyrobky() <= 0) {
			obchod.sklad.pricitajMliecneVyrobky(-10);
			obchod.predajna.pricitajMliecneVyrobky(10);
			vycerpanost++;
		}
		if (obchod.predajna.getMrazene() <= 0) {
			obchod.sklad.pricitajMrazene(-10);
			obchod.predajna.pricitajMrazene(10);
			vycerpanost++;
		}
		if (obchod.predajna.getElektronika() <= 0) {
			obchod.sklad.pricitajElektronika(-10);
			obchod.predajna.pricitajElektronika(10);
			vycerpanost++;
		}
		if (obchod.predajna.getOblecenie() <= 0) {
			obchod.sklad.pricitajOblecenie(-10);
			obchod.predajna.pricitajOblecenie(10);
			vycerpanost++;
		}
		if (obchod.predajna.getDrogeria() <= 0) {
			obchod.sklad.pricitajDrogeria(-10);
			obchod.predajna.pricitajDrogeria(10);
			vycerpanost++;
		}
		
		if (vycerpanost > 9)
			koniecSmeny();
	}
	
	// Metoda ktora nastane pri urcitej vycerpanosti skladnika
	public void koniecSmeny(){
		vycerpanost = -1;
	}
}
