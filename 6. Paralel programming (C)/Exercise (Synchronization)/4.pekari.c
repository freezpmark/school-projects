/*
Meno:
Datum:

Simulujte nasledujucu situaciu. V pekarni pracuju pekari (10 pekarov),
ktori pecu chlieb v peciach (4 pece). Pekar pripravuje chlieb nejaky cas
(v simulacii 4s) a potom ide k volnej peci a pecie v nej chlieb (2s).
Cela simulacia nech trva 30s.

1. Doplnte do programu pocitadlo pocitajuce, kolko chlebov bolo upecenych. [2b]

2. Zabezpecte, aby do obsadenej pece pekar vlozil chlieb az ked sa uvolni,
cize aby poclal, kym nebude nejaka pec volna. Simulujte situaciu, ze
ked pekar upecie 2 chleby, pocka na vsetkych kolegov a spravia si prestavku (v simulacii 4s). [5b]

3. Osetrite v programe spravne ukoncenie simulacie po uplynuti stanoveneho casu tak, aby pekar prerusil cinnost hned, ako je to mozne (ak uz zacal pripravu alebo pecenie moze ju dokoncit).  [3b]

Poznamky:
- na synchronizaciu pouzite iba mutexy, podmienene premenne alebo semafory
- nespoliehajte sa na uvedene casy, simulacia by mala fungovat aj s inymi casmi
- build (console): gcc pekari.c -o pekari -lpthread
*/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mutex2 = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;
int pec = 0;
int cakajuci = 0;
int N = 10;
int bariera = 0;

int pocetChlieb = 0;    // counter

// signal na zastavenie simulacie
int stoj = 0;

// pekar
void priprava(void) {
    sleep(4);
}

void pecenie(void) {
    sleep(2);
    pthread_mutex_lock(&mutex2);
    pocetChlieb++;
    pthread_mutex_unlock(&mutex2);
}

void prestavka(void){
    sleep(4);
}

void *pekar( void *ptr ) {
    int id = (int*) ptr;
    int chleby = 0;

    while(1) {
        priprava(); printf("%d. Pripraveny!\n", id);

        pthread_mutex_lock(&mutex);
        while(pec > 3 && !stoj)                             // v peci sa pecu 4 chleby
            pthread_cond_wait(&cond, &mutex);
        if (stoj) { pthread_mutex_unlock(&mutex); return NULL; }
        pec++;
        pthread_mutex_unlock(&mutex);

        pecenie();

        pthread_mutex_lock(&mutex);
        pec--;
        chleby++;
        pthread_cond_broadcast(&cond);          // ak by tu bol signal, mohlo by sa zobudit vlakno ktore je dole zaspate, ten dalej signalizovat nebude a nastane deadlock hore
        printf("             %d. Upiekol %d CHLEBOV!\n", id, chleby);

        if((chleby > 1 || cakajuci > 0) && !stoj) {
            cakajuci++;
            if(cakajuci == N) {
                bariera = 1;
                pthread_cond_broadcast(&cond); printf("%d. SIGNALIZUJE IDEME ODDYCHOVAT!\n", id);
            } else {
                printf("%d. caka..\n", id);
                while (!bariera) pthread_cond_wait(&cond, &mutex);
            }
            cakajuci--;
            if (cakajuci == 0) {
                bariera = 0;
                pthread_cond_broadcast(&cond);
            } else
                while (bariera) pthread_cond_wait(&cond, &mutex);
            chleby = 0;
            printf("                                %d. ide na prestavku\n", id);
            pthread_mutex_unlock(&mutex);
            prestavka();
        } else if (stoj) { pthread_mutex_unlock(&mutex); return NULL; }
            else pthread_mutex_unlock(&mutex);
    }
    return NULL;
}

int main(void) {
    int i;

    pthread_t pekari[N];

    for (i=0;i<10;i++) pthread_create( &pekari[i], NULL, &pekar, (void*) i);

    sleep(30);
    pthread_mutex_lock(&mutex);
    stoj = 1;
    pthread_cond_broadcast(&cond);
    pthread_mutex_unlock(&mutex);

    for (i=0;i<10;i++) pthread_join( pekari[i], NULL);

    printf("Bolo napecenych %d chlieb.\n", pocetChlieb);

    exit(EXIT_SUCCESS);
}
