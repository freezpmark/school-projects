/*
Meno:
Datum:

Simulujte nasledujucu situaciu. Desiati maliari maluju steny.
Maliarovi trva nejaky cas, kym stenu maluje (v simulacii 2s) a nejaky cas, kym si ide nabrat farbu do vedra (v simulacii 1s). Cela simulacia nech trva nejaky cas (30s).

1. Doplnte do programu pocitadlo celkoveho poctu vedier minutej farby a tiez nech si kazdy maliar pocita,
kolko vedier farby uz minul preniesol, na konci simulacie vypiste hodnoty pocitadiel. [2b]

2. Ked maliar minie 4 vedra, pocka na dvoch svojich kolegov a kazdy si spravi prestavku na nejaky cas (v simulacii 2s). [5b]

3. Osetrite v programe spravne ukoncenie simulacie hned po uplynuti stanoveneho casu (nezacne sa dalsia cinnost). [3b]


Poznamky:
- na synchronizaciu pouzite iba mutexy+podmienene premenne alebo semafory
- nespoliehajte sa na uvedene casy, simulacia by mala fungovat aj s inymi casmi
- build (console): gcc maliari.c -o maliari -lpthread
*/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;
int cakajuciC = 0;

pthread_mutex_t mutexCounterVedra = PTHREAD_MUTEX_INITIALIZER;
int vedraT = 0;     // counter
int maliariC[10] = {0};
int bariera = 0;

// signal na zastavenie
int stoj = 0;

// maliar - malovanie steny
void maluj(void) {
    sleep(2);
    pthread_mutex_lock(&mutexCounterVedra);
    vedraT++;
    pthread_mutex_unlock(&mutexCounterVedra);
}

//  maliar - branie farby
void ber(void) {
  sleep(1);
}

// maliar
void *maliar( void *ptr ) {
    int id = (int*) ptr;
    int vedraC = 0; // pre kazdeho maliara mame pocitadlo

    // pokial nie je zastaveny
    while(!stoj) {
        maluj();
        pthread_mutex_lock(&mutex);
        vedraC++; maliariC[id]++;
        printf("%d. ODMALOVANE..\n", id);

        while(bariera && !stoj)
            pthread_cond_wait(&cond, &mutex);
        if(stoj) break;

        /// BARRIERA
        if(vedraC > 3 || cakajuciC) {           // bud uz niekto caka, alebo zacne cakat na ostatnych pre oddych
            cakajuciC++;
            if (cakajuciC == 3) {
                bariera = 1;
                pthread_cond_broadcast(&cond);
                printf("                                            %d. maliar hlasi - IDE SA NA ODDYCH\n", id);
            } else {
                printf("              %d. maliar zacal cakat..\n", id);
                while (!bariera) pthread_cond_wait(&cond, &mutex);
            }
            cakajuciC--;
            if (cakajuciC == 0) {
                bariera = 0;
                pthread_cond_broadcast(&cond);
            } else
                while (bariera) pthread_cond_wait(&cond, &mutex);
            pthread_mutex_unlock(&mutex);
            printf("ODDYCHUJEM %d.\n", id);
            sleep(2);
            vedraC = 0;
        } else
            pthread_mutex_unlock(&mutex);

        if(stoj) break;
        ber();
        printf("%d. ODBERANE..\n", id);
    }

    pthread_mutex_unlock(&mutex);
    return NULL;
}

int main(void) {
    int i;
    pthread_t maliari[10];

    for (i=0;i<10;i++) pthread_create(&maliari[i], NULL, &maliar, (void*) i);

    sleep(30);
    pthread_mutex_lock(&mutex);
    stoj = 1;
    pthread_cond_broadcast(&cond);
    pthread_mutex_unlock(&mutex);

    for (i=0;i<10;i++) {
        pthread_join(maliari[i], NULL);
        printf("%d. maliar namaloval %d krat\n", i, maliariC[i]);
    }

    printf("Pocet minutej farby vedier: %d\n", vedraT);;

    exit(EXIT_SUCCESS);
}
