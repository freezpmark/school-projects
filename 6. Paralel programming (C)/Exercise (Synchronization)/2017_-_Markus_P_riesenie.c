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

pthread_mutex_t mutexM = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mutexZ = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t condM = PTHREAD_COND_INITIALIZER;
pthread_cond_t condZ = PTHREAD_COND_INITIALIZER;
pthread_cond_t condBarr = PTHREAD_COND_INITIALIZER;

int count       = 0;            /// zap -> maluje sa, kladny pocet -> obraz je pozerany
int counterM    = 0;            /// pocet zmien na stojane
int order       = 0;            /// poradie maliarov
int pref        = 0;            /// preferencia obdivovatelov nad maliarmi
int paintNum    = 3;            /// pocet maliarov
int bCounter    = 0;
int inBarr      = 0;

// signal na zastavenie
int stoj = 0;

void maliar_zmen() {
    sleep(3);
}

void maliar_maluj() {
    sleep(5);
}

void *maliar(void *i) {
    int id = (int) i;

    while (!stoj) {
        pthread_mutex_lock(&mutexM);
        while (count > 0 || id != order || pref) {      /// obdivovatel pozera || nieje na rade || posledny ukon obdivovatela je cakanie (count++ hned za tym, celkovo to znamena ze podmienka je splnena vtedy, ked nejaky obdivovatel caka)
            if (stoj) break;
            pthread_cond_wait(&condM, &mutexM );
        }
        if (stoj) break;

        count--;
        order = (order + 1) % paintNum;                 printf("                    %d. maliar vymiena obraz.. \n", id);
        maliar_zmen();                                  printf("                        %d. maliar zmenil obraz.. \n", id, order);
        counterM++;

        count++;
        pthread_cond_broadcast(&condZ);
        pthread_mutex_unlock(&mutexM);

        if (stoj) break;                                printf("                    %d. maliar maluje dalsi obraz\n", id);
        maliar_maluj();                                 printf("                        %d. maliar je pripraveny vymenit obraz\n", id);
    }
    pthread_cond_broadcast(&condM);
    pthread_mutex_unlock(&mutexM);
}

void obdivovatel_pozeraj() {
    sleep(1);
}

void obdivovatel_prestavka() {
    sleep(2);
}

void *obdivovatel(void *i) {
    int id = (int) i;

    while (!stoj) {
        pthread_mutex_lock(&mutexZ);
        while(count < 0 || count >= 4){     /// priravuje sa obraz, max 4 sa zmestia pri obraze
            if (stoj) break;
            pref = 1;
            pthread_cond_wait(&condZ, &mutexZ);
        }
        pref = 0;
        count++;
        pthread_mutex_unlock(&mutexZ);      /// broadcast condZ u maliara zaruci aby sa tu nemuselo signalizovat dalsim obdivovatelom


        if (stoj) break;                                printf("%d. obdivovatel zacina prezerat obraz..\n", id);
        obdivovatel_pozeraj();

        pthread_mutex_lock(&mutexZ);        /// ide sa do bariery atomicky
        pthread_cond_signal(&condZ);        /// po pozerani posle signal dalsiemu obdivovatelovi
        count--;                                /// znizi pocet aktualne pozerajucich

        bCounter++;
        if(bCounter == 8) {
             inBarr = 1;                                printf("%d zvola: IDEME NA PRESTAVKU!\n", id);
             pthread_cond_broadcast(&condBarr);
        } else
            while(!inBarr) {
                if (stoj) break;                        printf("%d. obdivovatel caka..\n", id);
                pthread_cond_wait(&condBarr, &mutexZ);
            }
        bCounter--;
        if(bCounter == 0) {
            inBarr = 0;
            pthread_cond_broadcast(&condBarr);
        } else
            while(inBarr) {
                if (stoj) break;
                pthread_cond_wait(&condBarr, &mutexZ);
            }
        pthread_mutex_unlock(&mutexZ);                  printf("%d. obdivovatel si dava prestavku..\n", id);

        pthread_cond_signal(&condM);        /// pred prestavkou sa posle signal maliarovi
        if (stoj) break;
        obdivovatel_prestavka();
    }
    pthread_cond_broadcast(&condZ);
    pthread_cond_broadcast(&condBarr);
    pthread_mutex_unlock(&mutexZ);
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
