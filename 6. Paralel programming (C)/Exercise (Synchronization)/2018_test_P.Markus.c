/*

https://goo.gl/Veyn4C
https://www.dropbox.com/s/ji6udlx60rl77r8/h2no3.c?dl=0

Meno:
Datum:

Simulujte nasledujucu situaciu. Výrobu molekul H2NO3 zabezpecuju výrobcovia prvkov H, N a O.
Na výrobu jednej molekuly H2NO3 je potrebných 2xH, 1xN a 3xO atomy, pricom atomy kazdeho prvku vyraba 7 vyrobcov. Vyrobca
    1. vyraba jeden atom nejaky cas (v simulacii 1, 2 a 3s, pre prvky O, H a N),
    2. potom pocka na ostatne atomy potrebne na vyrobu jednej molekuly,
    3. molekula sa vyrobi a potom vyrobca ide robit dalsi atom.
Vyroba molekuly netrva ziadny cas, cize uvazovat ci sa mozu vyrabat viacere molekuly subezne nema zmysel.

1. Doplnte do programu premenne pocitajuce spotrebovane atomy pre kazdy prvok, na konci simulacie vypiste pocty spotrebovanych atomov pre kazdy prvok. [2b]

2. Zabezpecte synchronizaciu tak, aby vyrobca po vyrobeni atomu pockal na dalsich vyrobcov (ak treba), aby mohla vzniknut molekula H2NO3. [6b]

3. Osetrite v programe spravne ukoncenie simulacie po vyrobeni 20 molekul. [2b]

Poznamky:
- na synchronizaciu pouzite iba mutexy+podmienene premenne; resp monitory
- nespoliehajte sa na uvedene casy, simulacia by mala fungovat aj s inymi casmi alebo s nahodne generovanymi casmi
- build (console): gcc h2no3.c -o h2no3 -lpthread

Konstukcie:
    pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
    pthread_cond_t cond = PTHREAD_COND_INITIALIZER;

    pthread_mutex_lock(&mutex);
    pthread_mutex_unlock(&mutex);

    pthread_cond_wait(&cond, &mutex);
    pthread_cond_signal(&cond);
    pthread_cond_broadcast(&cond);

    pthread_t thread;
    void *thread_fnc(void* param)
    {
        ...
        pthread_exit(NULL);
        return NULL;
    }
    pthread_create(&thread, NULL, &thread_fnc, (void*)param);
    pthread_join(thread, NULL);
*/


#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

// konstanty
#define CAS_H 2
#define CAS_N 3
#define CAS_O 1
#define POCET_H 7
#define POCET_N 7
#define POCET_O 7
#define POCET_H2NO3 20

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mutex2 = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;
pthread_cond_t cond2 = PTHREAD_COND_INITIALIZER;
pthread_cond_t cond3 = PTHREAD_COND_INITIALIZER;

// pocty vyrobenych atomov a molekul
int pocet_H = 0;
int pocet_N = 0;
int pocet_O = 0;
int pocet_H2NO3 = 0;

// vyroba atomu
void vyrob_atom(char prvok)
{
    if (prvok == 'H') {
        sleep(CAS_H);
        pthread_mutex_lock(&mutex2);
        pocet_H++;
        pthread_mutex_unlock(&mutex2);
    } else if (prvok == 'N') {
        sleep(CAS_N);
        pthread_mutex_lock(&mutex2);
        pocet_N++;
        pthread_mutex_unlock(&mutex2);
    } else if (prvok == 'O') {
        sleep(CAS_O);
        pthread_mutex_lock(&mutex2);
        pocet_O++;
        pthread_mutex_unlock(&mutex2);
    }
}

// vyroba molekuly
int cakajuci_H = 0;
int cakajuci_N = 0;
int cakajuci_O = 0;

int pocBarr = 0;
int inBarr = 0;

void vyrob_molekulu(char prvok)
{
    // zvysit pocet atomov cakajucich na vyrobenie molekuly
    switch (prvok) {
    case 'H': cakajuci_H++;  break;
    case 'N': cakajuci_N++;  break;
    case 'O': cakajuci_O++;  break;
    }



    pthread_mutex_lock(&mutex);



    // if(cakajuci_H == 2 && cakajuci_N == 1 && cakajuci_O == 3) {
    // count,,, pridat s pocBar...
    if(cakajuci_H > 1 && cakajuci_N > 0 && cakajuci_O > 2) {          //
        inBarr = 1;
        pthread_cond_broadcast(&cond);  printf("SIGNAL!\n");
    }
    else
        while (!inBarr && pocet_H2NO3 != 20)
            pthread_cond_wait(&cond, &mutex);
    if(pocet_H2NO3 == 20) { pthread_mutex_unlock(&mutex); return NULL; }
    pocBarr++;
    if(pocBarr == 6) {
        inBarr = 0;
        pthread_cond_broadcast(&cond);

        // vyrobit molekulu
        pocet_H2NO3++;
        printf("%c: H%dN%dO%d (%d/%d)\n", prvok, cakajuci_H, cakajuci_N, cakajuci_O, pocet_H2NO3, POCET_H2NO3);
    }
    else
        while (inBarr) pthread_cond_wait(&cond, &mutex);
    pthread_mutex_unlock(&mutex);

    // vynulovat pocet atomov cakajucich na vyrobenie molekuly
    cakajuci_H = 0;
    cakajuci_N = 0;
    cakajuci_O = 0;
    pthread_mutex_unlock(&mutex);
}

// vyrobca
void *vyrobca(void* param) {
    char prvok = (char)param;

    while (pocet_H2NO3 < POCET_H2NO3 && pocet_H2NO3 != 20) {
        vyrob_atom(prvok);
        vyrob_molekulu(prvok);
    }

    pthread_exit(NULL);
    return NULL;
}

// main f.
int main(void) {
    int i;
    int pocet = 0;

    pthread_t vyrobcovia[POCET_H+POCET_N+POCET_O];
    for (i=0; i<POCET_H; i++) pthread_create(&vyrobcovia[pocet++], NULL, &vyrobca, (void*)'H');
    for (i=0; i<POCET_N; i++) pthread_create(&vyrobcovia[pocet++], NULL, &vyrobca, (void*)'N');
    for (i=0; i<POCET_O; i++) pthread_create(&vyrobcovia[pocet++], NULL, &vyrobca, (void*)'O');

    for (i=0; i<pocet; i++) pthread_join(vyrobcovia[i], NULL);

    printf ("H:%d N:%d O:%d\n", pocet_H, pocet_N, pocet_O);

    exit(EXIT_SUCCESS);
}
