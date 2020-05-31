
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

int zhromazdene = 0;
int countBarr = 0;
int bariera = 0;
int kucharReady = 0;
int vkuchyni = 0;

int caka = 0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mutexKuch = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mutex_zber = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mutex_var = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t condBarr = PTHREAD_COND_INITIALIZER;
pthread_cond_t condZber = PTHREAD_COND_INITIALIZER;
pthread_cond_t condKuchar = PTHREAD_COND_INITIALIZER;
pthread_cond_t condKuchar2 = PTHREAD_COND_INITIALIZER;

void zbieraj() {
    sleep(1);
    pthread_mutex_lock(&mutex_zber);
    pocet_nazbieranych++;
    pthread_mutex_unlock(&mutex_zber);
}

void kontroluj() {
    sleep(3);
}

void *zberatel(void *i) {
    int id = (int) i;
    zberateliaC[id] = 0;

    while (!koniec) {
        zbieraj();
        zberateliaC[id]++;

        pthread_mutex_lock(&mutex);
        while((bariera || zhromazdene) && !koniec && (kucharReady || zberateliaC[id] > N-1))           /// zaistenie aby zberatelia neisli dalej ked sa organizuje kontrola zhromazdenych hub
            pthread_cond_wait(&condZber, &mutex);

        /// bariera
        if(zberateliaC[id] == N || countBarr > 0 && (kucharReady || zberateliaC[id] > N-1)) {          /// ak ma niekto 4 huby alebo niekto caka
            countBarr++;
            if(countBarr == N) {
                bariera = 1;
                pthread_cond_broadcast(&condBarr);
            } else {
                while (!bariera && !koniec)
                    pthread_cond_wait(&condBarr, &mutex);
            }
            zhromazdene += zberateliaC[id];                             ///
            zberateliaC[id] = 0;                                        ///
            if(koniec) { pthread_mutex_unlock(&mutex); return NULL; }   /// pre pripad keby nebolo mozne zostavit skupinu na kontrolu
            countBarr--;
            if (countBarr == 0) {
                if(kucharReady == 0 && !koniec) caka++;
                while(kucharReady == 0 && !koniec)         /// potrebujeme aj kuchara
                    pthread_cond_wait(&condBarr, &mutex);
                countBarr = N;                              /// pre signal kucharovi az ked budu pripraveny na kontrolu vsetci zberatelia
                bariera = 0;
                pthread_cond_broadcast(&condBarr);
            } else
                while (bariera && !koniec)
                    pthread_cond_wait(&condBarr, &mutex);

            countBarr--;                                             ///
            if (countBarr == 0) pthread_cond_signal(&condKuchar);    ///
            pthread_mutex_unlock(&mutex);
            if (koniec) return NULL;                                 /// pre pripad ked sa cakalo na kuchara a uz nepride
            kontroluj();
        } else
            pthread_mutex_unlock(&mutex);
    }
    pthread_mutex_lock(&mutex);             /// pre pripad ked zberatelia v bariere cakaju na ostatnych pre zhromazdenie hub
    zhromazdene += zberateliaC[id];
    pthread_mutex_unlock(&mutex);

    return NULL;
}

void uvar() {
    sleep(5);
    pthread_mutex_unlock(&mutex_var);
    pocet_vareni++;
    pthread_mutex_unlock(&mutex_var);
}


void *kuchar(void *i) {
    int id = (int) i;
    int tmp = 0;

    while (!koniec) {
        kuchariC[id] = 0;

        pthread_mutex_lock(&mutex);
        kucharReady++;
        pthread_cond_broadcast(&condBarr);      /// dame vediet ze kuchar je ready cez broadcast kvoli bariere
        while((zhromazdene == 0 || bariera) && !koniec)
            pthread_cond_wait(&condKuchar, &mutex);

        /// ide sa kontrolovat
        kucharReady--;
        pocet_neskontrolovanych += tmp = zhromazdene;
        zhromazdene = 0;
        pthread_cond_broadcast(&condZber);      /// moze ist dalsia grupa do bariery
        if (koniec) { pthread_mutex_unlock(&mutex); return NULL; }
        kuchariC[id] = 1;
        pthread_mutex_unlock(&mutex);
        kontroluj();

        pthread_mutex_lock(&mutexKuch);         /// pridanie noveho mutexu
        pocet_neskontrolovanych -= tmp;
        pocet_skontrolovanych += tmp;
        while(vkuchyni > 1 && !koniec)          /// len dvaja mozu naraz varit
            pthread_cond_wait(&condKuchar2, &mutexKuch);

        /// ide sa varit
        if(koniec) { pthread_mutex_unlock(&mutexKuch); return NULL; }
        vkuchyni++;
        kuchariC[id] = 2;
        pthread_mutex_unlock(&mutexKuch);
        uvar();

        pthread_mutex_lock(&mutexKuch);
        pocet_skontrolovanych -= tmp;
        pocet_uvarenych += tmp;
        tmp = 0;
        if(pocet_uvarenych >= LIM_UVARENYCH) {
            koniec = 1;
            pthread_cond_broadcast(&condBarr);
            pthread_cond_broadcast(&condZber);
            pthread_cond_broadcast(&condKuchar);
        }
        vkuchyni--;
        pthread_cond_signal(&condKuchar2);
        pthread_mutex_unlock(&mutexKuch);
    }
    return NULL;
}

void *casuj(){
    Sleep(500);
    int i;;
    while(!koniec) {
        for(i = 0; i < Z; i++)
            printf("%d ", zberateliaC[i]);
        printf("\t%d\t", caka);
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
