window.onload = function()
{
//UKAZAT -> pohyblivy objekt? co s jahodou..



    var canvas = document.createElement('canvas'),
        ctx = canvas.getContext('2d');
        canvas.width = 800;
        canvas.height = 440;

    var body = document.getElementsByTagName('body')[0]; //CANVAS STUFF
    body.appendChild(canvas);


/* why its not working
    var trava = new Image();
    trava.src = "images/trava.jpg";
    ctx.drawImage(trava,0, 40, canvas.width, canvas.height - 40); 
*/


/*
GAME MENU (start, how to play)
+ TRY AGAIN BUTTON AFTER LOSE
*/

// IF nieco {}


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
}