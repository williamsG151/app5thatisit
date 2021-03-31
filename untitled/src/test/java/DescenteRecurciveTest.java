import org.junit.Assert;
import org.junit.Test;

public class DescenteRecurciveTest {
    //@Test
    public boolean testwriter(String file, String algebra){
        Writer writer = new Writer(file, algebra);
        System.out.println("Debut d'analyse syntaxique");

        System.out.println("Debut d'analyse lexicale");
        Reader r = new Reader(file);
        AnalLex lexical = new AnalLex(r.toString());
        while(lexical.resteTerminal()){
            lexical.prochainTerminal();
        }
        System.out.println("Fin d'analyse lexicale");

        DescenteRecursive dr = new DescenteRecursive(file, lexical.terminalChain);

        try {
            ElemAST RacineAST = dr.AnalSynt();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            System.exit(51);

        }
        System.out.println("Analyse syntaxique terminee");
        //Assert.assertTrue(true);
        return dr.getError();
    }

    @Test
    public void test1(){
        Assert.assertFalse(testwriter("testS1.txt","1*2+3*4+5*6"));
    }
    @Test
    public void test2(){
        Assert.assertFalse(testwriter("testS2.txt","(1+2)*3"));
    }
    @Test
    public void test3(){
        Assert.assertTrue(testwriter("testS3.txt","Ab_cde + Cewf"));
    }
    @Test
    public void test4(){
        Assert.assertTrue(testwriter("testS4.txt","( U_x – ) * W_z / 35"));
    }
    @Test
    public void test5(){
        Assert.assertFalse(testwriter("testS5.txt","(55 - 47) * 14/ 2"));
    }
    @Test
    public void test6(){
        Assert.assertFalse(testwriter("testS6.txt","((A_b + 3))"));
    }
    @Test
    public void test7(){
        Assert.assertFalse(testwriter("testS7.txt","3 * Cewf + 56"));
    }
    @Test
    public void test8(){
        Assert.assertFalse(testwriter("testS8.txt","(V_y + V_X) / 6"));
    }
    @Test
    public void test9(){
        Assert.assertFalse(testwriter("testS9.txt","(U_x + V_y ) * W_z / 35"));
    }
}
