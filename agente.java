/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topicos1;

import java.util.Scanner;

/**
 *
 */
public class agente {

    /**
     * @param args the command line arguments
     */
    static boolean terminar = false, confundido = false, continuarPlatica = false, flag = true;
    static int emocion = 5, paciencia=5;
    static String usuario = "", respuesta = "";
    
    static int index = 0, palabraIndex = 0;
    static String respuestaAgente = "Agente>> ";

    public static void main(String[] args) {
        // TODO code application logic here

        Scanner escaner = new Scanner(System.in);
        System.out.println("Agente>> Hola, ¿como te llamas?");
        System.out.println(">>");
        System.out.print("");
        usuario = escaner.nextLine();
        System.out.println("Agente>> Mucho gusto " + usuario + ", ¿en qué puedo ayudarte?");
        System.out.println(">>");
        respuesta = escaner.nextLine();
        //System.out.println(""+respuesta);

        System.out.println(evaluar(respuesta));
        //System.out.println(evaluar(respuesta));
             //   System.out.println("?En que mas puedo ayudarte?");
        //respuesta=escaner.nextLine();

        /*System.out.println("?Algo mas en lo que pueda ayudarte?");
         respuesta=escaner.nextLine();
         System.out.println(evaluar(respuesta));*/
        while (!terminar) {
            palabraIndex = 0;
            respuestaAgente = "Agente>> ";
            index = 0;
            if (continuarPlatica) {
                if (confundido) {
                    System.out.println(generarPlaticaConfundido());
                    confundido = false;
                    flag = true;
                } else {
                    System.out.println(generarPlatica());
                }
            } else {
                System.out.print("");
                continuarPlatica = true;
            }
            System.out.println(">>");
            respuesta = escaner.nextLine();
            respuestaAgente = "Agente>> ";
            System.out.println(evaluar(respuesta));
        }

    }

    public static String evaluar(String res) {
        //System.out.println("metodo evaluar inicia");       
        while (index < res.length()) {
            //System.out.println("dentro de while en evaluar index: "+index);
            setRespuesta(hacerPalabra(res));
            index++;
        }
        //System.out.println("fuera de while en evaluar index: "+index);
        return respuestaAgente;
    }

    public static void setRespuesta(String palabra) {
        switch (palabra) {
            case "servicio":
                respuestaAgente += " Mira " + usuario + "..."
                        + "Esta es la información que se "
                        + "con respecto a  SERVICIO SOCIAL.";
                flag = false;
                break;
            case "odio":
                respuestaAgente+="¿Por qué me dices eso "+usuario+"?"+
                                                "  yo sólo he querido ayudarte.";
                break;
            case "residencias":
                respuestaAgente += " Pues fíjate que "
                        + "esto es lo que se  "
                        + "acerca de RESIDENCIAS, " + usuario + ".";
                flag = false;
                break;
            case "pudrete":
                respuestaAgente += " Oye, púdrete tú, " + usuario + ".";
                flag = false;
                break;
            case "quiero":
                respuestaAgente += " Déjame ver si puedo ayudarte...";
                flag = false;
                break;
            case "dime":
            case "dame":
                respuestaAgente += " Mmm, ¿qué puedo decirte?";
                flag = false;
                break;
            case "nada":
            //case "no":
            case "adios":
                respuestaAgente += " Ok, bye " + usuario + ".";
                flag = false;
                terminar = true;
                break;
            case "si":
            case "ok":
            case "claro":
                respuestaAgente += " Bueno, dime " + usuario + ". ¿Qué es?";
                continuarPlatica = false;
                flag = false;
                break;
            default:
                if ((flag && palabraIndex != 1 && index == respuesta.length()) || ((index == respuesta.length()) && palabraIndex == 1)) {
                   
                    if (((int) Math.floor(Math.random() * (2 - 1 + 1)) + 1) == 1) {
                        respuestaAgente += " Perdón " + usuario + " pero no te entendí.";
                    } else {
                        respuestaAgente += " Lo siento, no te entiendo " + usuario + ".";
                    }
                    confundido = true;
                }
                break;
        }
    }

    public static String generarPlatica() {
        switch ((int) Math.floor(Math.random() * (3 - 1 + 1)) + 1) {
            case 1:
                if (((int) Math.floor(Math.random() * (3 - 1 + 1)) + 1) == 1) {
                    respuestaAgente += "¿Algo más en lo que pueda ayudarte?";
                } else {
                    respuestaAgente += "Y ahora, ¿de qué platicamos?";
                }
                break;
            case 2:
                respuestaAgente += "Si hay otra cosa que quieras preguntarme, hazlo.";
                break;
            case 3:
                respuestaAgente += "Espero que eso responda tu pregunta " + usuario + ". ¿Quá más quieres saber?";
                break;
        }
        return respuestaAgente;
    }

    public static String generarPlaticaConfundido() {
        paciencia--;
        if(paciencia<1){
            switch ((int) Math.floor(Math.random() * (4 - 1 + 1)) + 1) {
            case 1:
                respuestaAgente += "Ya estuvo bueno, escribe bien.";
                break;
            case 2:
                respuestaAgente += "Me estoy desesperando. ";
                break;
            case 3:
                respuestaAgente += "Tal vez si supieras escribir te entendería...";
                break;
            case 4:
                respuestaAgente+="Me estresas...";
                break;
        }
        }
        else{
        switch ((int) Math.floor(Math.random() * (3 - 1 + 1)) + 1) {
            case 1:
                respuestaAgente += "¿Puedo intentar ayudarte de nuevo? Quizá esta vez te entienda.";
                break;
            case 2:
                respuestaAgente += "¿Puedes repetirme tu duda? ";
                break;
            case 3:
                respuestaAgente += "Intentémoslo de nuevo... ";
                break;
        }}
        

        return respuestaAgente;
    }

    public static String hacerPalabra(String texto) {
        String character = texto.charAt(index) + "";
        String palabra = "";
        while (!(character.equals(" ")) && !(character.equals(","))) {
            // System.out.println("dentro de while en hacerPalabra index: "+index);
            palabra = palabra + texto.charAt(index);
            index++;
            if (index < texto.length()) {
                character = texto.charAt(index) + "";
            } else {
                break;
            }
        }
        palabraIndex++;
        return palabra;
    }
}
