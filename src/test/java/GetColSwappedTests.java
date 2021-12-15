import org.junit.jupiter.api.Test;

import javax.rmi.CORBA.Util;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class GetColSwappedTests {

    @Test
    public void getColSwapped(){
        Main main = GenericTestFactory.getInstance(Main.class);
        Object[] parameters = {new int[][]{{1,2,3},{10,20,30},{100,200,300}}};
        int[][] correct = new int[][]{{3,2,1},{30,20,10},{300,200,100}};
        List<int[][]> answers = GenericTestFactory.invokeMethod(main,"getColSwapped", parameters, false);

        if(answers.size() != 1){
            fail();
        }
        int[][] actual = answers.get(0);
        if(actual.length != correct.length)
            fail();

        for(int i = 0; i < correct.length;i++){
            if(!Utils.arrayEquals(correct[i],actual[i])){
                fail();
            }
        }
    }


    @Test
    public void getColSwapped2(){
        Main main = GenericTestFactory.getInstance(Main.class);
        Object[] parameters = {new int[][]{{-1,-2,-3},{10,20,3000},{100,200,150}}};
        int[][] correct = new int[][]{{-3,-2,-1},{3000,20,10},{150,200,100}};
        List<int[][]> answers = GenericTestFactory.invokeMethod(main,"getColSwapped", parameters, false);

        if(answers.size() != 1){
            fail();
        }
        int[][] actual = answers.get(0);
        if(actual.length != correct.length)
            fail();

        for(int i = 0; i < correct.length;i++){
            if(!Utils.arrayEquals(correct[i],actual[i])){
                fail();
            }
        }
    }

}
