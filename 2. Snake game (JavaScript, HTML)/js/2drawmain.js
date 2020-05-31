

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

