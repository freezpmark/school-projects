
    function generateFood(Mapa)
    {
        var sX = Math.round(Math.random() * 38), 
            sY = Math.round(Math.random() * 19); 
        while ((Mapa[sX][sY] === 4) || (Mapa[sX][sY] === 5) || (Mapa[sX][sY] === 6)) {              // Dava pozor aby sa nevygenerovalo na hadovom tele
            sX = Math.round(Math.random() * 38);
            sY = Math.round(Math.random() * 19);
        }
        var vyber_jedlo = Math.round(Math.random() * 30)
        if (vyber_jedlo < 5)
            Mapa[sX][sY] = 3; // Jablko
        else if (vyber_jedlo < 10)
            Mapa[sX][sY] = 2; // Jahoda
        else
            Mapa[sX][sY] = 1; // Ceresna
        return Mapa;
    }




   function generateCeresna(Mapa)
    {
        var sX = Math.round(Math.random() * 38), 
            sY = Math.round(Math.random() * 19); 
        while ((Mapa[sX][sY] === 4) || (Mapa[sX][sY] === 5) || (Mapa[sX][sY] === 6)) {              // Dava pozor aby sa nevygenerovalo na hadovom tele
            sX = Math.round(Math.random() * 38);
            sY = Math.round(Math.random() * 19);
        }
        Mapa[sX][sY] = 1;
        return Mapa;
    }

    function generateJahoda(Mapa)
    {
        var sX = Math.round(Math.random() * 38), 
            sY = Math.round(Math.random() * 19); 
        while ((Mapa[sX][sY] === 4) || (Mapa[sX][sY] === 5) || (Mapa[sX][sY] === 6)) {              // Dava pozor aby sa nevygenerovalo na hadovom tele
            sX = Math.round(Math.random() * 38);
            sY = Math.round(Math.random() * 19);
        }
        Mapa[sX][sY] = 2;
        return Mapa;
    }

    function generateJablko(Mapa)
    {
        var sX = Math.round(Math.random() * 38), 
            sY = Math.round(Math.random() * 19); 
        while ((Mapa[sX][sY] === 4) || (Mapa[sX][sY] === 5) || (Mapa[sX][sY] === 6)) {              // Dava pozor aby sa nevygenerovalo na hadovom tele
            sX = Math.round(Math.random() * 38);
            sY = Math.round(Math.random() * 19);
        }
        Mapa[sX][sY] = 3;
        return Mapa;
    }


    function generateHad(Mapa)
    {
        var sX = Math.round(Math.random() * 20),
            sY = Math.round(Math.random() * 10);

        while ((sX - Had.length) < 0) {           // Dava pozor na to, aby sa zmestili aj 2 dalsie casti hada
            sX = Math.round(Math.random() * 20);
        }
        
        for (var i = 0; i < Had.length; i++) {      // Vlozenie dvojky do 3 casti hada
            Had[i] = { x: sX - i, y: sY };
            Mapa[sX - i][sY] = 5;
        }
        return Mapa;
    }
