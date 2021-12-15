import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class GetSumAtIndicesTests {
    @Test
    public void getSumAtIndices() {
        Main main = GenericTestFactory.getInstance(Main.class);
        Object[] parameters = {new int[]{1, 2, 3}, new int[]{2, 3, 4}};
        int[] correct = new int[]{3, 5, 7};
        List<int[]> answers = GenericTestFactory.invokeMethod(main, "getSumAtIndices", parameters, true);

        if (answers.size() != 2) {
            fail();
        }
        int[] actual = answers.get(0);
        if (actual.length != correct.length)
            fail();

        if (!Utils.arrayEquals(correct, actual)) {
            fail();

        }
    }

    @Test
    public void getSumAtIndices2() {
        Main main = GenericTestFactory.getInstance(Main.class);
        Object[] parameters = {new int[]{10, -10, 20, 11}, new int[]{20, -20, 40, 50}};
        int[] correct = new int[]{30, -30, 60, 61};
        List<int[]> answers = GenericTestFactory.invokeMethod(main, "getSumAtIndices", parameters, true);

        if (answers.size() != 2) {
            fail();
        }
        int[] actual = answers.get(0);
        if (actual.length != correct.length)
            fail();

        if (!Utils.arrayEquals(correct, actual)) {
            fail();

        }
    }
}
