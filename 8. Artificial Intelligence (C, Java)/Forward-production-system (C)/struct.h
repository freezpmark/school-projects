typedef struct facts
{
	char fakt[40];
	facts *next;
}FACTS;

typedef struct rule
{
	char name[20];
	char cond[80];
	char action[80];
	rule *next;
}RULE;
