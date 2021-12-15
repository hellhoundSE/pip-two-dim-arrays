import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");
    }

    public static int getSum(int[][] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                sum += arr[i][j];
            }
        }
        return sum;
    }

    public static int[][] getColSwapped(int[][] arr) {
        for (int[] row : arr) {
            {
                int t = row[0];
                row[0] = row[row.length - 1];
                row[row.length - 1] = t;
            }
        }
        return arr;
    }

    public static int getSumDiagonal(int[][] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (i == j)
                    sum += arr[i][j];
            }
        }
        return sum;
    }

    public static int[] getSumAtIndices(int[] arr, int[] arr2) {
        int[] sumArray = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {
            sumArray[i] = arr[i] + arr2[i];
        }
        return sumArray;
    }


    public static int[] getExceededArray(int[] arr, int[] arr2) {
        if (arr.length > arr2.length) {
            return Arrays.copyOfRange(arr, arr2.length, arr.length);
        }
        return Arrays.copyOfRange(arr2, arr.length, arr2.length);
    }




}
