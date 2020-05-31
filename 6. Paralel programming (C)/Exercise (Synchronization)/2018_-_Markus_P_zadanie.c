
/*
Meno:
Datum:

Simulujte nasledujucu situaciu:
Desiati zberatelia zbieraju v lese huby pricom kazdy jeden zber zabera nejaky cas (v simulacii 1s).
Pri urcitom mnozstve nazbieranych hub sa odkontroluju a pripravia (v simulacii 3s).
Po odkontrolovani ich kuchar nasledne uvari (v simulacii 5s). Pocet kucharov je tri.

1. Simulujte situaciu, pri ktorej ked niekto nazbiera 4 huby, zvola a pocka troch ostatnych zbierajucich zberatelov s ktorymi
nazhromazdi vsetky nazbierane huby a pojdu ich spolocne odkontrolovat a pripravit. Nazhromazdene huby budu
nasledne uvarene jednym kucharom. [3b]

2. Zabezpecte, aby len dvaja kuchari mohli varit sucasne. [2b]

3. Zabezpecte, aby zberatelia pockali aj na jedneho kuchara, ktory bude s nimi taktiez pripravovat nazhromazdene huby (s tym istym casom 3s). [2b]

4. Osetrite v programe spravne ukoncenie simulacie po uvareni 60 hub tak, aby uz nikto ziadnu cinnost nezacal. [2b]

5. Doplnte do programu premenne vyjadrujuce pocty celkovych vareni, pocet celkovo nazbieranych, nepripravenych, pripravenych a uvarenych hub. [1b]


Poznamky:
- na synchronizaciu pouzite iba mutexy + podmienene premenne
- nespoliehajte sa na uvedene casy, simulacia by mala fungovat aj s inymi casmi
- build (console): gcc zberatelia.c -o zberatelia -lpthread
*/


#include <pthread.h>
#include <unistd.h>
#include <stdio.h>

#define K 3
#define Z 10
#define N 4
#define LIM_UVARENYCH 60

int kuchariC[K], zberateliaC[Z];
int koniec = 0;

int pocet_nazbieranych = 0;
int pocet_neskontrolovanych = 0;
int pocet_skontrolovanych = 0;
int pocet_uvarenych = 0;

int pocet_vareni = 0;

void zbieraj() {
    sleep(1);
}

void kontroluj() {
    sleep(3);
}

void *zberatel(void *i) {
    int id = (int) i;
    zberateliaC[id] = 0;

    while (!koniec) {
        zbieraj();
        kontroluj();
    }

    return NULL;
}

void uvar() {
    sleep(5);
}


void *kuchar(void *i) {
    int id = (int) i;

    while (!koniec) {
        kuchariC[id] = 0;

        kontroluj();
        uvar();
    }
    return NULL;
}

void *casuj(){
    Sleep(500);
    int i;
    while(!koniec) {
        for(i = 0; i < Z; i++)
            printf("%d ", zberateliaC[i]);
        printf("\t\t");
        for(i = 0; i < K; i++)
            printf("%d ", kuchariC[i]);
        printf("\n");
        sleep(1);
    }
    return NULL;
}

int main() {

    int i;
    pthread_t kuchari[K], zberatelia[Z];
    pthread_t casovac;

    pthread_create(&casovac, NULL, casuj, NULL);
    for (i = 0; i < K; i++) pthread_create( &kuchari[i], NULL, kuchar, (void *)i);
    for (i = 0; i < Z; i++) pthread_create( &zberatelia[i], NULL, zberatel, (void *)i);

    for (i = 0; i < K; i++) pthread_join( kuchari[i], NULL);
    for (i = 0; i < Z; i++) pthread_join( zberatelia[i], NULL);

    printf("Pocet vareni: %d, nazbieranych: %d, neskontrolovanych: %d, skontrolovanych: %d, uvarenych: %d\n", pocet_vareni, pocet_nazbieranych, pocet_neskontrolovanych+zhromazdene, pocet_skontrolovanych, pocet_uvarenych);

    exit(EXIT_SUCCESS);
}
