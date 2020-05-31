package interaction_objects;

import manipulate_objects.Kosik;
import manipulate_objects.Pokladna;

public class Zakaznik extends INTERACTION_OBJECT {
	public int hotovost;
	public int spokojnost = 5;
	public Kosik nakup;
	
	public Zakaznik (int cash, Kosik nak) {
		super(1);
		this.hotovost = cash;
		this.nakup = nak;
		nak.dispozicia = false;
	}
	
	public static void Zaplatenie(Zakaznik Zak, Kosik kos, Pokladna pok, Pokladnik zam) {
		if (zam.rychlost < 2)
			Zak.spokojnost--;
		Zak.hotovost -= kos.pecivo * 3;
		pok.cash += kos.pecivo * 3;
		Zak.hotovost -= kos.mliecne_vyrobky * 5;
		pok.cash += kos.mliecne_vyrobky * 5;
		Zak.hotovost -= kos.trash_food * 7;
		pok.cash += kos.trash_food * 7;
		Zak.hotovost -= kos.mrazene * 10;
		pok.cash += kos.mrazene * 10;
		
		Zak.hotovost -= kos.drogeria * 10;
		pok.cash += kos.drogeria * 10;
		Zak.hotovost -= kos.oblecenie * 15;
		pok.cash += kos.oblecenie * 15;
		Zak.hotovost -= kos.lekaren * 20;
		pok.cash += kos.lekaren * 20;
		Zak.hotovost -= kos.elektronika * 25;
		pok.cash += kos.elektronika * 25;
		
	    kos.pecivo = 0;
	    kos.mliecne_vyrobky = 0;
	    kos.trash_food = 0;
	    kos.mrazene = 0;
	    kos.elektronika = 0;
	    kos.oblecenie = 0;
	    kos.drogeria = 0;
	    kos.lekaren = 0;
	    kos.dispozicia = true;
		Zak.pozicia = 0;
	}
}