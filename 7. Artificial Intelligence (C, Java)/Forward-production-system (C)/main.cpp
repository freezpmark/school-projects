#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "struct.h"
#define MAX 75

FACTS *insertFact       (FACTS *allFacts,   FACTS *pom);
FACTS *vymaz            (FACTS *allFacts,   FACTS *pom);
FACTS *insertAction     (FACTS *actions,    FACTS *expanded);
FACTS *apply            (FACTS *actions,    FACTS *allRules);
FACTS *deleteFact       (FACTS *expanded,   FACTS *allFacts);
FACTS *remove           (FACTS *expanded,   FACTS *token);
FACTS *insertExpAction  (FACTS *expanded,   char *text);
FACTS *expand           (FACTS *allFacts,   RULE *allRules, FACTS *expanded, int x);
RULE *copy              (RULE *pom);
RULE *insertRule        (RULE *allRules,    RULE *pom);
int exchange            (RULE *pom2AllRules,FACTS *pomAllFacts, RULE *exchangedRule, int x, int y);
bool empty              (FACTS *expanded);
void print              (FACTS *allFacts);

FACTS *expand(FACTS *allFacts, RULE *pomAllRules, FACTS *expanded, int start){
    /// Init
	int iC = start,     // index condition
        iF = 2,         // index fact
        nameSize;
	char cmp1[10],
         cmp2[10];
    FACTS *pomAllFacts   = allFacts;
    RULE *exchangedRule = NULL;
	RULE *pom2AllRules  = copy(pomAllRules);        // aby sa neprepisal prvy pom

	while (pomAllFacts != NULL) {                   /// prechod jednotlivych faktov
		while (pom2AllRules->cond[iC] == '(')       // prechadzanie cez medzery a zatvorky
            iC++;
		while (pom2AllRules->cond[iC] == ' ')
            iC++;
		if (pom2AllRules->cond[iC] != 0) {         // ak po preskakani tam nieco je, pokracujeme v ife
			if (pomAllFacts->fakt[iF] == ' ')      // kontrola faktov.. najprv nezbehne.. je tam P
                iF++;                              // preskok medzeri u faktov

			// ak su znaky rovnake, prejdem na dalsie (prechadzam cez vety v pravidlach a faktoch)
			// ak je tam otaznik, urobim exchange na pozicii X,
			while ((pomAllFacts->fakt[iF] == pom2AllRules->cond[iC]) || (pom2AllRules->cond[iC] == '?')) {
				if (pom2AllRules->cond[iC] == '?') {            // ideme nahradzat premennu menom
					exchangedRule = copy(pom2AllRules);
					 //for(int k = 0; k < 100; k++)  printf("%c", exchangedRule->name[k]);printf("\n");
					 if(!(nameSize = exchange(pom2AllRules, pomAllFacts, exchangedRule, iC, iF)))
                        break;                                  // vratil 0, cize sa nic nezmenilo
                     else {
                        iC += nameSize;                     // mozeme vynechat slovo vo faktoch aj podmienke, s ktorym uz pracovat nebudeme
                        iF += nameSize;
                        pom2AllRules = exchangedRule;
                     }
					//nameSize = exchange(pom2AllRules, pomAllFacts, exchangedRule, iC, iF);  //modifikovanie exchangedRule (uz pravda), a vracanie dlzky mena
					 //for(int k = 0; k < 100; k++)  printf("%c", exchangedRule->name[k]);printf("\nEND!!!\n");
				}
				else {
				    iC++;
                    iF++;
                }
			}
            //printf("Koniec\niF: %d, strlen(pomAllFacts->fakt: %d\n", iF, strlen(pomAllFacts->fakt));
			//s iFom som sa dostal na koniec faktu, to znamena ze potrebujem otestovat dalsie fakty, pretoze mi chyba nejake meno/veta
			//v opacnom pripade uz mozem returnovat hodnoty..
			if (iF == (int) strlen(pomAllFacts->fakt)) {
				if (pom2AllRules->cond[iC] == '(')          // ak mam dalsiu podmienku, zavolam rekurzivne dalsi exchange
					expanded = expand(allFacts, copy(pom2AllRules), expanded, iC);
				else    // ked som na konci podmienky, pridam akciu do expanded
                    if(pom2AllRules->cond[iC] == ')') {
                        expanded = insertExpAction(expanded, pom2AllRules->action);
                    }
                    else
                        printf("Nespravne znak %d %d:", pom2AllRules->cond[iC], iC);
			}
			else
            if (pom2AllRules->cond[iC] == '<') {		        // specialna podmienka
                iC += 3;                                        // posun
				sscanf(pom2AllRules->cond + iC, "%s", cmp1);    // nacitanie mien po <>
				sscanf(pom2AllRules->cond + iC + strlen(cmp1), "%s", cmp2);
				if (strcmp(cmp1, cmp2) == 0)                    // ked sa rovnaju, cela podmienka prestane platit, return
                    return expanded;
				return expanded = insertExpAction(expanded, pom2AllRules->action);    // ked su rozne, pridaj akciu do expanded
            }

			pom2AllRules = copy(pomAllRules);   // obnovenie douprovavaneho pravidla,
			//potrebujeme zacat so starymi premennymi, nedostali sme sa nakoniec podmienky.. musime zacat od iter s novym faktom
			iF = 2;
			iC = start;
			pomAllFacts = pomAllFacts->next;    // ide sa spracovavat dalsi fakt
		}
	}
	return expanded;
}

// nahradzenie premenných (?X) [pravidla] za normálne mená (Gordon) [fakty], len jedno meno premennej premiena
int exchange(RULE *pom2AllRules, FACTS *pomAllFacts, RULE *exchangingRule, int x, int y){
	//       ?X,    Maiev,      pom
	char var[3], name[20], newVar[20], end;
	int i, j, nameSize = 0, varSize = 0, newVarSize = 0;   // velkosti danych poli
	name[0] = '\0';

	// nacitanie var a jeho dlzky
	sscanf(exchangingRule->cond + x, "%s", var);        // od pozicia x zacne string ?X
	varSize = strlen(var);                              // var idem nahradit menom

	// nacitanie mena faktu, ktory bude nahradzat (mena idu postupne..)                                            // && pomAllFacts->fakt[y] != '<'
	if ((pomAllFacts->fakt[y] <= 'Z') && (pomAllFacts->fakt[y]!= '<')) {                  // ak tam nieje slovo, nedojde k nahradzaniu a vrati 0
		sscanf(pomAllFacts->fakt + y, "%s", name);      // od pozicie y zacne string slova v allFacts
		nameSize = strlen(name);

		y = x;      // pre doplnenie zvysku podmienky.. y sa potom stava offsetom pre staru verziu pravidla

		// cyklus na zmenu vsetkych premennych s takym menom, ktory sa ako prvy vyskytuje v riadku
		while (sscanf(exchangingRule->cond + x, "%s", newVar) != 0) {
			newVarSize = strlen(newVar);                 // to iste ako u prvom nacitani do var

			if (!strncmp(var, newVar, varSize)) {     // stringy su rovnake
				i = x;
                j = 0;
				while (i < (x + nameSize))           // vymena ?Z za normalne name
					exchangingRule->cond[i++] = name[j++];
				exchangingRule->cond[i] = '\0';

				//DOPLNI ZVYSOK PODMIENKY
				strcat(exchangingRule->cond, pom2AllRules->cond + y + varSize);   // najde sa '\0', tam zacne kopcit,
				//y je pociatocne x (teda tam kde zacalo ?X) (y bol povodne pre fakty, ale teraz je uz pravidlo)
				//namiesto neho je tam uz to normalne name.. ale v 'pomocne', ktory je este stary (nevymeneny),
				//je treba posunut o string velkosti tej premennej, aby to za menom pokracovalo
				x += 1 + nameSize;  // je treba posunut offset o name, lebo var tam uz nieje
				y += 1 + varSize;   // posunutie o varSize.. lebo to plati pre staru verziu
			}
			else {  	//ak tam nieje premenna v podmienke, je tam sucast vety.. na zaklade nej sa posuvam v oboch offsetoch (stareho aj noveho)
				x += 1 + newVarSize;
				y += 1 + newVarSize;
			}
			end = exchangingRule->cond[x-1];       // ak sme na konci PODMIENKY, breakneme while cyklus
			if (end == '\0') {
                break;
			}
		}
		// Spracovavanie akcie
		x = y = 3;      // na 4. znaku zacina akcia.. ktora prebieha analogicky
		while (sscanf(exchangingRule->action + x, "%s", newVar) != 0) {
			newVarSize = strlen(newVar);
			if ((varSize == newVarSize) && (!strncmp(var, newVar, varSize))) {
				i = x;
				j = 0;
				while (i < (x + nameSize))
					exchangingRule->action[i++] = name[j++];
				exchangingRule->action[i] = '\0';
				strcat(exchangingRule->action, pom2AllRules->action + y + varSize);
				x += 1 + nameSize;
				y += 1 + varSize;
			}
			else {
				x += 1 + newVarSize;
				y += 1 + newVarSize;
			}
			end = exchangingRule->action[x-1];
			if (end == 0)
                break;
		}
	}
	else
        return 0;
	return nameSize;
}

RULE *copy(RULE *pom){
	RULE *novy = new RULE;

	for (int i = 0; i < 20; i++)
		novy->name[i] = pom->name[i];
	for (int i = 0; i < MAX; i++) {
		novy->cond[i] = pom->cond[i];
		novy->action[i] = pom->action[i];
	}
	novy->next = NULL;

	return novy;
}

FACTS *insertAction(FACTS *actions, FACTS *expanded){

	if (expanded == NULL || (expanded->fakt[0] == 0))
        return actions;
	if (actions->fakt[0] == 0)
        return expanded;

	FACTS *iAction = actions;
	FACTS *addNew  = expanded;
	while (iAction->next != NULL)
        iAction = iAction->next;
	iAction->next = addNew;

	return actions;
}

FACTS *apply(FACTS *actions, FACTS *allFacts){
    FACTS *pom;
	char cmdBuff[40];
	int i;

	sscanf(actions->fakt + 3, "%s", cmdBuff);
	if (!strncmp(cmdBuff, "pridaj", 6)) {
		pom = new FACTS;
		pom->fakt[0] = '(';
		pom->fakt[1] = ' ';
		for (i = 2; i < 40; i++) {
			pom->fakt[i] = actions->fakt[i+8];
			if (pom->fakt[i] == ')')
                break;
		}
/*
		if(pom->fakt[i+3] == 's') {
            for(i = 3; pom->fakt[i+3] != ')'; i++)
                printf("%c", pom->fakt[i+3]);
            printf("\n");
		}*/

		pom->fakt[i+1] = 0;
		allFacts = insertFact(allFacts, pom);
	}
	else {       // akcia vymaz
		pom = new FACTS;
		pom->fakt[0] = '(';
		pom->fakt[1] = ' ';
		for (i = 2; i < 40; i++) {
			pom->fakt[i] = actions->fakt[i+7];    // o jeden znak menej
			if (pom->fakt[i] == ')')
                break;
		}
/*
		if(pom->fakt[i+3] == 's') {
            for(i = 3; pom->fakt[i+3] != ')'; i++)
                printf("%c", pom->fakt[i+3]);
            printf("\n");
		}*/

		pom->fakt[i+1] = 0;
		allFacts = vymaz(allFacts, pom);
	}

	return allFacts;
}

FACTS *vymaz(FACTS *allFacts, FACTS *pom){
    FACTS *pom2;
    pom2 = allFacts;

	if (!strncmp(pom2->fakt        , pom->fakt, strlen(pom->fakt))) // hned na prvom
		return pom2->next;
	while(strncmp(pom2->next->fakt , pom->fakt, strlen(pom->fakt))) // prehladava
            pom2 = pom2->next;
	if (!strncmp(pom2->next->fakt  , pom->fakt, strlen(pom->fakt))) // naslo sa, prepisem ho
		pom2->next = pom2->next->next;

	return allFacts;
}

FACTS *remove(FACTS *expanded, FACTS *findThis){
	if (expanded->fakt[0] == 0)
        return expanded;
	if (expanded == findThis)
		return expanded->next;

	while(expanded != NULL && expanded->next != findThis)
		expanded = expanded->next;
	if (expanded != NULL)
		expanded->next = findThis->next;

	return expanded;
}

// vlozenie novovytvoreneho textoveho faktu do expanded
FACTS *insertExpAction(FACTS *expanded, char *text){
	FACTS *token;

	FACTS *pom = new FACTS;
	pom->next = NULL;

	for (int i = 0; i < 40; i++)
		pom->fakt[i] = text[i];
	if (expanded->fakt[0] == 0)          // ak este nic nieje v expanded
        expanded = pom;
	else {
		for(token = expanded;            // prechadzanie existujucich faktov
		    token->next != NULL;
            token = token->next) ;
		token->next = pom;
	}

	return expanded;
}

void print(FACTS *allFacts){
	FACTS *token = allFacts;
	for(printf("\n");
        token != NULL;
        token = token->next)
            printf("%s\n", token->fakt);
}

bool empty(FACTS *expanded){
	if (expanded == NULL)
        return true;
	if (expanded->fakt[0] == 0)
        return true;
	return false;
}

FACTS *insertFact(FACTS *allFacts, FACTS *newFact){
    FACTS *allPom;
	FACTS *newPom = new FACTS;
	newPom->next = NULL;


	for (int i = 0; i < 35; i++)
		newPom->fakt[i] = newFact->fakt[i];     // newFact dame do noveho
    allPom = allFacts;
	if (allFacts->fakt[0] == 0)                 // ak tam ziadny nieje, rovno do prveho dame novy
		allFacts = newPom;
	else {
		while (allPom->next != NULL)
            allPom = allPom->next;
		allPom->next = newPom;
	}

	return allFacts;
}

RULE *insertRule(RULE *newRule, RULE *allRules){
	RULE *allPom;
	RULE *newPom = new RULE;
	newPom->next = NULL;

	allPom = allRules;

	for (int i = 0; i < 35; i++)
		newPom->name[i] = newRule->name[i];

	for (int i = 0; i < MAX; i++) {
		newPom->cond[i] = newRule->cond[i];
		newPom->action[i] = newRule->action[i];
	}
	if (allRules->name[0] == 0)
		allRules = newPom;
	else {
		while (allPom->next != NULL)
            allPom = allPom->next;
		allPom->next = newPom;
	}

	return allRules;
}

FACTS *deleteFact(FACTS *actions, FACTS *allFacts){
    if (actions->fakt[0] == 0)
        return actions;

	FACTS *pomActions = actions, *pomAllFacts;
	bool iter = false;
	char cmdBuff[40];

	while (pomActions != NULL) {       // prechod cez vsetky nove fakty (akcie)
        //pomAllFacts = allFacts;
		sscanf(pomActions->fakt + 3, "%s", cmdBuff);       // posunutie o medzery/zatvorky

		if (!strncmp(cmdBuff, "pridaj", 6)) {               // akcia PRIDAJ
            pomAllFacts = allFacts;
			while (pomAllFacts != NULL) {                   // prechod vsetkymi faktami
				if (!strncmp(pomAllFacts->fakt + 2, pomActions->fakt + 10, strlen(pomAllFacts->fakt + 2))) {
				    if((actions=remove(actions,pomActions)) == NULL)
                        return NULL;

					//actions = remove(actions, pomActions);  // ak sa nasiel actions v allFacts, ideme ho vyhladat v originalnej premennej (argument funkcie) a vymazeme ho
					pomActions = actions;                   // pre dalsiu iteraciu vonkajsieho cyklu
                    iter = true;                            // zaznacime, ze sme uz zaiterovali
                    break;
				}
				else                                        // ak niesu rovnake, hladam dalej a idem na dalsi fakt
                    pomAllFacts = pomAllFacts->next;
			}
			if (!iter)                                      // ak sa nic nemenilo, prechadzam do dalsej akcie
				pomActions = pomActions->next;
			iter = false;
		}

		else if (!strncmp(cmdBuff, "vymaz", 5)) {           // akcia VYMAZ
            pomAllFacts = allFacts;
			while (pomAllFacts != NULL) {                    // prechadzanie cez vsetky fakty
				if (!strncmp(pomAllFacts->fakt + 2, pomActions->fakt + 9, strlen(pomAllFacts->fakt + 2)))
					break;  // nasiel som fakt
				else
                    pomAllFacts = pomAllFacts->next;
			}

			// ak som nenasiel dany fakt, odstranim ho (ide o opak akcie pridaj, hladame akcie ktore zodpovedaju faktom..)
			if (pomAllFacts == NULL) {
				if ((actions = remove(actions, pomActions)) == NULL)
                    return NULL;
				pomActions = actions;
			}
			else
                pomActions = pomActions->next;
		}
/*
		else
			if (!strncmp(cmdBuff, "sprava", 6)) {       // odstranim ho, pretoze nic nerobi
				if ((actions = remove(actions, pomActions)) == NULL){
                    return NULL;
				}
				pomActions = actions;
			}*/
	}
	return actions;
}

int main(void){
	char stmt[MAX], c = 0;
	int i = 0;
	int processStmt = 0;
	FILE *file;

	FACTS *expanded, *actions;
	FACTS *allFacts;
	FACTS *newFact;
	allFacts = new FACTS;
	allFacts->fakt[0] = 0;
	allFacts->next = NULL;

	RULE *allRules;
	RULE *pomAllRules;
	RULE *newRule;
	allRules = new RULE;
	allRules->next = NULL;
	allRules->name[0] = 0;
	allRules->cond[0] = 0;
	allRules->action[0] = 0;

	/// nacitavanie faktov
    file = fopen("fakty.txt", "r");
    while (fscanf(file, "%c", &c) != EOF) {
		i = 0;
		if (c == '(') {
			processStmt = 1;                    // zacina sa proces vytvarania statementu
			while (processStmt != 0) {          // ked sa zatvorka uzatvorila, konci proces
				stmt[i++] = c;
				fscanf(file, "%c", &c);
				if (c == '(')
                    processStmt++;
				else if (c == ')')
                    processStmt--;
			}
			stmt[i]   = c;                              // posledne znak ktory sa nacital pred prerusenim
			stmt[i+1] = 0;                              // koniec stringu
			newFact = new FACTS;
			for (i = 0; i < 45; i++)
				newFact->fakt[i] = stmt[i];             // nacitanie stmt do premennej newFact
			newFact->next = NULL;
			allFacts = insertFact(allFacts, newFact);   // pridanie do vsetkych faktov
		}
	}
	//printf("%c%c%c\n", allFacts->fakt[0], allFacts->fakt[1], allFacts->fakt[2]);
    //printf("%c%c%c\n", allFacts->next->next->fakt[0], allFacts->next->next->fakt[1], allFacts->next->next->fakt[2]);
	fclose(file);

	/// nacitavanie pravidiel
	processStmt = 0;
	file = fopen("pravidla.txt", "r");
    while (fscanf(file, "%c", &c) != EOF) {
		i = 0;
		newRule = new RULE;
		while (c != ':') {
			newRule->name[i++] = c;
			fscanf(file, "%c", &c);
		}
		newRule->name[i]   = c;                // posledne c ktore sa nacitalo pred prerusenim
		newRule->name[i+1] = 0;                // koniec stringu
		newRule->next      = NULL;

		fscanf(file, "%s%c%c", stmt, &c, &c);   // len na prejdenie cez znaky
		if (c == '(') {                         // prva zatvorka po AK, nacitava sa cond
			processStmt = 1;
			i = 0;
			while (processStmt != 0) {
				stmt[i++] = c;
				fscanf(file, "%c", &c);
				if (c == '(')
                    processStmt++;
				if (c == ')')
                    processStmt--;
			}
			stmt[i] = c;
			stmt[i+1] = 0;
			for (i = 0; i < MAX; i++)
				newRule->cond[i] = stmt[i];     // nacitanie podmienky so zatvorkami
		}
		fscanf(file, "%s%c%c", stmt, &c, &c);
		if (c == '(') {                         // prva zatvorka po POTOM, nacitava sa action
			processStmt++;
			i = 0;
			while (processStmt != 0) {
				stmt[i++] = c;
				fscanf(file, "%c", &c);
				if (c == '(')
                    processStmt++;
				if (c == ')')
                    processStmt--;
			}
			stmt[i] = c;
			stmt[i+1] = 0;
			for (i = 0; i < MAX; i++)
				newRule->action[i] = stmt[i];   // nacitanie akcie so zatvorkami
		}
		allRules = insertRule(newRule, allRules);   // vlozenie noveho pravidlo do zasobnika pravidiel
		fscanf(file, "%c", &c);
		fscanf(file, "%c", &c);
	}
	fclose(file);

	///     STARTING PROCESS    ///
	pomAllRules = allRules;
	while (1) {
		expanded          = actions            = new FACTS;
		expanded->fakt[0] = actions->fakt[0]   = 0;
		expanded->next    = actions->next      = NULL;

		while (pomAllRules != NULL) {                               /// prechod cez vsetky pravidla
			expanded = expand(allFacts, pomAllRules, expanded, 0);  /// pre kazde pravidlo vytvorime vsetky mozne akcie pomocou faktov ktore su k dispozicii
			//FACTS *pom = actions;for( ; pom->next != NULL; pom = pom->next) {for(int a=0; a<40; a++) {printf("%c",pom->fakt[a]);}printf("\n");}printf("KONIEC!!!!\n");
			actions = insertAction(actions, expanded);              /// pridanie vsetkych najdenych akcii do radu akcii, ktore sa budu vykonavat (neduplicitne pomocou nasl. deleteFact) (mem)
			expanded = new FACTS;                // priprava pre dalsiu iteraciu
			expanded->fakt[0] = 0;
			expanded->next = NULL;
			pomAllRules = pomAllRules->next;
		}                                        // dostali sme vsetky nove akcie z danych faktov

		/// Pri rade vykonavanych akcii je treba skontrolovat, ci sme uz danu akciu nevykonali/alebo sme ju uz mali vo faktoch
        //FACTS *pom = actions;for( ; pom->next != NULL; pom = pom->next) {for(int a=0; a<60; a++) {printf("%c",pom->fakt[a]);}printf("\n");}printf("KONIEC!!!!\n");
		actions = deleteFact(actions, allFacts); /// vymaze vsetky fakty, ktore su uz zahrnute v allFacts
		// FACTS *pom3 = actions;for( ; pom3->next != NULL; pom3 = pom3->next) {for(int a=0; a<40; a++) {printf("%c",pom3->fakt[a]);}printf("\n");}printf("KONIEC 222222!!!!\n");
		if (!empty(actions))
			allFacts = apply(actions, allFacts);  // pridanie noveho faktu z vystupu prvej akcie v rade do mnoziny vsetkych faktov
		else
            break;                  // program konci

        pomAllRules = allRules;     // obnovenie pravidiel
		print(allFacts);
		scanf("%c", &c);
	}
	return 0;
}
