/*
Meno:
Datum:
Simulujte nasledujucu situaciu. Styria generovaci pismenok generuju pismenka, generovanie pismenka trva nejaky cas (1s) a ked ho vygeneruju, umiestnia ho na stol, kde sa zmesti 10 pismenok. Desiati testovaci beru pismenka zo stola a testuju ich, testovanie pismenka trva nejaky cas (2s). Cela simulacia nech trva 30s.
1. Doplnte do programu pocitadlo vygenerovanych a pocitadlo otestovanych pismenok, na konci simulacie vypiste hodnoty pocitadiel. [2b]
2. Osetrite v programe pristup k stolu - zmente umiestnovanie a branie pismenok tak, aby nehrozilo, ze generovac "prepise" pismenko, ktore nebolo otestovane, a ze testovac otestuje pismenko, ktore nebolo vygenerovane alebo uz bolo otestovane. [5b]
3. Osetrite v programe spravne ukoncenie generovacov a testovacov po uplynuti stanoveneho casu simulacie. [3b]
Poznamky:
- na synchronizaciu pouzite iba mutexy, podmienene premenne alebo semafory
- build (console): gcc generator_pismenok -o generator_pismenok -lpthread
*/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>

// stol s pismenkami
char stol[10] = {' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
int pozicia_na_umiestnenie = 0;
int pozicia_na_zobratie = 0;
int pocet_na_stole = 0;

// signal na zastavenie
int stoj = 0;

// pocty
int vygenerovane = 0;
int otestovane = 0;

pthread_mutex_t vygenerovane_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t otestovane_mutex = PTHREAD_MUTEX_INITIALIZER;

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t stol_plny_cond = PTHREAD_COND_INITIALIZER;
pthread_cond_t stol_prazdny_cond = PTHREAD_COND_INITIALIZER;

// generovanie pismenka
char generuj_pismenko(void)
{
    sleep(1);

	pthread_mutex_lock(&vygenerovane_mutex);
	vygenerovane++;
	pthread_mutex_unlock(&vygenerovane_mutex);

    return 'A';
}

// testovanie pismenka
void testuj_pismenko(char pismenko)
{
    sleep(2);

	pthread_mutex_lock(&otestovane_mutex);
	otestovane++;
	pthread_mutex_unlock(&otestovane_mutex);

}

// generator pismenok
void *generovac_pismenok( void *ptr ) {

    while(1) {

        // vygenerovanie pismenka
        char pismenko = generuj_pismenko();


		pthread_mutex_lock(&mutex);

        // ak je stol plny a nie je koniec simulacie -> caka
		while (pocet_na_stole == 10 && !stoj) pthread_cond_wait(&stol_plny_cond, &mutex);
		if (stoj) { pthread_mutex_unlock(&mutex); return NULL; }

        // umiestni pismenko na stol
        stol[pozicia_na_umiestnenie] = pismenko;
        pozicia_na_umiestnenie = (pozicia_na_umiestnenie + 1) % 10;

        // signalizujeme jednemu co mozno cakal
		pocet_na_stole++;
		pthread_cond_signal(&stol_prazdny_cond);

		pthread_mutex_unlock(&mutex);
    }
    return NULL;
}

// testovac pismenok
void *testovac_pismenok( void *ptr ) {

    // pokial nie je zastaveny
    while(1) {

		pthread_mutex_lock(&mutex);

        // ak je stol plny a nie je koniec simulacie -> caka
		while (pocet_na_stole == 0 && !stoj) pthread_cond_wait(&stol_prazdny_cond, &mutex);
		if (stoj) { pthread_mutex_unlock(&mutex); return NULL; }

        // vzatie pismenka zo stola
        char pismenko = stol[pozicia_na_zobratie];
        pozicia_na_zobratie = (pozicia_na_zobratie + 1) % 10;

        // signalizujeme jednemu co mozno cakal
		pocet_na_stole--;
		pthread_cond_signal(&stol_plny_cond);

		pthread_mutex_unlock(&mutex);


        // otestovanie pismenka
        testuj_pismenko(pismenko);
    }
    return NULL;
}

int main(void) {
    int i;

    pthread_t generovaci[4];
    pthread_t testovaci[10];

    for (i=0;i<4;i++) pthread_create( &generovaci[i], NULL, &generovac_pismenok, NULL);
    for (i=0;i<10;i++) pthread_create( &testovaci[i], NULL, &testovac_pismenok, NULL);

    sleep(30);

	pthread_mutex_lock(&mutex);
    stoj = 1;
	pthread_cond_broadcast(&stol_plny_cond);
	pthread_cond_broadcast(&stol_prazdny_cond);
	pthread_mutex_unlock(&mutex);

    for (i=0;i<4;i++) pthread_join( generovaci[i], NULL);
    for (i=0;i<10;i++) pthread_join( testovaci[i], NULL);

	printf("Vygenerovane: %d\n", vygenerovane);
	printf("Otestovane: %d\n", otestovane);

    exit(EXIT_SUCCESS);
}
