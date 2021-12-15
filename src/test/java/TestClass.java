import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestClass {


    @Test
    public void test(){
        Main main = GenericTestFactory.getInstance(Main.class);
        Object[] parameters = {new String[0]};
        GenericTestFactory.testSystemOutputFromMethod(main,"main","Hello world", parameters);
    }
}
