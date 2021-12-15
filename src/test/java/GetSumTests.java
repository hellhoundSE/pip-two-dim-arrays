import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GetSumTests {

    @Test
    public void getSumCorrect(){
        Main main = GenericTestFactory.getInstance(Main.class);
        Object[] parameters = {new int[][]{{1,2,3},{10,20,30}}};
        GenericTestFactory.testMethod(main,"getSum",66, parameters);
    }

    @Test
    public void getSumCorrect2(){
        Main main = GenericTestFactory.getInstance(Main.class);
        Object[] parameters = {new int[][]{{10,0,-10},{-20,50,30}}};
        GenericTestFactory.testMethod(main,"getSum",60, parameters);
    }
}
