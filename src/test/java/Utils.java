import java.util.Arrays;
import java.util.Objects;

public class Utils {
    public static boolean arrayEquals(Object[] arr1,Object[] arr2){
        if(arr1.length != arr2.length)
            return false;
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        for (int i = 0; i < arr1.length; i++){
            if(Objects.equals(arr1[i],arr2[i]))
                return false;
        }
        return true;
    }

    public static boolean equals(Object obj1, Object obj2){
        if(!Objects.equals(obj1.getClass().getName(), obj2.getClass().getName())){
            return false;
        }
        if(obj1.getClass().isPrimitive()){
            return Objects.equals(obj1, obj2);
        }
        if(obj1.getClass().isArray()){
            return arrayEquals((Object[])obj1,(Object[])obj2);
        }

        return Objects.equals(obj1, obj2);
    }
}
