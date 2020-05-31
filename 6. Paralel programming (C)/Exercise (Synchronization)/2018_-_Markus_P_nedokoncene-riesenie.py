from threading import RLock, Condition, Thread
from time import sleep

mutexM = RLock()
mutexO = RLock()
condM = Condition(mutexM)
condO = Condition(mutexO)
condBarr = Condition(mutexO)

count       = 0            # zap -> maluje sa, kladny pocet -> obraz je pozerany
counterM    = 0            # pocet zmien na stojane
order       = 0            # poradie maliarov
pref        = 0            # preferencia obdivovatelov nad maliarmi
paintNum    = 3            # pocet maliarov
bCounter    = 0
inBarr      = 0

# signal na zastavenie
stoj = 0

'''
    cond.wait()
    cond.notify_all()
    cond.notify()
    mutex.acquire()
    mutex.release()

    from threading import RLock, Condition, Thread
    from time import sleep
    mutex.aquire()/.release()
    thread.start()/join()
    creatujes tusim nejake Thread() a argumenty
    konstruktor volas
    cond.wait()/notify_all()
'''

def maliar_zmen():
    sleep(3)

def maliar_maluj():
    sleep(5)

def maliar(id):

    global count, counterM, paintNum, order
    #global condM, condO, condBarr

    while not stoj:

        mutexM.acquire()
        while count > 0 or id != order or pref:      # obdivovatel pozera, nieje na rade, posledny ukon obdivovatela je cakanie
            if stoj: 
                break
            condM.wait()
        if stoj:
            break

        count -= 1
        order = (order + 1) % paintNum                  
        print("                    {}. maliar maluje..".format(id))
        maliar_zmen()                                  
        print("                    {}. maliar domaloval.. nasleduje {}".format(id, order))
        counterM += 1

        count += 1
        condO.notify_all()
        mutexM.release()

        if stoj:
            break
        print("                    {}. maliar pripravuje dalsi obraz".format(id))
        maliar_maluj()                                 
        print("                    {}. maliar je pripraveny menit obraz".format(id))

    condM.notify_all()
    mutexM.release()

def obdivovatel_pozeraj():
    sleep(1)

def obdivovatel_prestavka():
    sleep(2)

def obdivovatel(id):
    global count, pref, bCounter, inBarr

    while not stoj:
        mutexO.acquire()
        while count < 0 or count >= 4:      # priravuje sa obraz, max 4 sa zmestia pri obraze
            if stoj:
                break
            pref = 1
            condO.wait()
        pref = 0
        count += 1
        mutexO.release()                    # broadcast condO u maliara zaruci aby sa tu nemuselo signalizovat dalsim obdivovatelom

        if stoj:
            break                                
        print("{}. obdivovatel zacina prezerat obraz..".format(id))
        obdivovatel_pozeraj()

        mutexO.acquire()                    # ide sa do bariery atomicky
        condO.notify()                      # po pozerani posle signal dalsiemu obdivovatelovi
        count -= 1                          # znizi pocet aktualne pozerajucich

        bCounter += 1
        if bCounter == 8:
             inBarr = 1                                
             print("{} zvola: IDEME NA PRESTAVKU!".format(id))
             condBarr.notify_all() 
        else:
            while not inBarr:
                if stoj:
                    break                        
                print("{}. obdivovatel caka..".format(id))
                condBarr.wait()
        bCounter -= 1
        if bCounter == 0:
            inBarr = 0
            condBarr.notify_all()
        else:
            while inBarr:
                if stoj:
                    break
                condBarr.wait()
        mutexO.release()                 
        
        print("{}. obdivovatel si dava prestavku..".format(id))
        condM.notify()                      # pred prestavkou sa posle signal maliarovi
        if stoj:
            break
        obdivovatel_prestavka()

    condO.notify_all()
    condBarr.notify_all()
    mutexO.release()

def main():
    tm = []
    for i in range(0, 3):
        t1 = Thread(target = maliar, args = (i,))
        tm.append(t1)
        print("created maliar")
    to = []
    for i in range(0, 8):
        t2 = Thread(target = obdivovatel, args = (i,))
        to.append(t2)
        print("created obdivovatela")
    
    #for i in range(0, 8):
    #    to[i].start()
    for i in range(0, 3):
        tm[i].start()
    for i in range(0, 8):
        to[i].start()

    sleep(6)
    stoj = 1

    for i in range(0, 3):
        tm[i].join()
    for i in range(0, 8):
        to[i].join()

    print("Pocet zmien obrazov na stojane: {}".format(counterM))


main()