#include <stdio.h>
#include <stdlib.h>
#include "structs.h"

void insertFront(TREE_ELE *c, FRONT *front1)    // stav, fronta
{
	FRONT_ELE *pom = malloc(sizeof(FRONT_ELE));
	pom->c = c;
	pom->next = NULL;

	if (front1->last != NULL)       // last - ide dnu, first - ide von
        front1->last->next = pom;	// ak je vo fronte prvok, nastavi sa u posledneho prvku ukazovatel next na novy prvok
	if (front1->first == NULL)
        front1->first = pom;	    // ak sme do prazdneho frontu pridali prvok, bude stale poslednym prvkom
	front1->last = pom;
}

void insertFront2(STATUS_HISTORY *c, FRONT *front2)
{
	FRONT_ELE *pom = malloc(sizeof(FRONT_ELE));
	pom->c = (void *) c;
	pom->next = NULL;

	if (front2->last != NULL)
        front2->last->next = pom;
	if (front2->first == NULL)
        front2->first = pom;
    front2->last = pom;
}

TREE_ELE *getFront(FRONT *front1)   // odstrani a vrati prvok z fronty
{
	if (front1->first == NULL)
        return NULL;
	TREE_ELE *pom = front1->first->c;
	front1->first = front1->first->next;
	if (front1->first == NULL)
        front1->last = NULL;	    // ak sme odobrali posledny prvok, nastavime last ako NULL
	return pom;
}

STATUS_HISTORY *getFront2(FRONT *front2)
{
	if (front2->first == NULL)
        return NULL;
	STATUS_HISTORY *pom = (STATUS_HISTORY *) front2->first->c;
	front2->first = front2->first->next;
	if (front2->first == NULL)
        front2->last = NULL;
	return pom;
}

int emptyFront(FRONT *p_front) {
    return p_front->last == NULL;
}
