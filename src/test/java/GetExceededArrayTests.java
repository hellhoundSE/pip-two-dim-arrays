import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class GetExceededArrayTests {
    @Test
    public void getExceededArray() {
        Main main = GenericTestFactory.getInstance(Main.class);
        Object[] parameters = {new int[]{1, 2, 3}, new int[]{2, 3, 4,6,8,8}};
        int[] correct = new int[]{6,8,8};
        List<int[]> answers = GenericTestFactory.invokeMethod(main, "getExceededArray", parameters, true);

        if (answers.size() != 2) {
            fail();
        }
        int[] actual = answers.get(0);

        if (!Utils.arrayEquals(correct, actual)) {
            fail();
        }
    }

    @Test
    public void getExceededArray2() {
        Main main = GenericTestFactory.getInstance(Main.class);
        Object[] parameters = {new int[]{33, 45, 66}, new int[]{2, 3, 4,20,30,40,50,60}};
        int[] correct = new int[]{20,30,40,50,60};
        List<int[]> answers = GenericTestFactory.invokeMethod(main, "getExceededArray", parameters, false);

        if (answers.size() != 2) {
            fail();
        }
        int[] actual = answers.get(0);

        if (!Utils.arrayEquals(correct, actual)) {
            fail();
        }
    }
}
