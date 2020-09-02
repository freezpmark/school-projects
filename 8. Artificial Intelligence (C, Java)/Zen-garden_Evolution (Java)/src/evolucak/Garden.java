package evolucak;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Garden {
	final static int SAND = 0;
	final static int ROCK = -1;
	int[][] map;
	int rows, cols;
	int halfPerimeter;
	int toRakeAmount;
	int rockAmount;

	public Garden(File f) {								// file parsing
		try (Scanner scan = new Scanner(f)) {
			rows = scan.nextInt();
			cols = scan.nextInt();
			map = new int[rows][cols];
			for (int i = 0; i < rows; i++)				// creating map
				for (int j = 0; j < cols; j++) 
					map[i][j] = SAND;
			for (int i, j; scan.hasNextInt(); rockAmount++) {	// adding rocks
				i = scan.nextInt();
				j = scan.nextInt();
				map[i][j] = ROCK;
			}
			toRakeAmount  = rows*cols - rockAmount;
			halfPerimeter = rows + cols;
		} catch (FileNotFoundException s) {
			s.printStackTrace();
		}
	}
	
	public int rakeGarden(int[] chromozome, boolean printConsole) {	// raking via chromozome and mark path
		Coordinate prevCoor, newCoor, coor;
		int transitionOrder = 1, cancelTransitNum = 0;
		int[][] mapI = copyMap();

		for (int i = 0; i < chromozome.length; i++) {
			coor = getDirection(chromozome[i]);

			// testing whether he can enter garden(Rock = -1, unrakedSand = 0, rakedSand = <1,..>)
			if (mapI[coor.r][coor.c] == 0) {
				while (inMapBounds(coor.r, coor.c)) {		// check if he isnt out of map
					if (mapI[coor.r][coor.c] != SAND) {		// collision to rakedSand/Rock
						prevCoor = coor.precedent;
						coor = changeDirection(chromozome[i], mapI, prevCoor);

						if (coor == null) {					// if we cant change direction, remove the path
							for(cancelTransitNum = 1; prevCoor != null; prevCoor = prevCoor.precedent)
								mapI[prevCoor.r][prevCoor.c] = 0;
							break;
						}
					}
					cancelTransitNum = 0;
					mapI[coor.r][coor.c] = transitionOrder;	// marking number into map, creating next coor via dir. num
					newCoor = new Coordinate(coor.r + coor.dirR, coor.c + coor.dirC, coor.dirR, coor.dirC, coor);
					coor = newCoor;							// overwriting coor, previous step is saved via last arg.
				}
				if(cancelTransitNum == 0)
					transitionOrder++;
			}
		}
		if (printConsole == true)				// printing map into console when we find solution
			printSolution(mapI);

		return countRakedSand(mapI);
	}

	public Coordinate getDirection(int num) {	// mapping coordinate border based on number
		if (num < 0)
			num *= -1;
		if (num <= cols)	//    r, c, dirC, dirR, prece
			return new Coordinate(0, num - 1, 1, 0, null);
		else if (num <= halfPerimeter)
			return new Coordinate(num - cols - 1, cols - 1, 0, -1, null);
		else if (num <= (halfPerimeter + rows))
			return new Coordinate(num - halfPerimeter - 1, 0, 0, 1, null);
		else
			return new Coordinate(rows - 1, num - halfPerimeter - rows - 1, -1, 0, null);
	}
	
	public Coordinate changeDirection(int gene, int[][] map, Coordinate coor) {		// changing dir. on collision
		Coordinate newCo = new Coordinate(coor.r, coor.c, coor.dirR, coor.dirC, coor);

		if (newCo.dirR != 0) { 			// we're moving through rows
			newCo.dirR = 0;
			if ((inMapBounds(newCo.r, newCo.c - 1) && map[newCo.r][newCo.c - 1] == SAND) 	// must be in map, and contains unraked sand
			 && (inMapBounds(newCo.r, newCo.c + 1) && map[newCo.r][newCo.c + 1] == SAND))  	// second side
				if (gene > 0) { 		// both sides are free, deciding via gen
					newCo.c++;
					newCo.dirC = 1; 	//   	< 
				} else {
					newCo.c--;
					newCo.dirC = -1;	// 		> 
				}
			else if (inMapBounds(newCo.r, newCo.c - 1) && map[newCo.r][newCo.c - 1] == SAND) { 		// only left side
				newCo.c--;
				newCo.dirC = -1;		// 		<
			} else if (inMapBounds(newCo.r, newCo.c + 1) && map[newCo.r][newCo.c + 1] == SAND) { 	// only right side
				newCo.c++;
				newCo.dirC = 1;			// 		>
			} else  	// nowhere to go
				return null;
			
		} else if (newCo.dirC != 0) { 		// we're moving through columns
			newCo.dirC = 0;
			if ((inMapBounds(newCo.r - 1, newCo.c) && map[newCo.r - 1][newCo.c] == SAND)
			 && (inMapBounds(newCo.r + 1, newCo.c) && map[newCo.r + 1][newCo.c] == SAND)) {
				if (gene > 0) {
					newCo.r++;
					newCo.dirR = 1;
				} else {
					newCo.r--;
					newCo.dirR = -1;
				}
			} else if ((inMapBounds(newCo.r - 1, newCo.c) && map[newCo.r - 1][newCo.c] == SAND)) {
				newCo.r--;
				newCo.dirR = -1;
			} else if ((inMapBounds(newCo.r + 1, newCo.c) && map[newCo.r + 1][newCo.c] == SAND)) {
				newCo.r++;
				newCo.dirR = 1;
			} else
				return null;
		}
		return newCo;
	}

	public boolean inMapBounds(int row, int col) {		// checking map bounds
		if ((col >= 0 && col < cols) && (row >= 0 && row < rows))
			return true;
		return false;
	}

	public void printSolution(int[][] map) {			// printing map into console
		System.out.print("\n");
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++)
				System.out.printf("%4d", map[i][j]);
			System.out.print("\n");
		}
		System.out.print("\n");
	}
	
	public int countRakedSand(int[][] map) {		// count raked sand
		int unrakedCounter = 0;
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				if (map[i][j] == SAND)
					unrakedCounter++;
		return (toRakeAmount - unrakedCounter);
	}

	public int[][] copyMap() {						// copies original map
		int[][] newMap 	= new int[map.length][];
		for (int i = 0; i < map.length; i++) {
			int[] pom 	= map[i];
			int cLength = pom.length;
			newMap[i] 	= new int[cLength];
			System.arraycopy(pom, 0, newMap[i], 0, cLength);
		}
		return newMap;
	}

	public int getGeneNum() {
		return halfPerimeter + rockAmount;
	}
	public int getPerimeter() {
		return 2 * halfPerimeter;
	}
}