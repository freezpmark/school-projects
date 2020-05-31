package interagujuceObjekty;

import velkoobchod.Velkoobchod;

public class Administrator extends Zamestnanec {
	
	public Administrator(String meno) {
		super(meno);
	}
	
	// Prekonana metoda umoznujuca administratorovi vyhodit kohokolvek
	public void vyhod(Clovek zloSyn) {
		//zloSyn = null;
	}
	
	// Metody na zamestnavanie ludi
	public void kupSBS(String meno, Velkoobchod obch) {
		SBS sbs = new SBS(meno);
		obch.setFinancie(-28);
		obch.addSBS(sbs);
	}
	public void kupSkladnika(String meno, Velkoobchod obch){
		Skladnik skladn = new Skladnik(meno);
		obch.setFinancie(-20);
		obch.addSkladnik(skladn);
	}
	public void kupPokladnika(String meno, Velkoobchod obchod) {
		obchod.setFinancie(-24);
		Pokladnik pok  = new Pokladnik(meno, obchod.stroj[obchod.getPokladnik().size()]);
		obchod.addPokladnik(pok);
	}
	public void kupUpratovaca(String meno, Velkoobchod obch) {
		Upratovac upra = new Upratovac(meno);
		obch.setFinancie(-16);
		obch.addUpratovac(upra);
	}
	
	// Metody na kupu produktov
	public void kupPecivo(int pocet, Velkoobchod Obchod) {
		if((pocet*1) > Obchod.getFinancie()) {
			System.out.println("Nemas dostatok financii!");
			return;
		}
		Obchod.sklad.pricitajPecivo(pocet);
		Obchod.setFinancie(-pocet*1);
	}
	public void kupMliecne_vyrobky(int pocet, Velkoobchod Obchod) {
		if((pocet*2) > Obchod.getFinancie()) {
			System.out.println("Nemas dostatok financii!");
			return;
		}
		Obchod.sklad.pricitajMliecneVyrobky(pocet);
		Obchod.setFinancie(-pocet*2);
	}
	public void kupMrazene(int pocet, Velkoobchod Obchod) {
		if((pocet*4) > Obchod.getFinancie()) {
			System.out.println("Nemas dostatok financii!");
			return;
		}
		Obchod.sklad.pricitajMrazene(pocet);
		Obchod.setFinancie(-pocet*4);
	}
	public void kupElektronika(int pocet, Velkoobchod Obchod) {
		if((pocet*5) > Obchod.getFinancie()) {
			System.out.println("Nemas dostatok financii!");
			return;
		}
		Obchod.sklad.pricitajElektronika(pocet);
		Obchod.setFinancie(-pocet*5);
	}
	public void kupOblecenie(int pocet, Velkoobchod Obchod) {
		if((pocet*4) > Obchod.getFinancie()) {
			System.out.println("Nemas dostatok financii!");
			return;
		}
		Obchod.sklad.pricitajOblecenie(pocet);
		Obchod.setFinancie(-pocet*4);
	}
	public void kupDrogeria(int pocet, Velkoobchod Obchod) {
		if((pocet*3) > Obchod.getFinancie()) {
			System.out.println("Nemas dostatok financii!");
			return;
		}
		Obchod.sklad.pricitajDrogeria(pocet);
		Obchod.setFinancie(-pocet*3);
	}
}
