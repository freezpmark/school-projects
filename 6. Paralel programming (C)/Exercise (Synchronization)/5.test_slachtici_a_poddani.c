/*
Meno:
Datum:

Simulujte nasledujucu situaciu. V malom kralovstve korunovali noveho krala a chodia sa mu neustale klanat styria slachtici a desiati poddani.
Prejavovanie ucty kralovi trva nejaky cas (v simulacii 1s) a nejaky cas si slahctic ci poddany dava prestavku (v simulacii 4s).
Cela simulacia nech trva 30s.

1. Doplnte do programu pocitadlo pocitajuce, kolko krat sa kralovi poklonili slachtici;
a pocitadlo pocitajuce, kolko krat sa kralovi poklonili poddani. [2b]

2. Zabezpecte, aby sa kralovi sucasne klanali maximalne dvaja slachtici a tiez aby sa kralovi neklanal slachtic spolu s poddanym
(cize alebo max. 2 slachtici, alebo lubovolne vela poddanych). Ak je pred kralom rad, slachtici maju samozrejme prednost. [5b]

3. Osetrite v programe spravne ukoncenie simulacie po uplynuti stanoveneho casu. [3b]

Poznamky:
- na synchronizaciu pouzite iba mutexy, podmienene premenne alebo semafory
- nespoliehajte sa na uvedene casy, simulacia by mala fungovat aj s inymi casmi
- build (console): gcc poslovia_a_pisari -o poslovia_a_pisari -lpthread
*/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>

pthread_mutex_t mutex   = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t cond     = PTHREAD_COND_INITIALIZER;
int slachC = 0;
int pref = 0;

int poddaniCount = 0;
int slachCount = 0;

// signal na zastavenie simulacie
int stoj = 0;

// klananie sa
void klananie(void) {
    sleep(1);
}

// prestavka medzi klananiami
void prestavka(void) {
    sleep(4);
}

// slachtic
void *slachtic( void *ptr ) {

    // pokial nie je zastaveny
    while(!stoj) {

        pthread_mutex_lock(&mutex);
        while(slachC < 0 || slachC > 1) {           // ak sa klanaju poddany || uz sa dvaja slach. klanaju
            pref = 1;
            pthread_cond_wait(&cond, &mutex);
        }
        if(stoj)
            break;
        pref = 0;
        slachC++;
        pthread_mutex_unlock(&mutex);

        printf("Slachtic sa klania...\n");
        klananie();

        pthread_mutex_lock(&mutex);
        slachCount++;
        slachC--;
        printf("                                        slachC: %d\n", slachC);

        pthread_cond_broadcast(&cond);
        pthread_mutex_unlock(&mutex);

        if(stoj)
            break;
        prestavka();
    }
    return NULL;
}

// poddany
void *poddany( void *ptr ) {

    // pokial nie je zastaveny
    while(!stoj) {

        pthread_mutex_lock(&mutex);
        while(slachC > 0 || pref)                // poddany da prednost cakajucemu slachticovi (pref), alebo nejaky slachtic sa uz isiel klanat
            pthread_cond_wait(&cond, &mutex);
        if(stoj)
            break;
        slachC--;
        pthread_mutex_unlock(&mutex);

        printf("Poddany sa klania...\n");
        klananie();

        pthread_mutex_lock(&mutex);
        poddaniCount++;
        slachC++;
        printf("                                        slachC: %d\n", slachC);
        pthread_cond_broadcast(&cond);
        pthread_mutex_unlock(&mutex);

        if(stoj)
            break;
        prestavka();
    }
    return NULL;
}

int main(void) {
    int i;

    pthread_t slachtici[12];
    pthread_t poddani[10];;;;

    for (i=0;i<4;i++) pthread_create( &slachtici[i], NULL, &slachtic, NULL);
    for (i=0;i<10;i++) pthread_create( &poddani[i], NULL, &poddany, NULL);

    sleep(30);
    stoj = 1;

    for (i=0;i<4;i++) pthread_join( slachtici[i], NULL);
    for (i=0;i<10;i++) pthread_join( poddani[i], NULL);

    printf("Pocet poddanych klanani: %d\nPocet slachtovych klanani: %d\n", slachCount, poddaniCount);

    exit(EXIT_SUCCESS);
}
