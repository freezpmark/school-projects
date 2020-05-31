/*
Meno: Peter Markuš
Datum: 30.10.2017

Simulujte nasledujúcu situáciu. V istej firme je 7 programátorov a 5 testerov.
Testeri sa delia na 2 skupiny - dvaja rıchli (testujú 2s) a traja pomalí (testujú 3s).
Programátori a testeri neustále pracujú na vyvíjanom programe,
prièom naraz na òom môu pracova jedine programátori alebo testeri, nie spolu, keïe by to pokazilo vısledky.

Programovanie trvá programátorovi nejakı èas - v simulácii 3s, prièom potom si dáva 2s prestávku.

Testovanie trvá tie nejakı èas, pod¾a vıkonnosti testera - 2s alebo 3s.
Testeri si dávajú 3s prestávku v menších skupinkách po troch, po tom,
èo sa ich nazbiera dostatoènı poèet po testovaní (kadı tester si musí spravi prestávku, kım zaène znova testova).

Cela simulácia nech trvá 30s.

1. Doplòte do programu poèítadlo poèítajúce, ko¾ko krát ktorı
tester a programátor pracoval na programe. [1b]

2. Zabezpeète, aby na programe naraz nepracovali testeri a programátori,
prièom èlenovia rovnakej skupiny môu pracova spoloène.
Ak na zaèatie práce èaká u celá druhá skupina - 7 programátorov,
alebo 5 testerov u ïalší èlenovia opaènej skupiny nezaèínajú prácu na programe. [4b]

2. Zabezpeète správne ošetrenie prestávky testerov v skupinke po troch. [4b]

3. Ošetrite v programe správne ukonèenie simulácie po uplynutí stanoveného èasu. [1b]

Poznámky:
- na synchronizáciu pouite iba mutexy a podmienené premenné
- nespoliehajte sa na uvedené èasy, simulácia by mala fungova aj s inımi èasmi
- build (console): gcc programatori_a_testeri -o programatori_a_testeri -lpthread
*/


 /// STRUCT predn
 // 2m riadky
 #include<stdio.h>
 #include<stdlib.h>
 #include<pthread.h>
 #include<unistd.h>
 
 pthread_mutex_t mutex    = PTHREAD_MUTEX_INITIALIZER;
 pthread_mutex_t mutex2   = PTHREAD_MUTEX_INITIALIZER;
 pthread_cond_t cond      = PTHREAD_COND_INITIALIZER;
 pthread_cond_t cond2     = PTHREAD_COND_INITIALIZER;
 pthread_cond_t cond3     = PTHREAD_COND_INITIALIZER;
 int testWait = 0;       // pocet aktualne cakajucich
 int progWait = 0;
 
 int testers = 0;        // pocet aktualne testujucich/programujucich
 int progs = 0;
 
 int barr = 0;
 int inBarrier = 0;
 
 
 // signal na zastavenie simulacie
 int stoj = 0;
 
 // programovanie
 void programovanie() {
     sleep(3);
 }
 
 // prestavka programatora
 void prestavka_programator() {
     sleep(2);
 }
 
 // testovanie
 void testovanie(int i) {
     i < 2 ? sleep(2) : sleep(3);
 }
 
 // prestavka testera
 void prestavka_tester(int i) {
     sleep(3);
 }
 
 // programator
 void *programator(void * ptr) {
     int id = (int) ptr;
     int progCounter = 0;
 
     // pokial nie je zastaveny
     while(!stoj) {
         pthread_mutex_lock(&mutex);
         while(testWait == 5 || testers) {     // 5 testovacov caka || niekto testuje
             if(stoj)
                 break;
             progWait++;
             pthread_cond_wait(&cond, &mutex);
         }
         if(stoj)
             break;
         progs++;
         progWait--;
         pthread_mutex_unlock(&mutex);
 
         printf("%d. Programujem...\n", id);
         programovanie();
 
         pthread_mutex_lock(&mutex);
         progCounter++;
         progs--;
         pthread_mutex_unlock(&mutex);
 
         pthread_cond_broadcast(&cond2);
 
         printf("%d. PROG oddychuje..\n", id);
         if(stoj)
             break;
         prestavka_programator();
     }
 
     pthread_cond_broadcast(&cond);
     pthread_mutex_unlock(&mutex);
     printf("%d. programator prog. %d-krat\n", id, progCounter);
     pthread_exit(0);
 }
 
 // tester
 void *tester(void * ptr) {
     int id = (int) ptr;
     int testCounter = 0;
 
     // pokial nie je zastaveny
     while(!stoj) {
 
         pthread_mutex_lock(&mutex2);
         while(progWait == 7 || progs) {  // 7 programatorov caka || niekto programuje
             if(stoj)
                 break;
             testWait++;
             pthread_cond_wait(&cond2, &mutex2);
         }
         if(stoj)
             break;
         testers++;
         testWait--;
         pthread_mutex_unlock(&mutex2);
 
         printf("                                    %d. Tester testuje..\n", id);
         testovanie(id);
 
         pthread_mutex_lock(&mutex2);
         testCounter++;
         barr++;
         testers--;
         if(barr == 3) {                 // prestavka po troch...
             inBarrier = 1;
             pthread_cond_broadcast(&cond3);
         } else
             while(!inBarrier) {
                 if(stoj)
                     break;
                 pthread_cond_wait(&cond3, &mutex2);
             }
         barr--;
         if(barr == 0) {
             inBarrier = 0;
             pthread_cond_broadcast(&cond3);
         } else
             while(inBarrier) {
                 if(stoj)
                     break;
                 pthread_cond_wait(&cond3, &mutex2);
             }
         pthread_mutex_unlock(&mutex2);
 
         pthread_cond_broadcast(&cond);
 
         printf("                                    %d. tester oddychuje..\n", id);
         if(stoj)
             break;
         prestavka_tester(id);
     }
 
     pthread_cond_broadcast(&cond2);
     pthread_cond_broadcast(&cond3);
     pthread_mutex_unlock(&mutex2);
     printf("%d. tester test. %d-krat\n", id, testCounter);
     pthread_exit(0);
 }
 
 int main(void) {
     int i;
 
     pthread_t programatori[7];
     pthread_t testeri[5];
 
     for (i = 0; i < 7; ++i) {
         pthread_create(&programatori[i], NULL, &programator, (void*) i);
     }
 
     for (i = 0; i < 5; ++i) {
         pthread_create(&testeri[i], NULL, &tester, (void*) i);
     }
 
     sleep(30);
     stoj = 1;
 
     for (i = 0; i < 7; ++i) {
         pthread_join(programatori[i], NULL);
     }
 
     for (i = 0; i < 5; ++i) {
         pthread_join(testeri[i], NULL);
     }
 
     exit(EXIT_SUCCESS);
 }
 