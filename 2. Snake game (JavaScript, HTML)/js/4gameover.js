
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



// drawImage po druhy krat len da
// tlacidlo na konci
