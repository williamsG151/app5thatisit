/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class NoeudAST extends ElemAST {

  // Attributs
  private final Terminal operateur;
  private ElemAST enfG=null;
  private ElemAST enfD=null;

  private ElemAST parent;

  /** Constructeur pour l'initialisation d'attributs
   */
  public NoeudAST(Terminal operateur) {
      this.operateur = operateur;
  }

  @Override
  public void setEnfD(ElemAST enfD) {
    this.enfD = enfD;
  }

  @Override
  public void setEnfG(ElemAST enfG){
    this.enfG = enfG;
  }

  public ElemAST getParent(){
    return parent;
  }

  public void setParent(ElemAST parent) {
    this.parent = parent;
  }

  public Terminal getFeuille() {
    return null;
  }

  /** Evaluation de noeud d'AST
   */
  public Integer EvalAST( ) {
    Integer gauche = enfG.EvalAST();
    Integer droite = enfD.EvalAST();
    Integer result = null;
    if(gauche != null && droite != null) {
      switch (operateur.chaine) {
        case "+":
          result = gauche + droite;
          break;
        case "-":
          result = gauche - droite;
          break;
        case "*":
          result = gauche * droite;
          break;
        case "/":
          result = gauche / droite;
          break;
        default:
          result = null;
          System.out.println("ERROR: An operator has not been defined");
      }
    }else{
      if(gauche == null && (enfG.getFeuille()!=null)){
        String message = String.format("This lexical unit being an IDENTIFIANT : %s, \n It is impossible to evaluate this arithmetic expression", enfG.getFeuille().chaine);
        System.out.println(message);
      }
      if(droite == null && (enfD.getFeuille()!=null)){
        String message = String.format("This lexical unit being an IDENTIFIANT : %s, \n It is impossible to evaluate this arithmetic expression", enfD.getFeuille().chaine);
        System.out.println(message);
      }
    }
    return result;
  }


  /** Lecture de noeud d'AST
   */
  public String LectAST(boolean postfix) {
    String result = null;
    if(postfix) {
      result = String.format("%s %s %s", enfG.LectAST(true), enfD.LectAST(true), operateur.chaine);
    }else{
      result = String.format("( %s %s %s )", enfG.LectAST(true), operateur.chaine, enfD.LectAST(true));
    }
    return result;
  }
}


