package systems;

import java.util.Random;
import java.util.Scanner;
import interaction_objects.Pokladnik;
import interaction_objects.Robot;
import interaction_objects.Zakaznik;
import interaction_objects.Administrator;
import manipulate_objects.Kosik;
import manipulate_objects.Pokladna;
import systems.Vytah;

public class Obchodny_dom {

	static public int vytahpocet;
	static public int staznosti;
	public int zarobok;
	public int cistota = 5;
	public int energia = 250;
	public int pocet_vybavenych, pocet_vyhodenych;
	public int pocet_robotov;
	
	Vytah Vyta;
	Sklad Skladik;
	Dom_jedlo PrvaCast;
	Dom_ostatne DruhaCast;
	
	Obchodny_dom() {
		this.Vyta = new Vytah();
		this.Skladik = new Sklad();
		this.PrvaCast = new Dom_jedlo();
		this.DruhaCast = new Dom_ostatne();
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Random generator = new Random();
		
		Obchodny_dom Dom = new Obchodny_dom();
		Zakaznik[] Zak = new Zakaznik[250];
		Robot[] Rob = new Robot[5];
		Pokladnik[] Pok = new Pokladnik[5];
		
		int i = 0, cislo, cislo2, pocet, rand, rand2, k = 0, l = 0, koniec = 1, pred_suma, mod = 0, pocet_zam = 0, 
				wave_zarobok, wave_kick, wave_energy, wave_dopln, wave_clean, wave_stazn;
		String retaz, citaj;
		
		Pokladna[] Stroj = new Pokladna[5];
		for(i = 0; i < Stroj.length; i++)
			Stroj[i] = new Pokladna();
		Kosik[] Kosiky = new Kosik[50];
		for(i = 0; i < Kosiky.length; i++)
			Kosiky[i] = new Kosik();
		
		Administrator Admin1 = new Administrator(100, "Marek_Horvath");
		Administrator Admin2 = new Administrator(150, "Jana_Figlova");
		
		System.out.println("\"dom\" - vypise stav Obchodneho domu");
		System.out.println("\"zasoby\" - vypise stav zasob v sklade aj v predajni");
		System.out.println("\"zampok\" - vypise obsah penazi u kazdej pokladni a mena pokladnikov k nim");
		System.out.println("\"pridajzak\" - pridanie zakaznika (naraz max 50 a dokopy max 250)");
		System.out.println("\"pridajpok\" - pridanie pokladnika (dokopy max 5)");
		System.out.println("\"pridajrob\" - pridanie robota (max 3)");
		
		konec:
		while(koniec >= 1) {
			if (koniec == 2) {
				System.out.println("\nZadajte novy prikaz (\"dom\", \"zasoby\", \"zampok\", \"pridajzak\", \"pridajpok\", \"pridajrob\")");
				koniec--;
			}
			koniec++;
			
			citaj = sc.nextLine();
			switch (citaj) {
				case "dom" : 	System.out.println("Energia domu: " + Dom.energia);
						System.out.println("Pocet zapnutych robotov: " + Dom.pocet_robotov);
						System.out.println("Pocet vybavenych zakaznikov: " + Dom.pocet_vybavenych);
						System.out.println("Pocet vyhodenych zakaznikov: " + Dom.pocet_vyhodenych);
						System.out.println("Pocet staznosti: " + Obchodny_dom.staznosti);
						System.out.println("Zarobok za celu dobu: " + Dom.zarobok);
						System.out.println("Cistota: " + Dom.cistota);
						break;
		
				case "zasoby" : System.out.println("ZASOBY V SKLADE");
						System.out.println("Pecivo = " + Dom.Skladik.pecivo);
						System.out.println("Mliecne vyrobky = " + Dom.Skladik.mliecne_vyrobky);
						System.out.println("Trash food = " + Dom.Skladik.trash_food);
					    System.out.println("Mrazene = " + Dom.Skladik.mrazene + "\n");
						System.out.println("Elektronika = " + Dom.Skladik.elektronika);
						System.out.println("Oblecenie = " + Dom.Skladik.oblecenie);
						System.out.println("Drogeria = " + Dom.Skladik.drogeria);
						System.out.println("Lekaren = " + Dom.Skladik.lekaren + "\n");
						
						System.out.println("ZASOBY V PREDAJNI");
						System.out.println("Pecivo = " + Dom.PrvaCast.pecivo);
						System.out.println("Mliecne vyrobky = " + Dom.PrvaCast.mliecne_vyrobky);
						System.out.println("Trash food = " + Dom.PrvaCast.trash_food);
						System.out.println("Mrazene = " + Dom.PrvaCast.mrazene + "\n");
						System.out.println("Elektronika = " + Dom.DruhaCast.elektronika);
						System.out.println("Oblecenie = " + Dom.DruhaCast.oblecenie);
						System.out.println("Drogeria = " + Dom.DruhaCast.drogeria);
						System.out.println("Lekaren = " + Dom.DruhaCast.lekaren);
						break;
						
				case "zampok" : for(cislo = 0; ((cislo < 5) && (Stroj[cislo].pouzivana == true)); cislo++)
									System.out.println(Pok[cislo].Meno_Priezvisko + " pouziva " + (cislo+1) + ". pokladnu so sumou " + Stroj[cislo].cash);
								if(cislo == 0)
									System.out.println("Niesu pridany ziadny pokladnici");
								break;
							
				case "pridajzak" : 
						System.out.println("Kolko chcete pridat zakaznikov?");
						wave_zarobok = wave_kick = wave_energy = wave_dopln = wave_clean = wave_stazn = Obchodny_dom.vytahpocet = 0;
						pocet = sc.nextInt();
						if (pocet > Kosiky.length) {
							System.out.println("Nedostatok dispozicnych kosikov");
							break;
						}
						if (pocet > (pocet_zam * 10)) {
							System.out.println("Nedostatok pokladni pre tolkych zakaznikov, trvalo by to pridlho");
							break;
						}
						
						for(cislo = 0; cislo < pocet; i++, cislo++) {
							if(i > Zak.length) {
								System.out.println("Vycerpal sa limit zakaznikov");
								break;
							}
							rand = generator.nextInt(100);
							Zak[i] = new Zakaznik(rand, Kosiky[cislo]);
							pred_suma = rand;
							
							// NAKUP
							rand = generator.nextInt(9);
							if (rand > 5) {													// 33% sanca na vstup do VYTAHU
								Dom.Vyta.PouziVytah(Zak[i], Admin1);			
								if(Zak[i].pozicia == 0)
									continue;
								while(pred_suma >= 20) {
									
									rand = generator.nextInt(5);
									if(rand >= 4) {
										if(pred_suma >= 10) {
											Dom.DruhaCast.drogeria--;
											Zak[i].nakup.drogeria++;
											pred_suma -= 10;
											wave_zarobok += 10;
										}
									}
									else if (rand == 3) {
										if(pred_suma >= 15) {
											Dom.DruhaCast.oblecenie--;
											Zak[i].nakup.oblecenie++;
											pred_suma -= 15;
											wave_zarobok += 15;
										}
									}
									else if (rand == 2) {
										if(pred_suma >= 20) {
											Dom.DruhaCast.lekaren--;
											Zak[i].nakup.lekaren++;
											pred_suma -= 20;
											wave_zarobok += 20;
										}
									}
									else if (rand == 1) {
										if(pred_suma >= 25) {
											Dom.DruhaCast.elektronika--;
											Zak[i].nakup.elektronika++;
											pred_suma -= 25;
											wave_zarobok += 25;
										}
									}
									if((Dom.DruhaCast.drogeria <= 0) || (Dom.DruhaCast.oblecenie <= 0) || (Dom.DruhaCast.lekaren <= 0)
											|| (Dom.DruhaCast.elektronika <= 0)) {

										// DOPLNENIE ZASOB
										for(cislo2 = 0; cislo2 < Rob.length; cislo2++) {
											if ((Rob[cislo2] != null) && (Rob[cislo2].tovar == true)) {
												Robot.dopln(Rob[cislo2], Dom.PrvaCast, Dom.DruhaCast, Dom.Skladik, Dom);
												break;
											}
											if (cislo2+1 == Rob.length) {
												System.out.println("Minul sa tovar v 2. casti domu. Chcete zapnut robota na doplnenie tovaru? (ano/nie)");
												while(koniec >= 1) {
													retaz = sc.nextLine();
													if(retaz.equals("ano")) {
														Rob[l] = new Robot(true, Dom);
														wave_energy += 30;
														Robot.dopln(Rob[l], Dom.PrvaCast, Dom.DruhaCast, Dom.Skladik, Dom);
														l++;
														break;
													}
													else if(retaz.equals("nie")) {
														Zak[i].spokojnost--;
														break;
													}
												}
											}
										}
										wave_energy += 3;
										wave_dopln++;
									}
								}
								Dom.Vyta.PouziVytah(Zak[i], Admin1);
								if(Zak[i].pozicia == 0)
									continue;
							}
							
							
							while(pred_suma >= 5) {
								rand = generator.nextInt(5);
								if(rand >= 4) {
									if(pred_suma >= 3) {
										Dom.PrvaCast.pecivo--;
										Zak[i].nakup.pecivo++;
										pred_suma -= 3;
										wave_zarobok += 3;
									}
								}
								else if (rand == 3) {
									if(pred_suma >= 5) {
										Dom.PrvaCast.trash_food--;
										Zak[i].nakup.trash_food++;
										pred_suma -= 5;
										wave_zarobok += 5;
									}
								}
								else if (rand == 2) {
									if(pred_suma >= 7) {
										Dom.PrvaCast.mliecne_vyrobky--;
										Zak[i].nakup.mliecne_vyrobky++;
										pred_suma -= 7;
										wave_zarobok += 7;
									}
								}
								else if (rand == 1) {
									if(pred_suma >= 10) {
										Dom.PrvaCast.mrazene--;
										Zak[i].nakup.mrazene++;
										pred_suma -= 10;
										wave_zarobok += 10;
									}
								}
								
								if((Dom.PrvaCast.pecivo <= 0) || (Dom.PrvaCast.trash_food <= 0) || (Dom.PrvaCast.mliecne_vyrobky <= 0)
										|| (Dom.PrvaCast.mrazene <= 0)) {
									
									// DOPLNENIE ZASOB
									for(cislo2 = 0; cislo2 < Rob.length; cislo2++) {
										if ((Rob[cislo2] != null) && (Rob[cislo2].tovar == true)) {
											Robot.dopln(Rob[cislo2], Dom.PrvaCast, Dom.DruhaCast, Dom.Skladik, Dom);
											break;
										}
										if (cislo2+1 == Rob.length) {
											System.out.println("Minul sa tovar v 1. casti domu. Chcete zapnut robota na doplnenie tovaru? (ano/nie)");
											while(koniec >= 1) {
												retaz = sc.nextLine();
												if(retaz.equals("ano")) {
													Rob[l] = new Robot(true, Dom);
													wave_energy += 30;
													Robot.dopln(Rob[l], Dom.PrvaCast, Dom.DruhaCast, Dom.Skladik, Dom);
													l++;
													break;
												}
												else if(retaz.equals("nie")) {
													Zak[i].spokojnost--;
													break;
												}
											}
										}
									}
									wave_energy += 3;
									wave_dopln++;
								}
							}
							
							//CISTOTA
							rand = generator.nextInt(10);
							if (rand >= 8)
								Dom.cistota--;
							
							if(Dom.cistota < 0) {
								for(cislo2 = 0; cislo2 < Rob.length; cislo2++) {
									if (Rob[cislo2] != null) {
										Robot.cisti(Rob[cislo2], Dom);
										break;
									}
									if (cislo2+1 == Rob.length) {
										System.out.println("V dome je necisto. Chcete zapnut robota a poslat ho upratovat? (ano/nie)");
										while(koniec >= 1) {
											retaz = sc.nextLine();
											if(retaz.equals("ano")) {
												Rob[l] = new Robot(Dom);
												wave_energy += 20;
												Robot.cisti(Rob[l], Dom);
												l++;
												break;
											}
											else if(retaz.equals("nie")) {
												Zak[i].spokojnost--;
												break;
											}
										}
									}
								}
								wave_energy += 2;
								wave_clean++;
							}
								
							
						rand = generator.nextInt(10);
						if(rand == 5)					// nervak
							Zak[i].spokojnost--;
							
						if (cislo < 10)
							mod = 0;
						else if (cislo < 20)
							mod = 1;
						else if (cislo < 30)
							mod = 2;
						else if (cislo < 40)
							mod = 3;
						else if (cislo < 50)
							mod = 4;
						
						Zakaznik.Zaplatenie(Zak[i], Kosiky[cislo], Stroj[mod], Pok[mod]);
						
						// KONTROLA SPOKOJNOSTI.. VYHODENIA
						if (Zak[i].spokojnost == 3) {
							for(cislo2 = 0; cislo2 < Rob.length; cislo2++) {
								if ((Rob[cislo2] != null) && (Rob[cislo2].security == true)) {
									Robot.vyhodenie(Rob[cislo2], Zak[i], Dom);
									wave_energy += 5;
									wave_kick++;
									break;
								}
								else if (cislo2+1 == Rob.length)
									wave_stazn++;
							}
						}
						if(Zak[i].pozicia == 0)
							continue;
						
						// ALARM
						if (Zak[i].spokojnost <= 2) {
							Administrator.alarm(Admin2);
							koniec = 0;
							break konec;
						}
						
						Dom.pocet_vybavenych++;
						}
						Dom.zarobok += wave_zarobok;
						
						System.out.println("Zarobok tejto vlny zakaznikov: " + wave_zarobok);
						System.out.println("Pocet staznosti: " + wave_stazn);
						System.out.println("Pocet pouziti vytahu: " + Obchodny_dom.vytahpocet);
						System.out.println("Pocet cisteni v obchode: " + wave_clean);
						System.out.println("Pocet doplneni tovaru: " + wave_dopln);
						System.out.println("Pouzitej energie bolo: " + wave_energy + " (zostava " + Dom.energia + " energie)");
						System.out.println("Pocet vyhodenych zakaznikov: " + wave_kick); 
						break;

				case "pridajpok" :  System.out.println("Kolko chcete pridat pokladnikov? (zostava " + (Pok.length - k) + " volnych pokladni)");
									pocet = sc.nextInt();
						
									if((pocet > Pok.length) || (pocet + k > (Pok.length))) {
										System.out.println("Nedostatok dispozicnych pokladni");
										retaz = sc.nextLine();
										break;
									}
									retaz = sc.nextLine(); // OSETRENIE...
						
									for(cislo = 0; ((k <= (Pok.length + 1)) && (cislo < pocet)); k++, cislo++) {
										rand2 = generator.nextInt(10);
										System.out.println("Zadajte meno " + (k+1) + ". pokladnika");
										retaz = sc.nextLine();
										Pok[k] = new Pokladnik(rand2, retaz, Stroj[k]);
										pocet_zam++;
									}
									if(pocet == 0)
										System.out.println("Neboli pridany ziadny pokladnici");
									else
										System.out.println("Bolo pridanych " + cislo + " Pokladnikov");
									break;
						
				case "pridajrob" :  if(l >= 20) {
			   							System.out.println("Vycerpal sa limit robotov");
			   							break;
		   							}
						   			System.out.println("Zadajte uroven robota ktory zaroven bude ovladat vsetky pod-úrovne (k dispozicii je " + Dom.energia + " energie)");
						   			System.out.println("1 - cistic (stoji 20 energie)");
						   			System.out.println("2 - dovazac tovaru (stoji 30 energie)");
						   			System.out.println("3 - bezpecnostny robot (stoji 50 energie)");
						   
						   			cislo = sc.nextInt();
						   			if((cislo >= 1) && (cislo <= 3)) {
						   				if (cislo == 1) {
						   					if (20 > Dom.energia) {
						   						System.out.println("Nedostatok energie na zapnutie");
						   						break;
						   					}
						   					Rob[l] = new Robot(Dom);
						   				}
						   				else if (cislo == 2) {
						   					if (30 > Dom.energia) {
						   						System.out.println("Nedostatok energie na zapnutie");
						   						break;
						   					}
						   					Rob[l] = new Robot(true, Dom);
						   				}
						   				else if (cislo == 3) {
						   					if (50 > Dom.energia) {
						   						System.out.println("Nedostatok energie na zapnutie");
						   						break;
						   					}
						   					Rob[l] = new Robot(true, true, Dom);
						   				}
						   				l++;
						   			}
						   			else
						   				System.out.println("Nespravne zadane cislo");
						   			citaj = sc.nextLine(); // osetrenie
						   			break;
			}
		}
	}
}