import org.junit.jupiter.api.Test;

public class GetSumDiagonalTests {
    @Test
    public void getDiagonalSumCorrect(){
        Main main = GenericTestFactory.getInstance(Main.class);
        Object[] parameters = {new int[][]{{1,2,3},{10,20,30},{7,8,90}}};
        GenericTestFactory.testMethod(main,"getSumDiagonal",111, parameters);
    }

    @Test
    public void getDiagonalSumCorrect2(){
        Main main = GenericTestFactory.getInstance(Main.class);
        Object[] parameters = {new int[][]{{2,4,6},{8,10,12},{14,16,18}}};
        GenericTestFactory.testMethod(main,"getSumDiagonal",30, parameters);
    }
}
