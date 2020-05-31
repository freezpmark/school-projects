package velkoobchod;

public class Sklad {
    private int pecivo = 1000;
    private int mliecneVyrobky = 1000;
    private int mrazene = 1000;
    private int elektronika = 200;
    private int oblecenie = 200;
    private int drogeria = 200;
    
    public void pricitajPecivo(int pocet) {pecivo += pocet;}
    public void pricitajMliecneVyrobky(int pocet) {mliecneVyrobky += pocet;}
    public void pricitajMrazene(int pocet) {mrazene += pocet;}
    public void pricitajElektronika(int pocet) {elektronika += pocet;}
    public void pricitajOblecenie(int pocet) {oblecenie += pocet;}
    public void pricitajDrogeria(int pocet) {drogeria += pocet;}
    
    void vypisSklad() {
		System.out.println("ZASOBY V SKLADE");
		System.out.println("Pecivo = " + pecivo);
		System.out.println("Mliecne vyrobky = " + mliecneVyrobky);
	    System.out.println("Mrazene = " + mrazene);
		System.out.println("Elektronika = " + elektronika);
		System.out.println("Oblecenie = " + oblecenie);
		System.out.println("Drogeria = " + drogeria + "\n");
	}
}
