package sorting;

import java.util.Arrays;

public class InsertionSort {

    private static void sort(int[] array) {
        if (array == null) {
            return;
        }

        int size = array.length;
        for (int i = 1; i < size; i++) {
            for (int j = i; j > 0 && array[j - 1] > array[j]; j--) {
                swap(array, j - 1, j);
            }
        }
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String[] args) {
        int[] array = {17, 5, 12, 2, 1, 9, 10};
        sort(array);
        System.out.println(Arrays.toString(array));
    }

}