/*
Meno:
Datum:

Simulujte nasledujucu situaciu. Traja maliari menia postupne za sebou svoje pripravene obrazy na jeden maliarsky stojan.
Zmena obrazu na stojane trva nejaky cas (v simulacii 3s), potom si idu namalovat novy obraz (v simulacii 5s).
Osem obdivovatelov si chodi pozerat tieto obrazy, pricom pozeranie im trva nejaky cas (v simulacii 1s)
a potom idu na prestavku (v simulacii 2s). Cela simulacia nech trva 30s.

1. Doplnte do programu premennu vyjadrujucu pocet zmien obrazov na stojane, po skonceni simulacie vypiste jej hodnotu. [2b]

2. Zabezpecte, aby iba jeden maliar mohol vymenienat obraz na stojane a to len vtedy, ked ziadny obdivovatel na obrazy nepozera.
Styria obdivovatelia sa mozu naraz pozerat na obraz, nasledne sa vzdy osem obdivovatelov medzi sebou pocka a daju si spolu prestavku. [5b]

3. Osetrite v programe spravne ukoncenie simulacie po uplynuti stanoveneho casu tak,
aby maliar ani obdivovatel po  stanovenom case uz ziadnu cinnost nezacal. [3b]

Poznamky:
- na synchronizaciu pouzite iba mutexy + podmienene premenne
- nespoliehajte sa na uvedene casy, simulacia by mala fungovat aj s inymi casmi
- build (console): gcc nastenkari.c -o nastenkari -lpthread
*/

#include <pthread.h>
#include <unistd.h>
#include <stdio.h>

// signal na zastavenie
int stoj = 0;

void maliar_zmen() {
    sleep(3);
}

void maliar_maluj() {
    sleep(5);
}

void *maliar(void *i) {

    while (!stoj) {
        maliar_zmen();
        maliar_maluj();
    }
}

void obdivovatel_pozeraj() {
    sleep(1);
}

void obdivovatel_prestavka() {
    sleep(2);
}

void *obdivovatel(void *i) {

    while (!stoj) {
        obdivovatel_pozeraj();
        obdivovatel_prestavka();
    }
}

int main() {
    
    int i;
    pthread_t maliari[3], zamestnanci[8];

    for (i = 0; i < 3; i++) pthread_create( &maliari[i], NULL, maliar, (void *)i);
    for (i = 0; i < 8; i++) pthread_create( &zamestnanci[i], NULL, obdivovatel, (void *)i);

    sleep(30);
    stoj = 1;

    for (i = 0; i < 3; i++) pthread_join( maliari[i], NULL);
    for (i = 0; i < 8; i++) pthread_join( zamestnanci[i], NULL);

    printf("Pocet zmien obrazov na stojane: %d\n", counterM);

    exit(EXIT_SUCCESS);
}
