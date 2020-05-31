
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
- build (console): gcc zberatelia.cpp -o zberatelia -lpthread
*/


#include <iostream>
#include <thread>
#include <chrono>
#include <mutex>
#include <condition_variable>

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
std::mutex mutex;;;
std::mutex mutexKuch;
std::mutex mutex_zber;
std::mutex mutex_var;
std::condition_variable condBarr;
std::condition_variable condZber;
std::condition_variable condKuchar;
std::condition_variable condKuchar2;

void zbieraj() {
    std::this_thread::sleep_for (std::chrono::seconds(1));
    std::unique_lock<std::mutex> lock;
    std::unique_lock<std::mutex> lock(mutex_zber);
    pocet_nazbieranych++;
    lock.unlock();
}

void kontroluj() {
    std::this_thread::sleep_for (std::chrono::seconds(3));
}

void *zberatel(void *i) {
    std::unique_lock<std::mutex> lock;
    int id = (int*) i;
    zberateliaC[id] = 0;

    while (!koniec) {
        zbieraj();
        zberateliaC[id]++;

        std::unique_lock<std::mutex> lock(mutex);
        while((bariera || zhromazdene) && !koniec && (kucharReady || zberateliaC[id] > N-1))           /// zaistenie aby zberatelia neisli dalej ked sa organizuje kontrola zhromazdenych hub
            condZber.wait(lock);

        /// bariera
        if(zberateliaC[id] == N || countBarr > 0) && (kucharReady || zberateliaC[id] > N-1){          /// ak ma niekto 4 huby alebo niekto caka
            countBarr++;
            if(countBarr == N) {
                bariera = 1;
                condBarr.notify_all();
            } else {
                while (!bariera && !koniec)
                    condBarr.wait(lock);
            }
            zhromazdene += zberateliaC[id];                             ///
            zberateliaC[id] = 0;                                        ///
            if(koniec) { lock.unlock(); return NULL; }      /// pre pripad keby nebolo mozne zostavit skupinu na kontrolu
            countBarr--;
            if (countBarr == 0) {
                if(kuchar == 0 && !koniec) caka++;
                while(kucharReady == 0 && !koniec)          /// potrebujeme aj kuchara
                    condBarr.wait(lock);
                countBarr = N;                              /// pre signal kucharovi az ked budu pripraveny na kontrolu vsetci zberatelia
                bariera = 0;
                condBarr.notify_all();
            } else
                while (bariera && !koniec)
                    condBarr.wait(lock);

            countBarr--;                                            ///
            if (countBarr == 0) condKuchar.notify_one();            ///
            lock.unlock();
            if (koniec) return NULL;                                /// pre pripad ked sa cakalo na kuchara a uz nepride
            kontroluj();
        } else
            lock.unlock();
    }
    lock.lock();                /// pre pripad ked zberatelia v bariere cakaju na ostatnych pre zhromazdenie hub
    zhromazdene += zberateliaC[id];
    lock.unlock();

    return NULL;
}

void uvar() {
    std::this_thread::sleep_for (std::chrono::seconds(5));
    std::unique_lock<std::mutex> lock;
    std::unique_lock<std::mutex> lock(mutex_var);
    pocet_vareni++;
    lock.unlock();
}


void *kuchar(void *i) {
    std::unique_lock<std::mutex> lock;
    std::unique_lock<std::mutex> lock2;
    int id = (int*) i;
    int tmp = 0;

    while (!koniec) {
        kuchariC[id] = 0;

        std::unique_lock<std::mutex> lock(mutex);
        kucharReady++;
        condBarr.notify_all();                          /// dame vediet ze kuchar je ready cez broadcast kvoli bariere
        while((zhromazdene == 0 || bariera) && !koniec)
            condKuchar.wait(lock);

        /// ide sa kontrolovat
        kucharReady--;
        pocet_neskontrolovanych += tmp = zhromazdene;
        zhromazdene = 0;
        condZber.notify_all();                          /// moze ist dalsia grupa do bariery
        if (koniec) { lock.unlock(); return NULL; }
        kuchariC[id] = 1;
        lock.unlock();
        kontroluj();

        std::unique_lock<std::mutex> lock(mutexKuch);   /// pridanie noveho mutexu
        pocet_neskontrolovanych -= tmp;
        pocet_skontrolovanych += tmp;
        while(vkuchyni > 1 && !koniec)                  /// len dvaja mozu naraz varit
            condKuchar2.wait(lock2);

        /// ide sa varit
        if(koniec) { lock2.unlock(); return NULL; }
        vkuchyni++;
        kuchariC[id] = 2;
        lock2.unlock();
        uvar();

        lock2.lock();
        pocet_skontrolovanych -= tmp;
        pocet_uvarenych += tmp;
        tmp = 0;
        if(pocet_uvarenych >= LIM_UVARENYCH) {
            koniec = 1;
            condBarr.notify_all();
            condZber.notify_all();
            condKuchar.notify_all();
        }
        vkuchyni--;
        condKuchar2.notify_one();
        lock2.lock();
    }
    return NULL;
}

void *casuj(){
    std::this_thread::sleep_for (std::chrono::milliseconds(500));
    int i;
    while(!koniec) {
        for(i = 0; i < Z; i++)
            std::cout << zberateliaC[i] << " ";
        std::cout << "\t" << caka << "\t";
        for(i = 0; i < K; i++)
            std::cout << kuchariC[i] << " ";
        std::cout << std::endl;
        std::this_thread::sleep_for (std::chrono::seconds(1));
    }
}

int main() {

    int i;
    std::thread kuchari[K], zberatelia[Z];
    std::thread casovac;

    casovac = std:thread(casuj, NULL);
    for (i = 0; i < K; i++) kuchari[i] = std::thread(kuchar, (void *)i);
    for (i = 0; i < Z; i++) zberatelia[i] = std::thread(zberatel, (void *)i);

    for (i = 0; i < K; i++) kuchari[i].join();
    for (i = 0; i < Z; i++) zberatelia[i].join();

    std::cout << "Pocet vareni: " << pocet_vareni << ", nazbieranych: " << pocet_nazbieranych << ", neskontrolovanych: " << pocet_neskontrolovanych+zhromazden << ", skontrolovanych: " << pocet_skontrolovanych << ", uvarenych: " << pocet_uvarenych << std::endl;

    return 0;
}
