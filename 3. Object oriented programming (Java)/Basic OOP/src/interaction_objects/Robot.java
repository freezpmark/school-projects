package interaction_objects;

import systems.Dom_jedlo;
import systems.Dom_ostatne;
import systems.Obchodny_dom;
import systems.Sklad;

public class Robot extends INTERACTION_OBJECT {
	boolean cisti = true;
	public boolean tovar = false;
	public boolean security = false;
	
	public Robot(Obchodny_dom Dom) {
		super(3);
		Dom.energia -= 20;
		Dom.pocet_robotov++;
	}

	public Robot(boolean tova, Obchodny_dom Dom) {
		super(3);
		Dom.energia -= 30;
		Dom.pocet_robotov++;
		this.tovar = tova;
	}
	
	public Robot(boolean tova, boolean secu, Obchodny_dom Dom) {
		super(3);
		Dom.energia -= 50;
		Dom.pocet_robotov++;
		this.tovar = tova;
		this.security = secu;
	}
	
	
	static public void cisti(Robot cist, Obchodny_dom Dom) {
		Dom.cistota = 5;
		Dom.energia -= 2;
	}
	
	static public void dopln(Robot tova, Dom_jedlo Prva, Dom_ostatne Druha, Sklad Skladik, Obchodny_dom Dom) {
		if (Prva.pecivo <= 0) {
			Skladik.pecivo -= 50;
			Prva.pecivo += 50;
		}
		
		if (Prva.mliecne_vyrobky <= 0) {
			Skladik.mliecne_vyrobky -= 50;
			Prva.mliecne_vyrobky += 50;
		}
		
		if (Prva.trash_food <= 0) {
			Skladik.trash_food -= 50;
			Prva.trash_food += 50;
		}

		if (Prva.mrazene <= 0) {
			Skladik.mrazene -= 50;
			Prva.mrazene += 50;
		}

		if (Druha.elektronika <= 0) {
			Skladik.elektronika -= 20;
			Druha.elektronika += 20;
		}
	
		if (Druha.oblecenie <= 0) {
			Skladik.oblecenie -= 20;
			Druha.oblecenie += 20;
		}
			
		if (Druha.drogeria <= 0) {
			Skladik.drogeria -= 20;
			Druha.drogeria += 20;
		}
		
		if (Druha.lekaren <= 0) {
			Skladik.lekaren -= 20;
			Druha.lekaren += 20;
		}
		Dom.energia -= 3;
	}
	

	
	public static void vyhodenie(Robot secu, Zakaznik amok, Obchodny_dom Dom) {
		amok.pozicia = 0;
		Dom.pocet_vyhodenych++;
		Dom.energia -= 5;
	}
}

