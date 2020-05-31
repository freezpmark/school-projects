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

#include <iostream>
#include <thread>
#include <chrono>
#include <mutex>
#include <condition_variable>

std::mutex mutexM;
std::mutex mutexZ;
std::condition_variable condM;
std::condition_variable condZ;
std::condition_variable condBarr;

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
    std::this_thread::sleep_for (std::chrono::seconds(3));
}

void maliar_maluj() {
    std::this_thread::sleep_for (std::chrono::seconds(5));
}

void *maliar(void *i) {
    int id = (int) i;
    std::unique_lock<std::mutex> lock;

    while (!stoj) {
        std::unique_lock<std::mutex> lock(mutexM);
        while (count > 0 || id != order || pref) {      /// obdivovatel pozera || nieje na rade || posledny ukon obdivovatela je cakanie (count++ hned za tym, celkovo to znamena ze podmienka je splnena vtedy, ked nejaky obdivovatel caka)
            if (stoj) break;
            condM.wait(lock);
        }
        if (stoj) break;

        count--;
        order = (order + 1) % paintNum;                 std::cout << "                  " << id << ". maliar vymiena obraz..\n";
        maliar_zmen();                                  std::cout << "                      " << id << ". maliar zmenil obraz..\n";
        counterM++;

        count++;
        condZ.notify_all();
        lock.unlock();

        if (stoj) break;                                std::cout << "                  " << id << ". maliar maluje dalsi obraz..\n";
        maliar_maluj();                                 std::cout << "                      " << id << ". maliar je pripraveny vymenit obraz..\n";
    }
    condM.notify_one();
    lock.unlock();
    std::terminate();
}

void obdivovatel_pozeraj() {
    std::this_thread::sleep_for (std::chrono::seconds(1));
}

void obdivovatel_prestavka() {
    std::this_thread::sleep_for (std::chrono::seconds(2));
}

void *obdivovatel(void *i) {
    int id = (int) i;
    std::unique_lock<std::mutex> lock;

    while (!stoj) {
        std::unique_lock<std::mutex> lock(mutexZ);
        while(count < 0 || count >= 4){     /// priravuje sa obraz, max 4 sa zmestia pri obraze
            if (stoj) break;
            pref = 1;
            condZ.wait(lock);
        }
        pref = 0;
        count++;
        lock.unlock();          /// broadcast condZ u maliara zaruci aby sa tu nemuselo signalizovat dalsim obdivovatelom


        if (stoj) break;                                std::cout << id << ". obdivovatel zacina prezerat obraz..\n";
        obdivovatel_pozeraj();

        lock.lock();            /// ide sa do bariery atomicky
        condZ.notify_one();        /// po pozerani posle signal dalsiemu obdivovatelovi
        count--;                                /// znizi pocet aktualne pozerajucich

        bCounter++;
        if(bCounter == 8) {
             inBarr = 1;                                std::cout << id << " zvola: IDEME NA PRESTAVKU!\n";
             condBarr.notify_all();
        } else
            while(!inBarr) {
                if (stoj) break;                        std::cout << id << ". obdivovatel caka..\n";
                condBarr.wait(lock);
            }
        bCounter--;
        if(bCounter == 0) {
            inBarr = 0;
            condBarr.notify_all();
        } else
            while(inBarr) {
                if (stoj) break;
                condBarr.wait(lock);
            }
        lock.unlock();                  std::cout << id << ". obdivovatel si dava prestavku..\n";

        condM.notify_one();          /// pred prestavkou sa posle signal maliarovi
        if (stoj) break;
        obdivovatel_prestavka();
    }
    condZ.notify_all();
    condBarr.notify_all();
    lock.unlock();
    std::terminate();
}

int main() {
    int i;

    std::thread maliari[3];
    std::thread obdivovatelia[8];

    for (i = 0; i < 3; i++) maliari[i] = std::thread(maliar, (void*) i);
    for (i = 0; i < 8; i++) obdivovatelia[i] = std::thread(obdivovatel, (void*) i);

    std::this_thread::sleep_for (std::chrono::seconds(30));
    stoj = 1;

    for (i = 0; i < 3; i++) maliari[i].join();
    for (i = 0; i < 8; i++) obdivovatelia[i].join();

    std::cout << "Pocet zmien obrazov na stojane: " << counterM << std::endl;

    return 0;
}