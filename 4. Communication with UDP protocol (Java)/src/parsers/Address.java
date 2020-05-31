package parsers;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class Address {
	public static InetAddress vytvorAdresu(String strAddress){
		String[] cisla = new String[4];
		int dot;
		for(int i = 0; i < 3; i++){
			dot = strAddress.indexOf(".");							// zisti index prvej bodky adresy, bodka obsahuje index prvej bodky v poli tj. 3
			cisla[i] = strAddress.substring(0, dot);				// do cisla[i] dame cisla od 0 po 3 nie vratane.. to su 3 cisla
			strAddress = strAddress.substring(dot+1, strAddress.length());	// do adresy dame adresu od indexu 4 po velkost stringu nie vratane
			
			// v cisla[0] mame teda 3 cisla po sebe.. cyklus ide opat.. pricom teraz pojde od druhej bodky nie vratane.. atd.
			// v cisla[1] mam dalsie 3 cisla.. atd.
			//   cisla[2] dalsie 3
		}
		cisla[3] = strAddress;			// to su zvysne 3 cisla, nieje teda treba to mat v cykle
		byte[] adresa = new byte[4];
		adresa[0] = (byte) Integer.parseInt(cisla[0]);		// je to cislo 169 - 256
		adresa[1] = (byte) Integer.parseInt(cisla[1]);		// 			   254 - 256
		adresa[2] = (byte) Integer.parseInt(cisla[2]);
		adresa[3] = (byte) Integer.parseInt(cisla[3]);		// v kazdom indexe adresy mame pole troch cisiel
		try {
			return InetAddress.getByAddress(adresa);		// vratime pole cisel
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
}
