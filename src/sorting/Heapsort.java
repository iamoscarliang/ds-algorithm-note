package sorting;

import java.util.Arrays;

public class Heapsort {

    private static void sort(int[] array) {
        if (array == null) {
            return;
        }

        int size = array.length;
        for (int i = Math.max(0, (size / 2) - 1); i >= 0; i--) {
            sink(array, size, i);
        }

        for (int i = size - 1; i >= 0; i--) {
            swap(array, 0, i);
            sink(array, i, 0);
        }
    }

    private static void sink(int[] ar, int n, int i) {
        while (true) {
            int left = 2 * i + 1; // Left  node
            int right = 2 * i + 2; // Right node
            int largest = i;

            if (right < n && ar[right] > ar[largest]) {
                largest = right;
            }

            if (left < n && ar[left] > ar[largest]) {
                largest = left;
            }

            if (largest != i) {
                swap(ar, largest, i);
                i = largest;
            } else break;
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