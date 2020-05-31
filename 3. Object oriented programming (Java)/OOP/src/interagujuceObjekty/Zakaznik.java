package interagujuceObjekty;

import java.util.Random;

import objektyPreManipulaciu.*;
import velkoobchod.*;

public class Zakaznik extends Clovek {
	
	private int hotovost;
	
	public Zakaznik(int peniaze) {
		hotovost = peniaze;
	}
	
	public int getHotovost() {return hotovost;}
	
	// Metoda na spracovanie zakaznika u pokladni
	public void zaplatenie(Kosik kosik, Pokladnik pokladnik, Pokladna stroj) {
		hotovost -= kosik.getPecivo() * 3;
		hotovost -= kosik.getMliecneVyrobky() * 5;
		hotovost -= kosik.getMrazene() * 10;
		stroj.pricitajPeniaze(kosik.getPecivo() * 3);
		stroj.pricitajPeniaze(kosik.getMliecneVyrobky() * 5);
		stroj.pricitajPeniaze(kosik.getMrazene() * 10);
		hotovost -= kosik.getDrogeria() * 10;
		hotovost -= kosik.getOblecenie() * 15;
		hotovost -= kosik.getElektronika() * 25;
		stroj.pricitajPeniaze(kosik.getDrogeria() * 10);
		stroj.pricitajPeniaze(kosik.getOblecenie() * 15);
		stroj.pricitajPeniaze(kosik.getElektronika() * 25);
		pokladnik.pridajZaneprazdnenost();
		kosik.setPecivo(0);
		kosik.setMliecneVyrobky(0);
		kosik.setMrazene(0);
		kosik.setElektronika(0);
		kosik.setOblecenie(0);
		kosik.setDrogeria(0);
	}
	
	// Metoda ktora nastane v nepritomnosti SBS alebo nevsimavosti zamestnancov
	public void kradni(Velkoobchod Obch) {
		Obch.setFinancie(-100);
	}
	
	// Metoda nakupu spracovavana pomocou randomizovanymi vypoctami
	public void nakupDoKosika(Velkoobchod obchod) {
		Random generator = new Random();
		int rand = 0;
		int toPay = hotovost;
		
		if(toPay >= 50) {
			rand = generator.nextInt(3);
			if(rand == 2 && obchod.predajna.getDrogeria() > 0) {
				if(toPay >= 10) {
					obchod.predajna.pricitajDrogeria(-1);
					toPay = obchod.kosik.pridajDrogeria(1, toPay);
				}
			}
			else if (rand >= 1 && obchod.predajna.getOblecenie() > 0) {
				if(toPay >= 15) {
					obchod.predajna.pricitajOblecenie(-1);
					toPay = obchod.kosik.pridajOblecenie(1, toPay);
				}
			}
			else if (rand >= 0 && obchod.predajna.getElektronika() > 0) {
				if(toPay >= 25) {
					obchod.predajna.pricitajElektronika(-1);
					toPay = obchod.kosik.pridajElektronika(1, toPay);
				}
			}
		}
			
		if(toPay >= 20) {
			rand = generator.nextInt(3);
			if(rand >= 2 && obchod.predajna.getPecivo() > 0) {
				if(toPay >= 2) {
					obchod.predajna.pricitajPecivo(-1);
					toPay = obchod.kosik.pridajPecivo(1, toPay);
				}
			}
			else if (rand >= 1 && obchod.predajna.getMliecneVyrobky() > 0) {
				if(toPay >= 7) {
					obchod.predajna.pricitajMliecneVyrobky(-1);
					toPay = obchod.kosik.pridajMliecneVyrobky(1, toPay);
				}
			}
			else if (rand >= 0 && obchod.predajna.getMrazene() > 0) {
				if(toPay >= 10) {
					obchod.predajna.pricitajMrazene(-1);
					toPay = obchod.kosik.pridajMrazene(1, toPay);
				}
			}
		}
		if(rand == 0)
			obchod.setCistota(-5);
	}
}
