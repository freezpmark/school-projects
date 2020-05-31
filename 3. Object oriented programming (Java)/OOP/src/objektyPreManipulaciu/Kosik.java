package objektyPreManipulaciu;

public class Kosik {
	
	private int pecivo;
	private int mliecneVyrobky;
	private int mrazene;
	private int elektronika;
	private int oblecenie;
	private int drogeria;
	
	public int getPecivo() {return pecivo;}
	public int getMliecneVyrobky() {return mliecneVyrobky;}
	public int getMrazene() {return mrazene;}
	public int getElektronika() {return elektronika;}
	public int getOblecenie() {return oblecenie;}
	public int getDrogeria() {return drogeria;}
	
	public void setPecivo(int cislo) {pecivo = cislo;}
	public void setMliecneVyrobky(int cislo) {mliecneVyrobky = cislo;}
	public void setMrazene(int cislo) {mrazene = cislo;}
	public void setElektronika(int cislo) {elektronika = cislo;}
	public void setOblecenie(int cislo) {oblecenie = cislo;}
	public void setDrogeria(int cislo) {drogeria = cislo;}
	
	/*
	 *  Metody na pridavanie produktov do kosika, 
	 *  pricom sa zaistuje aby zakaznik neisiel 
	 *  do minusu s hotovostou pri pokladni
	 */
	public int pridajMliecneVyrobky(int pocet, int prePay) {
		mliecneVyrobky += pocet;
		return (prePay - 7);
	}
	
	public int pridajPecivo(int pocet, int prePay) {
		pecivo += pocet;
		return (prePay - 3);
	}
	
	public int pridajMrazene(int pocet, int prePay) {
		mrazene += pocet;
		return (prePay - 10);
	}
	
	public int pridajElektronika(int pocet, int prePay) {
		elektronika += pocet;
		return (prePay - 25);
	}
	
	public int pridajOblecenie(int pocet, int prePay) {
		oblecenie += pocet;
		return (prePay - 15);
	}
	
	public int pridajDrogeria(int pocet, int prePay) {
		drogeria += pocet;
		return (prePay - 10);
	}
}
