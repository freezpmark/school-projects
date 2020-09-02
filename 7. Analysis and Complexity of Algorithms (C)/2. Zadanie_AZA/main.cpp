/*
Nech A je mnoina objektov, prièom kadı z objektov má priradenú svoju cenu. Na zaèiatku je A prázdna mnoina. Na vstupe je zadaná postupnos príkazov (indexujeme od 0), ktoré môu zväèšova mnoinu objektov A a meni cenu objektom z mnoiny A. Kadı z príkazov môe spôsobi vıpis jedného riadku do vıstupného súboru.

Zoznam príkazov:

v "a"
Do mnoiny A pribudne novı objekt s cenou "a". Èíslo "a" je celé èíslo medzi 0 a 2^30.
z "i" "a"
Cena objektu pridaného i-tym príkazom sa zmení na "a". Èísla "i" a "a" sú celé èísla medzi 0 a 2^30. Ak A neobsahuje objekt pridanı i-tym príkazom, na vıstup sa vypíše jeden riadok obsahujúci text "Chyba".
m
Na vıstup sa vypíše medián z cien objektov. V prípade, e poèet objektov je párny, vypíše sa priemer cien dvoch objektov s mediánovımi cenami zaokrúhlenı na celé èísla nadol. Ak A ešte neobsahuje prvky, na vıstup sa vypíše jeden riadok obsahujúci text "Chyba".
Formát vstupu

Na prvom riadku vstupu sa náchadza nezáporné celé èíslo -- poèet podvstupov. Nasledujúci riadok je prázdny. Následne na vstupe nasleduje zodpovedajúci poèet podvstupov. Za kadım podvstupom sa nachádza prázdny riadok. Kadı podvstup sa zaèína riadkom s jednım nezápornım celım èíslom 1<=k<=1000000 udávajúcim poèet prikazov v podvstupe. Nasleduje k riadkov obsahujúcich jednotlivé príkazy.

Formát vıstupu

Vıstupy jednotlivıch podvstupov sú oddelené prázdnym riadkom. Kadı vıpis je obsiahnutı vo svojom vlastnom riadku.

Príklad vstupu:

4

5
v 8
v 5
m
z 1 8
m

5
v 8
v 5
z 3 1
v 4
m

10
v 5
v 4
v 11
v 1022454
v 54
m
z 2 10000000
m
z 0 1
z 5 1

16
v 5
v 3
v 2
v 11
v 13
v 17
v 21
v 31
v 1
m
z 8 40
z 1 40
m
z 0 41
z 2 41
m

Príklad vıstupu (zodpovedá ukákovému vstupu):

6
8

Chyba
5

11
54
Chyba

11
17
31
*/



// uloha-2.cpp -- Uloha 2
// Peter Markus, 13.4.2017 12:58:10

#include<iostream>
#include<stdio.h>
#include<limits.h>
#include<stdlib.h>
#include<set>
#define N               1000000	//(1073741824)
#define AVG(max, min)   (max+min)/2

typedef struct product {
    int val;
	int id;
} PRODUCT;

typedef struct compare {
    bool operator()(const PRODUCT& left, const PRODUCT& right) const {
        if(left.val < right.val)
            return true;
        else if(left.val > right.val)
            return false;
        else if(left.val == right.val) {
        	if(left.id < right.id)
            	return true;
            else if(left.id > right.id)
            	return false;
        }
      	return false;
    }
} COMPARE;

std::set<PRODUCT, COMPARE> smaller;
std::set<PRODUCT, COMPARE>::iterator sit;
std::set<PRODUCT, COMPARE> bigger;
std::set<PRODUCT, COMPARE>::iterator bit;
int median = 0;

void insertNew(PRODUCT buff) {
  	if(bigger.size() == smaller.size()) {
    	if(buff.val < median) {
        	smaller.insert(buff);
        	sit = smaller.end();
            median = (*--sit).val;
        }
        else {
        	bigger.insert(buff);
            bit = bigger.begin();
            median = (*bit).val;
        }
    }
    else if(smaller.size() > bigger.size()) {
    	if(buff.val < median) {
        	sit = smaller.end();
          	bigger.insert(*--sit);		// pridanie prveho prvku zo small do big
          	smaller.erase(*sit);
          	smaller.insert(buff);
        }
        else
          	bigger.insert(buff);
      	bit = bigger.begin();
    	sit = smaller.end();
    	median = AVG((*bit).val, (*--sit).val);
  	}
    else{
    	if(buff.val < median)
        	smaller.insert(buff);
        else {
        	bit = bigger.begin();
          	smaller.insert(*bit);
          	bigger.erase(*bit);
          	bigger.insert(buff);
       	}
      	bit = bigger.begin();
    	sit = smaller.end();
    	median = AVG((*bit).val, (*--sit).val);
 	}
}

int main()
{
  	PRODUCT buff;
  	int *array;

    int i, j, k, l;
    int a, b;
    char c;
	int itemAdded;

    scanf("%d\n\n", &k);
    for(i = 0; i < k; i++) {
      	smaller.clear();
      	bigger.clear();
      	array = (int*)malloc(N*sizeof(int));
        scanf("%d\n", &l);
        itemAdded = 0;
        if(i != 0)
            printf("\n");
        for(j = 0; j < l; j++) {
            scanf("%c\n", &c);
            switch(c) {
            case 'v' :
              	itemAdded = 1;
                scanf(" %d\n", &a);
              	buff.val = a;
              	buff.id = j;
              	array[j] = a;
              	insertNew(buff);
                break;
            case 'z' :
                scanf(" %d %d\n", &a, &b);
                if(j <= a || array[a] == 0) {
                    printf("Chyba\n");
                    break;
                }
              	buff.id = a;
              	buff.val = array[a];

              // este v tom co musim erasovat, musim skontrolovat najprv
              // ci tam nieje menej prvkov nez v druhom sete
              if(median <= array[a]) {			// porovnavanie chyba maybe <=?
                if(bigger.size() < smaller.size()) {
                    sit = smaller.end();
          			bigger.insert(*--sit);		// pridanie prveho prvku zo small do big
          			smaller.erase(*sit);
                }
                bit = bigger.find(buff);
                bigger.erase((*bit));
              }
              else {
                if(smaller.size() < bigger.size()) {
                  	bit = bigger.begin();
          			smaller.insert(*bit);
          			bigger.erase(*bit);
                }
                sit = smaller.find(buff);
                smaller.erase((*sit));
              }
              buff.val = b;
              insertNew(buff);
              array[a] = b;
              break;
            case 'm' :
                if(itemAdded == 0 || j == 0) {
                    printf("Chyba\n");
                    break;
                }
                printf("%d\n", median);
                break;
            default : ;
            }
        }
    }
    return 0;
}
