#define QUANTITY 9  // pocet policok hlavolamu
#define BORDER 3  	// sqrt(pocet)

typedef struct {
	int position[QUANTITY];     // 3x3 naplnene cislami
} STATUS;

typedef struct status_History {
	STATUS s;
	char *ops;                  // postupnost operacii ku stavu
} STATUS_HISTORY;

typedef struct tree_ele {
	STATUS state;
	char prevOp;	            // predchadzajuci operator
	struct tree_ele *child1;    // nasledovnici vrchola
	struct tree_ele *child2;
	struct tree_ele *child3;
	struct tree_ele *child4;
} TREE_ELE;


typedef struct front_ele {
	TREE_ELE *c;
	struct front_ele *next;
} FRONT_ELE;

typedef struct front {
	FRONT_ELE *last;		    // posledny prvok frontu
	FRONT_ELE *first;		    // prvy prvok frontu
} FRONT;

typedef struct {
     int *table;                // hodnoty 0(na zaciatku)/1 - urcuje ci dany stav patri do mnoziny, ich pocet je 9!
     int koef[QUANTITY];        // 8!, 7!, 6! ...
} HASH_TABLE;


TREE_ELE *getFront(FRONT *p_front);
STATUS_HISTORY *getFront2(FRONT *p_front);
void insertFront(TREE_ELE *c, FRONT *p_front);
void insertFront2(STATUS_HISTORY *c, FRONT *p_front);
int emptyFront(FRONT *p_front);

HASH_TABLE *createTable(void);
void insertHash(STATUS s, HASH_TABLE *hashTable);
int fact(int i);
int hash(STATUS s, HASH_TABLE *hashTable);
int hashIsIn(STATUS s, HASH_TABLE *hashTable);
