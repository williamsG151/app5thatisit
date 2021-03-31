/** @author Ahmed Khoumsi */

import java.util.ArrayList;

/** Cette classe effectue l'analyse lexicale
 */
public class AnalLex {

  String sentence;
  int length;
  int index = 0;
  int state = 0;
  Terminal result = null;
  String UL = "";
  String type = null;
  private Boolean error = false;

  //Expressions régulières
  String IDm = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  String ID = "abcdefghijklmnopqrstuvwxyz_";
  String C = "0123456789";
  String S = "+*/-";
  String P = "()";

  ArrayList<Terminal> terminalChain;


  /**
   * Constructeur pour l'initialisation d'attribut(s)
   */
  public AnalLex(String sentence) {
    this.sentence = sentence.replaceAll("\\s+", "");
    length = this.sentence.length();
    terminalChain = new ArrayList<>();
  }


  /**
   * resteTerminal() retourne :
   * false  si tous les terminaux de l'expression arithmetique ont ete retournes
   * true s'il reste encore au moins un terminal qui n'a pas ete retourne
   */
  public boolean resteTerminal() {
    return !((index >= length) || error );
  }

  /**
   * prochainTerminal() retourne le prochain terminal
   * Cette methode est une implementation d'un AEF
   */
  public Terminal prochainTerminal() {

    while(state != -1){
      if(index == length){
        index--;
        state = 99;
      }
      String current = String.format("%c", sentence.charAt(index));
      if (state == 0) {
        if (C.contains(current)) {
          type = "C";
          state = 1;
          UL += current;
          index++;
        }else if(S.contains(current)){
          if("*/".contains(current)){
            type = "S1";
          }else{
            type = "S2";
          }
          state = 99;
          UL += current;
        }else if(P.contains(current)){
          if("(".equals(current)){
            type = "Pg";
          }else{
            type = "Pd";
          }
          state = 99;
          UL += current;
        }else if(IDm.contains(current)){
          type = "ID";
          state = 2;
          UL += current;
          index++;
        }else{
          dealWithError(current);
        }
      } else if (state == 99) {
        closeUL();
      }else if(state == 1){   // this state is for numbers
        if(C.contains(current)){
          UL += current;
          index++;
        }else{
          state = 99;
          index--;
        }
      }else if(state == 2){     // this state is for identifiants and is a final state
        if(ID.contains(current) || IDm.contains(current)){
          if(current.contains("_")){
            state = 3;
            if(index == length-1){    // this statement is only here to deal with the eventuality that the last character of the last lexical unit being an identifiant finishing with "_"
              dealWithError(current);
            }
          }
          UL += current;
          index++;
        }else{
          state = 99;
          index--;
        }
      }else if(state == 3){    // this state is for dealing with "_" in identifiant type only
        if( (ID.contains(current) || IDm.contains(current)) && !current.contains("_")){
          UL += current;
          index++;
          state = 2;
        }else{
          dealWithError(current);
        }
      }
    }
    resetValues();
    return result;
  }

  private void closeUL(){
    result = new Terminal(UL, type);
    terminalChain.add(result);
    state = -1;
    index++;
  }

  private void dealWithError(String current){
    erreurLex(current);
    result = new Terminal("ERROR", type);
    state = -1;
    error = true;
  }

  private void resetValues(){
    UL = "";
    state = 0;
  }

  /**
   * ErreurLex() envoie un message d'erreur lexicale
   */
  private void erreurLex(String s) {
    String message = String.format("A lexical error occured with symbol %s ", s);
    System.out.println(message);
  }

  //Methode principale a lancer pour tester l'analyseur lexical
  public static void main(String[] args) {
    String toWrite = "";
    System.out.println("Debut d'analyse lexicale");
    if (args.length == 0){
      args = new String [2];
      args[0] = "ExpArith.txt";
      args[1] = "ResultatLexical.txt";
    }
    Reader r = new Reader(args[0]);

    AnalLex lexical = new AnalLex(r.toString()); // Creation de l'analyseur lexical

    // Execution de l'analyseur lexical
    Terminal t = null;
    while(lexical.resteTerminal()){
      t = lexical.prochainTerminal();
      toWrite +=t.chaine + "\n" ;  // toWrite contient le resultat
    }				   //    d'analyse lexicale
    System.out.println(toWrite); 	// Ecriture de toWrite sur la console
    Writer w = new Writer(args[1],toWrite); // Ecriture de toWrite dans fichier args[1]
    System.out.println("Fin d'analyse lexicale");
  }

  public Boolean getError() {
    return error;
  }
}