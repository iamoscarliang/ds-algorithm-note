package sorting;

import java.util.Arrays;

public class MergeSort {

    public static int[] sort(int[] array) {
        int size = array.length;
        if (size <= 1) {
            return array;
        }

        int[] left = sort(Arrays.copyOfRange(array, 0, size / 2));
        int[] right = sort(Arrays.copyOfRange(array, size / 2, size));

        return merge(left, right);
    }

    private static int[] merge(int[] ar1, int[] ar2) {
        int size1 = ar1.length;
        int size2 = ar2.length;
        int size = size1 + size2;
        int i1 = 0, i2 = 0;
        int[] ar = new int[size];

        for (int i = 0; i < size; i++) {
            if (i1 == size1) {
                ar[i] = ar2[i2++];
            } else if (i2 == size2) {
                ar[i] = ar1[i1++];
            } else {
                if (ar1[i1] < ar2[i2]) {
                    ar[i] = ar1[i1++];
                } else {
                    ar[i] = ar2[i2++];
                }
            }
        }
        return ar;
    }

    public static void main(String[] args) {
        int[] array = {17, 5, 12, 2, 1, 9, 10};
        array = sort(array);
        System.out.println(Arrays.toString(array));
    }

}