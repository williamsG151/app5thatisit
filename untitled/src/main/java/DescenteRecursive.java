

/** @author Ahmed Khoumsi */

import java.util.ArrayList;

/** Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {

  // Attributs
  private final ArrayList<Terminal> terminalChain;
  private final ArrayList<ElemAST> rootScope = new ArrayList<>();
  private int index = 0;
  private int length;
  private ElemAST root = null;
  private ElemAST elemCourant = null;
  private Terminal ULcourant = null;
  private Boolean error = false;


/** Constructeur de DescenteRecursive :
      - recoit en argument le nom du fichier contenant l'expression a analyser
      - pour l'initalisation d'attribut(s)
 */
public DescenteRecursive(String in, ArrayList<Terminal> chain) {
    terminalChain = chain;
    length = chain.size();
}


/** AnalSynt() effectue l'analyse syntaxique et construit l'AST.
 *    Elle retourne une reference sur la racine de l'AST construit
 */
public ElemAST AnalSynt() {
    ULcourant = terminalChain.get(0);
    E();
    removeExtraParenthesis();
    return root;
}

private Boolean createAST(String attendu){
    switch (attendu) {
      case "C":
      case "ID": {
        FeuilleAST feuille = new FeuilleAST(ULcourant);
        if (root == null) {
          root = feuille;
        }
        if (elemCourant instanceof NoeudAST) {
          elemCourant.setEnfD(feuille);
          feuille.setParent(elemCourant);
        }
        elemCourant = feuille;
        getNextUL();
        break;
      }
      case "S1": {
        NoeudAST noeud = new NoeudAST(ULcourant);
        if ((root instanceof NoeudAST) ) {  //Cas ou il n'y a pas de parenthese
          noeud.setEnfG(elemCourant);
          elemCourant.getParent().setEnfD(noeud);
          elemCourant.setParent(noeud);
          elemCourant = noeud;
        } else {            //This if statement is only usefull for the first operation of the expression because root is not a node
          noeud.setEnfG(elemCourant);
          root = noeud;
          elemCourant = noeud;
        }
        getNextUL();
        break;
      }
      case "S2": {
        NoeudAST noeud = new NoeudAST(ULcourant);
        if(root != null) {
          noeud.setEnfG(root);
          root = noeud;
          elemCourant = noeud;
        }else {
          noeud.setEnfG(elemCourant);
          root = noeud;
          elemCourant = noeud;
        }
        getNextUL();
        break;
      }
      case "Pg":{
        if(root == null){
          rootScope.add(null);
        }else{
          rootScope.add(root);
          root = null;
          elemCourant = null;
        }
        getNextUL();
        break;
      }
      case "Pd":{
        ElemAST lastRoot = rootScope.get(rootScope.size()-1);
        if(lastRoot == null){
          elemCourant = root;
          root = lastRoot;
          rootScope.remove(rootScope.size()-1);

        }else{
          lastRoot.setEnfD(root);
          root = lastRoot;
          elemCourant = lastRoot;
          rootScope.remove(rootScope.size()-1);
        }

        getNextUL();
        break;
      }
      default: {
        erreurSynt(ULcourant.chaine);
        return false;
      }
    }
    return true;
}

private void getNextUL() {
  if (index < length-1) {
    index++;
    ULcourant = terminalChain.get(index);
  }
}
// Methode pour chaque symbole non-terminal de la grammaire retenue
// ... 
// ...
  private void E(){
  if(!error) {
      T();
      if ("S2".equals(ULcourant.type) && !error) {
        createAST("S2");
        E();
      }
    }
  }

  private void T() {
    if (!error) {
      F();
      if ("S1".equals(ULcourant.type) && !error) {
        createAST("S1");
        T();
      }
    }
  }

  private void F() {
    if (!error) {
      if ("C".equals(ULcourant.type) || "ID".equals(ULcourant.type)) {
        createAST("C");
      }else if ("Pg".equals(ULcourant.type)) {
        createAST("Pg");
        E();
        if ("Pd".equals(ULcourant.type) && !error) {
          createAST("Pd");
        } else {
          erreurSynt(ULcourant.chaine);
        }
      } else {
        erreurSynt(ULcourant.chaine);
      }
    }
  }

/** ErreurSynt() envoie un message d'erreur syntaxique
 */
private void erreurSynt(String s)
{
  String message1 = String.format("\nA syntax error have ben detected with this terminal symbol: %s", s);
  String message2 = "Error: ";
  for(int i =0; i<length; i++){
    if(i==index){
      message2 += String.format(" [ %s ] ", terminalChain.get(i).chaine);
    }else{
      message2 += terminalChain.get(i).chaine;
    }
  }
  String message3 = String.format("Error was found at lexical unit %d out of %d", index, length-1);
  System.out.println(message1);
  System.out.println(message2);
  System.out.println(message3);
  error = true;
}
private void removeExtraParenthesis(){
  if(root == null && elemCourant != null){
    root = elemCourant;
  }
}

  //Methode principale a lancer pour tester l'analyseur syntaxique 
  public static void main(String[] args) {
    String toWriteLect = "";
    String toWriteEval = "";

    System.out.println("Debut d'analyse syntaxique");
    if (args.length == 0){
      args = new String [2];
      args[0] = "ExpArith.txt";
      args[1] = "ResultatSyntaxique.txt";
    }

    System.out.println("Debut d'analyse lexicale");
    Reader r = new Reader(args[0]);
    AnalLex lexical = new AnalLex(r.toString());
    while(lexical.resteTerminal()){
      lexical.prochainTerminal();
    }
    System.out.println("Fin d'analyse lexicale");

    DescenteRecursive dr = new DescenteRecursive(args[0], lexical.terminalChain);

    try {
      ElemAST RacineAST = dr.AnalSynt();
//      boolean postfix = true;
//      toWriteLect += "Lecture de l'AST trouve : " + RacineAST.LectAST(postfix) + "\n";
//      System.out.println(toWriteLect);
//      toWriteEval += "Evaluation de l'AST trouve : " + RacineAST.EvalAST() + "\n";
//      System.out.println(toWriteEval);
//      Writer w = new Writer(args[1],toWriteLect+toWriteEval); // Ecriture de toWrite
//                                                               //dans fichier args[1]
    } catch (Exception e) {
      System.out.println(e);
      e.printStackTrace();
      System.exit(51);
    }
    System.out.println("Analyse syntaxique terminee");
  }

  public Boolean getError() {
    return error;
  }
}

