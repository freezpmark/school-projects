package interagujuceObjekty;

import velkoobchod.Velkoobchod;

public class Upratovac extends Zamestnanec {
	
	public Upratovac(String meno) {
		super(meno);
	}
	
	// Metoda na udrzbu cistotu popri priebehu spracovavania zakaznikov
	public void uprataj(Velkoobchod obchod) {
		if(obchod.getCistota() < 25)
			obchod.setCistota(1);
	}
}
