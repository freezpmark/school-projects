package objektyPreManipulaciu;

public class Pokladna {
	private boolean pouzite = false;
	private int peniaze;
	
	// Metoda na zapinanie ci vypinanie pokladne
	public void prepniStav() {
		if(this.pouzite == true)
			this.pouzite = false;
		else
			this.pouzite = true;
	}
	
	public boolean getPouzite() {return pouzite;}
	public void setPeniaze(int cislo) {peniaze = cislo;}
	public int getPeniaze() {return peniaze;}
	public void pricitajPeniaze(int cislo) {peniaze += cislo;}
}
