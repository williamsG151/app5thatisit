

/** @author Ahmed Khoumsi */

/** Classe Abstraite dont heriteront les classes FeuilleAST et NoeudAST
 */
public abstract class ElemAST {


  public void setEnfD(ElemAST enfD) {

  }

  public void setEnfG(ElemAST enfG){

  }

  public abstract Terminal getFeuille();

  public abstract ElemAST getParent();

  public abstract void setParent(ElemAST parent);
  
  /** Evaluation d'AST
   */
  public abstract Integer EvalAST();


  /** Lecture d'AST
   */
  public abstract String LectAST(boolean postfix);


/** ErreurEvalAST() envoie un message d'erreur lors de la construction d'AST
 */  
  public void ErreurEvalAST(String s) {	
    // 
  }

}
