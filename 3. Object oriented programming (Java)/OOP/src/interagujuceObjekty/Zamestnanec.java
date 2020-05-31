package interagujuceObjekty;

public class Zamestnanec extends Clovek {
	
	Zamestnanec(String meno) {
		this.menoPriezvisko = meno;
	}
	
	// Metoda na vyhodenie zlych ludi
	public void vyhod(Clovek zloSyn) {
		if(zloSyn instanceof Zamestnanec) {
			System.out.println("Nieje mozne vyhodit zamestnanca obchodu!");
			return;
		}
		//zloSyn = null;
	}
}
