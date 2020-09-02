#include <stdio.h>
#include <stdlib.h>
#include "structs.h"

HASH_TABLE *createTable()
{
	HASH_TABLE *p_pom = malloc(sizeof(HASH_TABLE));
	int allStateOptSum = fact(QUANTITY), i;
	p_pom->table = malloc(allStateOptSum * sizeof(int));

	for(i = 0; i < allStateOptSum; i++)     // vynulovanie tabulky - ziadny stav nemame prejdeny
		p_pom->table[i] = 0;
	for (i = 0; i < QUANTITY; i++)
        p_pom->koef[i] = fact(QUANTITY-1-i);
    p_pom->koef[QUANTITY-1] = 0;	        // pre jednoznacne urcenie stavu nam staci n-1 policok
	return p_pom;
}

int fact(int i)      // na urcenie velkosti hash tabulky
{
	if(i <= 1) return 1;
    int fact;
    for(fact = 1; i > 0; i--)
        fact *= i;
 return fact;
}

int hash(STATUS s, HASH_TABLE *hashTable)
{
	int i, j, vystup = 0;
	for (i = 0; i < QUANTITY - 1; i++)              // predspracovanie
		for (j = 0; j < i; j++)
			if (s.position[i] > s.position[j])
                s.position[i]--;
	for (i = 0; i < QUANTITY; i++)
        vystup += s.position[i] * hashTable->koef[i];
	return vystup;
}

void insertHash(STATUS s, HASH_TABLE *hashTable) {  // pridanie stavu do tabulky
	hashTable->table[hash(s, hashTable)] = 1;	    // nastavenie priznaku v tabulke na 1 (existujuci)
}

int hashIsIn(STATUS s, HASH_TABLE *hashTable) {
    return hashTable->table[hash(s, hashTable)];
}
