package velkoobchod;
import objektyPreManipulaciu.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import interagujuceObjekty.*;

public class Velkoobchod {
	static Random generator = new Random();
	private int financie = 2000;
	private int cistota = 20;
	
	private Administrator admin;
	private List<Pokladnik> pokl;
	private List<SBS> bezp;
	private List<Skladnik> skla;
	private List<Upratovac> upra;
	private Zakaznik[] zakaznici;
	
	public Sklad sklad;
	public Predajna predajna;
	public Kosik kosik;
	public Pokladna[] stroj;
	
	Velkoobchod(Administrator Admin) {
		this.sklad = new Sklad();
		this.predajna = new Predajna();
		this.kosik = new Kosik();
		this.stroj = new Pokladna[10];
		this.pokl = new ArrayList<Pokladnik>();
		this.bezp = new ArrayList<SBS>();
		this.skla = new ArrayList<Skladnik>();
		this.upra = new ArrayList<Upratovac>();
		this.admin = Admin;
		for(int i = 0; i < 10; i++)
			this.stroj[i] = new Pokladna();
	}
	
	// Vnorena trieda na vytvorenie navrhoveho vzoru Iterator
	private class ZakaznikIterator implements Iterator {
		private int index;
		
		@Override
		public boolean hasNext() {
			if(index < zakaznici.length)
				return true;
			else
				return false;			
		}
		
		@Override
		public Object getNext() {
			if(this.hasNext())
				return zakaznici[index++];
			return null;
		}
	}
	
	public Iterator getIterator() {
		return new ZakaznikIterator();
	}
	
	public void addPokladnik(Pokladnik pok) {pokl.add(pok);}
	public void addSBS(SBS sbs) {bezp.add(sbs);}
	public void addSkladnik(Skladnik skl) {skla.add(skl);}
	public void addUpratovac(Upratovac upr) {upra.add(upr);}
	
	public List<Pokladnik> getPokladnik() {return pokl;}
	public List<SBS> getSBS() {return bezp;}
	public List<Skladnik> getSkladnik() {return skla;}
	public List<Upratovac> getUpratovac() {return upra;}
	
	public Administrator getAdmin() {return admin;}
	public int getFinancie() {return financie;}
	public int getCistota() {return cistota;}
	public int getPocetZakaznikov() {return zakaznici.length;}
	public void setFinancie(int cislo) {financie += cislo;}
	public void setCistota(int cislo) {cistota += cislo;}
	
	// Metoda na zistenie poctu vyhodenych do vypisu vysledkov na konci dna
	public int getPocetVyhodenych() { 
		int pocetVyhodenych;
		pocetVyhodenych = 0;
		for(int i = 0; i < getSBS().size(); i++)
			if(bezp.get(i).getZasahy() == true) {
				pocetVyhodenych++;
				bezp.get(i).resetZasahy();
			}
		return pocetVyhodenych;
	}
	
	void vypisUvodneInfo() {
		System.out.println("\"info\" - vypise zamestnancov obchodu a tovar");
		System.out.println("\"kupit\" - zamestnavanie ludi alebo kupovanie produktov");
		System.out.println("\"start\" - startne den");
	}
	
	void vypisInfo() {
		System.out.println("Financie obchodu: " + getFinancie());
		System.out.println("Pocet pokladnikov: " + pokl.size());
		for(int i = 0; i < pokl.size(); i++)
			System.out.println("	" + (i+1) + "." + pokl.get(i).menoPriezvisko);
		
		System.out.println("Pocet SBS-karov: " + bezp.size());
		for(int i = 0; i < bezp.size(); i++)
			System.out.println("	" + (i+1) + "." + bezp.get(i).menoPriezvisko);
		
		System.out.println("Pocet skladnikov: " + skla.size());
		for(int i = 0; i < skla.size(); i++)
			System.out.println("	" + (i+1) + "." + skla.get(i).menoPriezvisko);
		
		System.out.println("Pocet upratovacov: " + upra.size());
		for(int i = 0; i < upra.size(); i++)
			System.out.println("	" + (i+1) + "." + upra.get(i).menoPriezvisko);
		
		System.out.println();
		sklad.vypisSklad();
		predajna.vypisPredajnu();
	}
	
	void vypisCien() {
		System.out.println("Zadaj zakupenie: (zostava " + financie + " euro)");
		System.out.println("\"upratovac\" - vyplata 16 eur denne");
		System.out.println("\"skladnik\" - vyplata 20 eur denne");
		System.out.println("\"pokladnik\" - vyplata 24 eur denne");
		System.out.println("\"SBS\" - vyplata 28 denne\n");
		System.out.println("\"pecivo\" - 1 euro");
		System.out.println("\"mliecne_vyrobky\" - 2 eura");
		System.out.println("\"mrazene\" - 4 eura");
		System.out.println("\"drogeria\" - 3 eura");
		System.out.println("\"oblecenie\" - 4 eura");
		System.out.println("\"elektronika\" - 5 eur");
	}
	
	/*
	 *  Metoda na vypis informacii vysledkov na konci dna 
	 *  a resetnutie atributov u zamestnancov pre nasledujuci den
	 */
	void koniecDna() {
		if(getPokladnik().size() * 50 > getPocetZakaznikov()) {
			System.out.println("Pokladnici museli pracovat aspon na " + ((100*getPocetZakaznikov())/(getPokladnik().size() * 50)) + "%");
			System.out.println("Pocet vybavenych zakaznikov " + getPocetZakaznikov());
		}
		else {
			System.out.println("Pokladnici pracovali na 100%");
			System.out.println("Pocet vybavenych zakaznikov " + getPokladnik().size() * 50);
		}
		System.out.println("Pocet prichytenych pri kradezi " + getPocetVyhodenych());
		if (cistota == 25)
			System.out.println("Obchod je perfektne cisty");
		else if(cistota >= -15)
			System.out.println("Obchod je cisty");
		else if(cistota < -15)
			System.out.println("Obchod je znecisteny");
		int vyplata;
		vyplata = vyplatZamestnancov();
		System.out.println("Zisk z dnesneho dna je: " + (spocitajSumuPokladni() - vyplata));
		System.out.println("Financie obchodu: " + getFinancie());
		vyprazdniPokladne();
		for(int i = 0; i < getSkladnik().size(); i++)
			skla.get(i).resetVycerpanost();
		for(int i = 0; i < getPokladnik().size(); i++)
			pokl.get(i).resetZaneprazdnenost();		
	}
	
	/*
	 *  Metoda na vyplatenie vsetkych zamestnancov na konci dna 
	 *  a zistenie zisku dna pomocou navratovej hodnoty suma
	 */
	int vyplatZamestnancov() { 
		int suma = 0;
		suma += (getPokladnik().size() * 24);
		suma += (getSBS().size() * 28);
		suma += (getSkladnik().size() * 20);
		suma += (getUpratovac().size() * 16);
		setFinancie(-suma);
		return suma;
	}
	
	// Metoda pomocou ktorej sa vypocita zisk na konci dna
	int spocitajSumuPokladni() {
		int suma = 0;
		for(int i = 0; stroj[i].getPouzite() == true; i++)
			suma += stroj[i].getPeniaze();
		return suma;
	}
	
	// Metoda na vyprazdnutie pokladni na konci dna
	void vyprazdniPokladne() {
		for(int i = 0; stroj[i].getPouzite() == true; i++) {
			financie += stroj[i].getPeniaze();
			stroj[i].setPeniaze(0);
		}
	}
	
	// Metoda na vytvorenie zakaznikov na zaciatku dna
	public void vytvorZakaznikov() {
		int pocet = generator.nextInt(250);
		zakaznici = new Zakaznik[pocet];		// Randomizovanie poctu zakaznikov
		for(int i = 0; i < pocet; i++)
			zakaznici[i] = new Zakaznik(generator.nextInt(100));	// Randomizovanie hotovosti zakaznika
	}
	
	// Metoda spracovania celeho priebehu zakaznika v obchode
	public void vykonajPriebeh(Zakaznik zakaznik) {
		// Bezpecnost (vyhadzovanie/kradnutie)
		if(zakaznik.getHotovost() == 50) {
			for(int i = 0; i < getSBS().size(); i++)
				if(bezp.get(i).getZasahy() == false) {
					bezp.get(i).vyhod(zakaznik);
					return;
				}
			
			if(generator.nextInt(4) == 4)		// 20% sanca ze Administrator prichyti zakaznika pri kradezi
				admin.vyhod(zakaznik);			// polymorfizmus
			else
				zakaznik.kradni(this);
			return;
		}
		
		// Nakup zakaznika
		zakaznik.nakupDoKosika(this);
		
		// Doplnenie tovaru v predajni ak su skladnici k dispozicii a ak je to potrebne
		for(int i = 0; i < getSkladnik().size(); i++)
			if(skla.get(i).getVycerpanost() != -1)
				skla.get(i).skladuj(this);
		
		// Zachovavanie cistoty
		for(int i = 0; i < getUpratovac().size(); i++)
			upra.get(i).uprataj(this);
		
		// Zaplatenie (jeden pokladnik na 50 zakaznikov)
		for(int i = 0; i < getPokladnik().size(); i++)
			if(pokl.get(i).getZaneprazdnenost() < 50)
				zakaznik.zaplatenie(kosik, pokl.get(i), stroj[i]);
	}
}