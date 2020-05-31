package velkoobchod;
import java.util.Random;
import java.util.Scanner;

import interagujuceObjekty.*;

public class Konzola {
	
	static Scanner sc;
	static Random generator = new Random();
	static boolean done;
	
	 void kupPocetPeciva(Velkoobchod obchod) {
		done = false;
		while(!done){
			try {
				System.out.println("Zadajte cislom kolko chcete kupit peciva:");
				obchod.getAdmin().kupPecivo(sc.nextInt(), obchod); 
				sc.nextLine();
				done = true;
			} catch(Exception e) {
				sc.next();
				System.out.println("Nezadali ste cislo.");
			}
		}
	}
	
	 void kupPocetMliecne(Velkoobchod obchod) {
		done = false;
		while(!done){
			try {
				System.out.println("Zadajte cislom kolko chcete kupit mliecnych vyrobkov:");
				obchod.getAdmin().kupMliecne_vyrobky(sc.nextInt(), obchod); 
				sc.nextLine();
				done = true;
			} catch(Exception e) {
				sc.next();
				System.out.println("Nezadali ste cislo.");
			}
		}
	}
	
	 void kupPocetMrazene(Velkoobchod obchod) {
		done = false;
		while(!done){
			try {
				System.out.println("Zadajte cislom kolko chcete kupit mrazenych vyrobkov:");
				obchod.getAdmin().kupMrazene(sc.nextInt(), obchod); 
				sc.nextLine();
				done = true;
			} catch(Exception e) {
				sc.next();
				System.out.println("Nezadali ste cislo.");
			}
		}
	}
	 void kupPocetDrogerie(Velkoobchod obchod) {
		done = false;
		while(!done){
			try {
				System.out.println("Zadajte cislom kolko chcete kupit drogerie:");
				obchod.getAdmin().kupDrogeria(sc.nextInt(), obchod); 
				sc.nextLine();
				done = true;
			} catch(Exception e) {
				sc.next();
				System.out.println("Nezadali ste cislo.");
			}
		}
	}
	
	 void kupPocetOblecenia(Velkoobchod obchod) {
		done = false;
		while(!done){
			try {
				System.out.println("Zadajte cislom kolko chcete kupit oblecenia:");
				obchod.getAdmin().kupOblecenie(sc.nextInt(), obchod); 
				sc.nextLine();
				done = true;
			} catch(Exception e) {
				sc.next();
				System.out.println("Nezadali ste cislo.");
			}
		}
	}

	 void kupPocetElektroniky(Velkoobchod obchod) {
		done = false;
		while(!done){
			try {
				System.out.println("Zadajte cislom kolko chcete kupit elektroniky:");
				obchod.getAdmin().kupElektronika(sc.nextInt(), obchod); 
				sc.nextLine();
				done = true;
			} catch(Exception e) {
				sc.next();
				System.out.println("Nezadali ste cislo.");
			}
		}
	}

	

	public static void main(String[] args) {
		
		Konzola CLI = new Konzola();
		sc = new Scanner(System.in);
		Administrator admin = new Administrator("Filip_Skolnik");
		Velkoobchod obchod = new Velkoobchod(admin);
		
		obchod.vypisUvodneInfo();

		// Cyklus rozhrania na spravovanie obchodu
		while(obchod.getFinancie() > 0) {
			System.out.println("\nZadajte novy prikaz (\"info\", \"kupit\", \"start\")");
			
			switch (sc.nextLine()) {
				case "info" :	// Informacie poctu zamestnancov a tovaru
					obchod.vypisInfo();
					break;
					
				case "kupit" :	// Zamestnavanie alebo kupovanie
					obchod.vypisCien();
					
					switch (sc.nextLine()) { 
						// Zamestnavanie ludi
						case "pokladnik" :
							System.out.println("Zadajte meno " + (obchod.getPokladnik().size()+1) + ". pokladnika:");
							admin.kupPokladnika(sc.nextLine(), obchod);
							break;
							
						case "SBS" :
							System.out.println("Zadajte meno " + (obchod.getSBS().size()+1) + ". SBSkara:");
							admin.kupSBS(sc.nextLine(), obchod);
							break;
							
						case "skladnik" :
							System.out.println("Zadajte meno " + (obchod.getSkladnik().size()+1) + ". skladnika:");
							admin.kupSkladnika(sc.nextLine(), obchod);
							break;
						
						case "upratovac" :
							System.out.println("Zadajte meno " + (obchod.getUpratovac().size()+1) + ". upratovaca:");
							admin.kupUpratovaca(sc.nextLine(), obchod);
							break;
					
						// Kupovanie produktov
						case "pecivo" :
							CLI.kupPocetPeciva(obchod);
							break;
						
						case "mliecne_vyrobky" :
							CLI.kupPocetMliecne(obchod);
							break;
						
						case "mrazene" :
							CLI.kupPocetMrazene(obchod);
							break;
							
						case "drogeria" :
							CLI.kupPocetDrogerie(obchod);
							break;
						
						case "oblecenie" :
							CLI.kupPocetOblecenia(obchod);
							break;
						
						case "elektronika" :
							CLI.kupPocetElektroniky(obchod);
							break;
						}
						break;
				
				case "start" : 	// Odstartuje pracovny den
						obchod.vytvorZakaznikov();
						for(Iterator iter = obchod.getIterator(); iter.hasNext();) {	// Cyklus na spracovanie zakaznikov pomocou navrhoveho vzoru Iterator
							Zakaznik zakaznik = (Zakaznik)iter.getNext();
							obchod.vykonajPriebeh(zakaznik);
						}
						obchod.koniecDna();
						break;
			}
		}
		System.out.println("Obchod skrachoval a konci!");
	}

}
