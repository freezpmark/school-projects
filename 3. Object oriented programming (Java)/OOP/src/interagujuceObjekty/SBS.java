package interagujuceObjekty;

public class SBS extends Zamestnanec {
	
	private boolean zasiahol;
	
	public SBS(String meno) {
		super(meno);
	}
	
	public boolean getZasahy() {return zasiahol;}
	public void resetZasahy() {zasiahol = true;}
	
	// Vyhadzovanie ludi v priebehu spracovavania zakaznikov
	public void vyhod(Clovek zloSyn) {
		if(zloSyn instanceof Zamestnanec) {
			System.out.println("Nieje mozne vyhodit zamestnanca obchodu!");
			return;
		}
		zasiahol = true;
		//zloSyn = null;
	}
}
