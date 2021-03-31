
/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class FeuilleAST extends ElemAST {

  private Terminal terminal;
  private ElemAST parent = null;


/**Constructeur pour l'initialisation d'attribut(s)
 */
  public FeuilleAST(Terminal courrant) {
        this.terminal = courrant;
  }

    /** Evaluation de feuille d'AST
   */
  public Integer EvalAST( ) {
      Integer result = null;
      if(terminal.type.equals("ID")){
          result = null;
      }else{
          result = Integer.parseInt(terminal.chaine);
      }
      return result;
  }

  public ElemAST getParent(){
      return parent;
  }

  public void setParent(ElemAST parent){
      this.parent = parent;
  }

public Terminal getFeuille() {
    return terminal;
}

    /** Lecture de chaine de caracteres correspondant a la feuille d'AST
  */
  public String LectAST(boolean postfix ) {
    return terminal.chaine;
  }

}
