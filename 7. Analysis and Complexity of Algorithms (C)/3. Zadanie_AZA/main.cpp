/*
Automat firmy ACME potrebuje pri svojej činnosti vydávať mince. Robí to tak, že ak je možné vydať mince so správnou celkovou hodnotou, tak si vyberie možnosť, ktorá minimalizuje hmotnosť vydaných mincí, a ak je takých viacero, tak automat preferuje vydávanie mincí s vyššou hodnotou. V každom momente má automat presný prehľad o hodnote, počte a hmotnosti mincí, ktoré má k dispozícii (všetky mince rovnakej hodnoty majú rovnakú hmotnosť). Vašou úlohou je napísať program pre automat, ktorý zistí, ako pre danú sumu na vstupe vydať mince tak, aby bola hmotnosť vydaných mincí najmenšia možná.

Vstup pozostáva z niekoľkých scenárov pre automat. Každý scenár zahŕňa jeden počiatočný stav automatu (dané hodnoty, hmotnosti a počty mincí) a postupnosti súm, ktoré bude treba postupne vydať (čiže mince budú v automate postupne ubúdať). Na výstup treba pre každú zo súm v scenári vypísať, koľko mincí jednotlivých hodnôt má automat vydať. Ak by bolo možné dosiahnuť minimálnu hmotnosť mincí dvomi spôsobmi (a1, a2, ..., am) aj (b1, b2, ..., bm), kde ai, bi sú počty kusov i-tej mince, a pre nejaké k platí ak > bk a aj = bj pre všetky j>k, tak automat musí zvoliť možnosť (a1, a2, ..., am).

Váš program by mal čítať dáta zo štandardného vstupu a písať výsledky (a nič iné) na štandardný výstup. Odporúčame dávať si pozor na korektné uvoľnovanie alokovanej pamäte.

Formát vstupu

Na vstupe je v prvom riadku počet scenárov, potom prázdny riadok a ďalej nasledujú jednotlivé scenáre oddelené vždy jedným prázdnym riadkom. Každý scenár má v prvom riadku počet rôznych druhov mincí m a potom nasleduje m riadkov s trojicami čísel (oddelené medzerami), ktoré udávajú postupne hodnotu mince, jej hmotnosť a počet takýchto mincí. Týchto m riadkov je usporiadaných podľa hodnoty mincí od najmenšej po najväčšiu (hodnoty pre jednotlivé typy mincí sú navzájom rôzne). V ďalšom riadku je počet súm n, ktoré treba vyplatiť, a v ďalšom riadku je n čísel oddelených medzerami, ktoré reprezentujú tieto sumy (treba dodržať ich poradie). Všetky čísla na vstupe sú kladné celé čísla medzi 1 a 100 000. Môžete predpokladať, že celkový počet mincí v automate je nanajvýš 100 000 a 1 ≤ n ≤ 200.

Formát výstupu

Pre každý scenár je výstupom postupnosť riadkov; postupnosti pre jednotlivé scenáre musia byť na výstupe presne v takom poradí, ako sú scenáre na vstupe. Výstup pre jednotlivé scenáre oddeľte jedným prázdnym riadkom. V každom scenári treba pre každú z m súm na vstupe vypísať na výstup presne jeden riadok (pričom tieto riadky sú presne v takom poradí, ako sumy na vstupe). Ak nie je možné vydať požadovanú sumu, mal by tento riadok obsahovať číslo -1 a nič iné; ak to možné je, musí obsahovať celé čísla oddelené medzerami (zakaždým jedna medzera), ktoré reprezentujú počty mincí jednotlivých hodnôt, ktoré má automat vydať (presne v takom poradí, ako boli mince zadané na vstupe).

Príklad vstupu:

            2

            2
            3 4 7
            5 9 7
            7
            1 7 10 10 15 10 10

            10
            5 59 444
            6 42 121
            7 82 868
            8 22 500
            9 39 606
            10 13 23
            12 67 484
            13 11 962
            14 10 713
            15 58 808
            5
            903 391 703 222 993

Príklad výstupu (zodpovedá vstupu vyššie):

            -1
            -1
            0 2
            0 2
            5 0
            0 2
            -1

            0 0 0 0 0 1 0 3 61 0
            0 0 0 0 0 0 0 1 27 0
            0 0 0 0 0 2 0 3 46 0
            0 0 0 0 0 0 0 2 14 0
            0 0 0 0 0 0 0 1 70 0
*/

// uloha-3.cpp -- Uloha 3
// Peter Markus, 4.5.2017 19:03:32

#include <stdio.h>
#include <stdlib.h>
#include <algorithm>
#define N 80000

using namespace std;

//hodnota, hmotnosť, počet (od najm. po najv. hodnota[su rozne])
//sumy (so zachovanou post)

int main()
{
  int i, j, k, l, m, n, p, r, s, t;
  int inputAmount;
  int sum;
  int *sums, *coins, *weights;
  int coinValue[N];
  int coinWeight[N];
  int *coinAmount;
  int *coinAmountStatic;;
  int *coinAmountHash;
  int **counting;
  FILE *fr;
  fr = fopen("test.txt", "w");
  //fprintf(fr, "%d", args);

  scanf("%d\n\n", &inputAmount);
  for(i = 0; i < inputAmount; i++) {
    //printf("%d. INPUT\n", i);
    scanf("%d\n", &m);			// pocet roznych druhov mincii
    coinAmountStatic = (int*) calloc(m, sizeof(int));
    coinAmount = (int*) calloc(m, sizeof(int));
    coinAmountHash = (int*) calloc(N, sizeof(int));
    for(j = 0; j < m; j++) {    // j-ta minca
      scanf("%d %d %d\n", &coinValue[j], &coinWeight[j], &coinAmountStatic[j]);
      coinAmount[j] = coinAmountStatic[j];
      coinAmountHash[coinValue[j]] = coinAmount[j];
    }
    scanf("%d\n", &n);			// pocet sum
    for(k = 0; k < n; k++) {
      //printf("%d. SUMA\n", k);

      /*if(k == 43) {
            printf("PRED\ntyp:   ");
            for(s = 0; s < sum; s++)
                printf("%d  ", s);
            printf("\rem:    ");
            for(s = 0; s < sum; s++)
                printf("%d  ", coinAmount[s]);
            printf("\n");
        }*/

      scanf("%d", &sum);
      sums 		= (int*) calloc((1+sum), sizeof(int));
      coins 	= (int*) calloc((1+sum), sizeof(int));
      weights 	= (int*) calloc((1+sum), sizeof(int));
      counting  = (int**) calloc(1+sum, sizeof(int*));
      for(t = 0; t < (1+sum); t++)
        counting[t] = (int *) calloc(m, sizeof(int));
      for(l = 0; l <= sum; l++) {
        sums[l] = l;
        if(l == 0)
          coins[l] = 0;
        else
          coins[l] = -1;
        weights[l] = 0;
      }

      for(r = 1; r <= sum; r++) {		// r-ta suma	for(p = 0; p < m; p++)  for(p = m-1; p >= 0; p--)
        for(p = 0; p < m; p++) {	    // p-ty minca

          if(r >= coinValue[p]) {		// hodnota mince musi byt vacsia/rovna ako r-ta suma
            if((coins[r - coinValue[p]] != -1) &&
              ((weights[r - coinValue[p]] + coinWeight[p] < weights[r]) || weights[r] == 0)) {
                if(counting[r-coinValue[p]][p] == coinAmount[p])
                    continue;
                if(coinAmountStatic[p] < coinAmountHash[coinValue[r]])
                    continue;

                if((r == sum) && (weights[r] == (weights[r - coinValue[p]] + coinWeight[p]))) {

                    /*for(s = 0; s < m; s++) {
                        if(counting[r-coinValue[p]][s] < counting[r][s])
                            val = 1;
                    }*/

                    //  tu kombinaciu ktora pouziva viac najvacsich minci

                    /*
                    if(coinValue[p] < coins[r]) {
                        printf("halo???????????");
                        continue;
                    }*/
                }
              	coins[r] = coinValue[p];
              	weights[r] = weights[r - coinValue[p]] + coinWeight[p];

                for(s = 0; s < m; s++)
                    counting[r][s] = counting[r-coinValue[p]][s];
                counting[r][p]++;
                if(counting[r][p] == coinAmount[p])
                    continue;
            }
          }
        }
      }

/*
      if(k == 43) {
      printf("Sum: ");
      for(s = 0; s <= sum; s++)
        printf("	%d", sums[s]);
      printf("\nCoin: ");
      for(s = 0; s <= sum; s++)
        printf("	%d", coins[s]);
      printf("\nWeig: ");
      for(s = 0; s <= sum; s++)
        printf("	%d", weights[s]);
      printf("\n\n");
      }*/
        if(coins[sum] == -1)
            fprintf(fr,"-1\n");//printf("-1\n");
        else {
            for(s = 0; s < m; s++) {
                fprintf(fr, "%d ", counting[sum][s]);	//printf("%d ", counting[sum][s]);
                coinAmount[s] -= counting[sum][s];
                coinAmountHash[coinValue[s]] -= counting[sum][s];
            }
            fprintf(fr,"\n");	//printf("\n");
        }
        // najdem najmensi weights, ak ich je viac, najdem ten, v ktorom bola celkova hodnota najvacsia
      /*if(k == 43) {
            printf("\nPO\ntype:		");
            for(s = 0; s < sum; s++)
                printf("%d  	", s);
            printf("\rem:    ");
            for(s = 0; s < sum; s++)
                printf("%d  	", coinAmount[s]);
            printf("\n");
        }*/
    }
     fprintf(fr,"\n");//printf("\n");
  }
  return 0;
}
