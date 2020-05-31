package evolucak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EvoluteSearch {
	Garden zen;
	int geneNum;
	int[][] population;

	static int TOURNAMENT_SELECT = 2; 		// 1 = 2 individs, other = 3 individs
	final static int POP_SIZE = 100;
	final static int GENERATIONS = 10000;
	final static float MIN_MUT_RATE = 0.05f;
	final static float MAX_MUT_RATE = 0.80f;
	final static float CROSS_RATE = 0.90f;
	
	public EvoluteSearch(Garden garden) {
		this.zen = garden;
		this.geneNum = garden.getGeneNum();
		this.population = new int[POP_SIZE][geneNum];
		genetic();
	}

	public void genetic() {							// main genetic algorithm
		long start = System.currentTimeMillis();
		float mutRateAct = MIN_MUT_RATE;
		int[] fitness = new int[POP_SIZE];
		for (int i = 0; i < POP_SIZE; i++) 			// generating starting population
			population[i] = generateChromozome();
		
		//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ main loop for generation growth
		for(int gCounter = 1; gCounter < GENERATIONS; gCounter++) {
			int[][] children = new int[POP_SIZE][geneNum];
			int max = 0, iMax = 0;
			for (int i = 0; i < POP_SIZE; i++) {	// raking garden and returns # of raked
				fitness[i] = zen.rakeGarden(population[i], false);
				if(fitness[i] > max) {				// found max/min values and printing them
					max = fitness[i];
					iMax = i;
				}
			}
			System.out.printf("Generation: %5d.		Max raked: %4d		Mutation rate: %1.2f \n", gCounter, max, mutRateAct);

			if (max == zen.toRakeAmount) {						// we've found the solution
				zen.rakeGarden(population[iMax], true);			// rake again with printed map
				for (int i = 0; i < geneNum; i++)
					System.out.printf("%d ", population[iMax][i]);	// printing the chromozome of solution
				System.out.println("\nTime for finding solution: " + (System.currentTimeMillis() - start) + " ms.");
				break;
			}
			// changing mut rate cos of local max
			mutRateAct = mutRateAct >= MAX_MUT_RATE ? MIN_MUT_RATE : (mutRateAct + 0.01f);

			//################################################## creating new population
			for (int i = 0; i < POP_SIZE; i += 2) {
				int individ1, individ2;
				if (TOURNAMENT_SELECT == 1) {					// 2 individuals in tournament
					individ1 = doTournament2(fitness);	// returns 1 max raked from 2 random fitValues
					individ2 = doTournament2(fitness);
				} else {                                        // 3 individuals in tournament
					individ1 = doTournament3(fitness);
					individ2 = doTournament3(fitness);
				}
				for(int j = 0; j < geneNum; j++){				// kopcenie lepsich genov do 2 potomkov
					children[i][j] 		= population[individ2][j];
					children[i + 1][j] 	= population[individ1][j];
				}
				if (Math.random() < CROSS_RATE) { 				// uniform crossing with 90% rate
					for (int child = 0; child < 2; child++){	// children mutation
						for (int j = 0; j < geneNum; j++) {
							if (Math.random() < mutRateAct) {	// 5-30% nastane mutacia
								int mutNum = (int) (Math.random() * (zen.getPerimeter())) + 1;	// 1-40
								int index = 0;
								if (Math.random() > 0.5)
									mutNum *= -1;
								for (int k = 0; (k < geneNum) && (index == 0); k++) {	// searching for children with random chro. number
									if (children[i + child][k] == mutNum)
										index = k;
								}
								if (index != 0) { 					// if we have found it, swap index values 
									int pom = children[i + child][j];
									children[i + child][j] = children[i + child][index];
									children[i + child][index] = pom;
								} else								// if we didnt, change value with random num.
									children[i + child][j] = mutNum;
							}
						}
					}
				}
			}//################################################## we've ended with creating new population
			for (int i = 0; i < geneNum; i++)				// keeping the best individual for next generation
				children[0][i] = population[iMax][i];
			
			population = children;
		}//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
	}

	public int[] generateChromozome() {			// vygeneruje pole chromozomov
		int[] chromozome = new int[geneNum];
		List<Integer> rangeNum = new ArrayList<Integer>();
		for (int i = 0; i <= zen.getPerimeter(); i++) {
			if(i != 0)
				rangeNum.add(i);				// adding number from 1-Perimeter
		}
		Collections.shuffle(rangeNum);			// randomly shuffle them (chromozome numbers)
		for (int i = 0; i < geneNum; i++) {		// select first "geneNum"(count) chromozome
			int num = rangeNum.get(i);
			if (Math.random() > 0.5)
				num *= -1;
			chromozome[i] = num;
		}
		return chromozome;
	}
	
	public int doTournament2(int[] fitness) {
		int individual1 = (int) (Math.random() * POP_SIZE);
		int individual2 = (int) (Math.random() * POP_SIZE);
		int max = fitness[individual1] > fitness[individual2] ? individual1 : individual2;
		return max;
	}
	
	public int doTournament3(int[] fitness) {
		int max = 0;
		for(int i = 0, ind; i < 2; i++) {
			ind = (int) (Math.random() * POP_SIZE);
			if(max < ind)
				max = ind;
		}
		return max;
	}
}