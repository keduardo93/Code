function llenaLaTabla() {

		c1=0;
		c2=0;
		c3=0;
		c4=0;
		c5=0;
		c6=0;
		c7=0;
		c8=0;
		c9=0;
		c10=0;
		c11=0;
		c12=0;
		c13=0;
		c14=0;
		c15=0;
		c16=0;
		c17=0;
		c18=0;
		c19=0;
		c20=0;

	if(document.getElementById) {
		for(var i = 0; i < 16; i++) {
			
			fijaElValorDelCuadro(i);
		}
	} else {
		alert("Su navegador no soporta Java Script");
	}
}

var numerosUsados = new Array(55);

function fijaElValorDelCuadro(Cuadro) {
	s="";
	
	var newNum;
	idCuadro = "square" + Cuadro;


do {
        
		newNum = Math.ceil(Math.random() * 54);
    } while (numerosUsados[newNum] );

	numerosUsados[newNum] = true;
	document.getElementById(idCuadro).innerHTML = "<img src='Loteria/Loteria" + newNum + ".PNG' width='90px' height='100px'"+'onclick="seleccionar('+idCuadro+');"'+">";
}


function seleccionar(id){
	var x = id.id;
	document.getElementById(x).innerHTML = '<img src="Loteria/blanco.png">';
	checkWin(x);


}

function checkWin(x){

 s=x; 
 avance = s.split("square");

checkArr();
	}

	function checkArr(){
		
		var check1 = ["0", "4", "8","12"]; //24
		var check2 = ["1", "5", "9","13"];
		var check3 = ["2", "6", "10","14"];
		var check4 = ["3", "7", "11","15"];
		
		var check5 = ["0", "1", "2","3"];
		var check6 = ["4", "5", "6","7"];
		var check7 = ["8", "9", "10","11"];
		var check8 = ["12", "13", "14","15"];
		
		var check9 = ["0", "3", "12","15"];
		
		var check10 = ["0", "5", "10","15"];
		var check11 = ["3", "6", "9","12"];
		
		var check12 = ["0", "1", "4","5"];
		var check13 = ["4", "5", "8","9"];
		var check14 = ["8", "9", "12","13"];

		var check15 = ["1", "2", "5","6"];
		var check16 = ["5", "6", "9","10"];
		var check17 = ["9", "10", "13","14"];

		var check18 = ["2", "3", "6","7"];
		var check19 = ["6", "7", "10","11"];
		var check20 = ["10", "11", "14","15"];



		//alert(avance.length);
		for(u=1; u<avance.length; u++){
			var aux = avance[u];
			

			for(v=0; v<check1.length; v++){
				if(aux==check1[v]){c1=c1+1;}

			}
			for(v=0; v<check2.length; v++){
				if(aux==check2[v]){c2=c2+1;}

			}
			for(v=0; v<check3.length; v++){
				if(aux==check3[v]){c3=c3+1;}

			}
			for(v=0; v<check4.length; v++){
				if(aux==check4[v]){c4=c4+1;}

			}
			for(v=0; v<check5.length; v++){
				if(aux==check5[v]){c5=c5+1;}

			}
			for(v=0; v<check6.length; v++){
				if(aux==check6[v]){c6=c6+1;}

			}
			for(v=0; v<check7.length; v++){
				if(aux==check7[v]){c7=c7+1;}

			}
			for(v=0; v<check8.length; v++){
				if(aux==check8[v]){c8=c8+1;}

			}
			for(v=0; v<check9.length; v++){
				if(aux==check9[v]){c9=c9+1;}

			}
			for(v=0; v<check10.length; v++){
				if(aux==check10[v]){c10=c10+1;}

			}
			for(v=0; v<check11.length; v++){
				if(aux==check11[v]){c11=c11+1;}

			}
			for(v=0; v<check12.length; v++){
				if(aux==check12[v]){c12=c12+1;}

			}
			for(v=0; v<check13.length; v++){
				if(aux==check13[v]){c13=c13+1;}

			}
			for(v=0; v<check14.length; v++){
				if(aux==check14[v]){c14=c14+1;}

			}
			for(v=0; v<check15.length; v++){
				if(aux==check15[v]){c15=c15+1;}

			}
			for(v=0; v<check16.length; v++){
				if(aux==check16[v]){c16=c16+1;}

			}
			for(v=0; v<check17.length; v++){
				if(aux==check17[v]){c17=c17+1;}

			}
			for(v=0; v<check18.length; v++){
				if(aux==check18[v]){c18=c18+1;}

			}
			for(v=0; v<check19.length; v++){
				if(aux==check19[v]){c19=c19+1;}

			}
			for(v=0; v<check20.length; v++){
				if(aux==check20[v]){c20=c20+1;}

			}

		}

		if(c1==4 || c2==4 || c3==4 || c4==4 || c5==4 || c6==4 || c7==4 || c8==4 || c9==4 || c10==4 || c11==4 || c12==4 || c13==4 || c14==4 || c15==4 || c16==4 || c17==4 || c18==4 || c19==4 || c20==4 ){
			
			document.getElementById("resultado").innerHTML="<h1>FELICIDADES GANASTEEEEE</h1>"
			
		}
		
		


	}





window.onload = llenaLaTabla;