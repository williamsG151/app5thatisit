import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AnalLexTest {

    public boolean testwriter(String file, String algebra){
        Writer writer = new Writer(file, algebra);
        Reader r = new Reader(file);

        AnalLex lexical = new AnalLex(r.toString()); // Creation de l'analyseur lexical

        // Execution de l'analyseur lexical
        Terminal t = null;
        while(lexical.resteTerminal()){
            t = lexical.prochainTerminal();
        }

        return lexical.getError();
    }

    @Test
    public void test1(){

        Assert.assertFalse(testwriter("testL1.txt","1*2+3*4+5*6"));
    }
    @Test
    public void test2(){
        Assert.assertFalse(testwriter("testL2.txt","(1+2)*3"));
    }
    @Test
    public void test3(){
        Assert.assertTrue(testwriter("testL3.txt","4*(2+ &)"));
    }
    @Test
    public void test4(){
        Assert.assertTrue(testwriter("testL4.txt","Abcde + Cewf_"));
    }
    @Test
    public void test5(){
        Assert.assertFalse(testwriter("testL5.txt","Ab_cde + Cewf"));
    }
    @Test
    public void test6(){
        Assert.assertTrue(testwriter("testL6.txt","a_b + 3"));
    }
    @Test
    public void test7(){
        Assert.assertFalse(testwriter("testL7.txt","( U_x – ) * W_z / 35"));
    }
    @Test
    public void test8(){
        Assert.assertFalse(testwriter("testL8.txt","(55 - 47)* 14 / 2"));
    }
    @Test
    public void test9(){
        Assert.assertFalse(testwriter("testL9.txt","(U_x + V_y ) * W_z / 35"));
    }
    @Test
    public void test10(){
        Assert.assertTrue(testwriter("testL10.txt","(U_x + V_y ) * W__z / 35"));
    }

}
