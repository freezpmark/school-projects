import time
from random import randint, random
import random

from faker import Factory
fake = Factory.create()

f1 = open('./testfile3.csv', 'w')
start = time.time()

j = 0
#print('id, meno, priezvisko, tel_cislo, email' ,file=f1)
for i in range(0, 500000):
    #print(i + 1, ',', random.randint(1, 1000), ',', fake.date(pattern=" %Y-%m-%d"), sep='', file=f1) teaches
    #print(i + 1, ',', random.randint(1, 1000), ',', fake.date(pattern=" %Y-%m-%d"), sep='', file=f1)
    #print(i + 1, ',', random.randint(1, 1000), ',', fake.date(pattern=" %Y-%m-%d"), ',', random.randint(1, 5), sep='', file=f1) studies
    #print(i + 1, ',', random.randint(1, 1000), ',', fake.date(pattern=" %Y-%m-%d"), ',', random.randint(2, 5), sep='', file=f1)
    #print(i + 1, ',', random.randint(1, 1000), ',', fake.date(pattern=" %Y-%m-%d"), ',', random.randint(2, 5), sep='', file=f1)
    #print(i + 1, ',', random.randint(1, 1000), ',', fake.date(pattern=" %Y-%m-%d"), ',', random.randint(3, 5), sep='', file=f1)
    print(i+1, ',', random.randint(1,10000), ',', random.randint(1, 5), file=f1)
    #print(i + 1,',', random.randint(1, 1000),',', fake.bs(), sep='', file=f1) EXAM
    #print(i + 1, ';', random.randint(1, 7), ';', fake.job(), ';', fake.paragraph(nb_sentences=3, variable_nb_sentences=True), sep='', file=f1) SUBJECT
    #print(i + 1, ',', random.randint(1,100000),',', fake.first_name(),',', fake.last_name(),',', fake.date(pattern="%Y-%m-%d"),',', fake.email(),',', random.randint(600,1400), file=f1) TEACHERS
    #print(i + 1, ',', random.randint(1, 100), ',', randint(10, 50), ',', fake.military_state(), i + 1,sep='', file=f1) CLASSROOM
    #print(i + 1,',', fake.company_suffix(), fake.last_name(), fake.city_suffix(), ',', fake.street_address(),',', fake.phone_number(),sep='', file=f1) SCHOOL
    #print(i + 1, fake.first_name(), fake.last_name(), fake.email(), sep=',', file=f1) STUDENT

end = time.time()
print(end - start)