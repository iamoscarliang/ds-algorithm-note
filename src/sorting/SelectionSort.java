package sorting;

import java.util.Arrays;

public class SelectionSort {

    public static void sort(int[] array) {
        if (array == null) {
            return;
        }

        int size = array.length;
        for (int i = 0; i < size; i++) {
            int swapIndex = i;
            for (int j = i + 1; j < size; j++) {
                if (array[j] < array[swapIndex]) {
                    swapIndex = j;
                }
            }
            swap(array, i, swapIndex);
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