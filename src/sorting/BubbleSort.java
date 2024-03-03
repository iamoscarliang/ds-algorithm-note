package sorting;

import java.util.Arrays;

public class BubbleSort {

    private static void sort(int[] array) {
        if (array == null) {
            return;
        }

        boolean sorted;
        do {
            sorted = true;
            int size = array.length;
            for (int i = 1; i < size; i++) {
                if (array[i - 1] > array[i]) {
                    swap(array, i - 1, i);
                    sorted = false;
                }
            }
        } while (!sorted);
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