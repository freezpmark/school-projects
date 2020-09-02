#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "structs.h"

TREE_ELE *findTree(TREE_ELE *root, STATUS s, char * ops);
STATUS *right(STATUS s);
STATUS *left(STATUS s);
STATUS *up(STATUS s);
STATUS *down(STATUS s);
void printSummary(STATUS *stav, char msg[255]);
void addFollower(TREE_ELE *vrchol, TREE_ELE *follow);
char invertOperation(char op);
int findBlank(STATUS s);

int main()
{
    int i, length, foundPath = 0, debug = 0;
    char c;
	long processed = 0;	        // pocet spracovanych stavov
	long created = 2;		    // pocet vytvorenych stavov (pociatocny a koncovy uz boli vytvorene)
	char *history2 = NULL;		// historia operatorov v 2. hladani pre tento stav
	TREE_ELE *pom_s, *pom_s2;
	STATUS intersect, *pom_stav = malloc(sizeof(STATUS));;
	STATUS_HISTORY *pom_Hs2, *pom_Hs = malloc(sizeof(STATUS_HISTORY));

	// alokacia pamate pre zaciatocny a koncovy stav
	STATUS *startingStatus = malloc(sizeof(STATUS));
	STATUS *endingStatus   = malloc(sizeof(STATUS));

	// uvodny vypis
	printf("Zaciatocny stav:\n 0 1 2 \n 3 4 5 \n 6 7 8 \n");
    printf("Koncovy stav:\n 1 2 3 \n 4 5 6 \n 7 8 0 \n");
    printf("Chcete tento stav menit? A/N\n");
    scanf("%c", &c);
    if(c == 'A' || c == 'a') {
        printf("Zadajte prvu maticu 3x3 pre zaciatocny stav\n");
        startingStatus->position[0] = 0;
        for(i = 0; i < 9; i++)
            scanf("%d", &startingStatus->position[i]);
        printf("Zadaj druhu maticu pre pozadovany cielovy stav\n");
        for(i = 0; i < 9; i++)
            scanf("%d", &endingStatus->position[i]);
    }
    else {
		for(i = 0; i < 9; i++)  // zaciatocny stav
            startingStatus->position[i] = i;
        for(i = 0; i < 9; i++)  // koncovy stav
            endingStatus->position[i] = (i+1);
        endingStatus->position[8] = 0;
	}

	// hashe a fronty pre hladanie 1. a 2. smerom
	HASH_TABLE *hash1 = createTable();
	HASH_TABLE *hash2 = createTable();
	FRONT *front1 = malloc(sizeof(FRONT));
	FRONT *front2 = malloc(sizeof(FRONT));
	front1->last = front1->first = NULL;
	front2->last = front2->first = NULL;

	// inicializacia 1. hladania
	TREE_ELE *root = malloc(sizeof(TREE_ELE));
	root->child1 = root->child2 = root->child3 = root->child4 = NULL;
	root->state = *endingStatus;
	insertFront(root, front1);
	insertHash(*endingStatus, hash1);

	// inicializacia 2. hladania
	STATUS_HISTORY *firstt = malloc(sizeof(STATUS_HISTORY));  // ked z 1. hladania sa stretnu, pomocou historie sa bude vracat [ASI]
	firstt->s = *startingStatus;
	char *opsArr = malloc(sizeof(char));
	firstt->ops = opsArr;
	firstt->ops[0] = '\0';
	insertFront2(firstt, front2);
	insertHash(*startingStatus, hash2);

	// prehladavaci cyklus
	clock_t start = clock();
	while (!(emptyFront(front1) || emptyFront(front2))) {   // pokym je front prazdny (pre pripad nenajdeneho riesenia)
		pom_Hs = getFront2(front2);     // vyberieme nespracovany prvok z fronty 2
		length = strlen(pom_Hs->ops);	// dlzka retazca s operatormi
		processed++;

		// ak sa stav uz vyskytol v prvom hladani, skoncime a ulozime stav aj s historiou operatorov
		if(hashIsIn(pom_Hs->s, hash1)){                  // pom_Hs->s je z front2, porovnanie s hash1
			foundPath = 1;
			intersect = pom_Hs->s;	                     // stav, v ktorom sa obojsmerne hladania stretli
			history2 = malloc((length+1)*sizeof(char));
			strcpy(history2, pom_Hs->ops);               // postupnost operatorov v 2. hladani
			break;
		}

		// POSUVANIE SA
		if(debug) printf("__________________________________________________________________________________________\n");
		if(debug) printSummary(&pom_Hs->s, "\nRozvinutie stavu pred DOWN");

		// pom_Hs   = novo vybraty z fronty
		// pom_stav = stav s posunutim
		// pom_Hs2  = stav s posunutim do premennej s historiou
        pom_stav = down(pom_Hs->s);                                // posunutie medzery
		if ((pom_stav != NULL) && !(hashIsIn(*pom_stav, hash2))) { // posun medzery nieje mimo 3x3 a s takym stavom sme sa nestretli
			// vytvaranie stavu s historiou, do ktorej dame aktualny stav
			pom_Hs2    = malloc(sizeof(STATUS_HISTORY));
			pom_Hs2->s = *pom_stav;

			// pridanie operatora do historie
			char *historyD = malloc((length+2)*sizeof(char));   // pomocna premenna na zvysenie miesta pre novy operator
			pom_Hs2->ops = historyD;                            // premennu aplikujeme na pom_Hs2
			strcpy(pom_Hs2->ops, pom_Hs->ops);                  // skopirujeme do nej string z pom_Hs
			pom_Hs2->ops[length]   = 'D';                       // a pridavame nove 2 znaky
			pom_Hs2->ops[length+1] = '\0';

			insertFront2(pom_Hs2, front2);	// vlozime novy stav s historiou do zasobnika nespracovanych
			insertHash(*pom_stav, hash2);	// vlozime novy stav medzi uz nadobudnute stavy do hashu

			// pre vysledny vypis a debugovanie
			if(debug) printSummary(pom_stav, "2 hladanie operator down:");
			created++;
		}
		// ak je mozne pouzit op. "left" a stav neexistuje, vytvorime novy uzol
		if(debug) printSummary(&pom_Hs->s, "\nRozvinutie stavu pred LEFT");
		pom_stav = left(pom_Hs->s);
		if ((pom_stav != NULL) && !(hashIsIn(*pom_stav, hash2))) {
			pom_Hs2 = malloc(sizeof(STATUS_HISTORY));
			pom_Hs2->s = *pom_stav;
			char *historyL = malloc((length+2)*sizeof(char));
			pom_Hs2->ops = historyL;
			strcpy(pom_Hs2->ops, pom_Hs->ops);
			pom_Hs2->ops[length]   = 'L';
			pom_Hs2->ops[length+1] = '\0';
			insertFront2(pom_Hs2, front2);
			insertHash(*pom_stav, hash2);
			created++;
		}
		if(debug) printSummary(&pom_Hs->s, "\nRozvinutie stavu pred RIGHT");
		pom_stav = right(pom_Hs->s);
		if ((pom_stav != NULL) && !(hashIsIn(*pom_stav, hash2))) {
			pom_Hs2 = malloc(sizeof(STATUS_HISTORY));
			pom_Hs2->s = *pom_stav;
			char *historyR = malloc((length+2)*sizeof(char));
			pom_Hs2->ops = historyR;
			strcpy(pom_Hs2->ops, pom_Hs->ops);
			pom_Hs2->ops[length]   = 'R';
			pom_Hs2->ops[length+1] = '\0';
			insertFront2(pom_Hs2, front2);
			insertHash(*pom_stav, hash2);
			created++;
		}
		if(debug) printSummary(&pom_Hs->s,"\nRozvijany stav pred UP");
		pom_stav = up(pom_Hs->s);
		if ((pom_stav != NULL) && !(hashIsIn(*pom_stav, hash2))) {
			pom_Hs2 = malloc(sizeof(STATUS_HISTORY));
			pom_Hs2->s = *pom_stav;
			char *historyU = malloc((length+2)*sizeof(char));
			pom_Hs2->ops = historyU;
			strcpy(pom_Hs2->ops, pom_Hs->ops);
			pom_Hs2->ops[length]   = 'U';
			pom_Hs2->ops[length+1] = '\0';
			insertFront2(pom_Hs2, front2);
			insertHash(*pom_stav, hash2);
			created++;
		}
		// konci druhe hladanie
		//--------------------------------------------------------
		// zacina prve hladanie, vybereme nespracovany prvok zo zasobnika prveho hladania
		pom_s = getFront(front1);
		processed++;

		pom_stav = down(pom_s->state);
		if ((pom_stav != NULL) && !(hashIsIn(*pom_stav, hash1))) {
            //vytvaranie stavu, do ktoreho dame aktualny stav
			pom_s2         = malloc(sizeof(TREE_ELE));
			pom_s2->state  = *pom_stav;

			pom_s2->prevOp = 'D';           // zapamatanie si predchadzajuceho operatora
			pom_s2->child1 = pom_s2->child2 = pom_s2->child3 = pom_s2->child4 = 0;  // pom_s2 je novy, takze nema childy
			addFollower(pom_s, pom_s2);     // pridanie childu do pom_s (predchadzajuceho stavu)

			insertFront(pom_s2, front1);	// vlozime smernik na novy vrchol stromu do zasobnika nespracovanych
			insertHash(*pom_stav, hash1);	// vlozime novy stav medzi uz nadobudnute stavy do hashu
			created++;
		}
		pom_stav = left(pom_s->state);
		if ((pom_stav != NULL) && !(hashIsIn(*pom_stav, hash1))) {
			pom_s2         = malloc(sizeof(TREE_ELE));
			pom_s2->state  = *pom_stav;
			pom_s2->prevOp = 'L';
			pom_s2->child1 = pom_s2->child2 = pom_s2->child3 = pom_s2->child4 = 0;
			addFollower(pom_s, pom_s2);
			insertFront(pom_s2, front1);
			insertHash(*pom_stav, hash1);
			created++;
		}
		pom_stav = right(pom_s->state);
		if ((pom_stav != NULL) && !(hashIsIn(*pom_stav, hash1))) {
			pom_s2         = malloc(sizeof(TREE_ELE));
			pom_s2->state  = *pom_stav;
			pom_s2->prevOp = 'R';
			pom_s2->child1 = pom_s2->child2 = pom_s2->child3 = pom_s2->child4 = 0;
			addFollower(pom_s, pom_s2);
			insertFront(pom_s2, front1);
			insertHash(*pom_stav, hash1);
			created++;
		}
		pom_stav = up(pom_s->state);
		if ((pom_stav != NULL) && !(hashIsIn(*pom_stav, hash1))) {
			pom_s2         = malloc(sizeof(TREE_ELE));
			pom_s2->state  = *pom_stav;
			pom_s2->prevOp = 'U';
			pom_s2->child1 = pom_s2->child2 = pom_s2->child3 = pom_s2->child4 = 0;
			addFollower(pom_s, pom_s2);
			insertFront(pom_s2, front1);
			insertHash(*pom_stav, hash1);
			created++;
		}
	}   // koniec prehladavania cyklom

	clock_t end = clock();
	float seconds = (float) (end - start) / CLOCKS_PER_SEC;
	printf("_________________________________________\n");
	printf("Trvanie prehladavania:    %f sekund\n", seconds);
	printf("Pocet vytvorenych uzlov:  %ld\n", created);
    printf("Pocet spracovanych uzlov: %ld\n", processed);
	if(foundPath) {
		char buffer[10000];
		printSummary(&intersect, "\nObojsmerne prehladavanie sa stretlo v stave:");

		// je nutne najst postupnost operatorov, ktorymi sme sa v 1. hladani dostali k "priesecniku"
		findTree(root, intersect, buffer);
		char *path = malloc((strlen(buffer) + strlen(history2) + 1)*sizeof(char));
		strcpy(path, history2);
		strcat(path, buffer);
		i = strlen(path);
		printf("\nPostupnost operacii: (%d)\n%s\n", --i, path);

	}
	else printf("Riesenie nebolo najdene\n");

	scanf("%c", &c);
	scanf("%c", &c);

	return 0;
}

void printSummary(STATUS *state, char msg[255]) {   // vypis stavu hlavolamu
	int i, j;

	printf("%s\n               ", msg);
	for(i = 0; i < BORDER; i++) {
	  for(j = 0; j < BORDER; j++)
		  printf("%d ", state->position[(i*BORDER)+j]);
	  printf("\n               ");
	}
}

char invertOperation(char op) {                     // prevedenie na inverzny operator
	char iOp;
	switch(op) 	{
	  	case 'L': iOp = 'R';	break;
	  	case 'D': iOp = 'U';    break;
	  	case 'U': iOp = 'D';	break;
		case 'R': iOp = 'L';	break;
		default:  iOp = op;
	}
	return iOp;
}

int findBlank(STATUS s) {
	int i;
	for(i = 0; s.position[i] != 0; i++) ;
	return i;
}

STATUS *right(STATUS s) {
	int blankPos = findBlank(s);                // cislo reprezentuje poziciu v 3x3
	if ((blankPos % BORDER) == 0) return NULL;	// ak je medzera na lavom kraji, nieje mozne operator pouzit
	STATUS *pom = malloc(sizeof(STATUS));
	*pom = s;                                   // prekopirovanie stareho stavu
	pom->position[blankPos] = pom->position[blankPos-1];    // presun cisla na miesto kde je nula
	pom->position[blankPos-1] = 0;                          // vloz nulu na miesto kde bolo cislo
	return pom;
}

STATUS *left(STATUS s) {
	int blankPos = findBlank(s);
	if (((blankPos+1) % BORDER) == 0) return NULL;
	STATUS *pom = malloc(sizeof(STATUS));
	*pom = s;
	pom->position[blankPos] = pom->position[blankPos+1];
	pom->position[blankPos+1] = 0;
	return pom;
}

STATUS *up(STATUS s) {
	int blankPos = findBlank(s);
	if ((blankPos + BORDER) >= QUANTITY) return NULL;
	STATUS *pom = malloc(sizeof(STATUS));
	*pom = s;
	pom->position[blankPos] = pom->position[blankPos+BORDER];
	pom->position[blankPos+BORDER] = 0;
	return pom;
}

STATUS *down(STATUS s) {
	int blankPos = findBlank(s);
	if ((blankPos - BORDER) < 0) return NULL;
	STATUS *pom = malloc(sizeof(STATUS));
	*pom = s;
	pom->position[blankPos] = pom->position[blankPos-BORDER];
	pom->position[blankPos-BORDER] = 0;
	return pom;
}

void addFollower(TREE_ELE *vrchol, TREE_ELE *follow) {
         if(vrchol->child1 == NULL) vrchol->child1 = follow;
	else if(vrchol->child2 == NULL) vrchol->child2 = follow;
	else if(vrchol->child3 == NULL) vrchol->child3 = follow;
	else if(vrchol->child4 == NULL) vrchol->child4 = follow;
}

TREE_ELE *findTree(TREE_ELE *root, STATUS s, char *ops) {   // hladanie vrchola stromu ktory obsahuje stav s
	if(root == NULL) return NULL;

	TREE_ELE *pom;
	int length = 0;
	char invOp = invertOperation(root->prevOp);
    pom = findTree(root->child1, s, ops);

	if(pom != NULL) {
		length = strlen(ops);
		ops[length]   = invOp;
		ops[length+1] = '\0';
		return pom;
	}
	pom = findTree(root->child2, s, ops);
	if(pom != NULL) {
		length = strlen(ops);
		ops[length]   = invOp;
		ops[length+1] = '\0';
		return pom;
    }
	if(root != NULL) {
		length = strlen(ops);
		ops[length]   = invOp;
		ops[length+1] = '\0';
		pom = findTree(root->child4, s, ops);
		return pom;
	}
	pom = findTree(root->child3, s, ops);
	if(pom != NULL) {
		length = strlen(ops);
		ops[length]   = invOp;
		ops[length+1] = '\0';
		return pom;
	}
	pom = findTree(root->child4, s, ops);
	if(pom != NULL) {
		length = strlen(ops);
		ops[length]   = invOp;
		ops[length+1] = '\0';
		return pom;
	}
	if(root->state.position == s.position) {
		ops[0] = invOp;
		ops[1] = '\0';
		return root;
	}

	ops[0] = 'N';
	ops[1] = '\0';
	return NULL;
}
