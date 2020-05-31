package systems;

import interaction_objects.Administrator;
import interaction_objects.Zakaznik;
import java.util.Scanner;
import java.util.Random;

public class Vytah {
	
	Scanner sc = new Scanner(System.in);
	String citaj;
	static Random rand = new Random();
	static int pokaz;
	public static boolean funkcnost = true;
	
	
	 void PouziVytah(Zakaznik obj, Administrator admin) {
			pokaz = rand.nextInt(31);
			
			
		if ((pokaz >= 20) || (Vytah.funkcnost == false)) {
			obj.spokojnost--;
			Administrator.oprava(admin);
			
			/*
			System.out.println("Vytah sa pokazil alebo uz bol pokazeny!");
			System.out.println("Chcete ho opravit? (ano/nie)");
			
			while(pokaz >= 20) {
				citaj = sc.nextLine();
				if(citaj.equals("ano")) {
					Administrator.oprava(admin);
					break;
				}
				else if(citaj.equals("nie")) {
					obj.pozicia = 0;
					Vytah.funkcnost = false;
					return;
				}
			}
			*/
		}
		Obchodny_dom.vytahpocet++;
		if (obj.pozicia == 1)
			obj.pozicia = 2;
		else
			obj.pozicia = 1;
	}
}