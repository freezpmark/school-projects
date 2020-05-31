package velkoobchod;

public class Predajna {
    private int pecivo = 100;
    private int mliecneVyrobky = 100;
    private int mrazene = 100;
    private int elektronika = 20;
    private int oblecenie = 20;
    private int drogeria = 20;
    
    public int getPecivo() {return pecivo;}
    public int getMliecneVyrobky() {return mliecneVyrobky;}
    public int getMrazene() {return mrazene;}
    public int getElektronika() {return elektronika;}
    public int getOblecenie() {return oblecenie;}
    public int getDrogeria() {return drogeria;}
	public void pricitajPecivo(int pocet) {pecivo += pocet;}
	public void pricitajMliecneVyrobky(int pocet) {mliecneVyrobky += pocet;}
	public void pricitajMrazene(int pocet) {mrazene += pocet;}
	public void pricitajElektronika(int pocet) {elektronika += pocet;}
	public void pricitajOblecenie(int pocet) {oblecenie += pocet;}
	public void pricitajDrogeria(int pocet) {drogeria += pocet;}
	
	void vypisPredajnu() {
		System.out.println("ZASOBY V PREDAJNI");
		System.out.println("Pecivo = " + pecivo);
		System.out.println("Mliecne vyrobky = " + mliecneVyrobky);
		System.out.println("Mrazene = " + mrazene);
		System.out.println("Elektronika = " + elektronika);
		System.out.println("Oblecenie = " + oblecenie);
		System.out.println("Drogeria = " + drogeria);
	}
}
