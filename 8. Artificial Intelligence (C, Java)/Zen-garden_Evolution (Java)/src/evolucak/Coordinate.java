package evolucak;

public class Coordinate {
	int r, c, dirR, dirC;	// row, column, direction row, direction column
	Coordinate precedent;
	
	Coordinate(int r, int c, int dirC, int dirR, Coordinate precedent) {
		this.precedent = precedent;
		this.dirR = dirC;
		this.dirC = dirR;
		this.c = c;
		this.r = r;
	}
}