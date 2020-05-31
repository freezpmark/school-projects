window.onload = function()
{
//UKAZAT -> pohyblivy objekt? co s jahodou..

    var canvas = document.createElement('canvas'),
        ctx = canvas.getContext('2d');
        canvas.width = 800;
        canvas.height = 440;

    var body = document.getElementsByTagName('body')[0]; //CANVAS STUFF
    body.appendChild(canvas);


        var menu = new Image();
        menu.src = "images/menu.png";
        ctx.drawImage(menu, 40, 40, 200, 200);

window.onclick = HrajHru;
function HrajHru()
{

{
//KONSTANTY
    var Dlzka_hada = 3,
        Aktivnost = true,
        Rychlost = 375,
        Skore = 0,
        Lvl = 1,
        Smer = 0,
        keybind = 0;
        Had = new Array(3);

// MATRIX
    var Mapa = new Array(40);                     
    for (var i = 0; i < Mapa.length; i++) {
        Mapa[i] = new Array(20);
    }

//GENEROVANIE MAPY
    Mapa = generateHad(Mapa);

//GENEROVANIE JEDLA NA ZACIATKU
    for(var i = 0; i < 4; i++)
        Mapa = generateCeresna(Mapa);

    for(var i = 0; i < 2; i++)
        Mapa = generateJahoda(Mapa);

    for(var i = 0; i < 2; i++)
        Mapa = generateJablko(Mapa);


//HRA ZACINA
    drawGame();
    var begin = new Audio("sound/start.mp3");
        begin.play();


    function drawGame()
    {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        keybind = 0; // fast left and right
        window.addEventListener('keydown', function(e) {
            if ((e.keyCode === 38 && Smer !== 3) && keybind !== 1) {
                Smer = 2; // hore
                keybind++;
            } else if ((e.keyCode === 40 && Smer !== 2) && keybind !== 1) {
                Smer = 3; // dole
                keybind++;
            } else if ((e.keyCode === 37 && Smer !== 0) && keybind !== 1) {
                Smer = 1; // vlavo
                keybind++;
            } else if ((e.keyCode === 39 && Smer !== 1) && keybind !== 1) {
                Smer = 0; // vpravo
                keybind++;
            }
        });

    //POHYB HADA URCUJUCA SMEROM, zacina od poslednej casti hada
        for (var i = Had.length - 1; i >= 0; i--) {
            if (i === 0) {

            //POHYB HADA URCUJUCA SMEROM
                switch(Smer) {
                    case 0: // vpravo
                        Had[0] = { x: Had[0].x + 1, y: Had[0].y }
                        break;
                    case 1: // vlavo
                        Had[0] = { x: Had[0].x - 1, y: Had[0].y }
                        break;
                    case 2: // hore
                        Had[0] = { x: Had[0].x, y: Had[0].y - 1 }
                        break;
                    case 3: // dole
                        Had[0] = { x: Had[0].x, y: Had[0].y + 1 }
                        break;
                }

        // KOLIZIE NA KONIEC MAPY
            if (Had[0].x < 0 ||
                Had[0].x >= 40 ||                   
                Had[0].y < 0 ||
                Had[0].y >= 20) {
                    showGameOver();
                    return;
            }

        // NAJDENIE JEDLA
            if (((Mapa[Had[0].x][Had[0].y] === 1) || (Mapa[Had[0].x][Had[0].y] === 2)) || (Mapa[Had[0].x][Had[0].y] === 3)) {
                Dlzka_hada++;
                var zjedenie = new Audio("sound/jedlo.mp3");
                    zjedenie.play();

                Had.push({ x: Had[Had.length - 1].x, y: Had[Had.length - 1].y });  // Vytvori dalsiu cast hada

                // DUNNO WHY -> Mapa[Had[Had.length - 1].x][Had[Had.length - 1].y] = 2;            // vlozi 2 do casti, ktora je o jedna pozadu
            

                if (Mapa[Had[0].x][Had[0].y] === 1) {    // Ceresna
                    Skore += Lvl*10;
                    Mapa = generateCeresna(Mapa);
                }

                if (Mapa[Had[0].x][Had[0].y] === 2) {   // Jahoda
                    Lvl -= 0.5;
                    Skore += Lvl*10;
                    Mapa = generateJahoda(Mapa);
                }

                if (Mapa[Had[0].x][Had[0].y] === 3) {   // Jablko
                    var levelup = new Audio("sound/levelup.mp3");
                        levelup.play();
                        Lvl++;
                        Mapa = generateJablko(Mapa);
                }
            }

            // PODMIENKY NA VYHRU
            if (Dlzka_hada == 250) {
                showGamewon();
                return;
            }

            if (Lvl === 15) {
                showGamewon();
                return;
            }

            if (Skore >= 10000) {
                showGamewon();
                return;
            }



        // NARAZ DO SEBA
            if ((Mapa[Had[0].x][Had[0].y] === 6) || (Mapa[Had[0].x][Had[0].y] === 5)) {
                showGameOver();
                return;
                
            }

            Mapa[Had[0].x][Had[0].y] = 4;                               // Hlava vzdy na zaciatku
            Mapa[Had[Had.length - 1].x][Had[Had.length - 1].y] = 6;     // Chvost vzdy na konci


            } else {
                if (i === (Had.length - 1))         // Za chvost da 0
                    Mapa[Had[i].x][Had[i].y] = 0;

            
                Had[i] = { x: Had[i - 1].x, y: Had[i - 1].y };        // -1 v (x && y) znamena posunutie pozicie pred nim a
                Mapa[Had[i].x][Had[i].y] = 5;                         // spravi telo
            }
        }

        drawMain();                                                 // nakresli border aj so skore

    // GRAFIKA <1,6>
        for (var x = 0; x < Mapa.length; x++) {
            for (var y = 0; y < Mapa[0].length; y++)  {
                if (Mapa[x][y] === 1) { 
                    var ceresna = new Image();
                    ceresna.src = "images/ceresna.png";
                    ctx.drawImage(ceresna, x * 20, y * 20 + 40, 20, 20);
                } else if (Mapa[x][y] === 2) {
                    var jahoda = new Image();
                    jahoda.src = "images/jahoda.png";
                    ctx.drawImage(jahoda, x * 20, y * 20 + 40, 20, 20);
                } else if (Mapa[x][y] === 3) {
                    var jablko = new Image();
                    jablko.src = "images/jablko.png";
                    ctx.drawImage(jablko, x * 20, y * 20 + 40, 20, 20);

                } else if (Mapa[x][y] === 5) {      //TELO
                    ctx.beginPath();
                    ctx.arc(x * 20 + 10, y * 20 + 50, 10, 0, 2 * Math.PI);
                    ctx.fillStyle = 'yellow';
                    ctx.fill();
                    ctx.strokeStyle = 'black';
                    ctx.stroke();
                } else if (Mapa[x][y] === 4) {      //HLAVA
                    if (Smer === 2) { // HORE
//HLAVA
                        ctx.beginPath();
                        ctx.arc(x * 20 + 10, y * 20 + 50, 10, 0, 2 * Math.PI);
                        ctx.fillStyle = 'orange';
                        ctx.fill();
                        ctx.strokeStyle = 'black';
                        ctx.stroke();
//OCI
                        ctx.beginPath();
                        ctx.arc(x * 20 + 7, y * 20 + 43, 2, 0, 2 * Math.PI, false);
                        ctx.fillStyle = "black";
                        ctx.fill();
                        ctx.beginPath();
                        ctx.arc(x * 20 + 13, y * 20 + 43, 2, 0, 2 * Math.PI, false);
                        ctx.fillStyle = "black";
                        ctx.fill();
                    }
                    if (Smer === 3) { // DOLE
//HLAVA
                        ctx.beginPath();
                        ctx.arc(x * 20 + 10, y * 20 + 50, 10, 0, 2 * Math.PI);
                        ctx.fillStyle = 'orange';
                        ctx.fill();
                        ctx.strokeStyle = 'black';
                        ctx.stroke();
//OCI
                        ctx.beginPath();
                        ctx.arc(x * 20 + 7, y * 20 + 47, 2, 0, 2 * Math.PI, false);
                        ctx.fillStyle = "black";
                        ctx.fill();
                        ctx.beginPath();
                        ctx.arc(x * 20 + 13, y * 20 + 47, 2, 0, 2 * Math.PI, false);
                        ctx.fillStyle = "black";
                        ctx.fill();
//USMEV
                        ctx.beginPath();
                        ctx.moveTo(x * 20 + 5, y * 20 + 53);
                        ctx.quadraticCurveTo(x * 20 + 10 , y * 20 + 55 , x * 20 + 15, y * 20 + 53);
                        ctx.strokeStyle = 'black';
                        ctx.stroke();
                    }
                    if (Smer === 1) { // DOLAVA
//HLAVA
                        ctx.beginPath();
                        ctx.arc(x * 20 + 10, y * 20 + 50, 10, 1.1 * Math.PI, 2.1 * Math.PI, false);
                        ctx.fillStyle = "orange";
                        ctx.fill();
                        ctx.strokeStyle = 'black';
                        ctx.stroke();

                        ctx.beginPath();
                        ctx.arc(x * 20 + 10, y * 20 + 50, 10, 1.9 * Math.PI, 0.9 * Math.PI, false);
                        ctx.fillStyle = "orange";
                        ctx.fill();
                        ctx.strokeStyle = 'black';
                        ctx.stroke();
//OKO
                        ctx.beginPath();
                        ctx.arc(x * 20 + 10, y * 20 + 45, 2, 0, 2 * Math.PI, false);
                        ctx.fillStyle = "black";
                        ctx.fill();
                    }
                    if (Smer === 0) { // DOPRAVA
                        ctx.beginPath();
                        ctx.arc(x * 20 + 10, y * 20 + 50, 10, 0.1 * Math.PI, 1.1 * Math.PI, false);
                        ctx.fillStyle = "orange";
                        ctx.fill();
                        ctx.strokeStyle = 'black';
                        ctx.stroke();
//OCI
                        ctx.beginPath();
                        ctx.arc(x * 20 + 10, y * 20 + 50, 10, 0.9 * Math.PI, 1.9 * Math.PI, false);
                        ctx.fillStyle = "orange";
                        ctx.fill();
                        ctx.strokeStyle = 'black';
                        ctx.stroke();

                        ctx.beginPath();
                        ctx.arc(x * 20 + 10, y * 20 + 45, 2, 0, 2 * Math.PI, false);
                        ctx.fillStyle = "black";
                        ctx.fill();
                    }

                } else if (Mapa[x][y] === 6) {   // CHVOST
                    ctx.beginPath();
                    ctx.arc(x * 20 + 10, y * 20 + 50, 7.5, 0, 2 * Math.PI);
                    ctx.fillStyle = '#FFFF77';
                    ctx.fill();
                    ctx.strokeStyle = 'black';
                    ctx.stroke();
                }
            } // DRUHY FOR
        } // PRVY FOR
        if (Aktivnost) {
            setTimeout(drawGame, Rychlost - (Lvl * 25.25));
        }
    }


    function drawMain()
    {
    var trava = new Image();
    trava.src = "images/trava.jpg";
    ctx.drawImage(trava,0, 40, canvas.width, canvas.height - 40); 

    ctx.beginPath();
    ctx.fillStyle = 'black';
    ctx.fillRect(0, 0, canvas.width, 40)
    ctx.stroke();

    ctx.fillStyle = 'white';        
    ctx.font = '18px sans-serif';
    ctx.fillText('Skore: ' + Skore + ' /10000     Lvl: ' + Lvl + ' /15     Dlzka hada: ' + Dlzka_hada + ' /250', 5, 25);
    }


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


    function showGameOver()
    {
        var naraz = new Audio("sound/naraz.mp3");
            naraz.play();

        // ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.beginPath();
        ctx.fillStyle = 'black';
        ctx.fillRect(0, 0, canvas.width, canvas.height)
        ctx.stroke();
    

        ctx.fillStyle = 'red';
        ctx.font = '40px sans-serif';
        ctx.fillText('Game Over!', ((canvas.width / 2) - (ctx.measureText('Game Over!').width / 2)), 100);       // GAME OVER VYPIS

        ctx.fillStyle = 'white';
        ctx.font = '20px sans-serif';
        ctx.fillText('Skore: ' + Skore, 375, 150);       // SKORE VYPIS
        ctx.fillText('Level: ' + Lvl, 375, 180);         // LEVEL VYPIS
        ctx.fillText('Dlzka: ' + Dlzka_hada, 375, 210);         // DLZKA HADA VYPIS

        ctx.fillStyle = 'yellow';
        ctx.font = '50px sans-serif';
        ctx.fillText('Click to play again', 200, 300);         // DLZKA HADA VYPIS

        var lose = new Image();
        lose.src = "images/lose.jpg";
        ctx.drawImage(lose, 40, 40, 200, 200); 


        Aktivnost = false;
        window.onclick = HrajHru;
    }


    function showGamewon()
    {
        var vyhra = new Audio("sound/vyhra.mp3");
            vyhra.play();
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        ctx.beginPath();
        ctx.fillStyle = 'black';
        ctx.fillRect(0, 0, canvas.width, canvas.height)
        ctx.stroke();

        ctx.fillStyle = 'green';
        ctx.font = '40px sans-serif';
        ctx.fillText('You have won!', ((canvas.width / 2) - (ctx.measureText('You have won!').width / 2)), 100);

        ctx.fillStyle = 'white';
        ctx.font = '20px sans-serif';
        ctx.fillText('Skore: ' + Skore, 375, 150);
        ctx.fillText('Level: ' + Lvl, 375, 180);
        ctx.fillText('Dlzka: ' + Dlzka_hada, 375, 210);

        ctx.fillStyle = 'yellow';
        ctx.font = '50px sans-serif';
        ctx.fillText('Click to play again', 200, 300);         // DLZKA HADA VYPIS

        var win = new Image();
        win.src = "images/win.jpg";
        ctx.drawImage(win, 40, 40, 200, 200);

        Aktivnost = false;
        window.onclick = HrajHru;
    }
}

}
// drawImage po druhy krat len da
// tlacidlo na konci





};









