/*
Úlohy: Úloha 1 - Úloha 1

Úlohou je pre danı neorientovanı ohodnotenı graf bez sluèiek a násobnıch hrán zisti cenu najlacnejšej zo všetkıch kostier, ktoré obsahujú tri dané hrany.

Všetky ceny sú kladné celé èísla. iadne dve hrany zadaného grafu nemajú rovnakú cenu. Ak zadanı graf neobsahuje iadnu kostru obsahujúcu dané tri hrany, vypíšte na vıstup miesto ceny kostry hodnotu -1.

Váš program by mal èíta dáta zo štandardného vstupu a písa vısledky (a niè iné) na štandardnı vıstup.

Formát vstupu

Na vstupe je v prvom riadku nezáporné celé èíslo k udávajúce poèet grafov na vstupe. Nasleduje k grafov; pred kadım grafom je jeden prázdny riadok. Graf zaèína riadkom, v ktorom sú dve celé èísla oddelené medzerou; prvé èíslo je poèet jeho vrcholov a druhé èíslo je poèet jeho hrán. Vrcholy grafu sú èíslované celımi èíslami od 1 nahor (bez medzier). Potom nasleduje zoznam hrán grafu s cenami, kadá v jednom riadku: poèiatoènı vrchol, medzera, koncovı vrchol, medzera, cena. Nakoniec nasleduje riadok so šiestimi èíslami vrcholov a, b, c, d, e, f oddelenımi medzerami; hrany ab, cd a ef musia by súèasou najlacnejšej kostry (tieto tri hrany sú rôzne a kadá z nich sa nachádza v danom grafe).

Príklad vstupu:

6

5 3
1 3 11
1 2 7
3 4 9
1 2 3 4 1 3

4 4
1 2 12
2 3 9
3 4 8
4 1 11
1 2 3 4 1 4

6 6
1 2 1
2 3 2
3 1 3
4 5 4
5 6 5
6 4 6
1 2 3 2 5 6

5 5
1 2 1
2 3 20
3 4 55
3 5 30
2 5 40
1 2 2 3 3 4

10 19
2 10 1
6 2 4
6 4 7
5 10 6
7 5 8
8 4 4
7 6 7
10 9 4
4 7 3
4 5 3
4 3 10
4 10 7
3 2 8
2 9 6
3 1 2
8 2 3
10 8 10
4 9 9
6 8 1
8 4 7 6 10 9

10 19
10 6 1
3 2 1
2 4 1
1 6 1
3 10 1
8 1 1
1 4 1
3 7 1
4 8 1
4 3 1
10 5 1
3 9 1
3 8 1
2 1 1
10 9 1
1 3 1
1 5 1
5 7 1
2 7 1
8 1 1 4 3 7

Formát vıstupu

Pre kadı graf na vstupe jedinı riadok s jedinım èíslom, ktoré udáva cenu najlacnejšej kostry obsahujúcej dané tri hrany. Ak taká neexistuje, vypíšte -1.

Príklad vıstupu (zodpovedá vstupu vyššie):

-1
31
-1
106
33
9
*/


// uloha-1.cpp -- Uloha 1
// Peter Markus, 6.4.2017 14:50:14


#include <stdio.h>
#include <stdlib.h>

typedef struct hrana {
	int poci, konc;
	int cena;
} HRANA;

typedef struct eInfo {
	int pred;         // index prislusneho pola = pred
	int rank;         // na zaciatku vzdy nula
} EINFO;

void vymen(HRANA *alfa, HRANA *beta) {
    HRANA tmp = *alfa;
    *alfa = *beta;
    *beta = tmp;
}

 int parti(HRANA *graf,  int start,  int koniec)
{
	int pivot = graf[koniec].cena;
	int pi = start, i;

    for (i = start; i < koniec; i++)
    	if (graf[i].cena <= pivot)
            vymen(&graf[pi++], &graf[i]);

    vymen(&graf[pi], &graf[koniec]);
    return pi;
}


void qSort(HRANA *graf,  int start,  int koniec) {
    if (start < koniec) {
         int pi = parti(graf, start, koniec);
        		  qSort(graf, start, --pi);
        		  qSort(graf, ++pi, koniec);
    }
}

 int najdiKoren(EINFO eInfo[],  int i)        // src/dst vrchol jednej hrany
{
       if (eInfo[i].pred != i)
           eInfo[i].pred  = najdiKoren(eInfo, eInfo[i].pred);
    return eInfo[i].pred;
}

void zjednot(EINFO eInfo[],  int a,  int b)
{
    a = najdiKoren(eInfo, a);
    b = najdiKoren(eInfo, b);
  	if (eInfo[a].rank > eInfo[b].rank)
        eInfo[b].pred = a;
    else if (eInfo[a].rank < eInfo[b].rank)
        eInfo[a].pred = b;
    else {
        eInfo[b].pred = a;
        eInfo[a].rank++;
    }
}

int main()
{
	int i, j, k, l, m, pv, ph;
    int a, b, c, d, e, f;
    int poci, konc, cena;
    HRANA pom;

    scanf("%d\n\n", &k);		// pocet grafov
  	for(i = 0; i < k; i++) {
    	scanf("%d %d\n", &pv, &ph);	// nacitavanie pocet vrcholov a hran
    	HRANA *graf = (HRANA*)malloc(ph*sizeof(HRANA));
    	for(j = 0; j < ph; j++) {	// nacitavanie grafu
      		scanf("%d %d %d\n", &poci, &konc, &cena);
      		graf[j].poci = poci;
      		graf[j].konc = konc;
      		graf[j].cena = cena;
    	}
    	scanf("%d %d %d %d %d %d\n", &a, &b, &c, &d, &e, &f);	// hrany ab, cd, ef

      	int kla, mal, ppang[6], couqe = 0;
      	ppang[0] = a;
        ppang[1] = b;
        ppang[2] = c;
        ppang[3] = d;
        ppang[4] = e;
        ppang[5] = f;
      /*	//printf("%d %d %d %d %d %d\n", a, b, c, d, e, f);
      for(kla = 0; kla < 6; kla++)
        printf("%d ", ppang[kla]); printf("\n");*/
      for(kla = 0; kla < 6; kla++) {
        for(mal = kla+1; mal < 6; mal++) {
          if(ppang[kla] == ppang[mal]) {
            couqe++;
            break;
          }
        }
      }
      //printf("COUQE: %d\n", couqe);
    	if(pv > ph+1 || couqe == 3) {
      		printf("-1\n");
      		continue;
    	}	// graf nema dostatocne vela hran pre vsetky vrcholy
    	else {
      		HRANA riesenie[pv];
      		int rh = 0, p = 0, suma = 0, z = 0;
      		int a1, b1;

      		for(m = 0, l = 0; m < ph && l < 3; m++) {	// pridavanie 3 danych hran do grafu
        		if((graf[m].poci == a && graf[m].konc == b) ||
           		   (graf[m].poci == c && graf[m].konc == d) ||
           		   (graf[m].poci == e && graf[m].konc == f) ||
           		   (graf[m].poci == b && graf[m].konc == a) ||
           		   (graf[m].poci == d && graf[m].konc == c) ||
           		   (graf[m].poci == f && graf[m].konc == e)) {
          			pom = graf[l];
          			graf[l++] = graf[m];
          			graf[m] = pom;
        		}
      		}
      		qSort(graf, 3, ph-1);

      		EINFO *eInfo = (EINFO*)malloc(pv*sizeof(EINFO));
      		for(j = 0; j < pv; j++) {
        		eInfo[j].pred = j;
        		eInfo[j].rank = 0;
      		}
      		while(rh < pv-1 && p != ph) {
        		HRANA potHrana = graf[p++];
        		a1 = najdiKoren(eInfo, --potHrana.poci);
        		b1 = najdiKoren(eInfo, --potHrana.konc);
        		if(a1 != b1) {
          			riesenie[rh++] = potHrana;
          			zjednot(eInfo, a1, b1);
        		}
        		else if(a1 == b1 && p < 4) {
          			printf("-1\n");
          			z++;
         			break;
        		}
      		if(z == 1)
        		continue;
      		}	// prve tri dane hrany obsahuju cyklus
      	if(rh != pv-1) {
        	printf("-1\n");
        	continue;
      	}	// nebolo pridanych dostatocny pocet hran na vytvorenie suvisleho grafu

      	for(j = 0; j < rh; j++)
        	suma += riesenie[j].cena;
      	printf("%d\n", suma);
    	}
  	}
    return 0;
}
